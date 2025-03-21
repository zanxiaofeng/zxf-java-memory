# JVM Timezone
- TimeZone.setDefault(timezone);
- TimeZone.getDefault();

# Oracle中TimeZone相关的系统变量
## DBTIMEZONE(DB Level)
- DBTIMEZONE returns the value of the database time zone. The return type is a time zone offset (a character type in the format '[+|-]TZH:TZM') or a time zone region name, depending on how the user specified the database time zone value in the most recent CREATE DATABASE or ALTER DATABASE statement.
## SESSIONTIMEZONE(Session Level)
- SESSIONTIMEZONE returns the time zone of the current session. The return type is a time zone offset (a character type in the format '[+|-]TZH:TZM') or a time zone region name, depending on how the user specified the session time zone value in the most recent ALTER SESSION statement.

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

# Date和Time类型的使用原则
- 尽量使用带时区的类型
- 不要使用不带时区的类型与带时区的类型比较

# Oracle中Date和Time相关的类型
- DATE
- TIMESTAMP (6)
- TIMESTAMP (6) WITH TIME ZONE
- TIMESTAMP (6) WITH LOCAL TIME ZONE

# JDBC core classes
- java.sql.Driver;
- java.sql.DriverAction;
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

# Usage of Jdbc by DriverManager
- java.sql.DriverManager::getConnection(String url, ***):Connection --> oracle.jdbc.driver.T4CConnection
- java.sql.Connection::prepareStatement(String sql):PreparedStatement --> oracle.jdbc.driver.OraclePreparedStatementWrapper
- java.sql.PreparedStatement::set****(int, ***):void
- java.sql.PreparedStatement::executeQuery():ResultSet --> oracle.jdbc.driver.ForwardOnlyResultSet
- java.sql.ResultSet::get****(int columnIndex ****)
- java.sql.ResultSet::get****(String columnLabel ****)

# Usage of Jdbc by DataSource
- Create DataSource
- javax.sql.DataSource::getConnection():Connection
- javax.sql.DataSource::getConnection(String username, String password):Connection
- java.sql.Connection::prepareStatement(String sql):PreparedStatement --> 
- java.sql.PreparedStatement::set****(int, ***):void
- java.sql.PreparedStatement::executeQuery():ResultSet --> 
- java.sql.ResultSet::get****(int columnIndex ****)
- java.sql.ResultSet::get****(String columnLabel ****)

# JDBC classes of DATE and TIME
- java.sql.Date
- java.sql.Time
- java.sql.Timestamp

# ORACLE JDBC classes of DATE and TIME
- oracle.sql.DATE;
- oracle.sql.TIMESTAMP;
- oracle.sql.TIMESTAMPLTZ;
- oracle.sql.TIMESTAMPTZ;

# Type Definition
## Types
- java.sql.SQLType
### JDBC Types
- java.sql.JDBCType
- java.sql.Types
### ORACLE Types
- oracle.jdbc.OracleType

# Type Mapping from Java to Oracle
## Abstract
- java.sql.PreparedStatement.setObject(int parameterIndex, Object x):void
- java.sql.PreparedStatement.setObject(int parameterIndex, Object x, int targetSqlType):void
- java.sql.PreparedStatement.setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength):void
- java.sql.PreparedStatement.setObject(int parameterIndex, Object x, SQLType targetSqlType):void
- java.sql.PreparedStatement.setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength):void
## Flow by object and targetSqlType
- oracle.jdbc.driver.OraclePreparedStatement.setObject(int paramIndex, Object x, int targetSqlType):void
- oracle.jdbc.driver.OraclePreparedStatement.setObjectInternal(int paramIndex, Object x, int targetSqlType, int scale):void
- *oracle.jdbc.driver.OraclePreparedStatement.setObjectCritical(int paramIndex, Object x, int targetSqlType, int scale):void
- oracle.jdbc.driver.JavaToJavaConverter.convert(S src, Class<T> target, OracleConnection conn, Object srcExtra, Object targetExtra)：T
- oracle.jdbc.driver.OraclePreparedStatement.set******Internal(int paramIndex, ***** x)
## Flow by object
- oracle.jdbc.driver.OraclePreparedStatement.setObject(int paramIndex, Object x):void
- oracle.jdbc.driver.OraclePreparedStatement.setObjectInternal(int, java.lang.Object):void
- *oracle.jdbc.driver.OraclePreparedStatement.sqlTypeForObject(Object x):int
- oracle.jdbc.driver.OraclePreparedStatement.setObjectInternal(int paramIndex, Object x, int targetSqlType, int scale):void
- *oracle.jdbc.driver.OraclePreparedStatement.setObjectCritical(int paramIndex, Object x, int targetSqlType, int scale):void
- oracle.jdbc.driver.JavaToJavaConverter.convert(S src, Class<T> target, OracleConnection conn, Object srcExtra, Object targetExtra)：T
- oracle.jdbc.driver.OraclePreparedStatement.set******Internal(int paramIndex, ***** x)

