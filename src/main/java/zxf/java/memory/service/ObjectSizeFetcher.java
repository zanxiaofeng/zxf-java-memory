package zxf.java.memory.service;

import java.lang.instrument.Instrumentation;

public class ObjectSizeFetcher {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst){
        instrumentation = inst;
    }

    public static Long getObjectSize(Object object){
        return instrumentation.getObjectSize(object);
    }
}
