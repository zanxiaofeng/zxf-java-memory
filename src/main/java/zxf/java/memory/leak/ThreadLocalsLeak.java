package zxf.java.memory.leak;

import java.io.IOException;

public class ThreadLocalsLeak {
    private ThreadLocal<Long> threadLocal;

    public Integer test() throws IOException {
        threadLocal.set(System.nanoTime());
        return 1;
    }
}
