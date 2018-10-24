package com.netty.echo.client;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;

import com.netty.echo.cluster.Node;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * TransportImpl
 */
public class NettyTransport implements Transport {

    private final Bootstrap bootstrap;
    private final ConcurrentMap<Node, Channel> connections = new ConcurrentHashMap<Node, Channel>();

    public NettyTransport(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public void send(Node node, String message) {
        Channel channel = connections.get(node);
        if(channel == null) {
            System.out.println("[CLIENT] - Message ignored because channel is null");
            return;
        }

        channel.writeAndFlush(message);
    }

    public void connectToNode(Node node) throws Exception {
        final Timer timer = new Timer("connecting-timer");
        final TimerTask task = new ReconnectTask(node);

        timer.schedule(task, 1000L, 1000L);
    }

    /**
     * The listener to control when the connection is closed
     */
    private class CloseChannelListener implements ChannelFutureListener {

        public void operationComplete(ChannelFuture future) throws Exception {
            System.out.println("[CLIENT] - Connection closed");

            Node node = null;
            for(Map.Entry<Node, Channel> entry :  connections.entrySet()) {
                if(entry.getValue() == future.channel()) {
                    node = entry.getKey();
                    Channel channel = entry.getValue();
                    channel.disconnect();
                    break;
                }
            }

            if(node != null) {
                connections.remove(node);
            }

            final Timer timer = new Timer("reconnect-timer");
            final TimerTask task = new ReconnectTask(node);

            timer.schedule(task, 1000L, 1000L);
        }
    }

    private class ReconnectTask extends TimerTask {
        private final Node node;

        public ReconnectTask(Node node) {
            this.node = node;
        }

        public void run() {
            System.out.println("[CLIENT] - " + Thread.currentThread().getName());
            try {
                Channel channel = bootstrap.connect(node.getHost(), node.getPort()).sync().channel();
                channel.closeFuture().addListener(new CloseChannelListener());
                connections.putIfAbsent(node, channel);

                System.out.println("[CLIENT] - Connected");
                cancel();
            }
            catch(Exception ex) {
                System.out.println("[CLIENT] - Connecting failed " + ex);
            }
        }
    }
}