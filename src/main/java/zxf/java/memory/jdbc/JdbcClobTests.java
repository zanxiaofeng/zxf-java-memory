package zxf.java.memory.jdbc;

import org.apache.commons.io.IOUtils;
import zxf.java.memory.util.DebugUtils;

import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcClobTests {
    public static void main(String[] args) throws SQLException, IOException {
        System.gc();
        DebugUtils.printMemInfoFromRuntime("Start");

        testJdbcClob();

        System.gc();
        DebugUtils.printMemInfoFromRuntime("End");
    }

    public static void testJdbcClob() throws SQLException, IOException {
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@host:port/service", "***", "***");
        List<JdbcEntity> entities = queryJdbcClob(connection);
        processJdbcClob(entities);
        connection.close();
    }

    public static List<JdbcEntity> queryJdbcClob(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, DATA FROM MY_TEST_TABLE");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<JdbcEntity> result = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString("ID");
            Clob data = resultSet.getClob("DATA");
            result.add(new JdbcEntity(id, data));
        }

        System.gc();
        DebugUtils.printMemInfoFromRuntime("After Query");
        return result;
    }

    public static void processJdbcClob(List<JdbcEntity> entities) throws SQLException, IOException {
        System.gc();
        DebugUtils.printMemInfoFromRuntime("Before Process");

        for (int i = 0; i < entities.size(); i++) {
            JdbcEntity entity = entities.get(i);
            if (i == 0) {
                //oracle.sql.CLOB
                System.out.println(entity.getData().getClass().getName());
            }
            //String xmlFromData = entity.getData().getSubString(1, (int)entity.getData().length());
            Reader reader = entity.getData().getCharacterStream();
            String xmlFromData = IOUtils.toString(reader);
            reader.close();
            entity.getData().free();
            entity.clearData();
            System.out.println(i + "::ID: " + entity.getId() + ", DATA: " + xmlFromData.length());
        }

        System.gc();
        DebugUtils.printMemInfoFromRuntime("After Process");
    }
}
