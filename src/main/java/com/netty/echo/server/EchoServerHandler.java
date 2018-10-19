package com.netty.echo.server;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * EchoServerHandler
 */
public class EchoServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[SERVER] - " + ctx.channel().remoteAddress() + " has joined!\n");
        ctx.write("[SERVER] - " + ctx.channel().remoteAddress() + " has joined!\n");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[SERVER] - " + ctx.channel().remoteAddress() + " has leave!\n");
    }

    // @Override
    // public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    //     super.channelRead(ctx, msg);
    //     System.out.println("[SERVER] - Received: channelRead " + msg);
    // }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        
        System.out.println("[SERVER] - Received: " + msg);

        ctx.writeAndFlush(msg + "\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}