Advanced Middleware RPC API Specification
=========================================
___Date: 8th April 2015___

This version: [*0.2.0*](#)  
Previous version: n/a  
Latest version: [*latest*](Middleware_RPC_API_Specification.html)  

Editors:

- [eProsima - The Middleware Experts][EPROS]
- [DFKI - German Research Center for Artificial Intelligence][DFKI]
- [ZHAW - School of Engineering (ICCLab)][ZHAW]

[EPROS]: http://eprosima.com/index.php/en/ "eProsima - The Middleware Experts"
[DFKI]: http://www.dfki.de/ "German Research Center for Artificial Intelligence"
[ZHAW]: http://blog.zhaw.ch/icclab "ZHAW - School of Engineering (ICCLab)"

Copyright © 2013-2015 by eProsima, DFKI, ZHAW. All Rights Reserved

----

Abstract
--------
Ahe Advanced Middleware GE enables flexible, efficient, scalable, and secure communication between distributed applications and to/between FIWARE GEs.
The __Middleware RPC API Specification__ describes the interfaces and procedures to do Request/Reply type Remote Procedure Calls (RPC).

It provides basic static compile-time Data-Mapping and generation of Function Stubs/Skeletons, created by a compile time IDL-Parser/Generator from the remote service description, which is provided in the Advanced Middleware Interface Definition Language (IDL) syntax, which is based on the Object Management Group (OMG) IDL draft submitted to W3C.

## Status of this Document

| **Date**        | **Description** | 
|-----------------|-----------------|
| 7-November-2014 | Release 0.1.0   |
| 8-April-2015    | Release 0.2.0   |

----

Introduction
------------
### Purpose

This document attempts to describe the Advanced Middleware RPC API.

### Reference Material

- [*Advanced Middleware IDL Specification*][AMiIDLSpec]
- [*Advanced Middleware RPC Dynamic Types API Specification*][AMiRPCDynSpec]

[AMiIDLSpec]: ./Middleware_IDL_Specification.html "Advanced Middleware IDL Specification"
[AMiRPCSpec]: ./Middleware_RPC_API_Specification.html "Advanced Middleware RPC API Specification"
[AMiRPCDynSpec]: ./Middleware_RPC_Dynamic_Types_API_Specification.html "Advanced Middleware RPC Dynamic Types API Specification"

A quick Example
----------------
Before the description of the public Advanced Middleware RPC API, a quick example is provided. It shows how a simple client is created, as well as a simple server. The example uses the following Advanced Middleware interface definition:
```
service Calculator
{
    i32 add(i32 num1, i32 num2);
};
```

### Creating a client
The following code shows how to instanciate and start a client and execute a call to the server:

```java
Context context = Advanced Middleware.createContext();
Connection connection = context.connect("tcp://192.168.1.18:8080?serialization=cdr");
CalculatorClient client = connection.getServiceProxy(CalculatorClient.class);

int result = client.add(3,4);
```

### Creating a server
The following code shows how to instanciate and start a server:
```java
Context context = Advanced Middleware.createContext();
Server server = context.createServer();
Service service = context.createService();

// User creates its implementation of the Middleware IDL service.
Calculator calculator_impl = new CalculatorImpl();

service.register(calculator_impl);
server.addService(service, "tcp://0.0.0.0:8080", "cdr");

server.run();
```


API Overview
-----------
This section enumerates and describes the classes provided by Advanced Middleware RPC API.

### Main entry point

#### org.fiware.kiara.Kiara

This class is the main entry point to use the Advanced Middlware. It creates or provides implementation of the top level Advanced Middleware interfaces, especially the `Context`.

**Functions**:

- **getTypeDescriptorBuilder**: This function returns an instance of the type descriptor builder. It is a part of the dynamic API and is described [*here*][AMiRPCDynSpec].
- **getDynamicValueBuilder**: This function returns an instance of the dynamic value builder. It is a part of the dynamic API and is described [*here*][AMiRPCDynSpec].
- **createContext**: This function creates a new instance of the Context class, which is described below.
- **shutdown**: This function closes and releases all internal Advanced Middleware structures (e.g. stops all pending tasks). Call this before you exit your application.


### Common interfaces

#### org.fiware.kiara.Context

This interface is the starting point to use the Advanced Middleware. It holds the configuration of the middleware and hides the process of negotiation, selection, and configuration of the correct implementation classes. Also it provides users a way to instantiate Advanced Middleware components.

**Functions:**

- **connect**: This function creates a new connection to the server. This connection might be used by proxies to send requests to the server.
- **createTransport**: This function provides a direct way to create a specific network `Transport` instance which can be configured for specific use cases.
- **createSerializer**: This function provides a direct way to create a specific `Serializer` instance which can be configured for specific use cases.
- **createServer**: This function creates a new `Server` instance used to add `Service` instances.
- **createService**: This function creates a new `Service` instance used to register `Servant` instances.


### Network transports

#### org.fiware.kiara.transport.Transport

This interface provides a basic abstraction for network transport implementations. To create a `Transport` instance directly, the developer must use the factory method `createTransport` of the interface *org.fiware.kiara.Context*, which will return a compliant network transport implementation.

**Functions:**

- **getTransportFactory**: This function returns an instance of the factory class used to create this transport instance.
 
---
#### org.fiware.kiara.transport.ServerTransport

This interface provides an abstraction for a server-side connection endpoint waiting for incoming connections.

**Functions:**

- **getTransportFactory**: This function returns an instance of a factory class which was used to create this server transport instance.
- **setDispatchingExecutor**: This function sets executor service used for dispatching incoming messages.
- **getDispatchingExecutor**: Returns executor service previously set.
- **isRunning**: Returns true if server is up and waiting for incoming connections.
- **startServer**: Starts server.
- **stopServer**: Stops server.
- **getLocalTransportAddress**: Returns transport address to which this server is bound.

---
#### org.fiware.kiara.transport.TransportFactory

This interface provides the abstraction of the factory that creates `Transport` and `ServerTransport` instances. It has a name and can be registered with the `Context` via an internal API.

**Functions:**

- **getName:** This function returns name of the transport.
- **getPriority:** This function returns the priority of the transport which depends on the efficiency.
- **isSecureTransport:** This function returns true when the transport uses secure connections.
- **createTransport:** This function instantiates a network transport when the user wants later to configure it.
- **createServerTransport:** This function instantiates a specific network `ServerTransport` instance which can be configured for specific use cases


### Serialization mechanisms

#### org.fiware.kiara.serialization.Serializer

This interface provides an abstraction for serialization mechanism implementations. To create a `Serializer` instance directly, the developer must use the factory method `createSerializer` of the interface *org.fiware.kiara.Context*, which will return a compliant serializer implementation.

**Functions:**

- **getName:** Returns name of the serializer.

#### org.fiware.kiara.transport.SerializerFactory

This interface provides the abstraction of the factory that creates serializers. It has a name and can be registered with the `Context` via an internal API.

**Functions:**

- **getName:** This function returns name of the serializer.
- **getPriority:** This function returns the priority of the serializer which depends on the efficiency.
- **createSerializer:** This function creates a new serializer.

### Dispatching strategies

#### java.util.concurrent.ExecutorService

Different threading strategies are implemented by using different implementations of the built-in *java.util.concurrent.ExecutorService* class:

- **Single thread:** by creating single thread executor with: [*java.util.concurrent.Executors.newSingleThreadExecutor*](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Executors.html#newSingleThreadExecutor())[*()*](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Executors.html#newSingleThreadExecutor())
- **Thread pool:** by creating fixed thread pool executor with: [*java.util.concurrent.Executors.newFixedThreadPool()*](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Executors.html#newFixedThreadPool(int))
- **Thread per request:** by creating cached thread pool executor with: [*java.util.concurrent.Executors.newCachedThreadPool()*](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Executors.html#newCachedThreadPool())


### Client API

#### org.fiware.kiara.client.Connection

The connection interface manages the connection to the server. It holds the required Transport and Serialization objects. Also it can create these object automatically depending on the server information. The connection provides the `ServiceProxy` interfaces, which will can be used by the application to call remote functions.

**Functions:**

- **getServiceProxy**: This function provides a new proxy instance to be used to call remote procedures. This proxy uses the `Connection` instance to send the requests to the server.
- **getDynamicProxy**: This function provides a new dynamic proxy instance to be uses to call remote procedures. This proxy uses the `Connection` instance to send the requests to the server.

---
#### org.fiware.kiara.client.AsyncCallback

This interface provides an abstraction used by the client to return the server’s reply when the call was asynchronous.

**Functions:**

- **onSuccess**: This function will be called when the remote function call was successfull. It must be implemented by the user.
- **onFailure**: This function will be called when the remote function call was *not* successfull.It must be implemented by the user.

### Server API

#### org.fiware.kiara.server.Server

Using this interface, users can start up multiple services on different ports. The implementation uses serialization mechanisms and network transports to listen for client requests and executes the proper `Servant` implementation. The optional negotiation protocol provides automatic discovery of all available services via the HTTP protocol.

**Functions:**

- **enableNegotiationService**: Enables the negotiation service on the specified port and configuration path.
- **disableNegotiationService**: Disables the negotiation service.
- **addService**: This function registers the service on a specified URL and with a specified serialization protocol.
- **removeService**: Removes a previously registered service.
- **run**: Starts the server.

---
#### org.fiware.kiara.server.Service

This interface represent a service that can be registered with the server.

**Functions:**

- **register:** Register a `Servant` object or `DynamicHandler` with the service.
- **loadServiceIDLFromString:** Load the service IDL from a string. This function is only required when the service is handled via dynamic handlers.

---
#### org.fiware.kiara.server.Servant

This interface provides an abstraction used by the server to execute the provided functions when a client request is received.

**Functions:**

- **getServiceName**: Returns the name of the service implemented by this servant.
- **process**: This function processes the incoming request message and returns the produced response message. It is automatically generated.

### Dependent API

This subsection contains the interfaces and classes that are dependent from the user Advanced Middleware IDL definition. In the static version of the Advanced Middleware implementation these interfaces and classes should be generated by the compile time preprocessor.  
This section uses the example in section [*API Usage Examples*](#api-usage-examples).

---
####x.y.\<IDL-ServiceName\>

This interface is a mapping of the Advanced Middleware IDL service. It exposes the service’s procedures. All classes that implement these service’s procedures, have to inherit from this interface. For example the imlementation of the servant have to inherit from this interface, allowing the user to implement the service’s procedures.

**Functions:**

- **add**: This function is the mapping of the Advanced Middleware IDL service procedure `add()`.

---
#### x.y.\<IDL-ServiceName\>Async

This interface is a mapping of the Advanced Middleware IDL service. It exposes the asynchronous version of the service’s procedures. All classes that that implement these service’s asynchronous procedures have to inherit from this interface.

**Functions:**

- **add**: This function is the asynchronous version of the Advanced Middleware IDL service’s procedure `add()`. It has no return value.

---
#### x.y.\<IDL-ServiceName\>Process

This class is a mapping of the Advanced Middleware IDL service. It provides the asynchronous version of the service’s processing procedures.

**Functions:**

- **add_processAsync**: This function is the asynchronous version of the Advanced Middleware IDL service’s process procedure. It has no return value.

---
#### x.y.\<IDL-ServiceName\>Client

This interface provides the synchronous and asynchronous version of the Advanced Middleware IDL service, because it implements the previously described interfaces x.y.\<IDL-ServiceName\> and x.y.\<IDL-ServiceInterface\>Async. The Advanced Middleware IDL service proxy will implement this interface, allowing the user to call the service’s remote procedures synchronously or asynchronously. It is only used on the client side in order to make the Proxy to implement all the functions for this service (both synchronous and asynchronous).

**Functions:**

- **add**: Function inherited from *x.y.\<IDL-ServiceName\>* interface. This function is the mapping of the Advanced Middleware IDL service.
- **add**: Function inherited from *x.y.\<IDL-ServiceName\>Async* interface. This function is the asynchronous version of the Advanced Middleware IDL service’s procedure.

---
#### x.y.\<IDL-ServiceName\>Proxy

This class encapsulates the implementation of the interface *x.y.\<IDL-ServiceName\>Client*. It provides the logic to call the Advanced Middleware IDL service’s remote procedures, synchronously or asynchronously.

**Functions:**

-   **add**: Function inherited from *x.y.\<IDL-ServiceName\>Client* interface. This function is the mapping of the Advanced Middleware IDL service.
-   **add**: Function inherited from *x.y.\<IDL-ServiceName\>Client* interface. This function is the asynchronous version of the Advanced Middleware IDL service’s procedure.

---
#### x.y.\<IDL-ServiceName\>Servant

This abstract class can be used by users to implement the Advanced Middleware IDL service’s procedures. This class implements the interface *org.fiware.kiara.server.Servant*, providing the mechanism the server will use to call the user’s procedure implementations. Also it inherits from the interface *x.y.\<IDL-ServiceName\>* leaving the implementation of this functions to the user.


Detailed API
------------

This section defines in detail the API provided by the classes defined above.

### Main entry point

| **org.fiware.kiara.Kiara**                                               ||||
|-----------------------------------------------------------------------|-|-|-|
| **Attributes**                                                           ||||
| _Name_                   | _Type_       |                        |          |
| n/a                      | n/a          |                        |          |
| **Public Operations**                                                    ||||
| _Name_                   | _Parameters_ | _Returns/Type_         | _Raises_ |
| getTypeDescriptorBuilder |              | TypeDescriptorBuilder  |          |
| getDynamicValueBuilder   |              | DynamicValueBuilder    |          |
| createContext            |              | Context                |          |
| shutdown                 |              | void                   |          |


### Common interfaces

| **org.fiware.kiara.Context**                                             ||||
|-----------------------------------------------------------------------|-|-|-|
| **Attributes**                                                           ||||
| _Name_                | _Type_         |                      |             |
| n/a                   | n/a            |                      |             |
| **Public Operations**                                                    ||||
| _Name_                | _Parameters_   | _Returns/Type_       | _Raises_    |
| connect               |                | Connection           | IOException |
|                       | url            | String               |             |
| connect               |                | Connection           | IOException |
|                       | transport      | Transport            |             |
|                       | serializer     | Serializer           |             |
| createService         |                | Service              |             |
| createServer          |                | Server               |             |
| createTransport       |                | Transport            | IOException |
|                       | String         | url                  |             |
| createServerTransport |                | ServerTransport      | IOException |
|                       | url            | String               |             |
| createSerializer      |                | Serializer           | IOException |
|                       | name           | String               |             |


### Network transports

| **org.fiware.kiara.transport.Transport**                                 ||||
|-----------------------------------------------------------------------|-|-|-|
| **Attributes**                                                           ||||
| _Name_                  | _Type_        |                        |          |
| n/a                     | n/a           |                        |          |
| **Public Operations**                                                    ||||
| _Name_                  | _Parameters_  | _Returns/Type_         | _Raises_ |
| getTransportFactory     |               | TransportFactory       |          |


### Dependent API

Cause the described classes in this section are dependant of the Advanced Middleware IDL service, this section will use the example in section [*API Examples*](#api-examples) to define them.
 
| **x.y.\<IDL-ServiceName\>**                                              ||||
|-----------------------------------------------------------------------|-|-|-|
| **Attributes**                                                           ||||
| _Name_            | _Type_        |                              |          |
| n/a               | n/a           |                              |          |
| **Public Operations**                                                    ||||
| _Name_            | _Parameters_  | _Returns/Type_               | _Raises_ |
| add               |               | int                          |          |
|                   | num1          | int                          |          |
|                   | num2          | int                          |          |


| **x.y.\<IDL-ServiceName\>Async**                                         ||||
|-----------------------------------------------------------------------|-|-|-|
| **Attributes**                                                           ||||
| _Name_            | _Type_        |                              |          |
| n/a               | n/a           |                              |          |
| **Public Operations**                                                    ||||
| _Name_            | _Parameters_  | _Returns/Type_               | _Raises_ |
| add               |               | void                         |          |
|                   | num1          | int                          |          |
|                   | num2          | int                          |          |
|                   | callback      | AsyncCallback\<Integer\>     |          |


| **x.y.\<IDL-ServiceName\>Client**                                        ||||
|-----------------------------------------------------------------------|-|-|-|
| **Attributes**                                                           ||||
| _Name_            | _Type_        |                              |          |
| n/a               | n/a           |                              |          |
| **Public Operations**                                                    ||||
| _Name_            | _Parameters_  | _Returns/Type_               | _Raises_ |
| Inherited from x.y.\<IDL-ServiceName\> and x.y.\<IDL-ServiceName\>Async  ||||


| **x.y.\<IDL-ServiceName\>Proxy**                                         ||||
|-----------------------------------------------------------------------|-|-|-|
| **Attributes**                                                           ||||
| _Name_            | _Type_                                    |  |          |
| m_ser             | org.fiware.kiara.serialization.Serializer |  |          |
| m_transport       | org.fiware.kiara.transport.Transport      |  |          |
| **Public Operations**                                                    ||||
| _Name_            | _Parameters_  | _Returns/Type_               | _Raises_ |
| Inherited from x.y.\<IDL-ServiceName\> and x.y.\<IDL-ServiceName\>Async  ||||

 
| **x.y.\<IDL-ServiceName\>Servant**                                       ||||
|-----------------------------------------------------------------------|-|-|-|
| **Attributes**                                                           ||||
| _Name_            | _Type_        |                              |          |
| n/a               | n/a           |                              |          |
| **Public Operations**                                                    ||||
| _Name_            | _Parameters_  | _Returns/Type_               | _Raises_ |
| getServiceName    |               | String                       |          |
| process           |               | TransportMessage             |          |
|                   | ser           | Serializer                   |          |
|                   | message       | TransportMessage             |          |
|                   | transport     | Transport                    |          |
|                   | messageId     | Object                       |          |
|                   | bis           | BinaryInputStream            |          |


| **x.y.\<IDL-ServiceName\>Process**                                       ||||
|-----------------------------------------------------------------------|-|-|-|
| **Attributes**                                                           ||||
| _Name_            | _Type_        |                              |          |
| n/a               | n/a           |                              |          |
| **Public Operations**                                                    ||||
| _Name_            | _Parameters_  | _Returns/Type_               | _Raises_ |
| add_processsAsync |               | void                         |          |
|                   | message       | TransportMessage             |          |
|                   | ser           | Serializer                   |          |
|                   | callback      | AsyncCallback<Integer>       |          |



API Usage Examples
------------------

Examples used in this section are based on the following [*Advanced Middleware IDL*][AMiIDLSpec]:
```
service Calculator
{
    i32 add(i32 num1, i32 num2);
};
```


### Client API

#### Direct connection to remote service

This example shows how to create a direct connection to a server using the TCP transport and the CDR serialization. After it creates the connection, the service proxy is instantiated and used to call a remote procedure.

```java
Context context = Kiara.createContext();
Connection connection = context.connect("tcp://192.168.1.18:8080?serialization=cdr");
CalculatorClient client = connection.getServiceProxy(CalculatorClient.class);

int result = client.add(3,4);
```

`Transport` and `Serialization` instances are implizitly created by the connection, based on the string parameter of the `connect` method. 

### Explicitly instanciate and configure Advanced Middleware components

This examples shows how to create a direct connection as above, but using a TCP transport and CDR serialization created and configured explicitly by the user.

```java
Context context = Kiara.createContext();
// User instantiates a transport object which can be configured later.
Transport transport = context.createTransport("tcp://192.168.1.18:8080");
// User instantiates a serialization object which can be configured later.
Serializer serializer = context.createSerializer("cdr");
Connection connection = context.connect(transport, serializer);
CalculatorClient client = connection.getServiceProxy(CalculatorClient.class);

int result = client.add(3,4);
```


### Server API

### Providing a service

This examples shows how to create a server and add a service to it.
```java
Context context = Kiara.createContext();
Server server = context.createServer();
Service service = context.createService();

// User creates and registers it's implementation of the servant.
Calculator calculator_impl = new CalculatorServantImpl();
service.register(calculator_impl);

// Add the service to the server
server.addService(service, "tcp://0.0.0.0:8080", "cdr");

server.run();
```

`Transport` and `Serialization` instances are implizitly created by the connection, based on the string parameters of the `addService` method. 

### Explicitly instanciate and configure Advanced Middleware components

This examples shows how to provide a service as above, but using a TCP transport and CDR serialization created and configured explicitly by the user.

```java
Context context = Kiara.createContext();
Server server = context.createServer();
Service service = context.createService();

// User creates and registers it's implementation of the servant.
Calculator calculator_impl = new CalculatorServantImpl();
service.register(calculator_impl);

// Transport and Serializer are expicitly created ...
Transport transport = context.createTransport("tcp://0.0.0.0:8080");
Serializer serializer = context.createSerializer("cdr");

// ... and bound to the service when adding it to the server
server.addService(service, transport, serializer);

server.run();
```



