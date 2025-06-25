package zxf.java.memory.jdbc;

import java.sql.*;

public class JDBCDataInitializer {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/FREE", "system", "123456")) {
            try (Statement statement = connection.createStatement()) {
                DatabaseMetaData dbMetaData = connection.getMetaData();
                ResultSet table1 = dbMetaData.getTables(null, null, "MY_TEST_TABLE", null);
                if (!table1.next()) {
                    statement.executeUpdate("CREATE TABLE MY_TEST_TABLE(ID VARCHAR2(50) PRIMARY KEY, CL_DATE DATE, CL_TIMESTAMP TIMESTAMP(6), CL_TIMESTAMP_TZ TIMESTAMP(6) WITH TIME ZONE, CL_TIMESTAMP_LTZ TIMESTAMP(6) WITH LOCAL TIME ZONE)");
                    statement.executeUpdate("INSERT INTO MY_TEST_TABLE(ID, CL_DATE, CL_TIMESTAMP, CL_TIMESTAMP_TZ, CL_TIMESTAMP_LTZ) VALUES ('id-1', TO_DATE('2023-01-01', 'YYYY-MM-DD'), TO_TIMESTAMP('2023-01-01 12:30:45.123456', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP_TZ('2023-01-01 12:30:45.123456 +08:00', 'YYYY-MM-DD HH24:MI:SS.FF6 TZH:TZM'), TO_TIMESTAMP('2023-01-01 12:30:45.123456', 'YYYY-MM-DD HH24:MI:SS.FF6'))");

                }
                ResultSet table2 = dbMetaData.getTables(null, null, "MY_TABLE_1", null);
                if (!table2.next()) {
                    statement.executeUpdate("CREATE TABLE MY_TABLE_1 (column1 VARCHAR(255), column2 VARCHAR(255))");
                }

                ResultSet table3 = dbMetaData.getTables(null, null, "MY_TABLE_2", null);
                if (!table3.next()) {
                    statement.executeUpdate("CREATE TABLE MY_TABLE_2 (column1 VARCHAR(255), column2 VARCHAR(255))");
                }

                System.out.println("Transaction committed successfully");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
