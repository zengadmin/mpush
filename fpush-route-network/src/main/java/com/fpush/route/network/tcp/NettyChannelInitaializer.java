package com.fpush.route.network.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NettyChannelInitaializer extends ChannelInitializer<SocketChannel>{
	NettySocketServer nettyWebsocketServer;
	public NettyChannelInitaializer(NettySocketServer nettyWebsocketServer) {
		this.nettyWebsocketServer=nettyWebsocketServer;
	}
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		socketChannel.pipeline().addLast(new HttpServerCodec());
        socketChannel.pipeline().addLast(new HttpObjectAggregator(65536));
        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
        socketChannel.pipeline().addLast(new NettySocketHandler(nettyWebsocketServer.messageDispatcher));
	}
	
}