# Type Mapping from Oracle to Java
## Abstract
- java.sql.ResultSet.getObject(int columnIndex):Object
- java.sql.ResultSet.getObject(int columnIndex, java.util.Map<String,Class<?>> map):Object
- java.sql.ResultSet.getObject(int columnIndex, Class<T> type):T
- java.sql.ResultSet.getObject(String columnLabel):Object
- java.sql.ResultSet.getObject(String columnLabel, java.util.Map<String,Class<?>> map):Object
- java.sql.ResultSet.getObject(String columnLabel, Class<T> type):T
## Read Object
- oracle.jdbc.driver.OraclePreparedStatement.executeQuery():ResultSet
- oracle.jdbc.driver.OracleStatement.createResultSet():OracleResultSet
- oracle.jdbc.driver.OracleResultSet.createResultSet(OracleStatement stmt):OracleResultSet
- oracle.jdbc.driver.OracleResultSet.ResultSetType.createResultSet(OracleStatement stmt):OracleResultSet
- oracle.jdbc.driver.ForwardOnlyResultSet.ForwardOnlyResultSet(PhysicalConnection conn, OracleStatement stmt)
- oracle.jdbc.driver.InsensitiveScrollableResultSet.getObject(int columnIndex, Class<T> type):T
- oracle.jdbc.driver.OracleStatement.getObject(long rowIndex, int columnIndex, Class<T> type):T
- this.accessors[columnIndex + this.offsetOfFirstUserColumn].getObject(this.physicalRowIndex(rowIndex), type);
## Setup Accessors
- oracle.jdbc.driver.T4CPreparedStatement.doDescribe():void
- oracle.jdbc.driver.T4CConnection.describe:T4C8Odscrarr
- oracle.jdbc.driver.T4C8Odscrarr.getAccessors():Accessor[]
- oracle.jdbc.driver.T4CTTIdcb.fillupAccessors(Accessor[] accessors, Accessor[] oldAccessors, int oldAccessorIndex, int accessorIndex, T4C8TTIuds ud, String colnames, long localCheckSum):Long
## Accessors
- oracle.jdbc.driver.Accessor(Representation _representation, OracleStatement _statement, int _representationMaxLength, boolean isStoredInBindData)
- oracle.jdbc.driver.TimestampAccessor
- oracle.jdbc.driver.TimestampltzAccessor
- oracle.jdbc.driver.TimestamptzAccessor
- oracle.jdbc.driver.BlobAccessor
- oracle.jdbc.driver.ClobAccessor

## Mappings
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
- ALTER DATABASE SET TIME_ZONE = 'Asia/Shanghai';
- ALTER SESSION SET TIME_ZONE='Asia/Hong_Kong';
- ALTER SESSION SET TIME_ZONE='+06:00';
- SELECT DBTIMEZONE, SESSIONTIMEZONE FROM DUAL;
- SELECT CURRENT_DATE, CURRENT_TIMESTAMP, LOCALTIMESTAMP, SYSDATE, SYSTIMESTAMP FROM DUAL;
- SELECT SYS_EXTRACT_UTC(LOCALTIMESTAMP), SYS_EXTRACT_UTC(CURRENT_TIMESTAMP) FROM DUAL;
- SELECT TRUNC(SYSDATE),CAST(SYSDATE AS TIMESTAMP)  FROM DUAL;

- SELECT SYSTIMESTAMP AT TIME ZONE 'Asia/Hong_Kong', SYSTIMESTAMP AT LOCAL FROM DUAL;
- SELECT CAST(TIMESTAMP '2024-10-22 00:00:00.00' AS TIMESTAMP WITH TIME ZONE) FROM DUAL;
- SELECT FROM_TZ(TIMESTAMP '2024-10-22 00:00:00.00', '+06:00') FROM DUAL;
- SELECT FROM_TZ(LOCALTIMESTAMP, '8:00') FROM DUAL;
- SELECT CAST(SYSTIMESTAMP AS TIMESTAMP), CAST(SYSTIMESTAMP AT LOCAL AS TIMESTAMP), CAST(SYSTIMESTAMP AT TIME ZONE '+06:00' AS TIMESTAMP) FROM DUAL;
- SELECT DUMP(SYSDATE) FROM DUAL;
- SELECT DATE '1998-12-25', TIMESTAMP '2024-10-22 00:00:00.00', TIMESTAMP '2024-10-22 00:00:00.00 +08:00' FROM DUAL;
- ALTER SESSION SET NLS_DATE_FORMAT='YYYY-MM-DD HH24:MI:SS';
- ALTER SESSION SET NLS_TIMESTAMP_FORMAT='YYYY-MM-DD HH24:MI:SS.FF';
- ALTER SESSION SET NLS_TIMESTAMP_TZ_FORMAT='YYYY-MM-DD HH24:MI:SS.FF TZR';
- SELECT UTL_RAW.CAST_TO_RAW('中国'), UNISTR('\4E2D\56FD'), ASCIISTR('中国') FROM DUAL;
- SELECT * FROM v$nls_parameters;

# Datetime Comparisons
- When you compare date and timestamp values, Oracle Database converts the data to the more precise data type before doing the comparison. For example, if you compare
- data of TIMESTAMP WITH TIME ZONE data type with data of TIMESTAMP data type, Oracle Database converts the TIMESTAMP data to TIMESTAMP WITH TIME ZONE, using the session time zone.
- The order of precedence for converting date and timestamp data is as follows:
- . DATE
- . TIMESTAMP
- . TIMESTAMP WITH LOCAL TIME ZONE
- . TIMESTAMP WITH TIME ZONE
- For any pair of data types, Oracle Database converts the data type that has a smaller number in the preceding list to the data type with the larger number.
# Explicit Conversion of Datetime Data Types
- If you want to do explicit conversion of datetime data types, use the CAST SQL function. You can explicitly convert DATE, TIMESTAMP, TIMESTAMP WITH TIME ZONE, and TIMESTAMP WITH LOCAL TIME ZONE to another data type in the list.