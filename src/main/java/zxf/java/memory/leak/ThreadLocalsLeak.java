package zxf.java.memory.leak;

import java.io.IOException;
import java.util.Random;

public class ThreadLocalsLeak {
    private ThreadLocal<byte[]> threadLocal = new ThreadLocal<>();

    public Integer test() throws IOException {
        byte[] buffer = new byte[1024];
        new Random().nextBytes(buffer);
        threadLocal.set(buffer);
        return 1;
    }
}
