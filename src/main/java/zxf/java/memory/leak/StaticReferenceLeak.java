package zxf.java.memory.leak;

import java.util.ArrayList;
import java.util.List;

public class StaticReferenceLeak {
    private static List<Integer> numbers = new ArrayList<>();

    public Integer test() {
        for (int i = 0; i < 1000000000; i++) {
            numbers.add(i);
        }
        return numbers.size();
    }
}