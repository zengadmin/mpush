package com.fpush.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fpush.common.api.cmd.Command;
import com.fpush.common.api.message.BaseMessageHandler;
import com.fpush.common.api.protocol.Packet;
import com.fpush.common.api.spi.common.CacheManager;
import com.fpush.common.api.spi.common.CacheManagerFactory;
import com.fpush.common.channel.ChannelSupervise;
import com.fpush.common.message.BindUserMessage;
import com.fyqz.tools.utils.Strings;
import com.fyqz.tools.utils.Utils;

import io.netty.channel.Channel;

public final class BindUserHandler extends BaseMessageHandler<BindUserMessage> {
	private static final Logger LOGGER = LoggerFactory.getLogger(BindUserHandler.class);
    private final CacheManager cacheManager = CacheManagerFactory.create();

	@Override
	public void handle(BindUserMessage message) {
		if (message.getPacket().cmd == Command.BIND.cmd) {
			bind(message);
		} else if(message.getPacket().cmd == Command.UNBIND.cmd) {
			unbind(message);
		}else {
			LOGGER.error("错误的指令!");
		}
	}

	//解除绑定
	private void unbind(BindUserMessage message) {
		if (Strings.isNullOrEmpty(message.sessionId)) {
			LOGGER.error("unbind user failure for invalid param,channel={}", message.getChannel());
			return;
		}
		ChannelSupervise.removeChannel(message.getChannel());
	}
	
	//绑定用户
	private void bind(BindUserMessage message) {
		if (Strings.isNullOrEmpty(message.sessionId)) {
			LOGGER.error("bind user failure for invalid param, channel={}", message.getChannel());
			return;
		}
		//TODO 验证用户
		
		
		ChannelSupervise.setChannelBySessionId(message.sessionId, message.getChannel());
		message.setRemoteIp(Utils.lookupLocalIp());
		cacheManager.set(message.sessionId, message);
	}

	@Override
	public BindUserMessage decode(Packet packet, Channel channel) {
		return new BindUserMessage(packet, channel);
	}

}
