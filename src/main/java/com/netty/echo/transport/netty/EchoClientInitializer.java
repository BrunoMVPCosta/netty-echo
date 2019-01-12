package com.netty.echo.transport.netty;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * EchoClientInitializer
 */
public class EchoClientInitializer extends ChannelInitializer<SocketChannel> {

    private final ConcurrentMap<UUID, CompletableFuture<byte[]>> futures;

    public EchoClientInitializer(ConcurrentMap<UUID, CompletableFuture<byte[]>> futures) {
        this.futures = futures;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());

        pipeline.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast("handler", new EchoClientHandler(futures));
    }
}