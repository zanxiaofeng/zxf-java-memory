package zxf.java.memory.jdbc;

import oracle.jdbc.OracleType;
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
        //PreparedStatement setupStatement = connection.prepareStatement("ALTER SESSION SET TIME_ZONE = '+7:0'");
        //setupStatement.execute();

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT DBTIMEZONE, SESSIONTIMEZONE FROM DUAL");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String dbTimezone = resultSet.getString("DBTIMEZONE");
            System.out.println("DBTIMEZONE => " + dbTimezone.toString());
            String sessionTimezone = resultSet.getString("SESSIONTIMEZONE");
            System.out.println("SESSIONTIMEZONE => " + sessionTimezone.toString());
        }
    }

    private static void queryJdbcTime(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT CL_DATE, CL_TIMESTAMP, CL_TIMESTAMP_TZ, CL_TIMESTAMP_LTZ FROM MY_TEST_TABLE WHERE CL_TIMESTAMP_LTZ > ?");
        preparedStatement.setObject(1, LocalDateTime.now().minusDays(15), OracleType.TIMESTAMP_WITH_LOCAL_TIME_ZONE);
        ResultSet resultSet = preparedStatement.executeQuery();
        //oracle.jdbc.driver.ForwardOnlyResultSet
        System.out.println(resultSet.getClass());
        while (resultSet.next()) {
            testDateOriginal(resultSet);
            testDateToClass(resultSet, String.class);
            testDateToClass(resultSet, Date.class);
            testDateToClass(resultSet, LocalDate.class);

            testTimestampOriginal(resultSet);
            testTimestampToClass(resultSet, String.class);
            testTimestampToClass(resultSet, Timestamp.class);
            testTimestampToClass(resultSet, LocalDate.class);
            testTimestampToClass(resultSet, LocalDateTime.class);
            testTimestampToClass(resultSet, ZonedDateTime.class);

            testTimestampTzOriginal(resultSet);
            testTimestampTzToClass(resultSet, String.class);
            testTimestampTzToClass(resultSet, Date.class);
            testTimestampTzToClass(resultSet, Timestamp.class);
            testTimestampTzToClass(resultSet, LocalDateTime.class);
            testTimestampTzToClass(resultSet, ZonedDateTime.class);

            testTimestampLtzOriginal(resultSet);
            testTimestampLtzToClass(resultSet, String.class);
            testTimestampLtzToClass(resultSet, Date.class);
            testTimestampLtzToClass(resultSet, Timestamp.class);
            testTimestampLtzToClass(resultSet, LocalDateTime.class);
            testTimestampLtzToClass(resultSet, ZonedDateTime.class);
        }
    }

    private static void testDateOriginal(ResultSet resultSet) throws SQLException {
        Object date = (Timestamp) resultSet.getObject("CL_DATE");
        System.out.println("#DATE => " + date.getClass() + ", value=" + date.toString());
    }

    private static <T> void testDateToClass(ResultSet resultSet, Class<T> klass) throws SQLException {
        T date = resultSet.getObject("CL_DATE", klass);
        System.out.println("$DATE => " + klass + ", value=" + date.toString());
    }

    private static void testTimestampOriginal(ResultSet resultSet) throws SQLException {
        Object timestamp = (TIMESTAMP) resultSet.getObject("CL_TIMESTAMP");
        System.out.println("#TIMESTAMP (6) => " + timestamp.getClass() + ", value=" + timestamp.toString());
    }

    private static <T> void testTimestampToClass(ResultSet resultSet, Class<T> klass) throws SQLException {
        T timestamp = resultSet.getObject("CL_TIMESTAMP", klass);
        System.out.println("$TIMESTAMP (6) => " + klass + ", value=" + timestamp.toString());
    }

    private static void testTimestampTzOriginal(ResultSet resultSet) throws SQLException {
        Object timestampTz = (TIMESTAMPTZ) resultSet.getObject("CL_TIMESTAMP_TZ");
        System.out.println("#TIMESTAMP (6) WITH TIME ZONE => " + timestampTz.getClass() + ", value=" + timestampTz.toString());
    }


    private static <T> void testTimestampTzToClass(ResultSet resultSet, Class<T> klass) throws SQLException {
        T timestampTz = resultSet.getObject("CL_TIMESTAMP_TZ", klass);
        System.out.println("$TIMESTAMP (6) WITH TIME ZONE => " + klass + ", value=" + timestampTz.toString());
    }

    private static void testTimestampLtzOriginal(ResultSet resultSet) throws SQLException {
        Object timestampLtz = (TIMESTAMPLTZ) resultSet.getObject("CL_TIMESTAMP_LTZ");
        System.out.println("#TIMESTAMP (6) WITH LOCAL TIME ZONE => " + timestampLtz.getClass() + ", value=" + timestampLtz.toString());
    }

    private static <T> void testTimestampLtzToClass(ResultSet resultSet, Class<T> klass) throws SQLException {
        T timestampLtz = resultSet.getObject("CL_TIMESTAMP_LTZ", klass);
        System.out.println("$TIMESTAMP (6) WITH LOCAL TIME ZONE => " + klass + ", value=" + timestampLtz.toString());
    }
}
