package zxf.java.memory.jdbc;

import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.pool.OracleDataSourceFactory;

import java.sql.*;

@Slf4j
public class JDBCBatchInsert {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/FREE", "system", "123456")) {
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
                log.info("Transaction committed successfully");
            } catch (Exception ex) {
                connection.rollback();
                log.error("Transaction rolled back due to error", ex);
            }
        }
    }
}
