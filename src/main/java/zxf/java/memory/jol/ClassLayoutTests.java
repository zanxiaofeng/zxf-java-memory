package zxf.java.memory.jol;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class ClassLayoutTests {
    public static void main(String[] args) {
        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseClass(Foo.class).toPrintable());
        System.out.println(ClassLayout.parseClass(Foo.Bar.class).toPrintable());
        Foo foo = Foo.create();
        System.out.println(ClassLayout.parseInstance(foo).toPrintable());
        System.out.println(ClassLayout.parseInstance(foo.getStr()).toPrintable());
        System.out.println(ClassLayout.parseInstance(foo.getBar().getIds()).toPrintable());
        System.out.println(ClassLayout.parseInstance(foo.getBar().getIds().get(0)).toPrintable());
        System.out.println(ClassLayout.parseInstance(foo.getBar().getIds().get(1)).toPrintable());
    }
}
