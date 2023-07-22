package zxf.java.memory.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zxf.java.memory.leak.*;
import zxf.java.memory.service.ObjectSizeFetcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/leak")
public class LeakController {
    private static List<InnerClassLeak.InnerClass> innerClasses = new ArrayList<>();

    @GetMapping("/gc")
    public void gc() {
        printMemoryInfo();
        System.gc();
        printMemoryInfo();
    }

    @GetMapping("/byStaticReference")
    public Integer byStaticReference() {
        return new StaticReferenceLeak().test();
    }

    @GetMapping("/byUnclosedResources")
    public Integer byUnclosedResources() throws IOException {
        return new UnclosedResourcesLeak().test();
    }

    @GetMapping("/byHashAndEqualsNotImplemented")
    public Integer byHashAndEqualsNotImplemented() throws IOException {
        return new HashAndEqualsNotImplementedLeak().test();
    }

    @GetMapping("/byInnerClass")
    public Integer byInnerClass() throws IOException {
        printMemoryInfo();
        for (int i = 0; i < 1024; i++) {
            InnerClassLeak.InnerClass object = new InnerClassLeak().create();
            innerClasses.add(object);
        }
        printMemoryInfo();
        return 1024;
    }

    @GetMapping("/byThreadLocal")
    public Integer byThreadLocal() throws IOException {
        printMemoryInfo();
        for (int i = 0; i < 1024; i++) {
            new Thread(() -> {
                try {
                    new ThreadLocalsLeak().test();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).run();
        }
        printMemoryInfo();
        return 1024;
    }

    private void printMemoryInfo() {
        Long maxHeapSize = Runtime.getRuntime().maxMemory();
        Long usedHeapSize = Runtime.getRuntime().totalMemory();
        Long freeHeapSize = Runtime.getRuntime().freeMemory();
        System.out.println(String.format("Memory Usage: max=%d, used=%d, free=%d", maxHeapSize, usedHeapSize, freeHeapSize));
    }
}
