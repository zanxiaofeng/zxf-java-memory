package zxf.java.memory.jol;

import org.openjdk.jol.info.ClassLayout;

public class ClassLayoutTests {
    public static void main(String[] args) {
        System.out.println(ClassLayout.parseClass(JolBean.class).toPrintable());
        System.out.println(ClassLayout.parseClass(JolBean.Data.class).toPrintable());

    }
}
