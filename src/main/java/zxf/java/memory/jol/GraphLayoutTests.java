package zxf.java.memory.jol;

import org.openjdk.jol.info.GraphLayout;

public class GraphLayoutTests {
    public static void main(String[] args) {
        Foo foo = Foo.create();
        System.out.println(GraphLayout.parseInstance(foo).toPrintable());
        System.out.println(GraphLayout.parseInstance(foo).toFootprint());
        System.out.println("totalSize: " + GraphLayout.parseInstance(foo).totalSize());
    }
}
