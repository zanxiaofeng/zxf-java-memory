package zxf.java.memory.jdbc;

import java.sql.*;

public class JDBCAutoTransaction {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/FREE", "system", "123456")) {
            // By default a Connection object is in auto-commit mode, which means that it automatically commits changes after executing each statement. If auto-commit mode has been disabled, the method commit must be called explicitly in order to commit changes; otherwise, database changes will not be saved.

            try (Statement statement = connection.createStatement()) {

                statement.executeUpdate("INSERT INTO MY_TABLE_1(column1, column2) VALUES('123','456')");
                // The data is automatically committed after the execution of the statement.
                // 数据在语句执行后会自动提交
                System.out.println("Transaction committed successfully");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
