package com.netty.echo.transport;

import com.netty.echo.net.Address;

/**
 * Transport
 */
public interface Transport {
    /**
     * Send a message to a specific address
     */
    void send(Address address, byte[] payload);
}