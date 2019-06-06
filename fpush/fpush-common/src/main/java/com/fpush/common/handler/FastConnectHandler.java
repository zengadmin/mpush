package com.fpush.common.handler;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpush.common.api.cmd.Command;
import com.fpush.common.api.message.BaseMessageHandler;
import com.fpush.common.api.protocol.JsonPacket;
import com.fpush.common.api.protocol.Packet;
import com.fpush.common.api.spi.common.CacheManager;
import com.fpush.common.api.spi.common.CacheManagerFactory;
import com.fpush.common.message.BindUserMessage;
import com.fpush.common.message.FastConnectMessage;
import com.fyqz.tools.log.Logs;
import com.fyqz.tools.utils.HttpUtil;
import com.fyqz.tools.utils.Strings;

import io.netty.channel.Channel;

public class FastConnectHandler extends BaseMessageHandler<FastConnectMessage> {
	private static final Logger LOGGER = LoggerFactory.getLogger(FastConnectHandler.class);
    private final CacheManager cacheManager = CacheManagerFactory.create();

	@Override
	public FastConnectMessage decode(Packet packet, Channel channel) {
		return new FastConnectMessage(packet,channel);
	}

	@Override
	public void handle(FastConnectMessage message) {
		if(message.getPacket().getCmd()==Command.FAST_CONNECT.cmd) {//快速连接机制
			try {
				conbyroute(message);//根据路由连接信息
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void conbyroute(FastConnectMessage message) throws JsonProcessingException, Exception {
		if (Strings.isNullOrEmpty(message.getSessionId())) {
			LOGGER.error("fast connect router failure for invalid param, channel={}", message.getChannel());
			return;
		}
		JsonPacket jsonpacket=new JsonPacket();
		jsonpacket.getBody().put("sessionId", message.getSessionId());
		jsonpacket.getBody().put("clientType", message.getClientType());
		jsonpacket.getBody().put("pushIds", message.getPushIds());
		jsonpacket.getBody().put("data",message.getData());
		jsonpacket.setCmd(Command.PUSH.cmd);
		ObjectMapper objectMapper = new ObjectMapper();
		//从redis里面获取route地址
//		String str=cacheManager.get(message.getSessionId(),String.class);
//		if(StringUtils.isEmpty(str)) {
//			LOGGER.error("redis get info is empty!");
//		}
//		JSONObject json=JSON.parseObject(str);
		BindUserMessage userMessage=cacheManager.get(message.getSessionId(), BindUserMessage.class);
		String remoteIp=Optional.ofNullable(userMessage).map(sl->sl.getRemoteIp()).orElse(null);
		try {	
//			HttpUtil.httpPostJson("http://"+json.getString("remoteIp")+":8080/push",objectMapper.writeValueAsString(jsonpacket));
			if(!StringUtils.isEmpty(remoteIp)) {
				HttpUtil.httpPostJson("http://"+remoteIp+":8080/push",objectMapper.writeValueAsString(jsonpacket));
			}else {
				Logs.PUSH.error("连接地址为空错误!");
			}
			
		}catch (Exception e) {
			LOGGER.error("http connection"+e.getMessage());
		}
	}

}
