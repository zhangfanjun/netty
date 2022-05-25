package com.zhang.netty.test.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


@Slf4j
public class ClientHandler1 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("客户端读取的信息：{}", msg);
        ctx.channel().writeAndFlush("客户端writeAndFlush：我是客户端");
        TimeUnit.MILLISECONDS.sleep(5000);
    }

    /**
     * 当事件到达pipeline时候触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("客户端：开始聊天");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //关闭Channel连接
        ctx.close();
    }

}
