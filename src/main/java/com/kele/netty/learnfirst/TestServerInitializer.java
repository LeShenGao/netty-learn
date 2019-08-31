package com.kele.netty.learnfirst;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 被添加到管道的子处理器以后，服务器运行时就会被触发创建
 *
 * @author nanbaby
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 当连接被初始化后，会被调用的方法
     *
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // pipeline 属于一个管道，这个管道里面，
        // 包含很多个 childHandler ，
        // 每个 childHandler 就相当于一个拦截器
        // 每个拦截器，都会根据请求，进行相应的业务处理
        ChannelPipeline pipeline = ch.pipeline();

        // netty 很重要的处理器，用于对请求响应进行编解码
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        // 自定义处理器
        pipeline.addLast("testHttpServerHandler", new TestHttpServerHandler());
    }
}
