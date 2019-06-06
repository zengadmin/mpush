package com.fpush.common.message;

import java.io.Serializable;
import java.util.Map;

import com.fpush.common.api.message.BaseMessage;
import com.fpush.common.api.protocol.Packet;

import io.netty.channel.Channel;

public class BindUserMessage extends BaseMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5819446013952780323L;
	public String sessionId;//唯一绑定ID
	public String osName;//操作系统名字
	public String osVersion;//操作系统版本
	public String clientVersion;//客户端版本
	public String remoteIp;//远程IP
	public String clientType;//客户端类型
	

	public BindUserMessage(Packet packet, Channel channel) {
		super(packet, channel);
	}

	@Override
	public void decodeJsonBody(Map<String, Object> body) {
		this.sessionId = (String) body.get("sessionId");
		this.osName = (String) body.get("osName");
		this.osVersion = (String) body.get("osVersion").toString();
		this.clientVersion = (String) body.get("clientVersion").toString();
		this.remoteIp = (String) body.get("remoteIp");
		this.clientType=(String)body.get("clientType");
}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	@Override
    public String toString() {
        return "BindUserMessage{" +
	                 "osName='" + osName + '\'' +
	                ", sessionId='" + sessionId + '\'' +
	                 ", clientVersion='" + clientVersion + '\'' +
	                  ", remoteIp='" + remoteIp + '\'' +
	                ", osVersion=" + osVersion +  
                '}';
    }
}
