package com.zhang.simple.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author zhangFanJun
 * @date 2022-04-20 00:08
 **/
public class EchoClient {

    private static final String host=  "127.0.0.1";
    private static final int port=  8080;

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .localAddress(new InetSocketAddress(host,port))
                    //这里使用SocketChannel，是因为上面channel用了NioServerSocketChannel
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //自定义添加事件
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            //连接
            ChannelFuture sync = bootstrap.connect().sync();
            sync.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }
}
