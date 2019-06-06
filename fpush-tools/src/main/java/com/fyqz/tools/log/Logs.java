package com.fyqz.tools.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fyqz.tools.config.CC;
import com.typesafe.config.ConfigRenderOptions;

public interface Logs {
	boolean logInit = init();

	static boolean init() {
		if (logInit)
			return true;
		LoggerFactory.getLogger("console").info(CC.fp.cfg.root().render(ConfigRenderOptions.concise().setFormatted(true)));
		return true;
	}

	Logger MONITOR =  LoggerFactory.getLogger("fpush.monitor.log"),

			PUSH = LoggerFactory.getLogger("fpush.push.log");
}
