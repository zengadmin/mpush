package com.fpush.route.network;

import com.fpush.route.network.tcp.NettySocketServer;
import com.fyqz.tools.config.CC;

public class RouteMain {

	public static void main(String[] args) {
		NettySocketServer m=new NettySocketServer(CC.fp.net.routerServerPort);
		m.init();
		m.start();
	}

}
