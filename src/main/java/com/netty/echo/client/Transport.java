package com.netty.echo.client;

import com.netty.echo.cluster.Node;

/**
 * Transport
 */
public interface Transport {

    void connectToNode(Node node) throws Exception;
    void send(Node node, String message);
}