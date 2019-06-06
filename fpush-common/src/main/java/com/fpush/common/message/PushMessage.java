package com.fpush.common.message;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fpush.common.api.message.BaseMessage;
import com.fpush.common.api.protocol.Packet;
import com.fyqz.tools.utils.Strings;

import io.netty.channel.Channel;

public class PushMessage extends BaseMessage {

	private String userId;//自己唯一ID
	
	private List pushIds;//要推送的ID是个字符串
	
	private String data;//推送内容数据
	
	public String clientType;//要推送的客户端类型
	
	
	@Override
	public void decodeJsonBody(Map<String, Object> body) {
		this.userId = (String) body.get("sessionId");
		this.data = (String) body.get("data");
		this.clientType=(String)body.get("clientType");
		String pushId=(String)body.get("pushIds");
		if(!Strings.isNullOrEmpty(pushId)) {
			pushIds=Arrays.asList(pushId.split(","));
		}
}
	
	
	
	public PushMessage(Packet packet, Channel channel) {
		super(packet, channel);
	}
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}



	public List getPushIds() {
		return pushIds;
	}



	public void setPushIds(List pushIds) {
		this.pushIds = pushIds;
	}



	public String getClientType() {
		return clientType;
	}


	public void setClientType(String clientType) {
		this.clientType = clientType;
	}


	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	@Override
    public String toString() {
        return "PushMessage{" +
	                 "userId='" + userId + '\'' +
	                ", pushIds='" + pushIds + '\'' +
	                 ", data='" + data + '\'' +
                '}';
    }


}
