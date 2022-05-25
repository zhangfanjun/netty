package com.zhang.netty.test.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;



@Slf4j
public class StartClient {
    private final static int PORT = 9012;
    private final static String IP = "localhost";

    public static void main(String[] args) throws InterruptedException {
        /**
         * 服务端是ServerBootstrap，客户端是Bootstrap
         * Bootstrap引导channel连接，UDP连接用bind方法，TCP连接用connect方法
         * */
        Bootstrap bootstrap = new Bootstrap();
        /**
         * 服务端是EventLoopGroup，客户端是NioEventLoopGroup
         * 这里创建默认0个线程，一个线程工厂，一个选择器提供者
         * */
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(eventLoopGroup)
                    /**
                     * 初始化socket，定义tcp连接的实例
                     * */
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /**
                             * 进行字符串的转换
                             * */
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                            /**
                             * 自定义处理器
                             * */
                            pipeline.addLast(new ClientHandler1());
                        }
                    });
            ChannelFuture future = bootstrap.connect(IP, PORT).sync();
            log.info("客户端访问");
            future.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
