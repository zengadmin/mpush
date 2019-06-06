package com.fpush.common.codec;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

public class RequestParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestParser.class);

	private FullHttpRequest fullReq;

	/**
	 * 构造一个解析器
	 * 
	 * @param req
	 */
	public RequestParser(FullHttpRequest req) {
		this.fullReq = req;
	}

	/**
	 * 解析请求参数
	 * 
	 * @return 包含所有请求参数的键值对, 如果没有参数, 则返回空Map
	 *
	 * @throws BaseCheckedException
	 * @throws IOException
	 */
	public Map<String, Object> parse() {
		HttpMethod method = fullReq.method();
		Map<String, Object> parmMap = new HashMap<String, Object>();
		if (HttpMethod.GET == method) {// 是GET请求
			QueryStringDecoder decoder = new QueryStringDecoder(fullReq.uri());
			decoder.parameters().entrySet().forEach(entry -> {
				parmMap.put(entry.getKey(), entry.getValue().get(0));
			});
		} else if (HttpMethod.POST == method) {// 是POST请求
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullReq);
			decoder.offer(fullReq);

			List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();
			try {
				for (InterfaceHttpData parm : parmList) {

					Attribute data = (Attribute) parm;
					parmMap.put(data.getName(), data.getValue());
				}
			} catch (Exception e) {
				LOGGER.info("========post方法错误==========");
			}

		} else {// 其他方法
			LOGGER.info("========调用其他方法==========");
		}

		return parmMap;
	}

	//解析json字符串
	public String parseJson() {
		ByteBuf jsonBuf = fullReq.content();
		String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
		return jsonStr;
	}
}
