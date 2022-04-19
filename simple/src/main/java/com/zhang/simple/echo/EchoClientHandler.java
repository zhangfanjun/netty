package com.zhang.simple.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangFanJun
 * @date 2022-04-20 00:08
 **/
@Slf4j
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        log.info("客户端接收到数据：buf：{}",byteBuf.toString(CharsetUtil.UTF_8));
        channelHandlerContext.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端发送事件");
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，我是客户端",CharsetUtil.UTF_8));
//        super.channelActive(ctx);
    }
}
