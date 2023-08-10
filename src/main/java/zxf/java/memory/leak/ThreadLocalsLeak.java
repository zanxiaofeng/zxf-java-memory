package zxf.java.memory.leak;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import zxf.java.memory.bean.MyThreadLocalBean;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalsLeak {
    private static ExecutorService threadPool = new ThreadPoolExecutor(1, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024 * 4 + 600), new CustomizableThreadFactory("zxf-byThreadLocal-"), new ThreadPoolExecutor.CallerRunsPolicy());
    private static AtomicInteger counter = new AtomicInteger();

    private ThreadLocal<MyThreadLocalBean> threadLocal = new ThreadLocal<>();

    public void execute(Boolean release) throws InterruptedException {
        try {
            MyThreadLocalBean myThreadLocalBean = new MyThreadLocalBean();
            System.out.println("ThreadLocalsLeak::execute, " + myThreadLocalBean + ", " + Thread.currentThread().getName());
            threadLocal.set(myThreadLocalBean);
            Thread.sleep(1);
        } finally {
            if (release) {
                threadLocal.remove();
            }
        }
    }

    public static Integer test(Boolean release) throws InterruptedException {
        int loopTimes = 1024 * 5;
        CountDownLatch countDownLatch = new CountDownLatch(loopTimes);
        for (int i = 0; i < loopTimes; i++) {
            threadPool.submit(() -> {
                try {
                    new ThreadLocalsLeak().execute(release);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        return loopTimes * counter.incrementAndGet();
    }
}
