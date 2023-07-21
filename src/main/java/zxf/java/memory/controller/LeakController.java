package zxf.java.memory.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zxf.java.memory.leak.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/leak")
public class LeakController {
    private static List<InnerClassLeak.InnerClass> innerClasses = new ArrayList<>();

    @GetMapping("/gc")
    public void gc() {
        System.gc();
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
        for (int i = 0; i < 1000000000; i++) {
            innerClasses.add(new InnerClassLeak().create());
        }
        return 1000000000;
    }


    @GetMapping("/byThreadLocal")
    public Integer byThreadLocal() throws IOException {
        for (int i = 0; i < 100000; i++) {
            new ThreadLocalsLeak().test();
        }
        return 100000;
    }
}
