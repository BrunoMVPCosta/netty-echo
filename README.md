Not sure what is this yet.

Future work:
 - Add a new folder for netty implementation
 - Add integration tests to the transport layer
 - Remove the `connectToAddress` method from the Transport interface
    - Add pooled connections to the address and validate if the channel is still good or exists before sending new message
 - Use Java Futures in the Transport interface and the implementation of it using netty
 - Add method `sendAndReceive` to the transport layer to allow request-response use cases