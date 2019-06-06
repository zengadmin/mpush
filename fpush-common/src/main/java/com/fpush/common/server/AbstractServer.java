package com.fpush.common.server;

import java.util.concurrent.atomic.AtomicReference;

import com.fpush.common.api.service.BaseService;
import com.fpush.common.api.service.Server;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public abstract class AbstractServer extends BaseService implements Server {

	public enum State {
		Created, Initialized, Starting, Started, Shutdown
	}
	protected final AtomicReference<State> serverState = new AtomicReference<>(State.Created);
	@Override
	public void init() {
		if (!serverState.compareAndSet(State.Created, State.Initialized)) {
			throw new RuntimeException("Server already init");
		}
	}

	@Override
	public boolean isRunning() {
		return serverState.get() == State.Started;
	}

	public abstract void createNioServer();
	
	public abstract void createEpollServer();
	
	@Override
	public void start() {
		if (!serverState.compareAndSet(State.Initialized, State.Starting)) {
			throw new RuntimeException("Server already started or have not init");
		}
	}

}
