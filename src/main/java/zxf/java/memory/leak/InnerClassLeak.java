package zxf.java.memory.leak;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InnerClassLeak {
    private byte[] buffer = new byte[3000];

    public InnerClassLeak() {
        new Random().nextBytes(buffer);
    }

    public InnerClass create() {
        Long sum = 0l;
        for (int i = 0; i < 3000; i++) {
            sum += buffer[i];
        }
        return new InnerClass(sum);
    }

    public class InnerClass {
        public Long sum;

        public InnerClass(Long sum) {
            this.sum = sum;
        }
    }
}
