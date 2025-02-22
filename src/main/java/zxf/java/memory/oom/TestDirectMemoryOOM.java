package zxf.java.memory.oom;

import zxf.java.memory.util.MemoryMonitor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestDirectMemoryOOM {
    public static List<ByteBuffer> contents = new ArrayList<>();

    //Please run with options: -XX:+UseG1GC -Xms256M -Xmx512M -XX:MaxDirectMemorySize=512M -XshowSettings -XX:+PrintGCDetails -XX:+PrintFlagsFinal -XX:+PrintCommandLineFlags -XX:NativeMemoryTracking=detail
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1024; i++) {
                //64M
                contents.add(ByteBuffer.allocateDirect(1024 * 1024 * 16));
                try {
                    Thread.sleep(2000);
                    System.out.println("..... " + i);
                    MemoryMonitor.loggingMonitoringInfo();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Scanner keyboard = new Scanner(System.in);

        MemoryMonitor.loggingMonitoringInfo();

        System.out.println("Please press enter to start");
        keyboard.nextLine();
        System.out.println("Started");

        thread.start();

        System.out.println("Please press enter to end");
        keyboard.nextLine();
    }
}
