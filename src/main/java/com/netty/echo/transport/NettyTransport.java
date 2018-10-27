package com.netty.echo.transport;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.netty.echo.net.Address;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * TransportImpl
 */
public class NettyTransport implements Transport {

    private final Bootstrap bootstrap;
    private final ServerBootstrap serverBootstrap;
    private final ConcurrentMap<Address, Channel> connections = new ConcurrentHashMap<Address, Channel>();

    public NettyTransport(Bootstrap bootstrap, ServerBootstrap serverBootstrap) {
        this.bootstrap = bootstrap;
        this.serverBootstrap = serverBootstrap;
    }

    public void send(Address address, byte[] payload) {
        send(address, new String(payload, StandardCharsets.UTF_8));
    }

    private void send(Address address, String message) {
        Channel channel = connections.get(address);
        if(channel == null) {
            System.out.println("[CLIENT] - Message ignored because channel is null");
            return;
        }

        channel.writeAndFlush(message);
    }

    public void connectToAddress(Address address) throws Exception {
        final Timer timer = new Timer("connecting-timer");
        final TimerTask task = new ReconnectTask(address);

        timer.schedule(task, 1000L, 1000L);
    }

    /**
     * The listener to control when the connection is closed
     */
    private class CloseChannelListener implements ChannelFutureListener {

        public void operationComplete(ChannelFuture future) throws Exception {
            System.out.println("[CLIENT] - Connection closed");

            Address address = null;
            for(Map.Entry<Address, Channel> entry :  connections.entrySet()) {
                if(entry.getValue() == future.channel()) {
                    address = entry.getKey();
                    Channel channel = entry.getValue();
                    channel.disconnect();
                    break;
                }
            }

            if(address != null) {
                connections.remove(address);
            }

            final Timer timer = new Timer("reconnect-timer");
            final TimerTask task = new ReconnectTask(address);

            timer.schedule(task, 1000L, 1000L);
        }
    }

    private class ReconnectTask extends TimerTask {
        private final Address address;

        public ReconnectTask(Address address) {
            this.address = address;
        }

        public void run() {
            System.out.println("[CLIENT] - " + Thread.currentThread().getName());
            try {
                Channel channel = bootstrap.connect(address.getHost(), address.getPort()).sync().channel();
                channel.closeFuture().addListener(new CloseChannelListener());
                connections.putIfAbsent(address, channel);

                System.out.println("[CLIENT] - Connected");
                cancel();
            }
            catch(Exception ex) {
                System.out.println("[CLIENT] - Connecting failed " + ex);
            }
        }
    }
}