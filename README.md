# KIARA Advanced Middleware

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

## Documentation

The following Manuals are available in the docs section:

* [KIARA Installation and Admin Guide](doc/Installation_and_Admin_Guide.md)
* [KIARA User and Developer Guide](doc/User_and_Developer_Guide.md)

### Architecture and Specification
* [Architecture](doc/specification/Architecture.md)
* [IDL Specification](doc/specification/Middleware_IDL_Specification.md)
* [RPC API Specification](doc/specification/Middleware_RPC_API_Specification.md)
* [RPC Dynamic Types API Specification](doc/specification/Middleware_RPC_Dynamic_Types_API_Specification.md)
