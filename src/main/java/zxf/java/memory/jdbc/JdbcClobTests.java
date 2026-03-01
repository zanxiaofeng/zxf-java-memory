package zxf.java.memory.jdbc;

import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleStatement;
import oracle.sql.CLOB;
import org.apache.commons.io.IOUtils;
import org.openjdk.jol.info.GraphLayout;
import zxf.java.memory.util.DebugUtils;
import zxf.java.memory.util.MemoryMonitor;

import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static oracle.jdbc.OracleConnection.CONNECTION_PROPERTY_DEFAULT_LOB_PREFETCH_SIZE;
import static oracle.jdbc.OracleConnection.CONNECTION_PROPERTY_DEFAULT_LOB_PREFETCH_SIZE_DEFAULT;

@Slf4j
public class JdbcClobTests {
    public static void main(String[] args) throws SQLException, IOException {
        System.gc();
        MemoryMonitor.logMemoryInfoFromMXBean("Start");

        testJdbcClob();

        System.gc();
        MemoryMonitor.logMemoryInfoFromMXBean("End");
    }

    public static void testJdbcClob() throws SQLException, IOException {
        log.info("oracle.jdbc.defaultLobPrefetchSize: {}", CONNECTION_PROPERTY_DEFAULT_LOB_PREFETCH_SIZE_DEFAULT);
        Properties jdbcProperties = new Properties();
        jdbcProperties.setProperty("oracle.jdbc.user", "system");
        jdbcProperties.setProperty("oracle.jdbc.password", "123456");
        jdbcProperties.setProperty(CONNECTION_PROPERTY_DEFAULT_LOB_PREFETCH_SIZE, "4000");
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/FREE", jdbcProperties);
        List<JdbcEntity> entities = queryJdbcClob(connection);
        processJdbcClob(entities);
        connection.close();
    }

    public static List<JdbcEntity> queryJdbcClob(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID, DATA FROM MY_TEST_TABLE");
        // SELECT Length(ID) * 2, DBMS_LOB.GetLength(DATA) * 2 FROM MY_TEST_TABLE
        //((OracleStatement)preparedStatement).setLobPrefetchSize(4000);
        log.info("OracleStatement.LobPrefetchSize, {}", ((OracleStatement) preparedStatement).getLobPrefetchSize());
        ResultSet resultSet = preparedStatement.executeQuery();

        Long totalSizeOfId = 0L;
        Long totalSizeOfClob = 0L;
        Long totalPrefetchOfClob = 0L;
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
        log.info("totalSizeOfId: {}, {}", totalSizeOfId, DebugUtils.formatSize(totalSizeOfId));
        log.info("totalSizeOfClob: {}, {}", totalSizeOfClob, DebugUtils.formatSize(totalSizeOfClob));
        log.info("totalPrefetchOfClob: {}, {}", totalPrefetchOfClob, DebugUtils.formatSize(totalPrefetchOfClob));

        log.info(GraphLayout.parseInstance(result).toFootprint());

        System.gc();
        MemoryMonitor.logMemoryInfoFromMXBean("After Query");
        return result;
    }

    public static void processJdbcClob(List<JdbcEntity> entities) throws SQLException, IOException {
        System.gc();
        MemoryMonitor.logMemoryInfoFromMXBean("Before Process");

        Long totalSize = 0L;
        for (int i = 0; i < entities.size(); i++) {
            JdbcEntity entity = entities.get(i);
            //String xmlFromData = entity.getData().getSubString(1, (int)entity.getData().length());
            Reader reader = entity.getData().getCharacterStream();
            String xmlFromData = IOUtils.toString(reader);
            totalSize += (entity.getId().length() + xmlFromData.length()) * 2;
            reader.close();
            entity.getData().free();
            entity.clearData();
            log.info("{}::ID: {}, DATA: {}", i, entity.getId(), xmlFromData.length() * 2);
            if (i % 1000 == 0) {
                System.gc();
                MemoryMonitor.logMemoryInfoFromMXBean("In Process");
            }
        }
        log.info("totalSize: {}", DebugUtils.formatSize(totalSize));

        System.gc();
        MemoryMonitor.logMemoryInfoFromMXBean("After Process");
    }
}
