package zxf.java.memory.jol;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
public class ClassLayoutTests {
    public static void main(String[] args) {
        log.info(VM.current().details());
        log.info("#################Class Info#################");
        log.info(ClassLayout.parseClass(Foo.class).toPrintable());
        log.info(ClassLayout.parseClass(Foo.Bar.class).toPrintable());
        log.info("#################Instance Info of Foo#################");
        Foo foo = Foo.create(Arrays.asList("12345", "67890"));
        log.info(ClassLayout.parseInstance(foo).toPrintable());
        log.info(ClassLayout.parseInstance(foo.getStr().toCharArray()).toPrintable());
        log.info(ClassLayout.parseInstance(foo.getBar()).toPrintable());
        log.info(ClassLayout.parseInstance(foo.getBar().getCh()).toPrintable());
        log.info(ClassLayout.parseInstance(foo.getBar().getIds()).toPrintable());
        log.info(ClassLayout.parseInstance(foo.getBar().getIds().get(0)).toPrintable());
        log.info(ClassLayout.parseInstance(foo.getBar().getIds().get(1)).toPrintable());
        log.info("#################Instance Info of String#################");
        log.info(ClassLayout.parseInstance("1235赞").toPrintable());

        log.info("#################Instance Info of Byte[]#################");
        log.info(ClassLayout.parseInstance("1235赞".getBytes(StandardCharsets.UTF_16LE)).toPrintable());
    }
}
