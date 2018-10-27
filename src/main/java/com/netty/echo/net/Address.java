package com.netty.echo.net;

import java.util.Objects;

public final class Address {

    private final String host;
    private final int port;

    private static final String SPLIT_IPv4 = ":";
    private static final String SPLIT_IPv6 = "]:";

    public Address(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    /**
     * Returns a new address instance given an address string
     * @param address the address string with the format host:port
     * @return the new address instance
     */
    public static Address from(String address) {

        if(address == null || address == "") {
            throw new IllegalArgumentException("Address cannot be empty or null");
        }

        String splitted = SPLIT_IPv4;

        if(address.startsWith("[")) {
            address = address.substring(1);
            splitted = SPLIT_IPv6;
        }

        String[] hostAndPort = address.split(splitted);
        if(hostAndPort.length != 2) {
            throw new MalformedAddressException(String.format("The address %s is not valid", address));
        }

        String host = hostAndPort[0];
        int port = Integer.parseInt(hostAndPort[1]);

        return new Address(host, port);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(this.getClass() != obj.getClass()) return false;

        Address object = (Address) obj;
        return this.getHost().equals(object.getHost())
            && this.getPort() == object.getPort();
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}