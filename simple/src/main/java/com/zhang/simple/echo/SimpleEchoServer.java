package com.zhang.simple.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.net.InterfaceAddress;

/**
 * @author zhangFanJun
 * @date 2022-04-20 00:08
 **/
public class SimpleEchoServer {

    private int port;
    private SimpleEchoServer(int port){
        this.port = port;
    }
    public static void main(String[] args) throws InterruptedException {
        SimpleEchoServer simpleEchoServer = new SimpleEchoServer(8080);
        //最终是为了执行这个方法
        simpleEchoServer.start();
    }

    private void start() throws InterruptedException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(eventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    //这里使用SocketChannel，是因为上面channel用了NioServerSocketChannel
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //自定义添加事件
                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            //绑定服务器，sync会阻塞到完成
            ChannelFuture sync = serverBootstrap.bind().sync();
            sync.channel().closeFuture().sync();

        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }
}