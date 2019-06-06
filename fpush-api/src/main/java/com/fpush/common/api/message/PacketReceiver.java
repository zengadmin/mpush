package com.fpush.common.api.message;

import com.fpush.common.api.protocol.Packet;

import io.netty.channel.Channel;

public interface PacketReceiver {
	void onReceive(Packet packet,  Channel channel);
}
