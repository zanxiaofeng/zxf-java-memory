package zxf.java.memory.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StringCacheService {
    private Map<String, String> cache = new ConcurrentHashMap<>();

    public Integer cache(String uuid) {
        cache.put(uuid, uuid);
        return cache.size();
    }
}
