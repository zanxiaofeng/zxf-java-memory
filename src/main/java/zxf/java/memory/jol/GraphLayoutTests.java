package zxf.java.memory.jol;

import org.openjdk.jol.info.GraphLayout;

import java.util.Arrays;

public class GraphLayoutTests {
    public static void main(String[] args) {
        JolBean jolBean = new JolBean();
        jolBean.setaString("1234567890");
        jolBean.setaBoolean(true);
        jolBean.setaInteger(1);
        jolBean.setaLong(2l);
        jolBean.setaDouble(2.333);
        jolBean.setaChar('a');
        jolBean.setData(new JolBean.Data());
        jolBean.getData().setCharacter('b');
        jolBean.getData().setIds(Arrays.asList("12345", "67890"));

        System.out.println(GraphLayout.parseInstance(jolBean).toFootprint());
    }
}
