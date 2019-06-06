package com.fpush.plat.network.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fpush.common.api.cmd.Command;
import com.fpush.common.api.message.MessageDispatcher;
import com.fpush.common.handler.BindUserHandler;
import com.fpush.common.handler.PushHandler;
import com.fpush.common.server.AbstractServer;
import com.fyqz.tools.utils.Utils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettySocketServer extends AbstractServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(NettySocketServer.class);
	private int port;
	MessageDispatcher messageDispatcher;

	public NettySocketServer(int port) {
		this.port = port;
		this.messageDispatcher = new MessageDispatcher();
	}

	@Override
	public void init() {
		this.messageDispatcher.register(Command.BIND, () -> new BindUserHandler());
		this.messageDispatcher.register(Command.UNBIND, () -> new BindUserHandler());
		this.messageDispatcher.register(Command.PUSH, () -> new PushHandler());
	}

	@Override
	public void start() {
		if (Utils.useNettyEpoll()) {
			createEpollServer();
		} else {
			createNioServer();
		}
	}

	@Override
	public void createNioServer() {
		EventLoopGroup bossNioGroup = new NioEventLoopGroup();
		EventLoopGroup workNioGroup = new NioEventLoopGroup();

		try {

			ServerBootstrap b = new ServerBootstrap();
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		    b.childOption(ChannelOption.SO_SNDBUF, 32 * 1024);
	        b.childOption(ChannelOption.SO_RCVBUF, 32 * 1024);
			b.group(bossNioGroup, workNioGroup).channelFactory(NioServerSocketChannel::new)
					.childHandler(new NettyChannelInitaializer(this));

			ChannelFuture future = b.bind(port).addListener(f -> {
				if (f.isSuccess()) {
					serverState.set(State.Started);
					LOGGER.info("server start success on:{}", port);
				} else {
					LOGGER.error("server start failure on:{}", port, f.cause());
				}
			}).sync();
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			LOGGER.error("======server start failure:" + e.getMessage() + "======");
		} finally {
			LOGGER.info("=======server close=========");
			bossNioGroup.shutdownGracefully();
			workNioGroup.shutdownGracefully();
		}
	}

	@Override
	public void createEpollServer() {
		EventLoopGroup bossEpollGroup = new EpollEventLoopGroup();
		EventLoopGroup workEpollGroup = new EpollEventLoopGroup();
		try {

			ServerBootstrap b = new ServerBootstrap();
			b.option(ChannelOption.SO_BACKLOG, 1024);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			b.group(bossEpollGroup, workEpollGroup).channelFactory(EpollServerSocketChannel::new)
					.childHandler(new NettyChannelInitaializer(this));

			ChannelFuture future = b.bind(port).addListener(f -> {
				if (f.isSuccess()) {
					serverState.set(State.Started);
					LOGGER.info("server start success on:{}", port);
				} else {
					LOGGER.error("server start failure on:{}", port, f.cause());
				}
			}).sync();
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			LOGGER.error("======server start failure:" + e.getMessage() + "======");
		} finally {
			LOGGER.info("=======server close=========");
			bossEpollGroup.shutdownGracefully();
			workEpollGroup.shutdownGracefully();
		}

	}
}
