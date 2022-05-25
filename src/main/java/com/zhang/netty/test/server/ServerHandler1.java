package com.zhang.netty.test.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


@Slf4j
public class ServerHandler1 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("Client Address ====== {}，读取的信息：{}", ctx.channel().remoteAddress(),msg);
        ctx.channel().writeAndFlush("服务端writeAndFlush：我是服务端");
        ctx.fireChannelActive();
        //睡眠
        TimeUnit.MILLISECONDS.sleep(500);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常
        cause.printStackTrace();
        //关闭Channel连接，并通知ChannelFuture，通常是出现异常或者是完成了操作
        ctx.close();
    }
}
