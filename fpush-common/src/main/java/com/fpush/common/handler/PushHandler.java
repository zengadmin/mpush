package com.fpush.common.handler;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fpush.common.api.cmd.Command;
import com.fpush.common.api.message.BaseMessageHandler;
import com.fpush.common.api.protocol.Packet;
import com.fpush.common.channel.ChannelSupervise;
import com.fpush.common.message.PushMessage;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public final class PushHandler extends BaseMessageHandler<PushMessage> {
	private static final Logger LOGGER = LoggerFactory.getLogger(PushHandler.class);

	@Override
	public PushMessage decode(Packet packet, Channel channel) {
		return new PushMessage(packet, channel);
	}

	@Override
	public void handle(PushMessage message) {
		if (message.getPacket().cmd == Command.PUSH.cmd) {
			pushmessage(message);
		} else {
			LOGGER.error("错误的指令!");
		}
	}

	@SuppressWarnings("unchecked")
	private void pushmessage(PushMessage message) {
		Optional<List<String>> optional = Optional.of(message.getPushIds());
		if (optional.isPresent()) {
			List<String> list = optional.get();
			list.stream().forEach(pushId -> {
				Channel channel = ChannelSupervise.findChannelBySessionId(pushId);
				if (channel != null) {
					channel.writeAndFlush(new TextWebSocketFrame(message.getData()));
				} else {
					LOGGER.info("推送channel为空");
				}
			});
		} else {
			LOGGER.info("推送pushIds为空");
		}
	}
}
