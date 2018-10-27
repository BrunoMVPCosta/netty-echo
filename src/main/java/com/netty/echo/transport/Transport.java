package com.netty.echo.transport;

import com.netty.echo.net.Address;

/**
 * Transport
 */
public interface Transport {

    void connectToAddress(Address address) throws Exception;
    /**
     * Send a message to a specific address
     */
    void send(Address address, String message);
}