package zxf.java.memory.jdbc;

import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

public class JdbcTimeTests {
    public static void main(String[] args) throws SQLException, IOException {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
        testJdbcTime();
    }

    public static void testJdbcTime() throws SQLException, IOException {
        Properties jdbcProperties = new Properties();
        jdbcProperties.setProperty("oracle.jdbc.user", "***");
        jdbcProperties.setProperty("oracle.jdbc.password", "***");
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@host:port/service", jdbcProperties);
        setupSessionTimezone(connection);
        queryJdbcTime(connection);
        connection.close();
    }

    private static void setupSessionTimezone(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("ALTER SESSION SET TIME_ZONE = '+7:0'");
        preparedStatement.execute();

        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT DBTIMEZONE,SESSIONTIMEZONE FROM DUAL");
        ResultSet resultSet = preparedStatement1.executeQuery();
        if (resultSet.next()) {
            String dbTimezone = resultSet.getString("DBTIMEZONE");
            System.out.println("DBTIMEZONE=> " + dbTimezone.toString());
            String sessionTimezone = resultSet.getString("SESSIONTIMEZONE");
            System.out.println("SESSIONTIMEZONE=> " + sessionTimezone.toString());
        }
    }

    private static void queryJdbcTime(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT CL_DATE, CL_TIMESTAMP, CL_TIMESTAMP_TZ, CL_TIMESTAMP_LTZ FROM MY_TEST_TABLE");
        ResultSet resultSet = preparedStatement.executeQuery();
        //oracle.jdbc.driver.ForwardOnlyResultSet
        System.out.println(resultSet.getClass());
        while (resultSet.next()) {
            testDateOriginal(resultSet, connection);
            testDateToClass(resultSet, connection, String.class);
            testDateToClass(resultSet, connection, Date.class);
            testDateToClass(resultSet, connection, LocalDate.class);

            testTimestampOriginal(resultSet, connection);
            testTimestampToClass(resultSet, connection, String.class);
            testTimestampToClass(resultSet, connection, Date.class);
            testTimestampToClass(resultSet, connection, Timestamp.class);
            testTimestampToClass(resultSet, connection, LocalDateTime.class);
            testTimestampToClass(resultSet, connection, ZonedDateTime.class);
        }
    }

    private static void testDateOriginal(ResultSet resultSet, Connection connection) throws SQLException {
        Object date = resultSet.getObject("CL_DATE");
        System.out.println("#TIMESTAMP (6) => class=" + date.getClass() + ", value=" + date.toString());
    }

    private static void testTimestampOriginal(ResultSet resultSet, Connection connection) throws SQLException {
        Object timestamp = (TIMESTAMP) resultSet.getObject("CL_TIMESTAMP");
        System.out.println("#TIMESTAMP (6) => " + timestamp.getClass() + ", value=" + timestamp.toString());

        Object timestampTz = (TIMESTAMPTZ) resultSet.getObject("CL_TIMESTAMP_TZ");
        System.out.println("#TIMESTAMP (6) WITH TIME ZONE => " + timestampTz.getClass() + ", value=" + timestampTz.toString());

        Object timestampLtz = (TIMESTAMPLTZ) resultSet.getObject("CL_TIMESTAMP_LTZ");
        System.out.println("#TIMESTAMP (6) WITH LOCAL TIME ZONE => " + timestampLtz.getClass() + ", value=" + timestampLtz.toString());
    }

    private static <T> void testDateToClass(ResultSet resultSet, Connection connection, Class<T> klass) throws SQLException {
        T date = resultSet.getObject("CL_DATE", klass);
        System.out.println("$TIMESTAMP (6) => " + klass + ", value=" + date.toString());
    }

    private static <T> void testTimestampToClass(ResultSet resultSet, Connection connection, Class<T> klass) throws SQLException {
        T timestamp = resultSet.getObject("CL_TIMESTAMP", klass);
        System.out.println("$TIMESTAMP (6) => " + klass + ", value=" + timestamp.toString());

        T timestampTz = resultSet.getObject("CL_TIMESTAMP_TZ", klass);
        System.out.println("$TIMESTAMP (6) WITH TIME ZONE => " + klass + ", value=" + timestampTz.toString());

        T timestampLtz = resultSet.getObject("CL_TIMESTAMP_LTZ", klass);
        System.out.println("$TIMESTAMP (6) WITH LOCAL TIME ZONE => " + klass + ", value=" + timestampLtz.toString());
    }
}
