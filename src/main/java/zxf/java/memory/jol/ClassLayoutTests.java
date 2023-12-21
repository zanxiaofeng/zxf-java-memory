package zxf.java.memory.jol;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ClassLayoutTests {
    public static void main(String[] args) {
        System.out.println(VM.current().details());
        System.out.println("#################Class Info#################");
        System.out.println(ClassLayout.parseClass(Foo.class).toPrintable());
        System.out.println(ClassLayout.parseClass(Foo.Bar.class).toPrintable());
        System.out.println("#################Instance Info of Foo#################");
        Foo foo = Foo.create(Arrays.asList("12345", "67890"));
        System.out.println(ClassLayout.parseInstance(foo).toPrintable());
        System.out.println(ClassLayout.parseInstance(foo.getStr().toCharArray()).toPrintable());
        System.out.println(ClassLayout.parseInstance(foo.getBar()).toPrintable());
        System.out.println(ClassLayout.parseInstance(foo.getBar().getCh()).toPrintable());
        System.out.println(ClassLayout.parseInstance(foo.getBar().getIds()).toPrintable());
        System.out.println(ClassLayout.parseInstance(foo.getBar().getIds().get(0)).toPrintable());
        System.out.println(ClassLayout.parseInstance(foo.getBar().getIds().get(1)).toPrintable());
        System.out.println("#################Instance Info of String#################");
        System.out.println(ClassLayout.parseInstance("1235赞").toPrintable());

        System.out.println("#################Instance Info of Byte[]#################");
        System.out.println(ClassLayout.parseInstance("1235赞".getBytes(StandardCharsets.UTF_16LE)).toPrintable());
    }
}
