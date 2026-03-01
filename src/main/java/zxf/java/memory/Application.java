package zxf.java.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zxf.java.memory.util.MemoryMonitor;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Slf4j
@SpringBootApplication
public class Application {
    //Please run with options: -XX:+UseG1GC -Xms256M -Xmx1024M -XshowSettings -XX:+PrintFlagsFinal -XX:NativeMemoryTracking=detail
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            log.error("Exception, thread={}", thread, throwable);
        });

        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void afterStarted() {
        MemoryMonitor.startMonitoring(150);
    }

    @PreDestroy
    public void beforeShutdown() {
        MemoryMonitor.stopMonitoring();
    }
}
