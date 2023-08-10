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
        DebugUtils.printMemInfoFromRuntime("gc.before");
        System.gc();
        DebugUtils.printMemInfoFromRuntime("gc.after");
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
        DebugUtils.printMemInfoFromRuntime("byInnerClass.before");
        for (int i = 0; i < 1024; i++) {
            InnerClassLeak.InnerClass object = new InnerClassLeak().create();
            innerClasses.add(object);
        }
        DebugUtils.printMemInfoFromRuntime("byInnerClass.after");
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
        DebugUtils.printMemInfoFromRuntime("byThreadLocal.before, release=" + release);
        Integer result = ThreadLocalsLeak.test(release);
        DebugUtils.printMemInfoFromRuntime("byThreadLocal.after, release=" + release);
        return result;
    }
}
