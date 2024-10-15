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

# JDBC core classes
- java.sql.Driver;
- java.sql.DriverManager;
- java.sql.SQLException;
- java.sql.Connection;
- java.sql.Statement;
- java.sql.PreparedStatement
- java.sql.ResultSet
- java.sql.ResultSetMetaData
- javax.sql.DataSource
- javax.sql.PooledConnection
- oracle.jdbc.pool.OraclePooledConnection[oracle]
- javax.sql.ConnectionPoolDataSource
- oracle.jdbc.pool.OracleConnectionPoolDataSource[oracle]

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

# Type Mapping from Java to Oracle
- oracle.jdbc.driver.JavaToJavaConverter<S, T>

# Auto commit
- If a connection is in auto-commit mode, then all its SQL statements will be executed and committed as individual transactions. Otherwise, its SQL statements are grouped into transactions that are terminated by a call to either the method commit or the method rollback. By default, new connections are in auto-commit mode.
- The commit occurs when the statement completes. The time when the statement completes depends on the type of SQL Statement:
- For DML statements, such as Insert, Update or Delete, and DDL statements, the statement is complete as soon as it has finished executing.
- For Select statements, the statement is complete when the associated result set is closed.
- For CallableStatement objects or for statements that return multiple results, the statement is complete when all of the associated result sets have been closed, and all update counts and output parameters have been retrieved.

# JDBC transaction
## Auto transaction
- In JDBC, when the auto-commit mode is set to true, it means that each individual SQL statement is treated as a transaction and will be automatically committed right after it is executed.
## Manual transaction
- If your need combine multiple JDBC executions or JDBC executions and other executions(Local file I/O, Network I/0), you need a manual transaction.

# SQL Fidddle
- https://sqlfiddle.com/oracle/online-compiler

# SQL Examples
- ALTER SESSION SET TIME_ZONE='Asia/Hong_Kong';
- SELECT DBTIMEZONE, SESSIONTIMEZONE FROM DUAL;
- SELECT CURRENT_DATE, CURRENT_TIMESTAMP, LOCALTIMESTAMP, SYSDATE, SYSTIMESTAMP FROM DUAL;
- SELECT SYS_EXTRACT_UTC(SYSDATE), SYS_EXTRACT_UTC(LOCALTIMESTAMP),SYS_EXTRACT_UTC(CURRENT_TIMESTAMP) FROM DUAL;
- SELECT TRUNC(SYSDATE),CAST(SYSDATE AS TIMESTAMP)  FROM DUAL;
- SELECT FROM_TZ(LOCALTIMESTAMP, '8:00') FROM DUAL;
- SELECT SYSTIMESTAMP AT TIME ZONE 'Asia/Hong_Kong', SYSTIMESTAMP AT LOCAL FROM DUAL;