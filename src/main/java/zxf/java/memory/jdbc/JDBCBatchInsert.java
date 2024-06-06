package zxf.java.memory.jdbc;

import java.sql.*;

public class JDBCBatchInsert {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@host:port/service", "user", "passowrd")) {
            connection.setAutoCommit(false);

            String sql = "INSERT INTO MY_TABLE_1(column1) VALUES(?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (int i = 0; i < 10000; i++) {
                    statement.setString(1, "Value_" + i);
                    statement.addBatch();
                    if (i % 1000 == 0) {
                        statement.executeBatch();
                    }
                }
                statement.executeBatch();

                connection.commit();
                System.out.println("Transaction committed successfully");
            } catch (Exception ex) {
                connection.rollback();
                System.out.println("Transaction rolled back due to error: " + ex.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }
}
