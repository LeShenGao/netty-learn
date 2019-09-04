package com.kele.netty.learnfirst;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 用于处理客户端发送的请求
 *
 * @author nanbaby
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取客户端发送过来的请求，并向客户端返回相应的方法
     *
     * @param ctx 用于获取 Channel 对象中的上下文
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            System.out.println("执行...");
            // 由于浏览器访问了两次，下面的代码会执行两次，为了不让下面的代码执行两次，必须对请求的地址进行判断
            // 浏览器访问两次的原因是：它第二次，会进行一个网站图标的请求 http://localhost:8888/favicon.ico
            // 基本每个网站，都会有这样一次请求
            HttpRequest request = (HttpRequest) msg;
            System.out.println("请求方法名称：" + request.method().name());
            URI uri = new URI(request.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求/favicon.ico");
                return;
            }

            // ByteBuf 极为重要。使用如下代码是构建出向客户端返回的内容。
            ByteBuf content = Unpooled.copiedBuffer("hello netty", CharsetUtil.UTF_8);
            // 构建 Http 响应
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            // 设置响应头
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            // writeAndFlush 使用该方法，能将响应给客户端的信息写入到"缓存"中，也能"刷新缓冲区"
            ctx.writeAndFlush(response);
        }
    }
}
