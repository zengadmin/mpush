package com.fpush.common.api.connection;

import com.fpush.common.api.connection.Connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class NettyConnection implements Connection{
	private volatile byte status = STATUS_NEW;
	private Channel channel;
	private boolean security=false;
	public  int clientType=1;//1代表浏览器,2代表android,3代表ios
	public String sessionId;
	@Override
	public ChannelFuture close() {
		if(status==STATUS_DISCONNECTED) return null;
		this.status=STATUS_DISCONNECTED;
		return this.channel.close();
	}

	@Override
	public Channel getChannel() {
		return channel;
	}

	@Override
	public void init(Channel channel, boolean security) {
		this.status = STATUS_CONNECTED;
        this.channel=channel;
        this.security=security;
        if(security) {
        	
        }
	}
	
	@Override
	public boolean isConnected() {
		return status==STATUS_CONNECTED;
	}

	@Override
	public String getId() {
		return channel.id().asShortText();
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public boolean isSecurity() {
		return security;
	}

	public void setSecurity(boolean security) {
		this.security = security;
	}

	public int getClientType() {
		return clientType;
	}

	public void setClientType(int clientType) {
		this.clientType = clientType;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	
}
