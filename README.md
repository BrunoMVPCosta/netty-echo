Not sure what is this yet.

Future work:
 - Add integration tests to the transport layer
 - Add pooled connections to the address and validate if the channel is still good or exists before sending new message
 - Use Java Futures in the Transport interface and the implementation of it using netty
 - Add method `sendAndReceive` to the transport layer to allow request-response use cases

## How to run

```
mvn exec:java -Dexec.mainClass=com.netty.echo.EchoClient -Dexec.args="8080 127.0.0.1:8081 127.0.0.1:8082"
```

```
mvn exec:java -Dexec.mainClass=com.netty.echo.EchoClient -Dexec.args="8081 127.0.0.1:8080 127.0.0.1:8082"
```

```
mvn exec:java -Dexec.mainClass=com.netty.echo.EchoClient -Dexec.args="8082 127.0.0.1:8080 127.0.0.1:8081"
```
