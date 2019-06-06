package com.fpush.monitor.service;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import com.fpush.common.api.common.Monitor;
import com.fpush.common.api.service.BaseService;
import com.fpush.common.api.service.Listener;
import com.fpush.monitor.data.MonitorResult;
import com.fpush.monitor.data.ResultCollector;
import com.fyqz.tools.common.JVMUtil;
import com.fyqz.tools.config.CC;
import com.fyqz.tools.log.Logs;
import com.fyqz.tools.thread.ThreadNames;
import com.fyqz.tools.utils.Utils;

public class MonitorService extends BaseService implements Monitor, Runnable {

	private static final int FIRST_DUMP_JSTACK_LOAD_AVG = 2, SECOND_DUMP_JSTACK_LOAD_AVG = 4,
			THIRD_DUMP_JSTACK_LOAD_AVG = 6, FIRST_DUMP_JMAP_LOAD_AVG = 4;

	private static final String dumpLogDir = CC.fp.monitor.dump_dir;
	private static final boolean dumpEnabled = CC.fp.monitor.dump_stack;
	private static final boolean printLog = CC.fp.monitor.print_log;
	private static final long dumpPeriod = CC.fp.monitor.dump_period.getSeconds();

	private volatile boolean dumpFirstJstack = false;
	private volatile boolean dumpSecondJstack = false;
	private volatile boolean dumpThirdJstack = false;
	private volatile boolean dumpJmap = false;

	private final ResultCollector collector;

	private final ThreadPoolManager threadPoolManager;

	public MonitorService() {
		threadPoolManager = new ThreadPoolManager();
		collector = new ResultCollector(threadPoolManager);
	}

	private Thread thread;

	@Override
	public void run() {
		while (isRunning()) {
			MonitorResult result = collector.collect();

			if (printLog) {
				Logs.PUSH.info(result.toJson());
			}

			if (dumpEnabled) {
				dump();
			}

			try {
				TimeUnit.SECONDS.sleep(dumpPeriod);
			} catch (InterruptedException e) {
				if (isRunning())
					stop();
			}
		}
	}

	private void stop() {

	}

	protected void doStart(Listener listener) throws Throwable {
		if (printLog || dumpEnabled) {
			thread = Utils.newThread(ThreadNames.T_MONITOR, this);
			thread.setDaemon(true);
			thread.start();
		}
		listener.onSuccess();
	}

	protected void doStop(Listener listener) throws Throwable {
		if (thread != null && thread.isAlive())
			thread.interrupt();
		listener.onSuccess();
	}

	private void dump() {
		double load = collector.getJvmInfo().load();
		if (load > FIRST_DUMP_JSTACK_LOAD_AVG) {
			if (!dumpFirstJstack) {
				dumpFirstJstack = true;
				JVMUtil.dumpJstack(dumpLogDir);
			}
		}

		if (load > SECOND_DUMP_JSTACK_LOAD_AVG) {
			if (!dumpSecondJstack) {
				dumpSecondJstack = true;
				JVMUtil.dumpJmap(dumpLogDir);
			}
		}

		if (load > THIRD_DUMP_JSTACK_LOAD_AVG) {
			if (!dumpThirdJstack) {
				dumpThirdJstack = true;
				JVMUtil.dumpJmap(dumpLogDir);
			}
		}

		if (load > FIRST_DUMP_JMAP_LOAD_AVG) {
			if (!dumpJmap) {
				dumpJmap = true;
				JVMUtil.dumpJmap(dumpLogDir);
			}
		}
	}

	@Override
	public void monitor(String name, Thread thread) {

	}

	@Override
	public void monitor(String name, Executor executor) {
		threadPoolManager.register(name, executor);
	}

	public ThreadPoolManager getThreadPoolManager() {
		return threadPoolManager;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}
}
