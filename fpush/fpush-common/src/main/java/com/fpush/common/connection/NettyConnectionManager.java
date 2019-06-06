package com.fpush.common.connection;

import java.util.concurrent.ConcurrentHashMap;

import com.fpush.common.api.connection.Connection;
import com.fpush.common.api.connection.ConnectionManager;
import com.fpush.common.api.connection.NettyConnection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
/**
* 
* @ClassName: NettyConnectionManager.java
* @Description: 连接管理对象
*
* @version: v1.0.0
* @author: zhangqingfeng
* @date: 2019年3月25日 下午3:09:20 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年3月25日     zhangqingfeng           v1.0.0               修改原因
 */
public class NettyConnectionManager implements ConnectionManager{
	
	private final ConcurrentHashMap<ChannelId,  NettyConnection> connections=new ConcurrentHashMap<>();
	
	@Override
	public  NettyConnection get(Channel channel) {
		return connections.get(channel.id());
	}

	@Override
	public  NettyConnection removeAndClose(Channel channel) {
		if(channel==null) return null;
		return connections.remove(channel.id());
	}

	@Override
	public void add( NettyConnection connection) {
		connections.putIfAbsent(connection.getChannel().id(), connection);
	}

	@Override
	public int getConnNum() {
		return connections.size();
	}

	@Override
	public void destroy() {
		connections.values().forEach(Connection::close);
		connections.clear();
	}

}
