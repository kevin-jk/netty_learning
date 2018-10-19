package com.kun.practise.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;




/**
 * ChannelInboundHandler
 *
 *
 * */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("Recevied from server:"+byteBuf.toString(CharsetUtil.UTF_8));
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 写出站数据
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，Netty",CharsetUtil.UTF_8));

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}

