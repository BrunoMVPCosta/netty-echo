package com.netty.echo.cluster;

import java.util.UUID;

/**
 * Node
 */
public class Node {

    private final String id;
    private final String host;
    private final int port;

    public Node(String host, int port) {
        this(UUID.randomUUID().toString(), host, port);
    }

    public Node(String id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }
}