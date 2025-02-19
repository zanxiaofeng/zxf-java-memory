package zxf.java.memory.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        System.out.println(String.format("Non-Heap usage for %s: max=%s, commit=%s, used=%s, free=%s, init=%s", title, formatSize(nonHeapMemoryUsage.getMax()), formatSize(nonHeapMemoryUsage.getCommitted()), formatSize(nonHeapMemoryUsage.getUsed()), formatSize(nonHeapMemoryUsage.getCommitted() - nonHeapMemoryUsage.getUsed()), formatSize(nonHeapMemoryUsage.getInit())));
    }

    public static void callJmap(String title) throws IOException, InterruptedException {
        String[] command = new String[]{"jmap", "-histo", getProcessId()};
        runCommand(command, title);
    }

    public static void callJcmd(String title) throws IOException, InterruptedException {
        String[] command = new String[]{"jcmd", getProcessId(), "VM.native_memory", "summary", "scale=MB"};
        runCommand(command, title);
    }

    public static void runCommand(String[] command, String title) throws InterruptedException, IOException {
        String commandLine = String.join(" ", command);
        System.out.println(String.format("Call %s for %s", commandLine, title));
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        if (process.waitFor(30, TimeUnit.MINUTES)) {
            System.out.println(String.format("Call %s return with code %d", commandLine, process.exitValue()));
        } else {
            System.out.println(String.format("Call %s timeout", commandLine));
        }
    }

    public static void main(String[] args) {
        System.out.println(formatSize(888));
        System.out.println(formatSize(888 * 1024));
        System.out.println(formatSize(888 * 1024 + 512));
        System.out.println(formatSize(888 * 1024 * 1024 + 512 * 1024));
        System.out.println(formatSize(888 * 1024 * 1024 * 1024l + 512 * 1024 * 1024l));
    }

    public static String formatSize(long v) {
        if (v < 1024) return v + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double) v / (1L << (z * 10)), " KMGTPE".charAt(z));
    }

    private static String getProcessId() {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        return jvmName.split("@")[0];
    }
}
