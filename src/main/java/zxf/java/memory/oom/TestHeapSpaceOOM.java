package zxf.java.memory.oom;

import zxf.java.memory.util.DebugUtils;
import zxf.java.memory.util.MemoryMonitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestHeapSpaceOOM {
    public static List<byte[]> contents = new ArrayList<>();

    //Please run with options: -XX:+UseG1GC -Xms256M -Xmx1024M -XshowSettings -XX:+PrintGCDetails -XX:+PrintFlagsFinal -XX:NativeMemoryTracking=detail
    public static void main(String[] args) throws InterruptedException, IOException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1024; i++) {
                //64M
                contents.add(new byte[1024 * 1024 * 64]);
                try {
                    Thread.sleep(2000);
                    System.out.println("..... " + i);
                    //DebugUtils.callJcmd("for heap space." + i);
                    //DebugUtils.printMemInfoFromMXBean("for heap space." + i);
                    //MemoryMonitor.logMemoryUsage();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        Scanner keyboard = new Scanner(System.in);

        DebugUtils.callJcmd("for heap space.begin");
        DebugUtils.printMemInfoFromMXBean("for heap space.begin");
        MemoryMonitor.logMemoryUsage();

        System.out.println("Please press enter to start");
        keyboard.nextLine();
        System.out.println("Started");

        thread.start();

        System.out.println("Please press enter to end");
        keyboard.nextLine();
    }
}
