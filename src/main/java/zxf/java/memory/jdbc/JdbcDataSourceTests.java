package zxf.java.memory.jdbc;

import oracle.jdbc.pool.OracleDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcDataSourceTests {
    public static void main(String[] args) throws SQLException {
        testOracleDataSource();

    }

    private static void testOracleDataSource() throws SQLException {
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setDriverType("oracle.jdbc.OracleDriver");
        dataSource.setURL("jdbc:log4jdbc:oracle:thin:@host:port/service");
        dataSource.setUser("***");
        dataSource.setPassword("***");
        System.out.println("DataSource->" + dataSource.getClass());

        Connection connection = dataSource.getConnection();
        System.out.println("Connection->" + connection.getClass());

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT DBTIMEZONE, SESSIONTIMEZONE FROM DUAL");
        System.out.println("PreparedStatement->" + preparedStatement.getClass());

        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("ResultSet->" + resultSet.getClass());

        if (resultSet.next()) {
            String dbTimezone = resultSet.getString("DBTIMEZONE");
            System.out.println("DBTIMEZONE => " + dbTimezone.toString());
            String sessionTimezone = resultSet.getString("SESSIONTIMEZONE");
            System.out.println("SESSIONTIMEZONE => " + sessionTimezone.toString());
        }

        connection.close();
    }

    private static void testPoolOracleDataSource() throws SQLException {
        DataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
        System.out.println("DataSource->" + dataSource.getClass());

        Connection connection = dataSource.getConnection();
        System.out.println("Connection->" + connection.getClass());

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT DBTIMEZONE, SESSIONTIMEZONE FROM DUAL");
        System.out.println("PreparedStatement->" + preparedStatement.getClass());

        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("ResultSet->" + resultSet.getClass());

        if (resultSet.next()) {
            String dbTimezone = resultSet.getString("DBTIMEZONE");
            System.out.println("DBTIMEZONE => " + dbTimezone.toString());
            String sessionTimezone = resultSet.getString("SESSIONTIMEZONE");
            System.out.println("SESSIONTIMEZONE => " + sessionTimezone.toString());
        }

        connection.close();
    }
}
