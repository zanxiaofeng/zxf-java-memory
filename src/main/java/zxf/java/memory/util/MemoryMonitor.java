package zxf.java.memory.util;


import lombok.extern.slf4j.Slf4j;

import java.lang.management.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.apache.logging.log4j.util.ProcessIdUtil.getProcessId;

@Slf4j
public class MemoryMonitor {

    public static void startMonitoring(int intervalSeconds) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(MemoryMonitor::loggingMonitoringInfo, 0, intervalSeconds, TimeUnit.SECONDS);
        log.info("Memory monitoring started with interval: {} seconds", intervalSeconds);
    }

    public static void loggingMonitoringInfo() {
        String title = LocalDate.now().toString();
        logMemoryInfoFromMXBean(title);
        logMemoryInfoFromNMT(title);
        //logMemoryInfoFromJmap(title);
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
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        log.info("JVM Heap Memory Summary: {}", title);
        log.info("Initial: {}, Used: {}, Committed: {}, Max: {}",
                DebugUtils.formatSize(heapMemoryUsage.getInit()),
                DebugUtils.formatSize(heapMemoryUsage.getUsed()),
                DebugUtils.formatSize(heapMemoryUsage.getCommitted()),
                DebugUtils.formatSize(heapMemoryUsage.getMax())
        );
        log.info("JVM Heap Memory Pools: {}", title);
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            if (memoryPoolMXBean.getType() == MemoryType.HEAP) {
                MemoryUsage usage = memoryPoolMXBean.getUsage();
                log.info("Memory Pool: {}, Type: {}, Managers: {}, Initial: {}, Used: {}, Committed: {}, Max: {}",
                        memoryPoolMXBean.getName(), memoryPoolMXBean.getType(),
                        memoryPoolMXBean.getMemoryManagerNames(),
                        DebugUtils.formatSize(usage.getInit()),
                        DebugUtils.formatSize(usage.getUsed()),
                        DebugUtils.formatSize(usage.getCommitted()),
                        DebugUtils.formatSize(usage.getMax()));
            }
        }

        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        log.info("JVM Non-Heap Memory Summary: {}", title);
        log.info("Initial: {}, Used: {}, Committed: {}, Max: {}",
                DebugUtils.formatSize(nonHeapMemoryUsage.getInit()),
                DebugUtils.formatSize(nonHeapMemoryUsage.getUsed()),
                DebugUtils.formatSize(nonHeapMemoryUsage.getCommitted()),
                DebugUtils.formatSize(nonHeapMemoryUsage.getMax())
        );

        log.info("JVM Non-Heap Memory Pools: {}", title);
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            if (memoryPoolMXBean.getType() == MemoryType.NON_HEAP) {
                MemoryUsage usage = memoryPoolMXBean.getUsage();
                log.info("Memory Pool: {}, Type: {}, Managers: {}, Initial: {}, Used: {}, Committed: {}, Max: {}",
                        memoryPoolMXBean.getName(), memoryPoolMXBean.getType(),
                        memoryPoolMXBean.getMemoryManagerNames(),
                        DebugUtils.formatSize(usage.getInit()),
                        DebugUtils.formatSize(usage.getUsed()),
                        DebugUtils.formatSize(usage.getCommitted()),
                        DebugUtils.formatSize(usage.getMax()));
            }
        }

        log.info("Non-JVM Memory Pools: {}", title);
        for (BufferPoolMXBean bufferPoolMXBean : ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class)) {
            log.info("Memory Pool: {}, Used: {},Count: {},Total Capacity: {}", bufferPoolMXBean.getName(),
                    DebugUtils.formatSize(bufferPoolMXBean.getMemoryUsed()),
                    DebugUtils.formatSize(bufferPoolMXBean.getCount()),
                    DebugUtils.formatSize(bufferPoolMXBean.getTotalCapacity()));
        }
    }

    public static void logMemoryInfoFromNMT(String title) {
        String[] command = new String[]{"jcmd", getProcessId(), "VM.native_memory", "summary", "scale=MB"};
        DebugUtils.runCommand(command, title);
    }

    public static void logMemoryInfoFromJmap(String title) {
        String[] command = new String[]{"jmap", "-histo", getProcessId()};
        DebugUtils.runCommand(command, title);
    }
}
