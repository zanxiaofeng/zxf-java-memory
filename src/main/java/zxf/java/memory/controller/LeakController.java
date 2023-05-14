package zxf.java.memory.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zxf.java.memory.leak.HashAndEqualsNotImplementedLeak;
import zxf.java.memory.leak.StaticReferenceLeak;
import zxf.java.memory.leak.UnclosedResourcesLeak;

import java.io.IOException;

@RestController
@RequestMapping("/leak")
public class LeakController {
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
}
