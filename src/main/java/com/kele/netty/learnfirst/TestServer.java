package com.kele.netty.learnfirst;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author nanbaby
 */
public class TestServer {
    public static void main(String[] args) throws Exception {
        // 该线程主要用于接受请求。
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 通过上面的线程进行扭转，将请求转到下面的线程，进行请求数据的处理。
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 构建服务端启动
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    // 将 boosGroup 接受到的请求转到 workerGroup 上。
                    .group(bossGroup, workerGroup)
                    // channel 使用了一个管道，通过反射的形式，创建传入的参数对象。
                    .channel(NioServerSocketChannel.class)
                    // 自定义处理器，专门用于对请求数据进行处理
                    .childHandler(new TestServerInitializer());

            // 启动服务
            // 绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
