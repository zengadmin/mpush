package com.fpush.common.message;

import java.util.Map;

import com.fpush.common.api.message.BaseMessage;
import com.fpush.common.api.protocol.Packet;

import io.netty.channel.Channel;

public class FastConnectMessage extends BaseMessage {
	private String sessionId;
	private String clientType;
	private String pushIds;
	private String data;

	public FastConnectMessage(Packet packet, Channel channel) {
		super(packet, channel);
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getPushIds() {
		return pushIds;
	}

	public void setPushIds(String pushIds) {
		this.pushIds = pushIds;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public void decodeJsonBody(Map<String, Object> body) {
		this.sessionId = (String) body.get("sessionId");
		this.data = (String) body.get("data");
		this.clientType = (String)body.get("clientType") ;
		this.pushIds = (String) body.get("pushIds");
	}

}
