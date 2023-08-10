package zxf.java.memory.bean;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadLocalBean {
    private static AtomicInteger counter = new AtomicInteger();
    private int no;
    private byte[] content;

    public MyThreadLocalBean() {
        this.content = new byte[1024 * 1024];
        new Random().nextBytes(this.content);
        this.no = counter.incrementAndGet();
    }

    @Override
    public String toString() {
        return String.valueOf(no);
    }
}
