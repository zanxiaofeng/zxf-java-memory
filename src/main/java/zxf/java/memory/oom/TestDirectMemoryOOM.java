package zxf.java.memory.oom;

import zxf.java.memory.util.DebugUtils;
import zxf.java.memory.util.MemoryMonitor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestDirectMemoryOOM {
    public static List<ByteBuffer> contents = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1024; i++) {
                //64M
                contents.add(ByteBuffer.allocateDirect(1024 * 1024));
                try {
                    Thread.sleep(2000);
                    System.out.println("..... " + i);
                    DebugUtils.callJcmd("for direct memory space." + i);
                    DebugUtils.printMemInfoFromMXBean("for direct memory space." + i);
                    MemoryMonitor.logMemoryUsage();
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Scanner keyboard = new Scanner(System.in);

        DebugUtils.callJcmd("for direct memory space.begin");
        DebugUtils.printMemInfoFromMXBean("for direct memory space.begin");
        MemoryMonitor.logMemoryUsage();

        System.out.println("Please press enter to start");
        keyboard.nextLine();
        System.out.println("Started");

        thread.start();

        System.out.println("Please press enter to end");
        keyboard.nextLine();
    }
}
