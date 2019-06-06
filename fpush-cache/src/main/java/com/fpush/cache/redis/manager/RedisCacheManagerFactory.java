package com.fpush.cache.redis.manager;

import com.fpush.common.api.spi.Spi;
import com.fpush.common.api.spi.common.CacheManager;
import com.fpush.common.api.spi.common.CacheManagerFactory;

@Spi(order = 1)
public final class RedisCacheManagerFactory implements CacheManagerFactory {
    @Override
    public CacheManager get() {
        return RedisManager.I;
    }
}
