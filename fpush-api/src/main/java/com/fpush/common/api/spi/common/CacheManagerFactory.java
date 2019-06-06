package com.fpush.common.api.spi.common;
import com.fpush.common.api.spi.Factory;
import com.fpush.common.api.spi.SpiLoader;

public interface CacheManagerFactory extends Factory<CacheManager> {
	static CacheManager create() {
		return SpiLoader.load(CacheManagerFactory.class).get();
	}
}
