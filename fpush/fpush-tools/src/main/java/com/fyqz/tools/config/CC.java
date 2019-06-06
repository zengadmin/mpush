package com.fyqz.tools.config;

import static java.util.stream.Collectors.toCollection;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.fyqz.tools.config.data.RedisNode;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.impl.ConfigBeanImpl;

public interface CC {
	Config cfg = load();

	static Config load() {
		Config config = ConfigFactory.load();
		String custom_config = "fp.conf";
		if (config.hasPath(custom_config)) {
			File file = new File(config.getString(custom_config));
			if (file.exists()) {
				Config custom = ConfigFactory.parseFile(file);
				config = custom.withFallback(config);
			}
		}
		return config;
	}

	
	interface fp {
        Config cfg = CC.cfg.getObject("fp").toConfig();
        /**
         * 核心配置
         * @author zhangqingfeng
         *
         */
        interface core {
            
        	Config cfg = fp.cfg.getObject("core").toConfig();
            
            String epoll_provider = cfg.getString("epoll-provider");
            
            static boolean useNettyEpoll() {
                if (!"netty".equals(CC.fp.core.epoll_provider)) return false;
                String name = CC.cfg.getString("os.name").toLowerCase(Locale.UK).trim();
                return name.startsWith("linux");//只在linux下使用netty提供的epoll库
            }
        }
        /**
         * 网络服务
         * @author zhangqingfeng
         *
         */
        interface net{
        	Config cfg=fp.cfg.getObject("net").toConfig();
        	int wsServerPort=cfg.getInt("ws-server-port");
        	int  routerServerPort=cfg.getInt("router-server-port");
        } 
        /**
         * 缓存服务
         * @author zhangqingfeng
         *
         */
		interface redis {
			Config cfg = fp.cfg.getObject("redis").toConfig();

			String password = cfg.getString("password");
			String clusterModel = cfg.getString("cluster-model");
			String sentinelMaster = cfg.getString("sentinel-master");

			List<RedisNode> nodes = cfg
					.getList("nodes")
					.stream()// 第一纬度数组
					.map(v -> RedisNode.from(v.unwrapped().toString())).collect(toCollection(ArrayList::new));

			static boolean isCluster() {
				return "cluster".equals(clusterModel);
			}

			static boolean isSentinel() {
				return "sentinel".equals(clusterModel);
			}

			static <T> T getPoolConfig(Class<T> clazz) {
				return ConfigBeanImpl.createInternal(cfg.getObject("config").toConfig(), clazz);
			}
		}
		//监控
		interface monitor {
            Config cfg = fp.cfg.getObject("monitor").toConfig();
            String dump_dir = cfg.getString("dump-dir");
            boolean dump_stack = cfg.getBoolean("dump-stack");
            boolean print_log = cfg.getBoolean("print-log");
            Duration dump_period = cfg.getDuration("dump-period");
            boolean profile_enabled = cfg.getBoolean("profile-enabled");
            Duration profile_slowly_duration = cfg.getDuration("profile-slowly-duration");
        }
	}
}
