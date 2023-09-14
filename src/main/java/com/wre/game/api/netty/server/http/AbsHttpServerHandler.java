package com.wre.game.api.netty.server.http;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.wre.game.api.message.NullObject;
import com.wre.game.api.message.ResultCode;
import com.wre.game.api.netty.message.JsonUtil;
import com.wre.game.api.netty.server.thread.GMHttpHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http server handler extends ChannelInboundHandlerAdapter
 * 
 * 注意,该类为一个Sharable对象,所有channel共享该对象,具有非共享状态（如成员变量）
 * 
 * Sharable:如果未指定此注释,则每次将其添加到管道时都必须创建一个新的处理程序实例. 因为它具有非共享状态（如成员变量）。
 */
@ChannelHandler.Sharable
public class AbsHttpServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbsHttpServerHandler.class);
	/**
	 * 处理 http指令，根据是否有cmd判断gm和充值命令
	 * @param hashMap
	 * @return
	 */
	public Object action(HashMap<String, String> hashMap) {
		return GMHttpHandler.action(hashMap);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (!(msg instanceof FullHttpRequest)) {
			sendAndClose(ctx, "BAD_REQUEST");
			return;
		}
		FullHttpRequest request = (FullHttpRequest) msg;
		String uri = request.uri();
		HttpMethod method = request.method();
		try {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, Charsets.toCharset(CharEncoding.UTF_8));
			Map<String, List<String>> uriAttributes = queryDecoder.parameters();
			// 获取url参数
			for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
				for (String attrVal : attr.getValue()) {
					hashMap.put(attr.getKey(), attrVal);
				}
			}
			// 获取body参数
			if (method.equals(HttpMethod.POST)) {
				// POST请求,由于你需要从消息体中获取数据,因此有必要把msg转换成FullHttpRequest
				// 根据不同的Content_Type处理body数据
				String contentType = request.headers().get("Content-Type").toString().split(";")[0];
				String jsonStr = request.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
				if (contentType.equals("application/json")) {
					if (!StringUtils.isEmpty(jsonStr)) {
						hashMap.putAll((HashMap<String, String>) JSONUtil.toBean(jsonStr, Map.class));
					}
				}else if (contentType.equals("application/x-www-form-urlencoded")) {
					QueryStringDecoder qsd = new QueryStringDecoder(jsonStr, false);
					Map<String, List<String>> uriAttr = qsd.parameters();
					for (Map.Entry<String, List<String>> attr : uriAttr.entrySet()) {
					     hashMap.put(attr.getKey(), attr.getValue().get(0));
					 }
				}else {
					LOGGER.info("未处理的post contentType类型"+contentType);
				}
			}
			if (hashMap.size() > 0) {
				Object reslut = action(hashMap);
				sendAndClose(ctx, JSON.toJSONString(reslut));
			} else {
				sendAndClose(ctx, JsonUtil.getResponseJsonStr(ResultCode.PARAMETER_CODE,HttpResponseStatus.UNPROCESSABLE_ENTITY.toString(), NullObject.getInstance()));
			}
		} catch (Exception e) {
			LOGGER.error("http server do request. exception :" + e.getMessage(),e);
			sendAndClose(ctx, HttpResponseStatus.EXPECTATION_FAILED.toString());
		} finally {
			// 如果不写这个手动释放则用SimpleChannelHandler，它在所有接收消息的地方都调用了 ReferenceCountUtil.release(msg)
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LOGGER.error(cause.getMessage(),cause);
		ctx.close();
	}

	public static void response(Channel channel, HttpContentType type, ByteBuf buf) {
		try {
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, type.toString());
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
			ChannelFuture future = channel.writeAndFlush(response);
			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture channelFuture) throws Exception {
					channelFuture.channel().close();
				}
			});
		} catch (Exception ex) {
			LOGGER.error("http.response failed", ex);
		}
	}

	/**
	 * 推送消息后并关闭连接
	 * 
	 * @param ctx
	 */
	public static void sendAndClose(ChannelHandlerContext ctx, String str) {
		response(ctx.channel(), HttpContentType.Text, Unpooled.wrappedBuffer(str.getBytes()));
	}

}
