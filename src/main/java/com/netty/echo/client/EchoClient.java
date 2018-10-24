package com.netty.echo.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import com.netty.echo.cluster.Cluster;
import com.netty.echo.cluster.DefaultCluster;
import com.netty.echo.cluster.Node;
import com.netty.echo.server.EchoServer;

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
    private final Cluster cluster;

    public EchoClient(Cluster cluster, Transport transport) {
        this.cluster = cluster;
        this.transport = transport;
    }

    public void start() throws Exception {
        System.out.println("[CLIENT] - Starting client with host " + cluster.getMember().getHost() + " and port " + cluster.getMember().getPort());

        while(true)
        {
            for (Node node : cluster.getMembers()) {
                transport.send(node, "[CLIENT:" + cluster.getMember().getPort() + "] to " + node.getPort() + " Message: " + UUID.randomUUID().toString() + "\r\n");
            }
            Thread.sleep(2 * 1000);
        }
    }

    public static void main(String[] args) throws Exception {
        if(args.length < 2) {
            System.err.println("Usage " + EchoClient.class.getSimpleName() + " <port> <host>:<port>...");
            return;
        }

        int localPort = Integer.parseInt(args[0]);
        Node localNode = new Node("local-node", "127.0.0.1", localPort);

        Cluster cluster = new DefaultCluster(localNode);

        for(int i = 1; i < args.length; i++) {

            String[] splitted = args[i].split(":");
            String host = splitted[0];
            int port = Integer.parseInt(splitted[1]);

            Node node = new Node(host, port);
            cluster.addMember(node);
        }

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
            .group(group)
            .channel(NioSocketChannel.class)
            .handler(new EchoClientInitializer());

        NettyTransport transport = new NettyTransport(bootstrap);
        for (Node node : cluster.getMembers()) {
            transport.connectToNode(node);
        }

        System.out.println("Cluster information");
        System.out.println("Cluster name: " + cluster.getName());
        System.out.println("Members: ");
        for (Node node : cluster.getMembers()) {
            System.out.println("\tMember: " + node.getId());
            System.out.println("\t\thost: " + node.getHost());
            System.out.println("\t\tport: " + node.getPort());
            System.out.println("--");
        }

        EchoServer server = new EchoServer(cluster.getMember().getPort());
        server.start();

        EchoClient client = new EchoClient(cluster, transport);
        client.start();
    }
}