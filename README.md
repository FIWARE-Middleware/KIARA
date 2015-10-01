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

* [KIARA Installation and Admin Guide](http://fiware-middleware-kiara.readthedocs.org/en/latest/manuals/Installation_and_Admin_Guide.html)
* [KIARA User and Developer Guide](http://fiware-middleware-kiara.readthedocs.org/en/latest/manuals/User_and_Programmer_Guide.html)

### Architecture and Specification
* [RPC API Specification](http://fiware-middleware-kiara.readthedocs.org/en/latest/specification/Middleware_RPC_API_Specification.html)
* [RPC Dynamic Types API Specification](http://fiware-middleware-kiara.readthedocs.org/en/latest/specification/Middleware_RPC_Dynamic_Types_API_Specification.html)
* [IDL Specification](http://fiware-middleware-kiara.readthedocs.org/en/latest/specification/Middleware_IDL_Specification.html)
