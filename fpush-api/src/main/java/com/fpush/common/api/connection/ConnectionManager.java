package com.fpush.common.api.connection;

import io.netty.channel.Channel;
/**
 * 
* 
* @ClassName: ConnectionManager.java
* @Description: 连接管理器
*
* @version: v1.0.0
* @author: zhangqingfeng
* @date: 2019年3月25日 下午3:36:43 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2019年3月25日     zhangqingfeng           v1.0.0               修改原因
 */
public interface ConnectionManager {
	//根据channel获取连接对象
	 NettyConnection get(Channel channel);
	//移除并且关闭通道连接
	 NettyConnection removeAndClose(Channel channel);
	//添加连接
	void add( NettyConnection connection);
	//获取连接数量
	int getConnNum();
	//清空所有连接
	void destroy();
}
