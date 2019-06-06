package com.fyqz.http_clients;

import java.util.HashMap;
import java.util.Map;

public class Packet {
	
	public byte cmd; // 命令
    
	public Map<String, Object> body=new HashMap<String,Object>();

	public Map<String, Object> getBody() {
		return body;
	}

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}
	public byte getCmd() {
		return cmd;
	}

	public void setCmd(byte cmd) {
		this.cmd = cmd;
	}

}
