package zxf.java.memory.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zxf.java.memory.leak.*;
import zxf.java.memory.util.DebugUtils;
import zxf.java.memory.util.MemoryMonitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/leak")
public class LeakController {
    private static List<InnerClassLeak.InnerClass> innerClasses = new ArrayList<>();

    @GetMapping("/gc")
    public void gc() {
        MemoryMonitor.logMemoryInfoFromMXBean("gc.before");
        System.gc();
        MemoryMonitor.logMemoryInfoFromMXBean("gc.after");
    }

    @GetMapping("/byStaticReference")
    public Integer byStaticReference() {
        MemoryMonitor.logMemoryInfoFromMXBean("byStaticReference.before");
        Integer result = new StaticReferenceLeak().test();
        MemoryMonitor.logMemoryInfoFromMXBean("byStaticReference.after");
        return result;
    }

    @GetMapping("/byUnclosedResources")
    public Integer byUnclosedResources() throws IOException {
        try {
            MemoryMonitor.logMemoryInfoFromMXBean("byUnclosedResources.before");
            Integer result = new UnclosedResourcesLeak().test();
            MemoryMonitor.logMemoryInfoFromMXBean("byUnclosedResources.after");
            return result;
        } catch (Throwable throwable) {
            MemoryMonitor.logMemoryInfoFromMXBean("byUnclosedResources.exception");
            throwable.printStackTrace();
            throw throwable;
        }
    }

    @GetMapping("/byHashAndEqualsNotImplemented")
    public Integer byHashAndEqualsNotImplemented() throws IOException {
        MemoryMonitor.logMemoryInfoFromMXBean("byHashAndEqualsNotImplemented.before");
        Integer result = new HashAndEqualsNotImplementedLeak().test();
        MemoryMonitor.logMemoryInfoFromMXBean("byHashAndEqualsNotImplemented.after");
        return result;
    }

    @GetMapping("/byInnerClass")
    public Integer byInnerClass(@RequestParam Integer count) throws IOException {
        MemoryMonitor.logMemoryInfoFromMXBean("byInnerClass.before");
        for (int i = 0; i < count; i++) {
            InnerClassLeak.InnerClass object = new InnerClassLeak().create();
            innerClasses.add(object);
            if (i % 5 == 0) {
                MemoryMonitor.logMemoryInfoFromMXBean("byInnerClass." + i);
            }
        }
        MemoryMonitor.logMemoryInfoFromMXBean("byInnerClass.after");
        return 1024;
    }

    /**
     * jmap -histo:live <pid>|grep MyThreadLocalBean
     *
     * @param release
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/byThreadLocal")
    public Integer byThreadLocal(@RequestParam(defaultValue = "false") Boolean release) throws InterruptedException {
        MemoryMonitor.logMemoryInfoFromMXBean("byThreadLocal.before, release=" + release);
        Integer result = ThreadLocalsLeak.test(release);
        MemoryMonitor.logMemoryInfoFromMXBean("byThreadLocal.after, release=" + release);
        return result;
    }
}
