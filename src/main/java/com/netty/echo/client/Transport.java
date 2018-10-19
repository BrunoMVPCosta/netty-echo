package com.netty.echo.client;

/**
 * Transport
 */
public interface Transport {

    void connectToNode(Node node) throws Exception;
    void send(Node node, String message);
}