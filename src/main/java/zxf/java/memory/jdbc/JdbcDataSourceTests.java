package zxf.java.memory.jdbc;

import oracle.jdbc.pool.OracleDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcDataSourceTests {
    public static void main(String[] args) throws SQLException {


    }

    private static void testOracleDataSource() throws SQLException {
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setURL("jdbc:log4jdbc:oracle:thin:@host:port/service");
        dataSource.setUser("***");
        dataSource.setPassword("***");
        System.out.println("DataSource->" + dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("Connection->" + connection.getClass());
        connection.close();
    }

    private static void testPoolOracleDataSource() throws SQLException {
        DataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
        System.out.println("DataSource->" + dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("Connection->" + connection.getClass());
        connection.close();
    }
}
