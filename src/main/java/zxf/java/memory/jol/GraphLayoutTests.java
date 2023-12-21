package zxf.java.memory.jol;

import org.openjdk.jol.info.GraphLayout;

import java.util.Arrays;

public class GraphLayoutTests {
    public static void main(String[] args) {
        Foo foo1 = Foo.create(Arrays.asList("12345", "67890", "ABCDE", "54321", "09876"));
        System.out.println(GraphLayout.parseInstance(foo1).toPrintable());
        System.out.println(GraphLayout.parseInstance(foo1).toFootprint());
        System.out.println("totalSize: " + GraphLayout.parseInstance(foo1).totalSize());

        Foo foo2 = Foo.create(Arrays.asList("12345", "67890", "ABCDE", "54321", "09876", "12345", "67890", "ABCDE", "54321", "09876"));
        System.out.println(GraphLayout.parseInstance(foo2).toPrintable());
        System.out.println(GraphLayout.parseInstance(foo2).toFootprint());
        System.out.println("totalSize: " + GraphLayout.parseInstance(foo2).totalSize());
    }
}
