package com.fpush.common.router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fpush.common.api.connection.NettyConnection;


public class RouterCenter{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(RouterCenter.class);

	 private LocalRouterManager localRouterManager;
	 
	 public RouterCenter(LocalRouterManager localRouterManager) {
		 this.localRouterManager=new LocalRouterManager();
	}
	 
	/**
     * 注册用户和链接
     *
     * @param userId
     * @param connection
     * @return
     */
    public boolean register(String userId, NettyConnection connection) {
        LocalRouter localRouter = new LocalRouter(connection);
        LocalRouter oldLocalRouter = null;
        try {
        	oldLocalRouter = localRouterManager.register(userId, localRouter);
        }catch(Exception e) {
        	  LOGGER.error("register router ex, userId={}, connection={}", userId, connection, e);
        }
        if (oldLocalRouter != null) {
            LOGGER.info("register router success, find old local router={}, userId={}", oldLocalRouter, userId);
        }
    	return true;
    }
    
    public boolean unRegister(String userId, int clientType) {
        localRouterManager.unRegister(userId, clientType);
        return true;
    }
    

    public LocalRouterManager getLocalRouterManager() {
        return localRouterManager;
    }

}
