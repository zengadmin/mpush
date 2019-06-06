package com.fpush.common.api.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * 
* 
* @ClassName: Connection.java
* @Description: 连接接口类
*
* @version: v1.0.0
* @author: zhangqingfeng
* @date: 2019年3月25日 下午3:09:46 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年3月25日     zhangqingfeng           v1.0.0               修改原因
 */
public interface Connection {
	byte STATUS_NEW = 0;// 新建连接状态
	byte STATUS_CONNECTED = 1;// 连接状态
	byte STATUS_DISCONNECTED = 2;// 断开连接状态
	
	ChannelFuture close();
	
    public Channel getChannel();
    
    void init(Channel channel, boolean security);
    
    public boolean isConnected();
    
	public String getId();
}
