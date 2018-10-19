package com.netty.echo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * EchoClient
 */
public class EchoClient {

    private final Transport transport;
    private final Node node;

    public EchoClient(Node node, Transport transport) {
        this.node = node;
        this.transport = transport;
    }

    public void start() throws Exception {
        System.out.println("[CLIENT] - Starting client with host " + node.getHost() + " and port " + node.getPort());
        
        while(true) 
        {
            transport.send(node, "netty rocks" + "\r\n");
            Thread.sleep(2 * 1000);
        }
    }

    public static void main(String[] args) throws Exception {
        if(args.length != 2) {
            System.err.println("Usage " + EchoClient.class.getSimpleName() + " <host> <port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
            .group(group)
            .channel(NioSocketChannel.class)
            .handler(new EchoClientInitializer());

        Node node = new Node(host, port);
        NettyTransport transport = new NettyTransport(bootstrap);
        transport.connectToNode(node);
        EchoClient client = new EchoClient(node, transport);
      
        client.start();
    }
}