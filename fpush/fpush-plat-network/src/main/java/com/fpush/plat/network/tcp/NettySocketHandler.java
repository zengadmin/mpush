package com.fpush.plat.network.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fpush.common.api.message.PacketReceiver;
import com.fpush.common.api.protocol.Packet;
import com.fpush.common.channel.ChannelSupervise;
import com.fpush.common.codec.PacketDecoder;
import com.fpush.common.codec.RequestParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

public class NettySocketHandler extends SimpleChannelInboundHandler<Object> {
	private static final Logger logger = LoggerFactory.getLogger(NettySocketHandler.class);

	private WebSocketServerHandshaker handshaker;
	private final PacketReceiver receiver;

	public NettySocketHandler(PacketReceiver receiver) {
		this.receiver = receiver;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.debug("收到消息：" + msg);
		if (msg instanceof FullHttpRequest) {
			// 以http请求形式接入，但是走的是websocket
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			// 处理websocket客户端的消息
			handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 添加连接
		logger.debug("客户端加入连接：" + ctx.channel());
		ChannelSupervise.addChannel(ctx.channel());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 断开连接
		logger.debug("客户端断开连接：" + ctx.channel());
		ChannelSupervise.removeChannel(ctx.channel());
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		// 判断是否关闭链路的指令
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
		// 判断是否ping消息
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// 本例程仅支持文本消息，不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
			logger.debug("本例程仅支持文本消息，不支持二进制消息");
			throw new UnsupportedOperationException(
					String.format("%s frame types not supported", frame.getClass().getName()));
		}
		if (frame instanceof TextWebSocketFrame) {// 文本消息
			String text = ((TextWebSocketFrame) frame).text();
			Packet packet = PacketDecoder.decodeFrame(text);// 解析客户端传递要绑定用户的数据
			Channel channel = ChannelSupervise.findChannel(ctx.channel().id().asShortText());
			receiver.onReceive(packet, channel);
		}
	}

	/**
	 * 唯一的一次http请求，用于创建websocket
	 */
	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		// 要求Upgrade为websocket，过滤掉get/Post
		if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
			HttpMethod method = req.method();
			String param = new RequestParser(req).parseJson(); // 将GET, POST所有请求参数转换成json对象
			String uri = req.uri();
			diapathcer(method, uri, param, ctx.channel());
			return;
		}
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
				"ws://localhost:8080/websocket", null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
		}
	}

	private void diapathcer(HttpMethod method, String uri, String param, Channel channel) {
		if (method == HttpMethod.GET && uri.startsWith("/push")) {
			logger.info("请求get方法,没有相关处理!");
		} else if (method == HttpMethod.POST && uri.startsWith("/push")) {
			Packet packet = PacketDecoder.decodeFrame(param);// 解析客户端传递要绑定用户的数据
			receiver.onReceive(packet, channel);
		}
	}

	/**
	 * 返回请求成功,并关闭监听
	 */
	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
		// 返回应答给客户端
		if (res.status().code() == 200) {
			ByteBuf buf = Unpooled.copiedBuffer("request success!", CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		f.addListener(ChannelFutureListener.CLOSE);
	}
}
