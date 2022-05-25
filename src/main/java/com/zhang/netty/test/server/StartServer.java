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
 * channel是启动的关键，由channel实例的pipeline添加处理器
 *
 * @author zhangfanjun
 * @date 2022/5/23
 */
@Slf4j
public class StartServer {

    private final static int PORT = 9012;

    public static void main(String[] args) throws InterruptedException {
        /**
         * 包含childGroup，childHandler，config，继承的父类AbstractBootstrap包括了parentGroup
         * */
        ServerBootstrap bootstrap = new ServerBootstrap();
        /**
         * EventLoopGroup用于处理所有ServerChannel和Channel的所有事件和IO
         * */
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            /**
             * 绑定两个事件组
             * */
            bootstrap.group(parentGroup, childGroup)
                    /**
                     * 初始化socket，定义tcp连接的实例
                     * 内部调用ReflectiveChannelFactory实现对NioServerSocketChannel实例化
                     * channelFactory是在AbstractBootstrap，也就是bootstrap的父类
                     * */
                    .channel(NioServerSocketChannel.class)
                    /**
                     * 添加处理器
                     * ChannelInitializer包括了Set<ChannelHandlerContext> initMap
                     *
                     * 这里比较有趣的事情就是使用被注册的channel去初始化其他的channel，
                     * 等初始化结束后移除该channel
                     * 所以SocketChannel是一个工具，
                     *
                     * 在bind绑定端口的时候，进行初始化和注册initAndRegister，
                     * 通过channel = channelFactory.newChannel()得到初始化channel
                     * init(channel)真正开始初始化，
                     * p = channel.pipeline()得到ChannelPipeline，
                     * p.addLast开始添加
                     * ch.eventLoop().execute将childHandler赋值并开启一个任务setAutoRead
                     * 所以最后在监听读取的时候将会按照下面添加的channel进行读取
                     *
                     * ChannelInitializer继承了ChannelInboundHandlerAdapter
                     * 间接继承ChannelHandlerAdapter，ChannelInboundHandler，
                     * */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /**
                             * ByteBuf和String之间的转换
                             *
                             *  Decoders解密
                             *  pipeline.addLast("frameDecoder", new {@link LineBasedFrameDecoder}(80))
                             *  pipeline.addLast("stringDecoder", new {@link StringDecoder}(CharsetUtil.UTF_8))
                             *
                             *  Encoder加密
                             *  pipeline.addLast("stringEncoder", new {@link StringEncoder}(CharsetUtil.UTF_8))
                             *
                             *  使用上面的加密解密后就可以直接读取字符串
                             *   void channelRead({@link ChannelHandlerContext} ctx, String msg) {
                             *       ch.write("Did you say '" + msg + "'?\n")
                             *  }
                             *
                             * */
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            //自定义处理器
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
