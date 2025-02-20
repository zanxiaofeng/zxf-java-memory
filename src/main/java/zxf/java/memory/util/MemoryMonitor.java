package zxf.java.memory.util;


import java.lang.management.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MemoryMonitor {

    public static void startMonitoring(int intervalSeconds) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(MemoryMonitor::logMemoryUsage, 0, intervalSeconds, TimeUnit.SECONDS);
        System.out.printf("Memory monitoring started with interval: %d seconds%n", intervalSeconds);
    }

    public static void logMemoryUsage() {
        System.out.println("Summary::");
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        System.out.printf("Heap usage: %s%n", memoryMXBean.getHeapMemoryUsage().toString());
        System.out.printf("Non-Heap usage: %s%n", memoryMXBean.getNonHeapMemoryUsage().toString());


        System.out.println("Details::");
        for (MemoryPoolMXBean memoryPoolMXBean : ManagementFactory.getMemoryPoolMXBeans()) {
            System.out.printf("%s:%n", memoryPoolMXBean.getName());
            System.out.printf("  managers: %s %n", String.join(", ", memoryPoolMXBean.getMemoryManagerNames()));
            System.out.printf("  type: %s %n", memoryPoolMXBean.getType());
            System.out.printf("  usage: %s %n", memoryPoolMXBean.getUsage());
            System.out.printf("  peakUsage: %s %n", memoryPoolMXBean.getPeakUsage());
            System.out.println();
        }
        for (BufferPoolMXBean bufferPoolBean : ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class)) {
            System.out.printf("%s:%n", bufferPoolBean.getName());
            System.out.printf("  usage: %s%n", bufferPoolBean.getMemoryUsed());
            System.out.printf("  count: %d%n", bufferPoolBean.getCount());
            System.out.printf("  total capacity: %d%n", bufferPoolBean.getTotalCapacity());
        }
    }

    public static void logMemoryManagerMXBeans() {
        for (MemoryManagerMXBean memoryManagerMXBean : ManagementFactory.getMemoryManagerMXBeans()) {
            System.out.println("MemoryManagerMXBeans");
            System.out.printf("  %s, %s, %s, %s", memoryManagerMXBean.getName(), memoryManagerMXBean.isValid(), memoryManagerMXBean.getClass(), String.join("|", memoryManagerMXBean.getMemoryPoolNames()));
        }
    }

    public static void main(String[] args) {
        logMemoryManagerMXBeans();
    }

}
