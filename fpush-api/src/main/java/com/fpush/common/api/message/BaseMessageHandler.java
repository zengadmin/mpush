package com.fpush.common.api.message;

import com.fpush.common.api.protocol.Packet;

import io.netty.channel.Channel;

public abstract class BaseMessageHandler<T extends BaseMessage> implements MessageHandler {

	public abstract T decode(Packet packet,  Channel channel);
	
	public abstract void handle(T message);
	
	public void handle(Packet packet,  Channel channel) {
		T t = decode(packet, channel);//根据参数调用，然后对各自处理类对象进行译码
		if (t != null) t.decodeBody();//解析实体类
		if (t != null) {
			handle(t);//调用
		}
	}
}
