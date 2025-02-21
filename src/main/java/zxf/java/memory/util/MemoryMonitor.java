package zxf.java.memory.util;


import lombok.extern.slf4j.Slf4j;

import java.lang.management.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.apache.logging.log4j.util.ProcessIdUtil.getProcessId;
import static zxf.java.memory.util.DebugUtils.formatSize;

@Slf4j
public class MemoryMonitor {

    public static void startMonitoring(int intervalSeconds) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(MemoryMonitor::loggingMonitoringInfo, 0, intervalSeconds, TimeUnit.SECONDS);
        log.info("Memory monitoring started with interval: {} seconds", intervalSeconds);
    }

    public static void loggingMonitoringInfo() {
        String title = LocalDateTime.now().toString();
        logMemoryInfoFromMXBean(title);
        logMemoryInfoFromNMT(title);
        //logMemoryInfoFromJmap(title);
        logCgroupMemoryInfo(title);
    }

    /*
     * init - represents the initial amount of memory (in bytes) that the Java virtual machine requests from the operating system for memory management during startup. The Java virtual machine may request additional memory from the operating system and may also release memory to the system over time. The value of init may be undefined.
     * used - represents the amount of memory currently used (in bytes).
     * committed - represents the amount of memory (in bytes) that is guaranteed to be available for use by the Java virtual machine. The amount of committed memory may change over time (increase or decrease). The Java virtual machine may release memory to the system and committed could be less than init. committed will always be greater than or equal to used.
     * max - represents the maximum amount of memory (in bytes) that can be used for memory management. Its value may be undefined. The maximum amount of memory may change over time if defined. The amount of used and committed memory will always be less than or equal to max if max is defined. A memory allocation may fail if it attempts to increase the used memory such that used > committed even if used <= max would still be true (for example, when the system is low on virtual memory).
     * +----------------------------------------------+
     * +////////////////           |                  +
     * +////////////////           |                  +
     * +----------------------------------------------+
     * |--------|
     *   init
     * |---------------|
     *       used
     * |---------------------------|
     *          committed
     * |----------------------------------------------|
     *                    max
     */
    public static void logMemoryInfoFromMXBean(String title) {
        log.info("logMemoryInfoFromMXBean: {}", title);
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

        System.out.println("=== JVM Heap Memory ===");
        printMemoryUsage(memoryMXBean.getHeapMemoryUsage(), "");
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            if (memoryPoolMXBean.getType() == MemoryType.HEAP) {
                System.out.println("   *Pool: " + memoryPoolMXBean.getName());
                printMemoryUsage(memoryPoolMXBean.getUsage(), "    ");
            }
        }

        System.out.println("=== JVM Non-Heap Memory ===");
        printMemoryUsage(memoryMXBean.getNonHeapMemoryUsage(), "");
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            if (memoryPoolMXBean.getType() == MemoryType.NON_HEAP) {
                System.out.println("   *Pool: " + memoryPoolMXBean.getName());
                printMemoryUsage(memoryPoolMXBean.getUsage(), "    ");
            }
        }

        System.out.println("=== Non-JVM Memory Pools ===");
        for (BufferPoolMXBean bufferPoolMXBean : ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class)) {
            System.out.println("   *Pool           : " + bufferPoolMXBean.getName());
            System.out.println("    Used           : " + bufferPoolMXBean.getMemoryUsed());
            System.out.println("    Count          : " + bufferPoolMXBean.getCount());
            System.out.println("    Total Capacity : " + bufferPoolMXBean.getTotalCapacity());
        }
    }

    public static void logMemoryInfoFromNMT(String title) {
        log.info("logMemoryInfoFromNMT: {}", title);
        DebugUtils.runCommand(new String[]{"jcmd", getProcessId(), "VM.native_memory", "summary", "scale=MB"});
    }

    public static void logMemoryInfoFromJmap(String title) {
        log.info("logMemoryInfoFromJmap: {}", title);
        DebugUtils.runCommand(new String[]{"jmap", "-histo", getProcessId()});
    }

    public static void logCgroupMemoryInfo(String title) {
        log.info("logCgroupMemoryInfo: {}", title);
        //Cgroup V1
        DebugUtils.runCommand(new String[]{"cat", "/sys/fs/cgroup/memory/memory.limit_in_bytes"});
        DebugUtils.runCommand(new String[]{"cat", "/sys/fs/cgroup/memory/memory.usage_in_bytes"});

        //Cgroup V2
        DebugUtils.runCommand(new String[]{"cat", "/sys/fs/cgroup/memory.max"});
        DebugUtils.runCommand(new String[]{"cat", "/sys/fs/cgroup/memory.current"});
    }

    private static void printMemoryUsage(MemoryUsage usage, String prefix) {
        System.out.println(prefix + "Used : " + formatSize(usage.getUsed()));
        System.out.println(prefix + "Committed : " + formatSize(usage.getCommitted()));
        System.out.println(prefix + "Max : " + formatSize(usage.getMax()));
    }
}
