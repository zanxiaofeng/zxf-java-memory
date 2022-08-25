package zxf.java.memory.service;

import org.springframework.stereotype.Service;
import zxf.java.memory.bean.MyBean;

import java.util.ArrayList;
import java.util.List;

@Service
public class BeanCacheService {
    private List<MyBean> cache = new ArrayList<>();

    public Integer cache(MyBean myBean) {
        cache.add(myBean);
        return cache.size();
    }
}
