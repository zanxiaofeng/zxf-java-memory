package zxf.java.memory.jdbc;

import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;
import oracle.sql.TIMESTAMPTZ;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
        queryJdbTime(connection);
        connection.close();
    }

    private static void queryJdbTime(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT CL_DATE, CL_TIMESTAMP, CL_TIMESTAMP_TZ, CL_TIMESTAMP_LTZ FROM MY_TEST_TABLE");
        ResultSet resultSet = preparedStatement.executeQuery();
        //oracle.jdbc.driver.ForwardOnlyResultSet
        System.out.println(resultSet.getClass());
        while (resultSet.next()) {
            testOriginal(resultSet, connection);
            testToClass(resultSet, connection);
        }
    }

    private static void testOriginal(ResultSet resultSet, Connection connection) throws SQLException {
        java.sql.Timestamp date = (java.sql.Timestamp) resultSet.getObject("CL_DATE");
        System.out.println("#TIMESTAMP (6) => class=" + date.getClass() + ", value=" + date.toLocalDateTime().toString());

        TIMESTAMP timestamp = (TIMESTAMP) resultSet.getObject("CL_TIMESTAMP");
        System.out.println("#TIMESTAMP (6) => class=" + timestamp.getClass() + ", value=" + timestamp.toLocalDateTime().toString());

        TIMESTAMPTZ timestampTz = (TIMESTAMPTZ) resultSet.getObject("CL_TIMESTAMP_TZ");
        System.out.println("#TIMESTAMP (6) WITH TIME ZONE => class=" + timestampTz.getClass() + ", value=" + timestampTz.toZonedDateTime().toString());

        TIMESTAMPLTZ timestampLtz = (TIMESTAMPLTZ) resultSet.getObject("CL_TIMESTAMP_LTZ");
        System.out.println("#TIMESTAMP (6) WITH LOCAL TIME ZONE => class=" + timestampLtz.getClass() + ", value=" + timestampLtz.toLocalDateTime(connection).toString());
    }

    private static void testToClass(ResultSet resultSet, Connection connection) throws SQLException {
        Date date = resultSet.getObject("CL_DATE", Date.class);
        System.out.println("$TIMESTAMP (6) => class=" + date.getClass() + ", value=" + date.toString());

        LocalDateTime timestamp = resultSet.getObject("CL_TIMESTAMP", LocalDateTime.class);
        System.out.println("$TIMESTAMP (6) => class=" + timestamp.getClass() + ", value=" + timestamp.toString());

        ZonedDateTime timestampTz = resultSet.getObject("CL_TIMESTAMP_TZ", ZonedDateTime.class);
        System.out.println("$TIMESTAMP (6) WITH TIME ZONE => class=" + timestampTz.getClass() + ", value=" + timestampTz.toString());

        LocalDateTime timestampLtz = resultSet.getObject("CL_TIMESTAMP_LTZ", LocalDateTime.class);
        System.out.println("$TIMESTAMP (6) WITH LOCAL TIME ZONE => class=" + timestampLtz.getClass() + ", value=" + timestampLtz.toString());
    }
}
