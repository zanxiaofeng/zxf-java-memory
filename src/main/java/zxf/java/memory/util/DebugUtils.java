package zxf.java.memory.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class DebugUtils {

    /*
     * Runtime.getRuntime().totalMemory(), Get current size of heap in bytes
     * Runtime.getRuntime().maxMemory(), Get maximum size of heap in bytes. The heap cannot grow beyond this size. Any attempt will result in an OutOfMemoryException.
     * Runtime.getRuntime().freeMemory(), Get amount of free memory within the heap in bytes. This size will increase after garbage collection and decrease as new objects are created.
     */
    public static void printMemInfoFromRuntime(String title) {
        long heapTotalSize = Runtime.getRuntime().totalMemory();
        long heapMaxSize = Runtime.getRuntime().maxMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long heapUsedSize = heapTotalSize - heapFreeSize;
        System.out.println(String.format("Heap usage for %s: max=%s, total=%s, used=%s, free=%s", title, formatSize(heapMaxSize), formatSize(heapTotalSize), formatSize(heapUsedSize), formatSize(heapFreeSize)));
        printMemInfoFromMXBean(title);
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
    public static void printMemInfoFromMXBean(String title) {
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        System.out.println(String.format("Heap usage for %s: max=%s, commit=%s, used=%s, free=%s, init=%s", title, formatSize(heapMemoryUsage.getMax()), formatSize(heapMemoryUsage.getCommitted()), formatSize(heapMemoryUsage.getUsed()), formatSize(heapMemoryUsage.getCommitted() - heapMemoryUsage.getUsed()), formatSize(heapMemoryUsage.getInit())));
    }

    public static void callJmap(String title) throws IOException, InterruptedException {
        System.out.println("Call jmap: " + title + " - " + ProcessHandle.current().pid());
        Process process = Runtime.getRuntime().exec("jmap -histo " + ProcessHandle.current().pid());
        logOutput(process.getInputStream(), "");
        logOutput(process.getErrorStream(), "ERROR - ");
        if (process.waitFor(30, TimeUnit.MINUTES)) {
            System.out.println("Call jmap: exit code=" + process.exitValue());
        } else {
            System.out.println("Call jmap: timeout");
        }
    }

    public static void main(String[] args) {
        System.out.println(formatSize(888));
        System.out.println(formatSize(888 * 1024));
        System.out.println(formatSize(888 * 1024 + 512)) ;
        System.out.println(formatSize(888 * 1024 * 1024 + 512 * 1024));
        System.out.println(formatSize(888 * 1024 * 1024 * 1024l + 512 * 1024 * 1024l));
    }

    public static String formatSize(long v) {
        if (v < 1024) return v + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double) v / (1L << (z * 10)), " KMGTPE".charAt(z));
    }

    private static void logOutput(InputStream inputStream, String prefix) {
        new Thread(() -> {
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            while (scanner.hasNextLine()) {
                synchronized (DebugUtils.class) {
                    System.out.println(prefix + scanner.nextLine());
                }
            }
            scanner.close();
        }).start();
    }
}
