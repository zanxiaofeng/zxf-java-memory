package zxf.java.memory.jol;

import org.openjdk.jol.info.GraphLayout;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GraphLayoutTests {
    public static void main(String[] args) {
        System.out.println("#################GraphLayout Info of String#################");
        String str = "1235赞";
        System.out.println(GraphLayout.parseInstance(str).toPrintable());
        System.out.println(GraphLayout.parseInstance(str).toFootprint());
        System.out.println("totalSize: " + GraphLayout.parseInstance(str).totalSize());

        System.out.println("#################GraphLayout Info of Byte[]#################");
        byte[] bytes = "1235赞".getBytes(StandardCharsets.UTF_16LE);
        System.out.println(GraphLayout.parseInstance(bytes).toPrintable());
        System.out.println(GraphLayout.parseInstance(bytes).toFootprint());
        System.out.println("totalSize: " + GraphLayout.parseInstance(bytes).totalSize());

        System.out.println("#################GraphLayout Info of Foo1#################");
        Foo foo1 = Foo.create(Arrays.asList("12345A", "67890", "ABCDE", "54321", "09876"));
        System.out.println(GraphLayout.parseInstance(foo1).toPrintable());
        System.out.println(GraphLayout.parseInstance(foo1).toFootprint());
        System.out.println("totalSize: " + GraphLayout.parseInstance(foo1).totalSize());

        System.out.println("#################GraphLayout Info of Foo2#################");
        Foo foo2 = Foo.create(Arrays.asList("12345A", "67890", "ABCDE", "54321", "09876", "12345", "67890", "ABCDE", "54321", "09876"));
        System.out.println(GraphLayout.parseInstance(foo2).toPrintable());
        System.out.println(GraphLayout.parseInstance(foo2).toFootprint());
        System.out.println("totalSize: " + GraphLayout.parseInstance(foo2).totalSize());
    }
}
