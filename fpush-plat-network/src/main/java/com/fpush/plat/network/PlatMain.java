package com.fpush.plat.network;

import com.fpush.plat.network.tcp.NettySocketServer;
import com.fyqz.tools.config.CC;

public class PlatMain {

	public static void main(String[] args) throws Exception {
		NettySocketServer m=new NettySocketServer(CC.fp.net.wsServerPort);
		m.init();
		m.start();
	}
}
