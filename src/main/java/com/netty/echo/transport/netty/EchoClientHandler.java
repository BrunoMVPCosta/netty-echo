package com.netty.echo.transport.netty;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * EchoClientHandler
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<String> {

    private final ConcurrentMap<UUID, CompletableFuture<byte[]>> futures;

    public EchoClientHandler(ConcurrentMap<UUID, CompletableFuture<byte[]>> futures) {
        this.futures = futures;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String text) throws Exception {
        System.out.println("[CLIENT] - Received: " + text);

        try {
            UUID messageId = UUID.fromString(text.replace("\n", ""));
            CompletableFuture<byte[]> future = futures.get(messageId);
            future.complete(text.getBytes());
            futures.remove(messageId);
        }
        catch (Exception ex) {}
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        System.out.println("[CLIENT] - Received: " + msg.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}