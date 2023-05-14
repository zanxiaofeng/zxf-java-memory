package zxf.java.memory.leak;

import java.util.HashSet;
import java.util.Set;

public class HashAndEqualsNotImplementedLeak {
    private static Set<Entry> set = new HashSet();

    public Integer test() {
        for (int i = 0; i < 1000000000; i++) {
            set.add(new Entry("Test"));
        }
        return set.size();
    }

    static class Entry {
        public String entry;

        public Entry(String entry) {
            this.entry = entry;
        }
    }

}
