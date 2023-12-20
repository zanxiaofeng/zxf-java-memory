package zxf.java.memory.jdbc;

import oracle.jdbc.OracleStatement;
import oracle.sql.CLOB;
import org.apache.commons.io.IOUtils;
import org.openjdk.jol.info.GraphLayout;
import zxf.java.memory.util.DebugUtils;

import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static oracle.jdbc.OracleConnection.CONNECTION_PROPERTY_DEFAULT_LOB_PREFETCH_SIZE;
import static oracle.jdbc.OracleConnection.CONNECTION_PROPERTY_DEFAULT_LOB_PREFETCH_SIZE_DEFAULT;

public class JdbcClobTests {
    public static void main(String[] args) throws SQLException, IOException {
        System.gc();
        DebugUtils.printMemInfoFromRuntime("Start");

        testJdbcClob();

        System.gc();
        DebugUtils.printMemInfoFromRuntime("End");
    }

    public static void testJdbcClob() throws SQLException, IOException {
        System.out.println("oracle.jdbc.defaultLobPrefetchSize: " + CONNECTION_PROPERTY_DEFAULT_LOB_PREFETCH_SIZE_DEFAULT);
        Properties jdbcProperties = new Properties();
        jdbcProperties.setProperty("oracle.jdbc.user", "***");
        jdbcProperties.setProperty("oracle.jdbc.password", "***");
        jdbcProperties.setProperty(CONNECTION_PROPERTY_DEFAULT_LOB_PREFETCH_SIZE, "4000");
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@host:port/service", jdbcProperties);
        List<JdbcEntity> entities = queryJdbcClob(connection);
        processJdbcClob(entities);
        connection.close();
    }

    public static List<JdbcEntity> queryJdbcClob(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, DATA FROM MY_TEST_TABLE");
        // SELECT Length(ID) * 2, DBMS_LOB.GetLength(DATA) * 2 FROM MY_TEST_TABLE
        //((OracleStatement)preparedStatement).setLobPrefetchSize(4000);
        System.out.println("OracleStatement.LobPrefetchSize, " + ((OracleStatement) preparedStatement).getLobPrefetchSize());
        ResultSet resultSet = preparedStatement.executeQuery();

        Long totalSizeOfId = 0l;
        Long totalSizeOfClob = 0l;
        Long totalPrefetchOfClob = 0l;
        List<JdbcEntity> result = new ArrayList<>();
        while (resultSet.next()) {
            String id = resultSet.getString("ID");
            CLOB data = (CLOB) resultSet.getClob("DATA");
            result.add(new JdbcEntity(id, data));
            // 1 char = 2 bytes
            totalSizeOfId += id.length() * 2;
            totalSizeOfClob += data.length() * 2;
            totalPrefetchOfClob += data.getPrefetchedDataSize() * 2;
        }
        System.out.println("totalSizeOfId: " + totalSizeOfId + ", " + DebugUtils.formatSize(totalSizeOfId));
        System.out.println("totalSizeOfClob: " + totalSizeOfClob + ", " + DebugUtils.formatSize(totalSizeOfClob));
        System.out.println("totalPrefetchOfClob: " + totalPrefetchOfClob + ", " + DebugUtils.formatSize(totalPrefetchOfClob));

        System.out.println(GraphLayout.parseInstance(result).toFootprint());

        System.gc();
        DebugUtils.printMemInfoFromRuntime("After Query");
        return result;
    }

    public static void processJdbcClob(List<JdbcEntity> entities) throws SQLException, IOException {
        System.gc();
        DebugUtils.printMemInfoFromRuntime("Before Process");

        Long totalSize = 0l;
        for (int i = 0; i < entities.size(); i++) {
            JdbcEntity entity = entities.get(i);
            //String xmlFromData = entity.getData().getSubString(1, (int)entity.getData().length());
            Reader reader = entity.getData().getCharacterStream();
            String xmlFromData = IOUtils.toString(reader);
            totalSize += (entity.getId().length() + xmlFromData.length()) * 2;
            reader.close();
            entity.getData().free();
            entity.clearData();
            System.out.println(i + "::ID: " + entity.getId() + ", DATA: " + xmlFromData.length() * 2);
            if (i % 1000 == 0) {
                System.gc();
                DebugUtils.printMemInfoFromRuntime("In Process");
            }
        }
        System.out.println("totalSize: " + DebugUtils.formatSize(totalSize));

        System.gc();
        DebugUtils.printMemInfoFromRuntime("After Process");
    }
}
