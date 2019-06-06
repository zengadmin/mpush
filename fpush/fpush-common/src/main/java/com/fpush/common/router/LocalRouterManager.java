package com.fpush.common.router;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalRouterManager implements RouterManager<LocalRouter> {
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalRouterManager.class);
	private static final Map<Integer, LocalRouter> EMPTY = new HashMap<>(0);
	/**
	 * 本地路由表
	 */
	private final Map<String, Map<Integer, LocalRouter>> routers = new ConcurrentHashMap<>();

	@Override
	public LocalRouter register(String sessionId, LocalRouter router) {
		LOGGER.info("register local router success sessionId={}, router={}", sessionId, router);
		return routers.computeIfAbsent(sessionId, s -> new HashMap<>(1)).put(router.getClientType(), router);
	}

	@Override
	public boolean unRegister(String sessionId, int clientType) {
		//LocalRouter router = routers.getOrDefault(sessionId, EMPTY).remove(clientType);
		//LOGGER.info("unRegister local router success userId={}, router={}", sessionId, router);
		return true;
	}

	@Override
	public Set<LocalRouter> lookupAll(String sessionId) {
		return new HashSet<>(routers.getOrDefault(sessionId, EMPTY).values());
	}

	@Override
	public LocalRouter lookup(String sessionId, int clientType) {
		LocalRouter router = routers.getOrDefault(sessionId, EMPTY).get(clientType);
		LOGGER.info("lookup local router sessionId={}, router={}", sessionId, router);
		return router;
	}

	public Map<String, Map<Integer, LocalRouter>> routers() {
		return routers;
	}

}
