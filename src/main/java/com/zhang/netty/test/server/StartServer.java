package com.zhang.netty.test.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;


/**
 * 深圳金雅福控股集团有限公司
 *
 * @author zhangfanjun
 * @date 2022/5/23
 */
@Slf4j
public class StartServer {

    private final static int PORT = 9012;
    public static void main(String[] args) throws InterruptedException {
        //包含childGroup，childHandler，config
        ServerBootstrap bootstrap = new ServerBootstrap();
        //EventLoopGroup用于处理所有ServerChannel和Channel的所有事件和IO
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            //对服务器的父事件循环组和子事件循环组，
            bootstrap.group(parentGroup, childGroup)
                    //内部调用ReflectiveChannelFactory实现对NioServerSocketChannel实例化，channelFactory是在AbstractBootstrap
                    .channel(NioServerSocketChannel.class)
                    //添加处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new ServerHandler1());
                        }
                    });
            ChannelFuture future = bootstrap.bind(PORT).sync();
            log.info("服务器已启动");
            future.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
