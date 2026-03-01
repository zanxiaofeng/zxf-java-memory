package zxf.java.memory.jol;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.GraphLayout;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
public class GraphLayoutTests {
    public static void main(String[] args) {
        log.info("#################GraphLayout Info of String#################");
        String str = "1235赞";
        log.info(GraphLayout.parseInstance(str).toPrintable());
        log.info(GraphLayout.parseInstance(str).toFootprint());
        log.info("totalSize: {}", GraphLayout.parseInstance(str).totalSize());

        log.info("#################GraphLayout Info of Byte[]#################");
        byte[] bytes = "1235赞".getBytes(StandardCharsets.UTF_16LE);
        log.info(GraphLayout.parseInstance(bytes).toPrintable());
        log.info(GraphLayout.parseInstance(bytes).toFootprint());
        log.info("totalSize: {}", GraphLayout.parseInstance(bytes).totalSize());

        log.info("#################GraphLayout Info of Foo1#################");
        Foo foo1 = Foo.create(Arrays.asList("12345A", "67890", "ABCDE", "54321", "09876"));
        log.info(GraphLayout.parseInstance(foo1).toPrintable());
        log.info(GraphLayout.parseInstance(foo1).toFootprint());
        log.info("totalSize: {}", GraphLayout.parseInstance(foo1).totalSize());

        log.info("#################GraphLayout Info of Foo2#################");
        Foo foo2 = Foo.create(Arrays.asList("12345A", "67890", "ABCDE", "54321", "09876", "12345", "67890", "ABCDE", "54321", "09876"));
        log.info(GraphLayout.parseInstance(foo2).toPrintable());
        log.info(GraphLayout.parseInstance(foo2).toFootprint());
        log.info("totalSize: {}", GraphLayout.parseInstance(foo2).totalSize());
    }
}
