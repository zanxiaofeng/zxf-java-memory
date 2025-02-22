package zxf.java.memory.oom;

import zxf.java.memory.util.MemoryMonitor;

import java.io.IOException;
import java.util.Scanner;

public class TestThreadStackSpaceOOM {

    //Please run with options: -XX:+UseG1GC -Xss64M -XshowSettings -XX:+PrintFlagsFinal -XX:+PrintCommandLineFlags -XX:NativeMemoryTracking=detail
    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner keyboard = new Scanner(System.in);

        MemoryMonitor.loggingMonitoringInfo();

        System.out.println("Please press enter to start");
        keyboard.nextLine();

        System.out.println("Started");
        for (int i = 0; i < 102400; i++) {
            final Integer x = i + 1;
            Thread thread = new Thread(() -> {
                try {
                    if (x % 1000 == 0) {
                        System.out.println("..... " + x);
                    }
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
            if (i % 5000 == 0) {
                MemoryMonitor.loggingMonitoringInfo();
            }
        }

        System.out.println("Please press enter to end");
        keyboard.nextLine();
    }
}
