package zxf.java.memory.jdbc;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleType;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPTZ;
import oracle.sql.TIMESTAMPLTZ;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.*;
import java.util.Properties;
import java.util.TimeZone;

public class JdbcTimeTests {
    public static void main(String[] args) throws SQLException, IOException {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+10:00"));

        Properties jdbcProperties = new Properties();
        jdbcProperties.setProperty("oracle.jdbc.user", "***");
        jdbcProperties.setProperty("oracle.jdbc.password", "***");

        DriverManager.setLogWriter(new PrintWriter(System.out));
        Connection connection = DriverManager.getConnection("jdbc:log4jdbc:oracle:thin:@host:port/service", jdbcProperties);
        setupSessionTimezone(connection);

        testJdbcTimeFromOracleToJava(connection);
        testJdbcTimeFromJavaToOracle(connection, LocalDateTime.now());
        testJdbcTimeFromJavaToOracle(connection, ZonedDateTime.now(ZoneId.of("GMT+06:00")));

        connection.close();
    }

    private static void setupSessionTimezone(Connection connection) throws SQLException {
        String sessionTimeZoneBeforeSetup = ((OracleConnection) connection).getSessionTimeZone();
        System.out.println("SessionTimeZoneBeforeSetup=" + sessionTimeZoneBeforeSetup);

        PreparedStatement setupStatement = connection.prepareStatement("ALTER SESSION SET TIME_ZONE = '+07:00'");
        setupStatement.execute();

        String sessionTimeZoneAfterSetup = ((OracleConnection) connection).getSessionTimeZone();
        System.out.println("SessionTimeZoneAfterSetup=" + sessionTimeZoneAfterSetup);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT DBTIMEZONE, SESSIONTIMEZONE FROM DUAL");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String dbTimezone = resultSet.getString("DBTIMEZONE");
            System.out.println("DBTIMEZONE => " + dbTimezone.toString());
            String sessionTimezone = resultSet.getString("SESSIONTIMEZONE");
            System.out.println("SESSIONTIMEZONE => " + sessionTimezone.toString());
        }
    }

    private static void testJdbcTimeFromOracleToJava(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, CL_DATE, CL_TIMESTAMP, CL_TIMESTAMP_TZ, CL_TIMESTAMP_LTZ FROM MY_TEST_TABLE WHERE ID = ?");
        preparedStatement.setString(1, "id-1");
        ResultSet resultSet = preparedStatement.executeQuery();
        //oracle.jdbc.driver.ForwardOnlyResultSet
        System.out.println(resultSet.getClass());

        if (resultSet.next()) {
            testDateOriginal(resultSet);
            testDateToClass(resultSet, String.class);
            testDateToClass(resultSet, Timestamp.class);
            testDateToClass(resultSet, java.util.Date.class);
            testDateToClass(resultSet, java.sql.Date.class);
            testDateToClass(resultSet, LocalDate.class);
            testDateToClass(resultSet, LocalDateTime.class);

            testTimestampOriginal(resultSet);
            testTimestampToClass(resultSet, String.class);
            testTimestampToClass(resultSet, Timestamp.class);
            testTimestampToClass(resultSet, java.util.Date.class);
            testTimestampToClass(resultSet, java.sql.Date.class);
            testTimestampToClass(resultSet, LocalDateTime.class);

            testTimestampTzOriginal(resultSet, connection);
            testTimestampTzToClass(resultSet, String.class);
            testTimestampTzToClass(resultSet, Timestamp.class);
            testTimestampTzToClass(resultSet, java.util.Date.class);
            testTimestampTzToClass(resultSet, java.sql.Date.class);
            testTimestampTzToClass(resultSet, LocalDateTime.class);
            testTimestampTzToClass(resultSet, ZonedDateTime.class);
            testTimestampTzToClass(resultSet, OffsetDateTime.class);

            testTimestampLtzOriginal(resultSet, connection);
            testTimestampLtzToClass(resultSet, String.class);
            testTimestampLtzToClass(resultSet, Timestamp.class);
            testTimestampLtzToClass(resultSet, java.util.Date.class);
            testTimestampLtzToClass(resultSet, java.sql.Date.class);
            testTimestampLtzToClass(resultSet, LocalDateTime.class);
            testTimestampLtzToClass(resultSet, ZonedDateTime.class);
            testTimestampLtzToClass(resultSet, OffsetDateTime.class);
        }
    }

    private static void testJdbcTimeFromJavaToOracleDefault(Connection connection) throws SQLException {
        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT DUMP(?) AS CL_SQL_TIMESTAMP_T, DUMP(?) AS CL_SQL_DATE_T, DUMP(?) AS CL_LOCAL_DATE_T, DUMP(?) AS CL_LOCAL_DATETIME_T, DUMP(?) AS CL_ZONED_DATETIME_T, DUMP(?) AS CL_OFFSET_DATETIME_T FROM DUAL");
        preparedStatement1.setObject(1, new java.sql.Timestamp(System.currentTimeMillis()));
        preparedStatement1.setObject(2, new java.sql.Date(System.currentTimeMillis()));
        preparedStatement1.setObject(3, LocalDate.now());
        preparedStatement1.setObject(4, LocalDateTime.now());
        preparedStatement1.setObject(5, ZonedDateTime.now());
        preparedStatement1.setObject(6, OffsetDateTime.now());
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        if (resultSet1.next()) {
            Object sqlTimestampT = resultSet1.getObject("CL_SQL_TIMESTAMP_T");
            //180, TIMESTAMP
            System.out.println("#CL_SQL_TIMESTAMP_T => " + sqlTimestampT.getClass() + ", value=" + sqlTimestampT);

            Object sqlDateT = resultSet1.getObject("CL_SQL_DATE_T");
            //12, DATE
            System.out.println("#CL_SQL_DATE_T => " + sqlDateT.getClass() + ", value=" + sqlDateT);

            Object localDateT = resultSet1.getObject("CL_LOCAL_DATE_T");
            //180, TIMESTAMP
            System.out.println("#CL_LOCAL_DATE_T => " + localDateT.getClass() + ", value=" + localDateT);

            Object localDatetimeT = resultSet1.getObject("CL_LOCAL_DATETIME_T");
            //180, TIMESTAMP
            System.out.println("#CL_LOCAL_DATETIME => " + localDatetimeT.getClass() + ", value=" + localDatetimeT);

            Object zonedDatetimeT = resultSet1.getObject("CL_ZONED_DATETIME_T");
            //181, TIMESTAMP WITH TIME ZONE
            System.out.println("#CL_ZONED_DATETIME_T => " + zonedDatetimeT.getClass() + ", value=" + zonedDatetimeT);

            Object offsetDatetimeT = resultSet1.getObject("CL_OFFSET_DATETIME_T");
            //181, TIMESTAMP WITH TIME ZONE
            System.out.println("#CL_OFFSET_DATETIME_T => " + offsetDatetimeT.getClass() + ", value=" + offsetDatetimeT);
        }

        PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT ? AS CL_SQL_TIMESTAMP, ? AS CL_SQL_DATE, ? AS CL_LOCAL_DATE, ? AS CL_LOCAL_DATETIME, ? AS CL_ZONED_DATETIME, ? AS CL_OFFSET_DATETIME FROM DUAL");
        preparedStatement2.setObject(1, new java.sql.Timestamp(System.currentTimeMillis()));
        preparedStatement2.setObject(2, new java.sql.Date(System.currentTimeMillis()));
        preparedStatement2.setObject(3, LocalDate.now());
        preparedStatement2.setObject(4, LocalDateTime.now());
        preparedStatement2.setObject(5, ZonedDateTime.now());
        preparedStatement2.setObject(6, OffsetDateTime.now());

        ResultSet resultSet2 = preparedStatement2.executeQuery();
        if (resultSet2.next()) {
            Object sqlTimestamp = resultSet2.getObject("CL_SQL_TIMESTAMP");
            Assert.isInstanceOf(TIMESTAMP.class, sqlTimestamp);
            System.out.println("#CL_SQL_TIMESTAMP => " + sqlTimestamp.getClass() + ", value=" + sqlTimestamp.toString());

            Object sqlDate = resultSet2.getObject("CL_SQL_DATE");
            Assert.isInstanceOf(Timestamp.class, sqlDate);
            System.out.println("#CL_SQL_DATE => " + sqlDate.getClass() + ", value=" + sqlDate.toString());

            Object localDate = resultSet2.getObject("CL_LOCAL_DATE");
            Assert.isInstanceOf(TIMESTAMP.class, localDate);
            System.out.println("#CL_LOCAL_DATE => " + localDate.getClass() + ", value=" + localDate.toString());

            Object localDatetime = resultSet2.getObject("CL_LOCAL_DATETIME");
            Assert.isInstanceOf(TIMESTAMP.class, localDatetime);
            System.out.println("#CL_LOCAL_DATETIME => " + localDatetime.getClass() + ", value=" + localDatetime.toString());

            Object zonedDatetime = resultSet2.getObject("CL_ZONED_DATETIME");
            Assert.isInstanceOf(TIMESTAMPTZ.class, zonedDatetime);
            String zonedDatetimeStringValue = ((TIMESTAMPTZ) zonedDatetime).stringValue(connection);
            ZonedDateTime zonedDatetimZonedDateTime = ((TIMESTAMPTZ) zonedDatetime).zonedDateTimeValue();
            System.out.println("#CL_ZONED_DATETIME => " + zonedDatetime.getClass() + ", string=" + zonedDatetimeStringValue + ", zoned=" + zonedDatetimZonedDateTime);

            Object offsetDatetime = resultSet2.getObject("CL_OFFSET_DATETIME");
            Assert.isInstanceOf(TIMESTAMPTZ.class, offsetDatetime);
            String offsetDatetimeStringValue = ((TIMESTAMPTZ) offsetDatetime).stringValue(connection);
            ZonedDateTime offsetDatetimZonedDateTime = ((TIMESTAMPTZ) offsetDatetime).zonedDateTimeValue();
            System.out.println("#CL_OFFSET_DATETIME => " + offsetDatetime.getClass() + ", string=" + offsetDatetimeStringValue + ", zoned=" + offsetDatetimZonedDateTime);
        }
    }

    private static void testJdbcTimeFromJavaToOracle(Connection connection, Object now) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ? AS CL_DATE, ? AS CL_TIMESTAMP, ? AS CL_TIMESTAMP_TZ, ? AS CL_TIMESTAMP_LTZ FROM DUAL");
        preparedStatement.setObject(1, now, OracleType.DATE);
        preparedStatement.setObject(2, now, OracleType.TIMESTAMP);
        preparedStatement.setObject(3, now, OracleType.TIMESTAMP_WITH_TIME_ZONE);
        preparedStatement.setObject(4, now, OracleType.TIMESTAMP_WITH_LOCAL_TIME_ZONE);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            testDateOriginal(resultSet);
            testTimestampOriginal(resultSet);
            testTimestampTzOriginal(resultSet, connection);
            testTimestampLtzOriginal(resultSet, connection);
        }
    }

    private static void testDateOriginal(ResultSet resultSet) throws SQLException {
        Object date = resultSet.getObject("CL_DATE");
        Assert.isInstanceOf(Timestamp.class, date);
        System.out.println("#DATE => " + date.getClass() + ", value=" + date.toString());
    }

    private static <T> void testDateToClass(ResultSet resultSet, Class<T> klass) throws SQLException {
        T date = resultSet.getObject("CL_DATE", klass);
        System.out.println("$DATE => " + klass + ", value=" + date.toString());
    }

    private static void testTimestampOriginal(ResultSet resultSet) throws SQLException {
        Object timestamp = resultSet.getObject("CL_TIMESTAMP");
        Assert.isInstanceOf(TIMESTAMP.class, timestamp);
        System.out.println("#TIMESTAMP (6) => " + timestamp.getClass() + ", value=" + timestamp.toString());
    }

    private static <T> void testTimestampToClass(ResultSet resultSet, Class<T> klass) throws SQLException {
        T timestamp = resultSet.getObject("CL_TIMESTAMP", klass);
        System.out.println("$TIMESTAMP (6) => " + klass + ", value=" + timestamp.toString());
    }

    private static void testTimestampTzOriginal(ResultSet resultSet, Connection connection) throws SQLException {
        Object timestampTz = resultSet.getObject("CL_TIMESTAMP_TZ");
        Assert.isInstanceOf(TIMESTAMPTZ.class, timestampTz);
        String stringValue = ((TIMESTAMPTZ) timestampTz).stringValue(connection);
        ZonedDateTime zonedDateTime = ((TIMESTAMPTZ) timestampTz).zonedDateTimeValue();
        System.out.println("#TIMESTAMP (6) WITH TIME ZONE => " + timestampTz.getClass() + ", string=" + stringValue + ", zoned=" + zonedDateTime);
    }


    private static <T> void testTimestampTzToClass(ResultSet resultSet, Class<T> klass) throws SQLException {
        T timestampTz = resultSet.getObject("CL_TIMESTAMP_TZ", klass);
        System.out.println("$TIMESTAMP (6) WITH TIME ZONE => " + klass + ", value=" + timestampTz.toString());
    }

    private static void testTimestampLtzOriginal(ResultSet resultSet, Connection connection) throws SQLException {
        Object timestampLtz = resultSet.getObject("CL_TIMESTAMP_LTZ");
        Assert.isInstanceOf(TIMESTAMPLTZ.class, timestampLtz);
        String stringValue = ((TIMESTAMPLTZ) timestampLtz).stringValue(connection);
        ZonedDateTime zonedDateTime = ((TIMESTAMPLTZ) timestampLtz).zonedDateTimeValue(connection);
        System.out.println("#TIMESTAMP (6) WITH LOCAL TIME ZONE => " + timestampLtz.getClass() + ", string=" + stringValue + ", zoned=" + zonedDateTime);
    }

    private static <T> void testTimestampLtzToClass(ResultSet resultSet, Class<T> klass) throws SQLException {
        T timestampLtz = resultSet.getObject("CL_TIMESTAMP_LTZ", klass);
        System.out.println("$TIMESTAMP (6) WITH LOCAL TIME ZONE => " + klass + ", value=" + timestampLtz.toString());
    }
}
