package com.zhang.netty.test.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 深圳金雅福控股集团有限公司
 *
 * @author zhangfanjun
 * @date 2022/5/23
 */
public class ServerHandler1 extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("Client Address ====== " + ctx.channel().remoteAddress());
        ctx.channel().writeAndFlush("from server:" + UUID.randomUUID());
        ctx.fireChannelActive();
        TimeUnit.MILLISECONDS.sleep(500);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
