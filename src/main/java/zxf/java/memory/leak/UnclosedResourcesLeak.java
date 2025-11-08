package zxf.java.memory.leak;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicInteger;

public class UnclosedResourcesLeak {
    private final AtomicInteger count = new AtomicInteger(0);
    private static final int MAX_ITERATIONS = 1000; // 可配置的合理上限

    public Integer test() throws IOException {
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            // 使用本地测试服务器或模拟数据避免网络调用
            // URL url = new URL("http://localhost:8080/test"); // 本地测试
            // URLConnection urlConnection = url.openConnection();
            // try (InputStream is = urlConnection.getInputStream()) {
            //     byte[] buffer = new byte[1024];
            //     while (is.read(buffer) != -1) {
            //         // 读取数据但不处理，仅用于演示
            //     }
            // }
            
            // 使用虚拟数据避免网络调用
            try (InputStream is = new ByteArrayInputStream("test data for memory leak demonstration".getBytes())) {
                byte[] buffer = new byte[1024];
                while (is.read(buffer) != -1) {
                    // 读取数据但不处理，仅用于演示
                }
            }
        }
        count.addAndGet(MAX_ITERATIONS);
        return count.get();
    }
}