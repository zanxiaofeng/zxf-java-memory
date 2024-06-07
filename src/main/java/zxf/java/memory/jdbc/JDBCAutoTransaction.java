package zxf.java.memory.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCAutoTransaction {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@host:port/service", "user", "passowrd")) {
            // By default a Connection object is in auto-commit mode, which means that it automatically commits changes after executing each statement. If auto-commit mode has been disabled, the method commit must be called explicitly in order to commit changes; otherwise, database changes will not be saved.

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("INSERT INTO MY_TABLE_1(column1, column2) VALUES('123','456')");
                // The data is automatically committed after the execution of the statement.
                System.out.println("Transaction committed successfully");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
