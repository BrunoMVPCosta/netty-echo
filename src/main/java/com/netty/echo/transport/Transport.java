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

    /**
     * Send and waits for the reply
     * @param address
     * @param payload
     * @return
     */
    byte[] sendAndReceive(Address address, byte[] payload);
}