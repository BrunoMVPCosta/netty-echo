package com.netty.echo.client;

/**
 * Node
 */
public class Node {

    private final String host;
    private final int port;

    public Node(String host, int port) {
        this.host = host;
        this.port = port;
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