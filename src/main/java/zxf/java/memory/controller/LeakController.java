package zxf.java.memory.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zxf.java.memory.leak.*;
import zxf.java.memory.util.DebugUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/leak")
public class LeakController {
    private static List<InnerClassLeak.InnerClass> innerClasses = new ArrayList<>();

    @GetMapping("/gc")
    public void gc() {
        DebugUtils.printMemInfoFromMXBean("gc.before");
        System.gc();
        DebugUtils.printMemInfoFromMXBean("gc.after");
    }

    @GetMapping("/byStaticReference")
    public Integer byStaticReference() {
        DebugUtils.printMemInfoFromMXBean("byStaticReference.before");
        Integer result = new StaticReferenceLeak().test();
        DebugUtils.printMemInfoFromMXBean("byStaticReference.after");
        return result;
    }

    @GetMapping("/byUnclosedResources")
    public Integer byUnclosedResources() throws IOException {
        try {
            DebugUtils.printMemInfoFromMXBean("byUnclosedResources.before");
            Integer result = new UnclosedResourcesLeak().test();
            DebugUtils.printMemInfoFromMXBean("byUnclosedResources.after");
            return result;
        } catch (Throwable throwable) {
            DebugUtils.printMemInfoFromMXBean("byUnclosedResources.exception");
            throwable.printStackTrace();
            throw throwable;
        }
    }

    @GetMapping("/byHashAndEqualsNotImplemented")
    public Integer byHashAndEqualsNotImplemented() throws IOException {
        DebugUtils.printMemInfoFromMXBean("byHashAndEqualsNotImplemented.before");
        Integer result = new HashAndEqualsNotImplementedLeak().test();
        DebugUtils.printMemInfoFromMXBean("byHashAndEqualsNotImplemented.after");
        return result;
    }

    @GetMapping("/byInnerClass")
    public Integer byInnerClass(@RequestParam Integer count) throws IOException {
        DebugUtils.printMemInfoFromMXBean("byInnerClass.before");
        for (int i = 0; i < count; i++) {
            InnerClassLeak.InnerClass object = new InnerClassLeak().create();
            innerClasses.add(object);
            if (i % 5 == 0) {
                DebugUtils.printMemInfoFromMXBean("byInnerClass." + i);
            }
        }
        DebugUtils.printMemInfoFromMXBean("byInnerClass.after");
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
        DebugUtils.printMemInfoFromMXBean("byThreadLocal.before, release=" + release);
        Integer result = ThreadLocalsLeak.test(release);
        DebugUtils.printMemInfoFromMXBean("byThreadLocal.after, release=" + release);
        return result;
    }
}
