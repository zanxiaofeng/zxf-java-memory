package zxf.java.memory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zxf.java.memory.bean.MyBean;
import zxf.java.memory.service.BeanCacheService;
import zxf.java.memory.service.StringCacheService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/a")
public class AController {
    private List<String> keys = new ArrayList<>();

    @Autowired
    private StringCacheService stringCacheService;

    @Autowired
    private BeanCacheService beanCacheService;

    @GetMapping("/cache/string")
    public String cacheString() {
        Integer result = 0;
        for (int i = 0; i < 100000; i++) {
            String uuid = UUID.randomUUID().toString();
            result = stringCacheService.cache(uuid);
            if (i % 1000 == 0) {
                keys.add(uuid);
            }
        }
        return result.toString();
    }

    @GetMapping("/cache/bean")
    public String cacheBean() {
        Integer result = 0;
        for (int i = 0; i < 100000; i++) {
            result = beanCacheService.cache(new MyBean());
        }
        return result.toString();
    }
}
