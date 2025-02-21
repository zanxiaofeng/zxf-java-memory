package zxf.java.memory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zxf.java.memory.util.MemoryMonitor;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {
    //Please run with options: -XX:+UseG1GC -Xms256M -Xmx1024M -XshowSettings -XX:+PrintFlagsFinal -XX:NativeMemoryTracking=detail
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.out.println("Exception, thread=" + thread + ", throwable=" + throwable);
            throwable.printStackTrace(System.out);
        });

        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void afterStarted() {
        MemoryMonitor.startMonitoring(30);
    }
}
