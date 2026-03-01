package zxf.java.memory.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DebugUtils {

    public static void runCommand(String[] command) {
        String commandLine = String.join(" ", command);
        Process process = null;
        try {
            log.info("Call {}", commandLine);

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line);
                }
            }

            if (process.waitFor(60, TimeUnit.SECONDS)) {
                log.info("Call {} return with code {}", commandLine, process.exitValue());
            } else {
                log.warn("Call {} timeout, destroying process", commandLine);
                process.destroyForcibly();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (process != null && process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }

    public static String formatSize(long v) {
        if (v < 0) {
            return String.valueOf(v);
        }
        if (v < 1024) return v + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double) v / (1L << (z * 10)), " KMGTPE".charAt(z));
    }

    public static void main(String[] args) {
        log.info(formatSize(888));
        log.info(formatSize(888 * 1024));
        log.info(formatSize(888 * 1024 + 512));
        log.info(formatSize(888 * 1024 * 1024 + 512 * 1024));
        log.info(formatSize(888 * 1024 * 1024 * 1024L + 512 * 1024 * 1024L));
    }
}
