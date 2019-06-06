package com.fpush.common.api.message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.fpush.common.api.cmd.Command;
import com.fpush.common.api.protocol.Packet;

import io.netty.channel.Channel;

public final class MessageDispatcher implements PacketReceiver {
	
	private final Map<Byte, MessageHandler> handlers = new HashMap<>();
	
	@Override
	public void onReceive(Packet packet,   Channel channel) {
		MessageHandler handler = handlers.get(packet.cmd);
		if (handler != null) {
			handler.handle(packet, channel);// 消息处理类
		}
	}

	public void register(Command command, MessageHandler handler) {
		handlers.put(command.cmd, handler);
	}

	public void register(Command command, Supplier<MessageHandler> handlerSupplier, boolean enabled) {
		if (enabled && !handlers.containsKey(command.cmd)) {
			register(command, handlerSupplier.get());
		}
	}

	public void register(Command command, Supplier<MessageHandler> handlerSupplier) {
		this.register(command, handlerSupplier, true);
	}
	
	public MessageHandler unRegister(Command command) {
		return handlers.remove(command.cmd);
	}
	
	
}
