package com.fpush.common.api.protocol;

public final class JsonPacket extends Packet {

	@Override
	public String toString() {
		return "JsonPacket{" + "cmd=" + cmd + ", body=" + body + '}';
	}
}
