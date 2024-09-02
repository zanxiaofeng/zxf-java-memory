package zxf.java.memory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.json.GsonBuilderUtils;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.out.println("Exception, thread=" + thread + ", throwable=" + throwable);
            throwable.printStackTrace(System.out);
        });
        SpringApplication.run(Application.class, args);
    }
}
