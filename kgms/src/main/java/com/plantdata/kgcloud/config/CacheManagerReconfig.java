package com.plantdata.kgcloud.config;

import com.google.common.collect.Maps;
import com.plantdata.kgcloud.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.SerializationCodec;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
@Slf4j
public class CacheManagerReconfig {
    public static final String CACHE_GRAPH_KGNAME = "kgcloud:cache:graph:kgname";

    @Autowired
    private CacheManager cacheManager;

    @PostConstruct
    public void init() {
        Map<String, CacheConfig> config = Maps.newHashMap();
        config.put(CACHE_GRAPH_KGNAME, new CacheConfig(60 * 60 * 1000, 30 * 60 * 1000));
        RedissonSpringCacheManager redissonSpringCacheManager = (RedissonSpringCacheManager) cacheManager;
        redissonSpringCacheManager.setConfig(config);
        redissonSpringCacheManager.setCodec(new StringCodec());
        log.debug("重写CacheManager缓存配置成功，config=[{}]", JacksonUtils.writeValueAsString(config));
    }
}
