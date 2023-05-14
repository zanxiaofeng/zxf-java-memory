package zxf.java.memory.leak;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OuterClassLeak {
    private static List<InnerClass> cache = new ArrayList<>();
    private byte[] buffer = new byte[300];

    public OuterClassLeak() {
        new Random().nextBytes(buffer);
    }

    public void create() {
        Long sum = 0l;
        for (int i = 0; i < 300; i++) {
            sum += buffer[i];
        }
        cache.add(new InnerClass(sum));
    }

    class InnerClass {
        public Long sum;

        public InnerClass(Long sum) {
            this.sum = sum;
        }

    }
}
