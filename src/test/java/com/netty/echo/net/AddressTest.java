package com.netty.echo.net;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * AddressTest
 */
public class AddressTest extends TestCase {

    public AddressTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AddressTest.class);
    }

    public void testfromIPv4LocalAddress() {
        final String expectedHost = "127.0.0.1";
        final int expectedPort = 8080;

        Address address = Address.from("127.0.0.1:8080");
        assertEquals(expectedHost, address.getHost());
        assertEquals(expectedPort, address.getPort());
    }

    public void testfromIPv4Address() {
        final String expectedHost = "216.58.211.36";
        final int expectedPort = 80;

        Address address = Address.from("216.58.211.36:80");
        assertEquals(expectedHost, address.getHost());
        assertEquals(expectedPort, address.getPort());
    }

    public void testfromIPv6LocalAddress() {
        final String expectedHost = "::1";
        final int expectedPort = 80;

        Address address = Address.from("[::1]:80");
        assertEquals(expectedHost, address.getHost());
        assertEquals(expectedPort, address.getPort());
    }

    public void testfromIPv6Address() {
        final String expectedHost = "2a03:2880:f129:83:face:b00c::25de";
        final int expectedPort = 80;

        Address address = Address.from("[2a03:2880:f129:83:face:b00c::25de]:80");
        assertEquals(expectedHost, address.getHost());
        assertEquals(expectedPort, address.getPort());
    }

    public void testfromWithEmptyAddressShouldThrowIllegalArgumentException() {
        try {
            Address.from("");
        } catch (IllegalArgumentException ex) {
            assertEquals("Address cannot be empty or null", ex.getMessage());
        }
    }

    public void testfromWithInvalidAddressShouldThrowException() {
        String address = "12.2.3.4.2";
        try {
            Address.from(address);
        } catch (MalformedAddressException ex) {
            assertEquals(String.format("The address %s is not valid", address), ex.getMessage());
        }
    }

    public void testTwoIdenticalInstancesShouldBeEqual() {
       Address address1 = Address.from("127.0.0.1:80");
       Address address2 = Address.from("127.0.0.1:80");

       assertEquals(address1, address2);
    }

    public void testTwoDifferentInstancesShouldNotBeEqual() {
        Address address1 = Address.from("127.0.0.1:80");
        Address address2 = Address.from("127.0.0.1:81");

        assertFalse(address1.equals(address2));
     }
}