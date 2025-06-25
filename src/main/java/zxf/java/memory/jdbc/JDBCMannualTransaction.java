package zxf.java.memory.jdbc;

import java.sql.*;

public class JDBCMannualTransaction {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/FREE", "system", "123456")) {
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("INSERT INTO MY_TABLE_1(column1, column2) VALUES('123','456')");
                statement.executeUpdate("UPDATE MY_TABLE_2 SET column2='345' WHERE column1='123'");
                //Add other operations here like local file I/O or network I/O.
                connection.commit();
                System.out.println("Transaction committed successfully");
            } catch (Exception ex) {
                connection.rollback();
                System.out.println("Transaction rolled back due to error: " + ex.getMessage());
            }
        }
        // Assumption: the autoCommit will be set to true when the connection's close method is called.
    }
}
