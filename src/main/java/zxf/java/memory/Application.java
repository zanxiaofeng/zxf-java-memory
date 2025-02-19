package zxf.java.memory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zxf.java.memory.util.DebugUtils;

@SpringBootApplication
public class Application {
    //Please run with options: -XX:+UseG1GC -Xms512M -Xmx1024M -XshowSettings -XX:+PrintFlagsFinal
    public static void main(String[] args) {
        DebugUtils.printMemInfoFromMXBean("Application.start");

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.out.println("Exception, thread=" + thread + ", throwable=" + throwable);
            throwable.printStackTrace(System.out);
        });

        SpringApplication.run(Application.class, args);
    }
}
