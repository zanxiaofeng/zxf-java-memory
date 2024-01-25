# JVM Timezone
- TimeZone.setDefault(timezone);
- TimeZone.getDefault();

# Oracle中TimeZone相关的系统变量
- DBTIMEZONE
- SESSIONTIMEZONE

# Oracle中Date和Time相关的类型
- DATE
- TIMESTAMP (6)
- TIMESTAMP (6) WITH TIME ZONE
- TIMESTAMP (6) WITH LOCAL TIME ZONE

# Oracle中Date和Time相关的系统变量
- SYSDATE(DATE, OS level)
- SYSTIMESTAMP(TIMESTAMP (6) WITH TIME ZONE, OS level)
- CURRENT_DATE(DATE, Session level)
- CURRENT_TIMESTAMP(TIMESTAMP (6) WITH TIME ZONE, Session level)
- LOCALTIMESTAMP(TIMESTAMP (6), Session level)

# SQL for TIMEZONE and DATETIME
- ALTER SESSION SET TIME_ZONE = '-12:0';
- SELECT DBTIMEZONE,SESSIONTIMEZONE FROM DUAL;
- SELECT CURRENT_DATE, CURRENT_TIMESTAMP, LOCALTIMESTAMP, SYSDATE, SYSTIMESTAMP FROM DUAL;

# JDBC classes of DATE and TIME
- java.sql.Date
- java.sql.Time
- java.sql.Timestamp

# ORACLE JDBC classes of DATE and TIME
- oracle.sql.DATE;
- oracle.sql.TIMESTAMP;
- oracle.sql.TIMESTAMPLTZ;
- oracle.sql.TIMESTAMPTZ;

# Type Mapping from Oracle to Java
- oracle.jdbc.driver.Representation
- DATE = new Representation("DATE", new Class[]{Timestamp.class, Date.class, LocalDate.class, LocalDateTime.class, LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, Calendar.class, DATE.class, TIMESTAMP.class, String.class, java.sql.Date.class, Time.class});
- TIMESTAMP = new Representation("TIMESTAMP", new Class[]{Timestamp.class, TIMESTAMP.class, LocalDate.class, LocalDateTime.class, LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, Calendar.class, Date.class, DATE.class, String.class, java.sql.Date.class, Time.class, byte[].class});
- TIMESTAMPTZ = new Representation("TIMESTAMPTZ", new Class[]{TIMESTAMPTZ.class, LocalDate.class, LocalDateTime.class, LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, Timestamp.class, TIMESTAMP.class, Calendar.class, Date.class, DATE.class, String.class, java.sql.Date.class, Time.class, byte[].class});
- OLD_TIMESTAMPTZ = new Representation("OLD_TIMESTAMPTZ", new Class[]{TIMESTAMPTZ.class, Timestamp.class, LocalDate.class, LocalDateTime.class, LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, TIMESTAMP.class, Calendar.class, Date.class, String.class, java.sql.Date.class, Time.class});
- TIMESTAMPLTZ = new Representation("TIMESTAMPLTZ", new Class[]{TIMESTAMPLTZ.class, LocalDate.class, LocalDateTime.class, LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, Timestamp.class, TIMESTAMP.class, Calendar.class, Date.class, DATE.class, String.class, java.sql.Date.class, Time.class, byte[].class});
- BLOB = new Representation("BLOB", new Class[]{Blob.class, BLOB.class, OracleBlob.class, InputStream.class, byte[].class});
- CLOB = new Representation("CLOB", new Class[]{Clob.class, CLOB.class, OracleClob.class, Reader.class, String.class, InputStream.class});
- BFILE = new Representation("BFILE", new Class[]{BFILE.class, OracleBfile.class, InputStream.class, byte[].class});
- JSON = new Representation("JSON", (Class[])var0.toArray(new Class[var0.size()]));


