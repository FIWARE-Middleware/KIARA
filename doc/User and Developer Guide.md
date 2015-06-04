KIARA User and Developer Guide
==============================

Version 0.2.0

<!--TOC max2-->

## Introduction

KIARA Advanced Middleware is a Java based communication middleware for modern,
efficient and secure applications.
It is an implementation of the FIWARE Advanced Middleware Generic Enabler.

This first release focuses on the basic features of RPC communication:

* Modern Interface Definition Language (IDL) with a syntax based on the Corba IDL.
* Easy to use and extensible Application Programmer Interface (API).
* IDL derived operation mode providing Stubs and Skeletons for RPC
  Client/Server implementations.
* Synchronous and Asynchronous function calls.

Later versions will include additional features like:

* Application derived and Mapped operation mode providing dynamic declaration 
of functions and data type mapping.
* Advanced security features like field encryption and authentication.
* Additional communication patterns like publish/subscribe.

KIARA Advanced Middleware is essentially a library which is incorporated into
the developed applications, the requirements are rather minimal. 
In particular it requires no service running in the background.

### Middleware Operation Modes
The KIARA Advanced Middleware supports multiple operation modes. From traditional IDL-based approaches like Corba, DDS, Thrift up to newer approaches which start with the application data structure and automatically create the wire format.

We therefore differentiate three operation modes.

#### IDL derived operation mode
The IDL derived operation mode is similar to the traditional middleware approaches.

Based on the IDL definition we generate with a precompiler stub- and skeleton-classes, which have to be used by the application to implement the server and client (or Publisher/Subscriber) application parts.

**Prerequisite:** IDL definition  
**Generated:** Stubs and Skeletons (at compile time) which have to be used by the application  
**Examples:** Corba, DDS, Thrift, …  

#### Application derived operation mode
This mode is typical for some modern (e.g. RMI, WebService,...) frameworks. 
Based on an application specific interface definitions, the framework automatically generates Server- and Client-Proxy-Classes, which serialize the application internal data structures and send them over the wire. Using Annotations, the required serialization and transport mechanisms and type mappings can be influenced. 

This mode implicitly generates an IDL definition based on the Java interfaces definition and provide this IDL through a “service registry” for remote partners.

**Prerequisite:** Application-Interface-Definition (has to be the same on client and server side)  
**Generated:**    Server-/Client-Proxies (generated at runtime)  
**Examples:**     RMI, JAX-RS, Spring REST, …  

#### Mapped operation mode
Goal of the mapped operation mode is to separate the application interfaces from the data structure used to transport the data over the wire. Therefore the middleware has to map the application internal data structure and interfaces to a common IDL definition. Advantage is, that the application interface on client and server (or publisher/subscriber) side can be different.

