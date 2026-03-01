package zxf.java.memory.jdbc;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class JdbcTimeTests {
    public static void main(String[] args) throws SQLException, IOException {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+10:00"));

        Properties jdbcProperties = new Properties();
        jdbcProperties.setProperty("oracle.jdbc.user", "system");
        jdbcProperties.setProperty("oracle.jdbc.password", "123456");

        DriverManager.setLogWriter(new PrintWriter(System.out));
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/FREE", jdbcProperties);
        setupSessionTimezone(connection);

        testJdbcTimeFromOracleToJava(connection);
        testJdbcTimeFromJavaToOracleDefault(connection);
        testJdbcTimeFromJavaToOracle(connection, LocalDateTime.now());
        testJdbcTimeFromJavaToOracle(connection, ZonedDateTime.now(ZoneId.of("GMT+06:00")));

        connection.close();
    }

    private static void setupSessionTimezone(Connection connection) throws SQLException {
        String sessionTimeZoneBeforeSetup = ((OracleConnection) connection).getSessionTimeZone();
        log.info("SessionTimeZoneBeforeSetup={}", sessionTimeZoneBeforeSetup);

        PreparedStatement setupStatement = connection.prepareStatement("ALTER SESSION SET TIME_ZONE = '+07:00'");
        setupStatement.execute();

        String sessionTimeZoneAfterSetup = ((OracleConnection) connection).getSessionTimeZone();
        log.info("SessionTimeZoneAfterSetup={}", sessionTimeZoneAfterSetup);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT DBTIMEZONE, SESSIONTIMEZONE FROM DUAL");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String dbTimezone = resultSet.getString("DBTIMEZONE");
            log.info("DBTIMEZONE => {}", dbTimezone);
            String sessionTimezone = resultSet.getString("SESSIONTIMEZONE");
            log.info("SESSIONTIMEZONE => {}", sessionTimezone);
        }
    }

    private static void testJdbcTimeFromOracleToJava(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, CL_DATE, CL_TIMESTAMP, CL_TIMESTAMP_TZ, CL_TIMESTAMP_LTZ FROM MY_TEST_TABLE WHERE ID = ?");
        preparedStatement.setString(1, "id-1");
        ResultSet resultSet = preparedStatement.executeQuery();
        //oracle.jdbc.driver.ForwardOnlyResultSet
        log.info("{}", resultSet.getClass());

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
            log.info("#CL_SQL_TIMESTAMP_T => {}, value={}", sqlTimestampT.getClass(), sqlTimestampT);

            Object sqlDateT = resultSet1.getObject("CL_SQL_DATE_T");
            //12, DATE
            log.info("#CL_SQL_DATE_T => {}, value={}", sqlDateT.getClass(), sqlDateT);

            Object localDateT = resultSet1.getObject("CL_LOCAL_DATE_T");
            //180, TIMESTAMP
            log.info("#CL_LOCAL_DATE_T => {}, value={}", localDateT.getClass(), localDateT);

            Object localDatetimeT = resultSet1.getObject("CL_LOCAL_DATETIME_T");
            //180, TIMESTAMP
            log.info("#CL_LOCAL_DATETIME => {}, value={}", localDatetimeT.getClass(), localDatetimeT);

            Object zonedDatetimeT = resultSet1.getObject("CL_ZONED_DATETIME_T");
            //181, TIMESTAMP WITH TIME ZONE
            log.info("#CL_ZONED_DATETIME_T => {}, value={}", zonedDatetimeT.getClass(), zonedDatetimeT);

            Object offsetDatetimeT = resultSet1.getObject("CL_OFFSET_DATETIME_T");
            //181, TIMESTAMP WITH TIME ZONE
            log.info("#CL_OFFSET_DATETIME_T => {}, value={}", offsetDatetimeT.getClass(), offsetDatetimeT);
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
            log.info("#CL_SQL_TIMESTAMP => {}, value={}", sqlTimestamp.getClass(), sqlTimestamp);

            Object sqlDate = resultSet2.getObject("CL_SQL_DATE");
            Assert.isInstanceOf(Timestamp.class, sqlDate);
            log.info("#CL_SQL_DATE => {}, value={}", sqlDate.getClass(), sqlDate);

            Object localDate = resultSet2.getObject("CL_LOCAL_DATE");
            Assert.isInstanceOf(TIMESTAMP.class, localDate);
            log.info("#CL_LOCAL_DATE => {}, value={}", localDate.getClass(), localDate);

            Object localDatetime = resultSet2.getObject("CL_LOCAL_DATETIME");
            Assert.isInstanceOf(TIMESTAMP.class, localDatetime);
            log.info("#CL_LOCAL_DATETIME => {}, value={}", localDatetime.getClass(), localDatetime);

            Object zonedDatetime = resultSet2.getObject("CL_ZONED_DATETIME");
            Assert.isInstanceOf(TIMESTAMPTZ.class, zonedDatetime);
            String zonedDatetimeStringValue = ((TIMESTAMPTZ) zonedDatetime).stringValue(connection);
            ZonedDateTime zonedDatetimZonedDateTime = ((TIMESTAMPTZ) zonedDatetime).zonedDateTimeValue();
            log.info("#CL_ZONED_DATETIME => {}, string={}, zoned={}", zonedDatetime.getClass(), zonedDatetimeStringValue, zonedDatetimZonedDateTime);

            Object offsetDatetime = resultSet2.getObject("CL_OFFSET_DATETIME");
            Assert.isInstanceOf(TIMESTAMPTZ.class, offsetDatetime);
            String offsetDatetimeStringValue = ((TIMESTAMPTZ) offsetDatetime).stringValue(connection);
            ZonedDateTime offsetDatetimZonedDateTime = ((TIMESTAMPTZ) offsetDatetime).zonedDateTimeValue();
            log.info("#CL_OFFSET_DATETIME => {}, string={}, zoned={}", offsetDatetime.getClass(), offsetDatetimeStringValue, offsetDatetimZonedDateTime);
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
        log.info("#DATE => {}, value={}", date.getClass(), date);
    }

    private static <T> void testDateToClass(ResultSet resultSet, Class<T> klass) throws SQLException {
        T date = resultSet.getObject("CL_DATE", klass);
        log.info("$DATE => {}, value={}", klass, date);
    }

    private static void testTimestampOriginal(ResultSet resultSet) throws SQLException {
        Object timestamp = resultSet.getObject("CL_TIMESTAMP");
        Assert.isInstanceOf(TIMESTAMP.class, timestamp);
        log.info("#TIMESTAMP (6) => {}, value={}", timestamp.getClass(), timestamp);
    }

    private static <T> void testTimestampToClass(ResultSet resultSet, Class<T> klass) throws SQLException {
        T timestamp = resultSet.getObject("CL_TIMESTAMP", klass);
        log.info("$TIMESTAMP (6) => {}, value={}", klass, timestamp);
    }

    private static void testTimestampTzOriginal(ResultSet resultSet, Connection connection) throws SQLException {
        Object timestampTz = resultSet.getObject("CL_TIMESTAMP_TZ");
        Assert.isInstanceOf(TIMESTAMPTZ.class, timestampTz);
        String stringValue = ((TIMESTAMPTZ) timestampTz).stringValue(connection);
        ZonedDateTime zonedDateTime = ((TIMESTAMPTZ) timestampTz).zonedDateTimeValue();
        log.info("#TIMESTAMP (6) WITH TIME ZONE => {}, string={}, zoned={}", timestampTz.getClass(), stringValue, zonedDateTime);
    }


    private static <T> void testTimestampTzToClass(ResultSet resultSet, Class<T> klass) throws SQLException {
        T timestampTz = resultSet.getObject("CL_TIMESTAMP_TZ", klass);
        log.info("$TIMESTAMP (6) WITH TIME ZONE => {}, value={}", klass, timestampTz);
    }

    private static void testTimestampLtzOriginal(ResultSet resultSet, Connection connection) throws SQLException {
        Object timestampLtz = resultSet.getObject("CL_TIMESTAMP_LTZ");
        Assert.isInstanceOf(TIMESTAMPLTZ.class, timestampLtz);
        String stringValue = ((TIMESTAMPLTZ) timestampLtz).stringValue(connection);
        ZonedDateTime zonedDateTime = ((TIMESTAMPLTZ) timestampLtz).zonedDateTimeValue(connection);
        log.info("#TIMESTAMP (6) WITH LOCAL TIME ZONE => {}, string={}, zoned={}", timestampLtz.getClass(), stringValue, zonedDateTime);
    }

    private static <T> void testTimestampLtzToClass(ResultSet resultSet, Class<T> klass) throws SQLException {
        T timestampLtz = resultSet.getObject("CL_TIMESTAMP_LTZ", klass);
        log.info("$TIMESTAMP (6) WITH LOCAL TIME ZONE => {}, value={}", klass, timestampLtz);
    }
}
