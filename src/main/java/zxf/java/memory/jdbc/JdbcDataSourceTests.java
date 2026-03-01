package zxf.java.memory.jdbc;

import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.pool.OracleDataSource;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Properties;

@Slf4j
public class JdbcDataSourceTests {
    public static void main(String[] args) throws SQLException {
        testOracleDataSource();
        testPoolOracleDataSource();
    }

    private static void testOracleDataSource() throws SQLException {
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setDriverType("oracle.jdbc.OracleDriver");
        dataSource.setURL("jdbc:oracle:thin:@//localhost:1521/FREE");
        dataSource.setUser("system");
        dataSource.setPassword("123456");
        log.info("DataSource->{}", dataSource.getClass());

        Connection connection = dataSource.getConnection();
        log.info("Connection->{}", connection.getClass());

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT DBTIMEZONE, SESSIONTIMEZONE FROM DUAL");
        log.info("PreparedStatement->{}", preparedStatement.getClass());

        ResultSet resultSet = preparedStatement.executeQuery();
        log.info("ResultSet->{}", resultSet.getClass());

        if (resultSet.next()) {
            String dbTimezone = resultSet.getString("DBTIMEZONE");
            log.info("DBTIMEZONE => {}", dbTimezone);
            String sessionTimezone = resultSet.getString("SESSIONTIMEZONE");
            log.info("SESSIONTIMEZONE => {}", sessionTimezone);
        }

        connection.close();
    }

    private static void testPoolOracleDataSource() throws SQLException {
        PoolDataSource poolDataSource = PoolDataSourceFactory.getPoolDataSource();
        log.info("DataSource->{}", poolDataSource.getClass());
        poolDataSource.setURL("jdbc:oracle:thin:@//localhost:1521/FREE");
        poolDataSource.setUser("system");
        poolDataSource.setPassword("123456");
        poolDataSource.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        poolDataSource.setConnectionPoolName("My-POOL");
        poolDataSource.setSQLForValidateConnection("SELECT * FROM DUAL");
        poolDataSource.setFastConnectionFailoverEnabled(true);
        poolDataSource.setValidateConnectionOnBorrow(true);
        poolDataSource.setInitialPoolSize(1);
        poolDataSource.setMinPoolSize(2);
        poolDataSource.setMaxPoolSize(5);
        poolDataSource.setConnectionWaitDuration(Duration.ofSeconds(15));
        poolDataSource.setConnectionValidationTimeout(5);
        poolDataSource.setInactiveConnectionTimeout(1800);
        poolDataSource.setMaxConnectionReuseTime(1800);
        poolDataSource.setAbandonedConnectionTimeout(60);
        poolDataSource.setTimeToLiveConnectionTimeout(180);
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("oracle.net.keepAlive", "true");
        connectionProperties.setProperty("oracle.net.TCP_KEEPIDLE", "60");
        poolDataSource.setConnectionProperties(connectionProperties);

        Connection connection = poolDataSource.getConnection();
        log.info("Connection->{}", connection.getClass());

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT DBTIMEZONE, SESSIONTIMEZONE FROM DUAL");
        log.info("PreparedStatement->{}", preparedStatement.getClass());

        ResultSet resultSet = preparedStatement.executeQuery();
        log.info("ResultSet->{}", resultSet.getClass());

        if (resultSet.next()) {
            String dbTimezone = resultSet.getString("DBTIMEZONE");
            log.info("DBTIMEZONE => {}", dbTimezone);
            String sessionTimezone = resultSet.getString("SESSIONTIMEZONE");
            log.info("SESSIONTIMEZONE => {}", sessionTimezone);
        }

        connection.close();
    }
}