**Prerequisite:** Application-Interface-Definition (can be different on server and client side) IDL Definition  
**Generated:**    Server-/Client-Proxis (generated at runtime, which map the attributes & operations  
**Examples:**    KIARA


> The first release of KIARA will provide support for the traditional IDL derived operation mode. Application derived and mapped operation mode will follow in a future release. 


## A quick example

In the next chapters we will use the following example application to explain the basic concepts of building an application using KIARA.

### Calculator

The KIARA Calculator example application provides an API to ask for simple 
mathematics operations over two numbers. It is a common used example when trying to understand how an RPC framework works.

Basically the service provides two functions:

* `float add (float n1, float n2)` : 
  Returns the result of adding the two numbers introduced as parameters (n1 and n2).

* `float subtract (float n1, float n2)` : 
  Returns the result of subtracting the two numbers introduced as parameters (n1 and n2).

The KIARA Calculator example is provided within this distribution, so it can be used as starting point.

### Basic procedure

Before diving into the details describing the features and configure your projectfor KIARA, the following quick example should show the basic steps to create a simple client and server application in the different operation modes.

Detailed instructions on how to execute the particular steps are given in
chapter [Building a KIARA RPC application](#building-a-kiara-rpc-application).

#### IDL derived application process

In the IDL derived approach, first the IDL definition has to be created:
```idl
service Calculator
{
    float32 add (float32 n1, float32 n2);
    float32 subtract (float32 n1, float32 n2);
};
```

The developer has to implement the functions inside the class `CalculatorServantImpl`:
```java
public static class CalculatorServantImpl extends CalculatorServant
{
    @Override
    public float add (/*in*/ float n1, /*in*/ float n2) {
	    return (float) n1 + n2;
    }
    
    @Override
    public float subtract (/*in*/ float n1, /*in*/ float n2) {
	    return (float) n1 - n2;
    }
    ...
}
```

Now the server can be started:
```java 
Context context = Kiara.createContext();
Server server = context.createServer();
Service service = context.createService();

// Create and register an instance of the CalculatorServant implementation.
CalculatorServant Calculator_impl = new CalculatorServantImpl();
service.register(Calculator_impl);

// register the service on port 9090 using CDR serialization 
server.addService(service, "tcp://0.0.0.0:9090", "cdr");

// run the server
server.run();
```

The client can connect and call the remote functions via the proxy class:

```java 
Context context = Kiara.createContext();

// setup the connection to the server
Connection connection = context.connect("tcp://192.168.1.18:9090?serialization=cdr");

// get the client Proxy implementation
CalculatorClient client = connection.getServiceProxy(CalculatorClient.class);

// call the remote methods
float result = client.add(3, 5);
```

#### Application derived application example

> This example will be added, when the feature is implemented.

#### Mapping application example
> This example will be added, when the feature is implemented.


## Using KIARA Advanced Middleware to create an RPC application

KIARA Advanced Middleware allows the developer to easily implement a distributed application using remote procedure invocations. In client/server paradigm, a server offers a set of remote procedures that the client can remotely call. How the client calls these procedures should be transparent.

For the developer, a proxy object represents the remote server, and this object
offers the remote procedures implemented by the server. In the same way, how the server obtains a request from the network and how it sends the reply should also be transparent. The developer just writes the behaviour of the remote procedures.

KIARA Advanced Middleware offers this transparency and facilitates the development.

### IDL derived operation mode
The general steps to build an application in IDL derived operation mode are:

1. Define a set of remote procedures: using the KIARA Interface Definition Language.
2. Generation of specific remote procedure call support code: a Client-Proxy and a Server-Skeleton.
3. Implement the servant: with the needed behaviour.
4. Implement the server: filling the server skeleton with the behaviour of the procedures.
5. Implement the client: using the client proxy to invoke the remote procedures.

This section describes the basic concepts of these four steps that a developer
has to follow to implement a distributed application. The advanced concepts are
described in section Advanced Concepts.


## Building a KIARA RPC application
### Defining a set of remote procedures using the KIARA IDL

The KIARA Interface Definition Language (IDL) is used to define the remote
procedures (operations) the server will offer. Simple and Complex Data Types used as parameter types in these remote procedures are also defined in the IDL file. 
In addition the KIARA IDL supports the declaration and application of Annotations to add metadata to almost any IDL element. These can be used by the code generator, when implementing the service functionality or configure some specific runtime functionality. The IDL syntax is based on the OMG IDL 3.5. The basic structure of an IDL File is shown in the following picture.

<span style="width: 400px; float: right;">![GitHub Workflow](./images/IDLFileStructure.png "KIARA IDL File Structure")<span>

Following a short overview of the supported KIARA IDL elements. For a detailed description please see the chapter [KIARA Interface Definition Language](#kiara-interface-definition-language).

* **Import Declarations**: 
  Definitions can be split into multiple files and/or share common elements 
  among multiple definitions using the import statement.
* **Namespace Declarations**: 
  Within a definition file the declarations can be grouped into modules. Modules are used to define scopes for IDL identifiers. KIARA supports the 
  modern keyword namespace. Namespaces can be nested to support multi-level 
  namespaces.
* **Constant Declarations**: 
  A constant declarations allows the definition of literals, which can be used 
  as values in other definitions (e.g. as return values, default parameters, 
  etc.)
* **Type Declarations**
    * **Basic Types**: 
      KIARA IDL supports the OMG IDL basic data types like float, double, 
      (unsigned) short/int/long, char, wchar, boolean, octet, etc. 
      Additionally it supports modern aliases like float32, float64, i16, ui16, i32, ui32, i64, ui64 and byte
    * **Constructed Types**: 
      Constructed Types are combinations of other types like. 
      The following constructs are supported:
      * **Structures** (struct)
    * **Template Types**: 
      Template types are frequently used data structures like the various forms of collections. The following Template Types are supported:
      * **List**: 
      Ordered collection of elements of the same type “list” is the modern 
      variant of the OMG IDL keyword “sequence”
      * **Strings**: 
      collection of chars, will be mapped to the String representation of the
      language.
    * **Complex Declarations**: 
      In addition to the above Type declarations, KIARA supports ultidimensional Arrays using the bracket notation (e.g. `int monthlyRevenue[12][10]`) 
* **Service Declarations**: 
  KIARA supports interface and service declarations via IDL. Meaning that the 
  user can declare different services where the operations are going to be 
  placed.
* **Operation Declarations**: 
  Operations can be declared within the services following the standard OMG IDL notation.

The IDL file (`calculator.idl`) for our example application shows the usage of some of the above elements.
```idl
  service Calculator
  {
      float32 add (float32 n1, float32 n2);
      float32 substract (float32 n1, float32 n2);
  };
```

### Generating remote procedure call support code
KIARA Advanced Middleware includes a Java application named `kiaragen`.
This application parses the IDL file and generates Java code for the defined set of remote procedures. 

All support classes will be generated (e.g. for structs):

* `x.y.<StructName>`: Support classes containing the definition of the data types as well as the serialization code.

Using the `-example` option (described below), kiaragen will generate the following files for each of your module/service definitions:

* `x.y.<IDL-ServiceName>`: 
  Interface exposing the defined synchronous service operation calls.
* `x.y.<IDL-ServiceName>Async`: 
  Interface exposing the asynchronous operation calls.
* `x.y.<IDL-ServiceName>Client`: 
  Interface exposing all client side calls (sync & async).
* `x.y.<IDL-ServiceName>Proxy`: 
  This class encapsulates all the logic needed to call the remote operations. (Client side proxy → stub).
* `x.y.<IDL-ServiceName>Servant`: 
  This abstract class provides all the mechanisms (transport, un/marshalling, etc.) the server requires to call the server functions. 
* `x.y.<IDL-ServiceName>ServantExample`: 
  This class will be extended to implement the server side functions (see [Servant Implementation](#servant-implementation).
* `x.y.ClientExample`: 
  This class contains the code needed to run a possible example of the client side application.
* `x.y.ServerExample`: 
  This class contains the code needed to run a possible example of the server side application.

The package name `x.y.` can be declared when generating the support code using `kiaragen` (see `-package` option below).

#### Generate support code manually using kiaragen 

To call `kiaragen` manually it has to be installed and in your run path.
Instructions to install the kiaragen tool can be found in the [KIARA Installation and Administration Guide](Installation and Admin Guide.md)

The usage syntax is:
```
$ kiaragen [options] <IDL file> [<IDL file> …]
```

Options:

|        Option         |                   Description
|-----------------------|-------------------------------------------------
| `-help`               | Shows help information 
| `-version`            | Shows the current version of KIARA / kiaragen
| `-package`            | Defines the package prefix of the generated Java classes. Default: no package
| `-d <path>`           | Specify the output directory for the generated files. Default: current working dir
| `-replace`            | Replaces existing generated files.
| `-example <platform>` | Generates the support files (interfaces, stubs,  skeletons,...) for the given target platform. These classes will be used to by the developer to implement both client and server part or the application. Supported values:  gradle (creates also build.gradle files)
| `--ppDisable`         | Disables the preprocessor.
| `--ppPath <path>`     | Specifies the path of the preprocessor. Default: Systems C++ preprocessor
| `-t <path>`           | Specify the output temploral directory for the files generated by the preprocessor. Default: machine temp path


For our example the call could be:
```
$ kiaragen -example gradle -package com.example src/main/idl/calculator.idl
Loading templates...
org.fiware.kiara.generator.kiaragen
org.fiware.kiara.generator.idl.grammar.Context
Processing the file calculator.idl...
Creating destination source directory... OK
Generating Type support classes...
Generating application main entry files for interface Calculator... OK
Generating specific server side files for interface Calculator... OK
Generating specific client side files for interface Calculator... OK
Generating common server side files... OK
Generating common client side files... OK
```

This would generate the following files:
```tree
.
└── src                                      // source files
    ├── main
    │   ├── idl                              // IDL definitions for kiaragen
    │   │   └── calculator.idl               
    │   └── java                             // Generated support files
    │       └── com.example                      
    │            │                           // Generated using --example 
    │            ├── Calculator.java         // Interface of service
    │            ├── CalculatorAsync.java    // Interface of async calls
    │            ├── CalculatorClient.java   // Interface client side 
    │            ├── CalculatorProxy.java    // Client side implementation
    │            ├── CalculatorServant.java  // Abstract server side skeleton
    │            ├── CalculatorServantExample.java // Dummmy servant impl. 
    │            ├── ClientExample.java      // Example client code 
    │            └── ServerExample.java      // Example server code
    ├── build_client.gradle                  // generated support files 
    ├── build_server.gradle                  // generated support files 
```


### Servant implementation
Please note that the code inside the file `x.y.<IDL-ServiceName>ServantExample.java` (which in this case is `CalculatorServantExample.java`) has to be modified in order to specify the behaviour of each declared function.
```java
class CalculatorServantExample extends CalculatorServant {
	
  public float add (/*in*/ float n1, /*in*/ float n2) {
		return (float) n2 + n2;
	}

	public float substract (/*in*/ float n1, /*in*/ float n2) {
		return (float) n1 - n2;
	}

}
```

### Implementing the server

The source code generated using kiaragen tool (by using the `-example` option) contains a simple implementation of a server. This implementation can obviously be extended as far as the user wants, this is just a very simple server capable of executing remote procedures.

The class containing the mentioned code is named ServerExample, and its code is shown below:
```java
public class ServerExample {
	
	public static void main (String [] args) throws Exception {
		
		System.out.println("CalculatorServerExample");
		
		Context context = Kiara.createContext();
		Server server = context.createServer();
		
		CalculatorServant Calculator_impl = new CalculatorServantExample();
		
		Service service = context.createService();
		
		service.register(Calculator_impl);
		
		//Add service waiting on TCP with CDR serialization
		server.addService(service, "tcp://0.0.0.0:9090", "cdr");
		
		server.run();
	
	}
	
}
```

### Implementing the client

The source code generated using kiaragen tool (by using the `-example` option) contains a simple implementation of a client. This implementation must be extended in order to show the output received from the server.

In the KIARA Calculator example, as we have defined first the add function in the IDL file, this will be the one used by default in the generated code. The code for doing this is shown in the following snippet:
```java
public class ClientExample {
	public static void main (String [] args) throws Exception {
		System.out.println("CalculatorClientExample");
		
    float n1 = (float) 3.0;
    float n2 = (float) 5.0;

		float ret = (float) 0.0;
		
		Context context = Kiara.createContext();
		
		Connection connection = 
                     context.connect("tcp://127.0.0.1:9090?serialization=cdr");
		Calculator client = connection.getServiceProxy(CalculatorClient.class);
		
    try {
			ret = client.add(n1, n2);				
			System.out.println("Result: " + ret);		
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return;
		}
	}
}
```

The previous code has been shown exactly the way it is generated, with only two differences:

* Parameter initialization: Both of the parameters n1 and n2 have been initialized to random values (in this case 3 and 5).
* Result printing: To have feedback of the response sent by the server when the remote procedure is executed.


### Compiling the client and the server
For the client and server examples to compile, some jar files are needed. These files are located under the lib directory provided with this distribution, and they must be placed in the root working directory, under the lib folder:
```tree
.
├── src                                      // source files
├── lib                                      // generated support files 
├── build_client.gradle                      // generated support files 
└── build_server.gradle 
```

To compile the client using gradle, the call would be the next (changing the file build_client.gradle to build_server.gradle will compile the server):
```
$ gradle -b build_client.gradle build
:compileJava
:processResources UP-TO-DATE
:classes
:jar
:assemble
:compileTestJava UP-TO-DATE
:processTestResources UP-TO-DATE
:testClasses UP-TO-DATE
:test UP-TO-DATE
:check UP-TO-DATE
:build

BUILD SUCCESSFUL

Total time: 3.426 secs
```

After compiling both of them the following files will be generated:
```tree
.
├── src                                // source files
├── build                                    // generated by gradle 
│   ├── classes                              // Compiled .class files
│   ├── dependency-cache                     // Inner gradle files
│   ├── libs                                 // Executable jar files
│   └── tmp                                  // Temporal files used by gradle
├── lib                                
├── build_client.gradle                // generated support files 
└── build_server.gradle                // Generated support files 
```


In order to execute the examples, copy them to the same locations where the libraries mentioned before are placed (lib directory), and execute them using the command `java -jar file_to_execute.jar`
All the files needed to compile and execute this project are located under the examples/Calculator directory included with this distribution.


## Building a KIARA RPC application using the dynamic API
The "KIARA RPC Dynamic API" allows the developers to easily execute calls in an RPC framework without having to statically generate code to support them. In the following sections, the different concepts of this feature will be explained.

Using the dynamic API we still need the IDL file, which declares the "contract" between server and client by defining the data types and services (operations) the server offers.

For the dynamic API the IDL format is identical to the one used for the static/compile time version. For example the IDL file for our demo application (`calculator.idl`) is identical to the static use-case:
```idl
service Calculator
{
    float32 add (float32 n1, float32 n2);
    float32 substract (float32 n1, float32 n2);
};
```

### Declaring the remote calls and data types at runtime

In the dynamic approach, the comple time `kiaragen` code-generator will not be required anymore. Instead, the middleware provides a function to load the IDL definition from a String object. The generation of the IDL String has to be done by the developer. For example it can be loaded from a File, from a URL or generated by an algorithm.

The process to declare the dynamic part is as follows:

* The server loads the IDL String (e.g. from a file).
* The IDL definition will then be provided to the clients connecting with the server.
* On the server the developer has to provide objects to act as servants and execute code depending on the function the client has requested.

#### Loading the IDL definition
On the server side, in order to provide the user with a definition of the functions that the server offers, the first thing to be done is to load the IDL definition into the application.

Therefore, the `Service` class provides a public function that can be used to load the IDL information from a String object. It is the developers responsibility to load the String from the source (e.g. from a file).
The following snippet shows an example on how to do this:

```java
// Load IDL content string from file
String idlString = new String(Files.readAllBytes(Paths.get("calculator.idl")));
/* This is just one way to do it. Developer decides how to do it */

// Load service information dynamically from IDL
Service service = context.createService();
service.loadServiceIDLFromString(idlString);
```

#### Implementing the service functionality
Unlike in the static approach, in the dynamic version exists no Servant class to code the behaviour of the functions. 
To deal with this, KIARA provides a functional interface `DynamicFunctionHandler` that acts as a servant implementation. This class must be used to implement the function and register it with the service, which means to map the business logic of each function with its registered name.
```java
// Create type descriptor and dynamic builder
final TypeDescriptorBuilder tdbuilder = Kiara.getTypeDescriptorBuilder();
final DynamicValueBuilder dvbuilder = Kiara.getDynamicValueBuilder();
// Create type descriptor int (used for the return value)
final PrimitiveTypeDescriptor intType = 
                        tdbuilder.createPrimitiveType(TypeKind.INT_32_TYPE);  

// Implement the functional interface for the add function
DynamicFunctionHandler addHandler = new DynamicFunctionHandler() {
     @Override
     public void process(
          DynamicFunctionRequest request, 
          DynamicFunctionResponse response 
     ) {
          // read the parameters
          int a = (Integer)((DynamicPrimitive)request.getParameterAt(0)).get();
          int b = (Integer)((DynamicPrimitive)request.getParameterAt(1)).get();
          // create the return value
          final DynamicPrimitive intValue = 
                              (DynamicPrimitive)dvbuilder.createData(intType);
          intValue.set(a+b);    // implmement the function
          response.setReturnValue(intValue);
     }
}

// Register function and map handler (do this for every function)
service.register("Calculator.add", addHandler);
```

### Implementing the server
Because the server functionality is not encapsuled in generated Servant classes, the server implmentation is a bit more extensive. It still follows the same pattern as in the static API, but the implementation and registration of the dynamic functions has to be done completely by the developer.

The following ServerExample class shows, how this would look like:
```java
public class ServerExample {
	public static void main (String [] args) throws Exception {
		System.out.println("CalculatorServerExample");
		
		Context context = Kiara.createContext();
		Server server = context.createServer();

		// Enable negotiation with clients
		server.enableNegotiationService("0.0.0.0", 8080, "/service");

		Service service = context.createService();
		String idlContent = 
        new String(Files.readAllBytes(Paths.get("calculator.idl")))
		service.loadServiceIDLFromString(idlContent);

		// Create descriptor and dynamic builder
		final TypeDescriptorBuilder tdbuilder = Kiara.getTypeDescriptorBuilder();
		final DynamicValueBuilder dvbuilder = Kiara.getDynamicValueBuilder();
		
		// Declare handlers
		DynamicFunctionHandler addHandler;
		DynamicFunctionHandler substractHandler;
		addHandler = /* Implement handler for the add function */;
		substractHandler = /* Implement handler for the substract function */;
	       
		// Register services
		service.register(“Calculator.add”, addHandler);
		service.register(“Calculator.substract”, substractHandler);

		//Add service waiting on TCP with CDR serialization
		server.addService(service, "tcp://0.0.0.0:9090", "cdr");
		
		server.run();
	}
}
```

### Implementing the client
On the client side the key point is the negotiation with the server to download the IDL it provides. After downloading, it will automatically parse the content and generate the necessary information to create the dynamic objects.
When the `DynamicProxy` is created the functions provided by the server can be executed by using `DynamicFunctionRequest` objects. The parameters of the functions have to be set in the request using `DynamicData` objects. The call of the request function `execute()` will finally perform the call to the server and return the result in a `DynamicFunctionResponse` object.
The following code shows the client implementation:
```java
public class ClientExample {
	public static void main (String [] args) throws Exception {
		System.out.println("CalculatorClientExample");
		
		Context context = Kiara.createContext();

		// Create connection indicating the negotiation service
		Connection connection = 
                     context.connect("kiara://127.0.0.1:9090/service");

		// Create client by using the proxy’s name
		DynamicProxy client = connection.getDynamicProxy(“Calculator”);

		// Create request object
		DynamicFunctionRequest request = client.createFunctionRequest(“add”);
		((DynamicPrimitive) request.getParameterAt(0)).set(8);
		((DynamicPrimitive) request.getParameterAt(1)).set(5);

		// Create response object and execute RPC
		DynamicFunctionResponse response = request.execute();
		if (response.isException()) {
			DynamicData result = response.getReturnValue();
			System.out.println(“Exception = “ + (DynamicException) result);
		} else {
			DynamicData result = response.getReturnValue();
			System.out.println(“Result = “ + (DynamicPrimitive) result);
		}
    // shutdown the client
		Kiara.shutdown();
	}
}
```
