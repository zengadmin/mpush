package com.fpush.common.api.message;

import java.util.Map;

import com.fpush.common.api.protocol.Packet;

import io.netty.channel.Channel;

public abstract class BaseMessage implements Message {
	transient protected Packet packet;
	transient protected Channel channel;

	protected void decodeJsonBody(Map<String, Object> body) {

	}

	public BaseMessage(Packet packet, Channel channel) {
		this.packet=packet;
		this.channel=channel;
	}
	@Override
	public void decodeBody() {
		Map<String, Object> body = packet.getBody();
		decodeJsonBody(body);
	}

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	

}
