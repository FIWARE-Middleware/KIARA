Advanced Middleware IDL Specification
=====================================

*Date: 8th April 2015*

| This version: :doc:`0.2.0 <Middleware_IDL_Specification>`
| Previous version: n/a
| Latest version: :doc:`latest <Middleware_IDL_Specification>`
|

**Editors:**

-  `eProsima - The Middleware
   Experts <http://eprosima.com/index.php/en/>`__
-  `DFKI - German Research Center for Artificial
   Intelligence <http://www.dfki.de/>`__
-  `ZHAW - School of Engineering
   (ICCLab) <http://blog.zhaw.ch/icclab>`__

Copyright © 2013-2015 by eProsima, DFKI, ZHAW. All Rights Reserved

--------------

Abstract
--------

Ahe Advanced Middleware GE enables flexible, efficient, scalable, and
secure communication between distributed applications and to/between
FIWARE GEs. The **Interface Definition Language (IDL)** Specifications
allowes to describes the Data Types and Operations supported by a
Service. Following is a list of the main features it supports:

-  **IDL, Dynamic Types & Application Types**: It support the usual
   schema of IDL compilation to generate support code for the data
   types.
-  **IDL Grammar**: An OMG-like grammar for the IDL as in DDS, Thrift,
   ZeroC ICE, CORBA, etc.
-  **Types**: Support of simple set of basic types, structs, and various
   high level types such as lists, sets, and dictionaries (maps).
-  **Type inheritance, Extensible Types, Versioning**: Advanced data
   types, extensions, and inheritance, and other advanced features will
   be supported.
-  **Annotation Language**: The IDL is extended with an annotation
   language to add properties to the data types and operations. These
   will, for example, allows adding security policies and QoS
   requirements.

Status of this Document
-----------------------

+----------------+-----------------------+
| **Date**       | **Description**       |
+================+=======================+
| 8-April-2015   | 0.2.0 First Release   |
+----------------+-----------------------+

--------------

Preface
-------

The foundation of the FIWARE Middleware Interface Definition Language
(IDL) is the Object Management Group (OMG) IDL 3.5. See `Appendix
C <#appendix-c-omg-idl-3.5-grammar>`__ for the OMG IDL 3.5 grammar.

To maintain backward compatibility, the FIWARE Middleware IDL grammar
embraces all OMG IDL 3.5 features. IDL parsers are not required to
implement all of the extended OMG features. Check the documentation of
the specific parser implementations.

The basic subset needed by DDS and future standard RPC over DDS must be
supported.

Related documentation
---------------------

-  `OMG DDS-XTypes 1.0 <http://www.omg.org/spec/DDS-XTypes/1.0/>`__

Syntax Definition
-----------------

The FIWARE Middleware IDL specification consists of one or more type
definitions, constant definitions, exception definitions, or module
definitions. Some definitions are allowed in the grammar for backward
compatibility, but are not used by the FIWARE Middleware IDL and
therefore will be ignored by the implmentations.

::

    <specification> ::= <import>* <definition>+
    <definition> ::= <type_dcl> ";"
                 |   <const_dcl> ";"
                 |   <except_dcl> ";"
                 |   <interface> ";"
                 |   <module> ";"
                 |   <value> ";"
                 |   <type_id_dcl> ";"
                 |   <type_prefix_dcl> ";"
                 |   <event> ";"
                 |   <component> ";"
                 |   <home_dcl> ";"
                 |   <annotation_dcl> ";"
                 |   <annotation_appl> <definition>

| See section `Import Declaration <#import-declaration>`__ for the
  specification of ``<import>``
| See section `Module Declaration <#module-declaration>`__ for the
  specification of ``<module\>``
| See section `Interface Declaration <#interface-declaration>`__ for
  the specification of ``<interface>``
| See section `Value Declaration <#value-declaration>`__ for the
  specification of ``<value\>``
| See section `Constant Declaration <#constant-declaration>`__ for the
  specification of ``<const_dcl>``
| See section `Type Declaration <#type-declaration>`__ for the
  specification of ``<type_dcl>``
| See section `Exception Declaration <#exception-declaration>`__ for
  the specification of ``<except_dcl>``
| See section `Repository Identity Related
  Declarations <#repository-identity-related-declarations>`__ for the
  specification of ``<type_id_dcl>`` and ``<type_prefix_dcl>``
| See section `Event Declaration <#event-declaration>`__ for the
  specification of ``<event>``
| See section `Component Declaration <#component-declaration>`__ for
  the specification of ``<component>``
| See section `Event Declaration <#event-declaration>`__ for the
  specification of ``<home_dcl>``
| See section `Annotation Declaration <#annotation-declaration>`__ for
  the specification of ``<annotation_dcl>``
| See section `Annotation Application <#annotation-application>`__ for
  the specification of ``< annotation_appl>``

Import Declaration
~~~~~~~~~~~~~~~~~~

An import statement conforms to the following syntax:

::

    <import> ::= "import" <imported_scope> ";"
    <imported_scope> ::= <scoped_name> | <string_lieral>

Import declarations are not supported by FIWARE Middleware. Any FIWARE
Middleware IDL parser has to inform users about this and ignore the
declaration.

Module Declaration
~~~~~~~~~~~~~~~~~~

A module definition conforms to the following syntax:

::

    <module> ::= ("module" | "namespace") <identifier> "{" <definition> + "}"

The module construct is used to scope IDL identifiers. FIWARE Middleware
IDL supports the OMG IDL 3.5 keyword ``module``, but also adds the
modern keyword ``namespace`` as an alias.

Examples of module definitions:

::

    namespace MyNamespace {
       ...
    };

    namespace YourNamespace {
       namespace HisNamespace {
          ...
       };
    };

Interface Declaration
~~~~~~~~~~~~~~~~~~~~~

An interface definition conforms to the following syntax:

::

    <interface> ::= <interface_dcl> | <forward_dcl>
    <interface_dcl> ::= <interface_header> "{" <interface_body> "}"
    <forward_dcl> ::= [ "abstract" | "local" ] ("interface" | "service") <identifier>
    <interface_header> ::= [ "abstract" | "local" ]("interface" | "service") <identifier>
                           [ <interface_inheritance_spec> ]
    <interface_body> ::= <export>*
    <export> ::= <type_dcl> ";"
              |   <const_dcl> ";"
              |   <except_dcl> ";"
              |   <attr_dcl> ";"
              |   <op_dcl> ";"
              |   <type_id_dcl> ";"
              |   <type_prefix_dcl> ";"

Example of interface definition:

::

    service MyService {
       ...
    };

Interface Header
^^^^^^^^^^^^^^^^

The interface header consists of three elements:

1. An optional modifier specifying if the interface is an abstract
   interface.
2. The interface name. The name must be preceded by the old OMG IDL 3.5
   keyword ``interface`` or the new modern keyword ``service``.
3. An optional inheritance specification.

An interface declaration containing the keyword ``abstract`` in its
header, declares an abstract interface. Abstract interfaces have
slightly different rules from *regular* interfaces, as described in
section `Abstract interface <#abstract-interface>`__.

An interface declaration containing the keyword ``local`` in its header,
declares a local interface. Local interfaces are not currently supported
by the FIWARE Middleware. Any FIWARE Middleware IDL parser has to inform
users about this, and explain the interface will be used as a *regular*
interface.

Interface Inheritance Specification
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The syntax for interface inheritance is as follows:

::

    <interface_inheritance_spec> ::= ":" <interface_name> { "," <interface_name> }*
    <interface_name> ::= <scoped_name>
    <scoped_name> ::= <identifier>
                |   "::" <identifier>
                |   <scoped_name> "::" <identifier>

Each ``<scoped_name>`` in an ``<interface_inheritance_spec>`` must be
the name of a previously defined interface or an alias to a previously
defined interface.

Interface Body
^^^^^^^^^^^^^^

The interface body contains the following kind of declarations:

-  Constant declarations whose syntax is described in section `Constant
   Declaration <#constant-declaration>`__.
-  Type declarations whose syntax is described in section `Type
   Declaration <#type-declaration>`__.
-  Exception declarations whose syntax is described in section
   `Exception Declaration <#exception-declaration>`__.
-  Attribute declarations whose syntax is described in section
   `Attribute Declaration <#attribute-declaration>`__.
-  Operation declarations whose syntax is described in section
   `Operation Declaration <#operation-declaration>`__.

Abstract interface
^^^^^^^^^^^^^^^^^^

An interface declaration contains the keyword ``abstract`` in its
header, declares an abstract interface. The following special rule apply
to abstract interfaces:

-  Abstract interfaces may only inherit from other abstract interfaces.

Value Declaration
~~~~~~~~~~~~~~~~~

Value type declarations are supported by FIWARE Middleware IDL, but
aren’t by FIWARE Middleware. Any FIWARE Middleware IDL parser has to
explain that these declarations are not used and the parser will ignore
them.

Constant Declaration
~~~~~~~~~~~~~~~~~~~~

A constant definition conforms to the following syntax:

::

    <const_dcl> ::= "const" <const_type>
                    <identifier> "=" <const_exp>
    <const_type> ::= <integer_type>
                 |   <char_type>
                 |   <wide_char_type>
                 |   <boolean_type>
                 |   <floating_pt_type>
                 |   <string_type>
                 |   <wide_string_type>
                 |   <fixed_pt_const_type>
                 |   <scoped_name>
                 |   <octet_type>
    <const_exp> ::= <or_expr>
    <or_expr> ::= <xor_expr>
               |  <or_expr> "|" <xor_expr>
    <xor_expr> ::= <and_expr>
               |   <xor_expr> "^" <and_expr>
    <and_expr> ::= <shift_expr>
               |   <and_expr> "&" <shift_expr>
    <shift_expr> ::= <add_expr>
                 |   <shift_expr> ">>" <add_expr>
                 |   <shift_expr> "<<" <add_expr>
    <add_expr> ::= <mult_expr>
               |   <add_expr> "+" <mult_expr>
               |   <add_expr> "-" <mult_expr>
    <mult_expr> ::= <unary_expr>
                |   <mult_expr> "*" <unary_expr>
                |   <mult_expr> "/" <unary_expr>
                |   <mult_expr> "%" <unary_expr>
    <unary_expr> ::= <unary_operator> <primary_expr>
                 |   <primary_expr>
    <unary_operator> ::= "-"
                     |   "+"
                     |   "~"
    <primary_expr> ::= <scoped_name>
                   |   <literal>
                   |   "(" <const_exp> ")"
    <literal> ::= <integer_literal>
              |   <string_literal>
              |   <wide_string_literal>
              |   <character_literal>
              |   <wide_character_literal>
              |   <fixed_pt_literal>
              |   <floating_pt_literal>
              |   <boolean_literal>
    <boolean_literal> ::= "TRUE"
                      |   "FALSE"
    <positive_int_const> ::= <const_exp>

Examples for constant declarations:

::

    const string c_str = "HelloWorld";
    const i32 c_int = 34;
    const boolean c_bool = true;

Type Declaration
~~~~~~~~~~~~~~~~

As in OMG IDL 3.5, FIWARE Middleware IDL provides constructs for naming
data types; that is, it provides C language-like declarations that
associate an identifier with a type. The IDL uses the keyword
``typedef`` to associate a name with a data type.

Type declarations conform to the following syntax:

::

    <type_dcl> ::= "typedef" <type_declarator>
               |   <struct_type>
               |   <union_type>
               |   <enum_type>
               |   "native" <simple_declarator>
               |   <constr_forward_decl>
    <type_declarator> ::= <type_spec> <declarators>

For type declarations, FIWARE Middleware IDL defines a set of type
specifiers to represent typed value. The syntax is as follows:

::

    <type_spec> ::= <simple_type_spec>
                |   <constr_type_spec>
    <simple_type_spec> ::= <base_type_spec>
                       |   <template_type_spec>
                       |   <scoped_name>
    <base_type_spec> ::= <floating_pt_type>
                     |   <integer_type>
                     |   <char_type>
                     |   <wide_char_type>
                     |   <boolean_type>
                     |   <octet_type>
                     |   <any_type>
                     |   <object_type>
                     |   <value_base_type>
    <template_type_spec> ::= <sequence_type>
                         |   <set_type>
                         |   <map_type>
                         |   <string_type>
                         |   <wide_string_type>
                         |   <fixed_pt_type>
    <constr_type_spec> ::= <struct_type>
                       |   <union_type>
                       |   <enum_type>
    <declarators> ::= <declarator> { "," <declarator> }*
    <declarator> ::= <simple_declarator>
                 |   <complex_declarator>
    <simple_declarator> ::= <identifier>
    <complex_declarator> ::= <array_declarator>

The ``<scoped_name\>`` in ``<simple_type_spec>`` must be a previously
defined type introduced by a type declaration(\ ``<type_dcl>`` - see
section `Type Declaration <#type-declaration>`__).

The next subsections describe basic and constructed type specifiers.

Basic Types
^^^^^^^^^^^

The syntax for the supported basic types is as follows:

::

    <floating_pt_type> ::= "float"
                       |   "double"
                       |   "long" "double"
                       |   "float32"
                       |   "float64"
                       |   "float128"
    <integer_type> ::= <signed_int>
                   |   <unsigned_int>
    <signed_int> ::= <signed_short_int>
                 |   <signed_long_int>
                 |   <signed_longlong_int>
    <signed_short_int> ::= "short"
                       |   "i16"
    <signed_long_int> ::= "long"
                      |    "i32"
    <signed_longlong_int> ::= "long" "long"
                          |   "i64"
    <unsigned_int> ::= <unsigned_short_int>
                   |   <unsigned_long_int>
                   |   <unsigned_longlong_int>
    <unsigned_short_int> ::= "unsigned" "short"
                         |   "ui16"
    <unsigned_long_int> ::= "unsigned" "long"
                        |   "ui32"
    <unsigned_longlong_int> ::= "unsigned" "long" "long"
                            |   "ui64"
    <char_type> ::= "char"
    <wide_char_type> ::= "wchar"
    <boolean_type> ::= "boolean"
    <octet_type> ::= "octet"
                 |   "byte"
    <any_type> ::= "any"

Each IDL data type is mapped to a native data type via the appropriate
language mapping. The syntax allows to use some OMG IDL 3.5 keywords and
to use new modern keyword. For example, FIWARE Middleware IDL supports
both keywords: ``long`` and ``i32``.

The **any** type is not supported currently by FIWARE Middleware. Any
FIWARE Middleware IDL parser has to inform users about this.

Constructed Types
~~~~~~~~~~~~~~~~~

| Constructed types are **structs**, **unions**, and **enums**.
| Their syntax is as follows:

::

    <type_dcl> ::= "typedef" <type_declarator>
               |   <struct_type>
               |   <union_type>
               |   <enum_type>
               |   "native" <simple_declarator>
               |   <constr_forward_decl>
    <constr_type_spec> ::= <struct_type>
                       |   <union_type>
                       |   <enum_type>
    <constr_forward_decl> ::= "struct" <identifier>
                          |   "union" <identifier>

Structures
^^^^^^^^^^

The syntax for the ``struct`` type is as follows:

::

    <struct_type> ::= "struct" <identifier> "{" <member_list> "}"
    <member_list> ::= <member> +
    <member> ::= <type_spec> <declarators> ";"

Example of struct syntax:

::

    struct MyStruct {
        i32 f_int;
        string f_str;
        boolean f_bool;
    };

Unions
^^^^^^

The syntax for the ``union`` type is as follows:

::

    <union_type> ::= "union" <identifier> "switch"
                     "(" <switch_type_spec> ")"
                     "{" <switch_body> "}"
    <switch_type_spec> ::= <integer_type>
                       |   <char_type>
                       |   <boolean_type>
                       |   <enum_type>
                       |   <scoped_name>
    <switch_body> ::= <case> +
    <case> ::= <case_label> + <element_spec> ";"
    <case_label> ::= "case" <const_exp> ":"
                 |   "default" ":"
    <element_spec> ::= <type_spec> <declarator>

The ``<scoped_name>`` in the ``<switch_type_spec>`` production must be a
previously defined ``integer``, ``char``, ``boolean`` or ``enum`` type.

Example of union syntax:

::

    union MyUnion switch(i32)
    {
       case 1:
          i32 f_int;
       case 2:
          string f_str;
       default:
          boolean f_bool;
    };

Enumerations
''''''''''''

| Enumerated types consist of ordered lists of identifiers.
| The syntax is as follows:

::

    <enum_type> ::= "enum" <identifier>
                    "{" <enumerator> { "," <enumerator> } * "}"
    <enumerator> ::= <identifier>

Example of an enumerated type:

::

    enum MyEnum {
       ENUM1,
       ENUM2,
       ENUM3
    };

Template Types
^^^^^^^^^^^^^^

Template types are:

::

    <template_type_spec> ::= <sequence_type>
                         |   <set_type>
                         |   <map_type>
                         |   <string_type>
                         |   <wide_string_type>
                         |   <fixed_pt_type>

Lists
'''''

The FIWARE Middleware IDL defined the template type ``list``. A list is
similar to the OMG IDL 3.5 ``sequence`` type. It is one-dimensional
array with two characteristics: a maximum size (which is fixed at
compile time) and a length (which is determined at run time). The syntax
is as follows:

::

    <sequence_type> ::= "sequence" "<" <simple_type_spec> "," <positive_int_const> ">"
                    |   "sequence" "<" <simple_type_spec> ">"
                    |   "list" "<" <simple_type_spec> "," <positive_int_const> ">"
                    |   "list" "<" <simple_type_spec> ">"

Examples of list type declarations:

::

    list<string> mylist;
    list<string, 32> myboundedlist;

Sets
''''

The FIWARE Middleware IDL includes the template type ``set``. At
marshalling level it is like the template type ``list``. But at a higher
level, contrary to the list type, a set can only contain unique values.
The syntax is as follows:

::

    <set_type> ::= "set" "<" <simple_type_spec> "," <positive_int_const> ">"
                    |   "set" "<" <simple_type_spec> ">"

Examples of set type declarations:

::

    set<string> myset;
    set<string, 32> myboundedset;

Maps
''''

The FIWARE Middleware IDL includes the template type ``map``, using the
upcoming definition in OMG IDL 4.0. Maps are a collections, similar to
lists, but items are associated with a *key*. Like lists, maps may be
bounded or unbounded. The syntax is as follows:

::

    <map_type> ::= "map" "<" <simple_type_spec> ","
                        <simple_type_spec> "," <positive_int_const> ">"
                    |   "map" "<" <simple_type_spec> "," <simple_type_spec> ">"

Examples of map type declaration:

::

    map<i32, string> mymap;
    map<i32, string, 32> myboundedmap;

In CDR marshalling, objects of type map shall be represented according
to the following equivalent OMG IDL 3.5 definition:

::

    struct MapEntry_<key_type>_<value_type>[_<bound>] {
        <key_type> key;
        value_type> value;
    };

    typedef sequence<MapEntry_<key_type>_<value_type>[_<bound>][, <bound>]>
       Map_<key_type>_<value_type>[_<bound>];

Strings
'''''''

The syntax for defining a string is as follows:

::

    <string_type> ::= "string" "<" <positive_int_const> ">"
                  |   "string"

Wstrings
''''''''

The syntax for defining a wstring is as follows:

::

    <wide_string_type> ::= "wstring" "<" <positive_int_const> ">"
                       |   "wstring"

Fixed Type
''''''''''

The ``fixed`` data type represents a fixed-point decimal number of up to
31 significant digits. The scale factor is a non-negative integer less
than or equal to the total number of digits.

The ``fixed`` data type will be mapped to the native fixed point
capability of a programming language, if available. If there is not a
native fixed point type, then the IDL mapping for that language will
provide a fixed point data types. The syntax of the fixed type is as
follows:

::

    <fixed_pt_type> ::= "fixed" "<" <positive_int_const> "," <positive_int_const> ">"
    <fixed_pt_const_type> ::= "fixed"

Complex Types
^^^^^^^^^^^^^

Arrays
''''''

The syntax for array is as follows:

::

    <array_declarator> ::= <identifier> <fixed_array_size>+
    <fixed_array_size> ::= "[" <positive_int_const> "]"

Example of array type declarations:

::

    i32 myi32array[32];
    string mystrarray[32];

Native Types
^^^^^^^^^^^^

The syntax for native types is as follows:

::

    <type_dcl> ::= "native" <simple_declarator>
    <simple_declarator> ::= <identifier>

Native types are not supported by FIWARE Middleware. Any FIWARE
Middleware IDL parser has to inform users about this and ignore this
definition.

Exception Declaration
~~~~~~~~~~~~~~~~~~~~~

Exception declarations permit the declaration of struct-like data
structures, which may be returned to indicate that an exceptional
condition has occurred during the performance of a request. The syntax
is as follows:

::

    <except_dcl> ::= "exception" <identifier> "{" <member>* "}"

Example of an exception declaration:

::

    exception myException {
       string msg;
       i32 code;
    };

Operation Declaration
~~~~~~~~~~~~~~~~~~~~~

Operation declarations in OMG IDL 3.5 and FIWARE Middleware IDL are
similar to C function declarations. The syntax is as follows:

::

    <op_dcl> ::= [ <op_attribute> ] <op_type_spec>
                 <identifier> <parameter_dcls>
                 [ <raises_expr> ] [ <context_expr> ]
    <op_attribute> ::= "oneway"
    <op_type_spec> ::= <param_type_spec>
                   | "void"

Example of an operation declaration:

::

    service myService {
        void set(i32 param);
        i32 get();
        i32 add(i32 param1, i32 param2) raises (myException);
    };

An operation declaration consists of:

-  An optional *operation attribute* that is supported by FIWARE
   Middleware IDL for backward compatibility. Operation attributes are
   described in section `Operation
   attribute <#operation-attribute>`__.
-  The *type* of the operation’s return result. Operations that do not
   return a result must specify the void type.
-  An *identifier* that names the operation in the scope of the
   interface in which it is defined.
-  A *parameter list* that specifies zero or more parameter declarations
   for the operation. Parameter declaration is described in section
   `Parameter Declarations <#parameter-declarations>`__.
-  An optional *raises expression* that indicates which exception may be
   raised as a result of an invocation of this operation. Raises
   expression are described in section `Raises
   Expressions <#raises-expressions>`__.
-  An optional *context expression* that is inherited from OMG IDL 3.5,
   but FIWARE Middleware will not use. Context expressions are described
   in section `Context Expressions <#context-expressions>`__.

Operation attribute
^^^^^^^^^^^^^^^^^^^

The syntax for operation attributes is as follows:

::

    <op_attribute> ::= "oneway"

This attribute is supported in FIWARE Middleware for backward
compatibility. But in FIWARE Middleware IDL the preferedby way to define
a **oneway** function is using the **@Oneway** annotation as described
in section `Oneway functions <#oneway-functions>`__.

Parameter Declarations
^^^^^^^^^^^^^^^^^^^^^^

Parameter declarations in FIWARE Middleware IDL operation declarations
have the following syntax:

::

    <parameter_dcls> ::= "(" <param_dcl> { "," <param_dcl> }* ")"
                     |   "(" ")"
    <param_dcl> ::= [ <param_attribute> ] <param_type_spec> <simple_declarator>
    <param_attribute> ::= "in"
                      |   "out"
                      |   "inout"
    <raises_expr> ::= "raises" "(" <scoped_name> { "," <scoped_name> }* ")"
    <param_type_spec> ::= <base_type_spec>
                      |   <string_type>
                      |   <wide_string_type>
                      |   <scoped_name>

The FIWARE Middleware IDL will *not* use output parameters, as modern
IDLs do. It supports the keywords ``in``, ``inout``, and ``out``, but
any FIWARE Middleware IDL parser will inform users all parameters will
be input parameters.

Raises Expressions
^^^^^^^^^^^^^^^^^^

There are two kinds of raises expressions.

Raises Expression
'''''''''''''''''

A raises expression specifies which exceptions may be raised as a result
of an invocation of the operation or accessing a readonly attribute. The
syntax for its specification is as follows:

::

    <raises_expr> ::= "raises" "(" <scoped_name> { "," <scoped_name> }* ")"

The ``<scoped_name>``\ s in the raises expression must be previously
defined exceptions.

getraises and setraises Expression
''''''''''''''''''''''''''''''''''

The syntax is as follows:

::

    <attr_raises_expr> ::= <get_excep_expr> [ <set_excep_expr> ]
                       |   <set_excep_expr>
    <get_excep_expr> ::= "getraises" <exception_list>
    <set_excep_expr> ::= "setraises" <exception_list>
    <exception_list> ::= "(" <scoped_name> { "," <scoped_name> }* ")"

``getraises`` and ``setraises`` expressions are used in attribute
declarations. Like in attribute declarations, theses expressions are
supported by FIWARE Middleware IDL but not by FIWARE Middleware. Any
FIWARE Middleware IDL parser has to inform users about this and it will
ignore these expressions.

Context Expressions
^^^^^^^^^^^^^^^^^^^

The syntax for content expressions is as follows:

::

    <context_expr> ::= "context" "(" <string_literal> { "," <string_literal> }* ")"

Context expressions are supported by FIWARE Middleware IDL but not by
FIWARE Middleware. Any FIWARE Middleware IDL parser has to inform users
about this and it will ignore these expressions.

Attribute Declaration
~~~~~~~~~~~~~~~~~~~~~

The syntax for attribute declarations is as follows:

::

    <attr_dcl> ::= <readonly_attr_spec> | <attr_spec>
    <readonly_attr_spec> ::= "readonly" "attribute" <param_type_spec>
                             <readonly_attr_declarator>
    <readonly_attr_declarator> ::= <simple_declarator> <raises_expr>
                               |   <simple_declarator> { "," <simple_declarator> }*
    <attr_spec> ::= "attribute" <param_type_spec>
                    <attr_declarator>
    <attr_declarator> ::= <simple_declarator> <attr_raises_expr>
                      |   <simple_declarator> { "," <simple_declarator> }*

These declarations are supported by FIWARE Middleware IDL but not by
FIWARE Middleware. Any FIWARE Middleware IDL parser has to inform users
about this and it will ignore these declarations.

Repository Identity Related Declarations
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The syntax for repository identity related declarations is as follows:

::

    <type_id_dcl> ::= "typeid" <scoped_name> <string_literal>
    <type_prefix_dcl> ::= "typeprefix" <scoped_name> <string_literal>

These declarations are supported by FIWARE Middleware IDL but not by
FIWARE Middleware. Any FIWARE Middleware IDL parser has to inform users
about this and it will ignore these declarations.

Event Declaration
~~~~~~~~~~~~~~~~~

The syntax for event declarations is as follows:

::

    <event> ::= ( <event_dcl> | <event_abs_dcl> | <event_forward_dcl>)
    <event_forward_dcl> ::= [ "abstract" ] "eventtype" <identifier>
    <event_abs_dcl> ::= "abstract" "eventtype" <identifier>
                        [ <value_inheritance_spec> ]
                        "{" <export>* "}"
    <event_dcl> ::= <event_header> "{" <value_element> * "}"
    <event_header> ::= [ "custom" ] "eventtype"
                       <identifier> [ <value_inheritance_spec> ]

These declarations are supported by FIWARE Middleware IDL but not by
FIWARE Middleware. Any FIWARE Middleware IDL parser has to inform users
about this and it will ignore these declarations.

Component Declaration
~~~~~~~~~~~~~~~~~~~~~

The syntax for component declarations is as follows:

::

    <component> ::= <component_dcl> | <component_forward_dcl>
    <component_forward_dcl> ::= "component" <identifier>
    <component_dcl> ::= <component_header> "{" <component_body> "}"
    <component_header> ::= "component" <identifier>
                           [ <component_inheritance_spec> ]
                           [ <supported_interface_spec> ]
    <supported_interface_spec> ::= "supports" <scoped_name> { "," <scoped_name> }*
    <component_inheritance_spec> ::= ":" <scoped_name>
    <component_body> ::= <component_export>*
    <component_export> ::= <provides_dcl> ";"
                       |   <uses_dcl> ";"
                       |   <emits_dcl> ";"
                       |   <publishes_dcl> ";"
                       |   <consumes_dcl> ";"
                       |   <attr_dcl> ";"
    <provides_dcl> ::= "provides" <interface_type> <identifier>
    <interface_type> ::= <scoped_name> | "Object"
    <uses_dcl> ::= "uses" [ "multiple" ] <interface_type> <identifier>
    <emits_dcl> ::= "emits" <scoped_name> <identifier>
    <publishes_dcl> ::= "publishes" <scoped_name> <identifier>
    <consumes_dcl> ::= "consumes" <scoped_name> <identifier>

These declarations are supported by FIWARE Middleware IDL but not by
FIWARE Middleware. Any FIWARE Middleware IDL parser has to inform users
about this and it will ignore these declarations.

Home Declaration
~~~~~~~~~~~~~~~~

The syntax for home declarations is as follows:

::

    <home_dcl> ::= <home_header> <home_body>
    <home_header> ::= "home" <identifier>
                      [ <home_inheritance_spec> ]
                      [ <supported_interface_spec> ]
                      "manages" <scoped_name>
                      [ <primary_key_spec> ]
    <home_inheritance_spec> ::= ":" <scoped_name>
    <primary_key_spec> ::= "primarykey" <scoped_name>
    <home_body> ::= "{" <home_export>* "}"
    <home_export ::= <export>
                 |   <factory_dcl> ";"
                 |   <finder_dcl> ";"
    <factory_dcl> ::= "factory" <identifier>
                      "(" [ <init_param_decls> ] ")"
                      [ <raises_expr> ]
    <finder_dcl> ::= "finder" <identifier>
                     "(" [ <init_param_decls> ] ")"
                     [ <raises_expr> ]

These declarations are supported by FIWARE Middleware IDL but not by
FIWARE Middleware. Any FIWARE Middleware IDL parser has to inform users
about this and it will ignore these declarations.

Annotation Declaration
~~~~~~~~~~~~~~~~~~~~~~

An annotation type is a form of aggregated type similar to a structure
with members that could be given constant values. FIWARE Middleware IDL
annotations are the ones used in future OMG IDL 4.0, whose are similar
to the one provided by Java.

An annotation is defined with a header and a body. The syntax is as
follows:

::

    <annotation_dcl> ::= <annotation_def> ";"
                     |   <annotation_forward_dcl>
    <annotation_def> ::= <annotation_header> "{" <annotation_body> "}"

Annotation Header
^^^^^^^^^^^^^^^^^

The header consists of: - The keyword ``@annotation``, followed by an
identifier that is the name given to the annotation. - Optionally a
single inheritance specification.

The syntax of an annotation header is as follows:

::

    <annotation_header> ::= "@annotation" <identifier> [<annotation_inheritance_spec>]
    <annotation_inheritance_spec> ::= ":" <scoped_name>

Annotation Body
^^^^^^^^^^^^^^^

The body contains a list of zero to several member embedded within
braces. Each attribute consists of: - The keyword ``attribute``. - The
member type, which must be a constant type ``<const_type>``. - The name
given to the member. - An optional default value, given by a constant
expression ``<const_expr>`` prefixed with the keyword **default**. The
constant expression must be compatible with the member type.

The syntax of annotation body is as follows:

::

    <annotation_body> ::= <annotation_member>*
    <annotation_member> ::= <const_type> <simple_declarator>
                            [ "default" <const_expr> ] ";"

Annotation Forwarding
^^^^^^^^^^^^^^^^^^^^^

Annotations may also be forward-declared, which allow referencing an
annotation whose definition is not provided yet.

The syntax of a forwarding annotation is as follows:

::

    <annotation_forward_dcl> ::= "@annotation" <scoped_name>

Annotation Application
~~~~~~~~~~~~~~~~~~~~~~

An annotation, once its type defined, may be applied using the following
syntax:

::

    <annotation_appl> ::= "@" <scoped_name> [ "(" [ <annotation_appl_params> ] ")" ]
    <annotation_appl_params> ::= <const_exp>
                            |   <annotation_appl_param> { "," <annotation_appl_param> }*
    <annotation_appl_param> ::= <identifier> "=" <const_exp>

Applying an annotation consists in prefixing the element under
annotation with: - The annotation name prefixed with a commercial at (@)
- Followed by the list of values given to the annotation’s members
within parentheses and separated by comma. Each parameter value consist
in: - The name of the member - The symbol '=' - A constant expression,
whose type must be compatible with the member’s declaration.

Members may be indicated in any order. Members with no default value
must be given a value. Members with default value may be omitted. In
that case, the member is considered as valued with its default value.

Two shortened forms exist: - In case, there is no member, the annotation
application may be as short as just the name of the annotation prefixed
by '@' - In case there is only one member, the annotation application
may be as short as the name of the annotation prefixed by '@' and
followed with the constant value of that unique member within (). The
type of the provided constant expression must compatible with the
members’ declaration

An annotation may be applied to almost any IDL construct or
sub-construct. Applying and annotation consists actually in adding the
related meta-data to the element under annotation. Full FIWARE
Middleware IDL described in section `Appendix B: FIWARE Middleware IDL
Grammar <#h.h832exl87ix3>`__ shows this.

Built-in annotations
~~~~~~~~~~~~~~~~~~~~

FIWARE Middleware will support some built-in annotations, that any user
can use in IDL files.

Member IDs
^^^^^^^^^^

All members of aggregated types have an integral member ID that uniquely
identifies them within their defining type. Because OMG IDL 3.5 has no
native syntax for expressing this information, IDs by default are
defined implicitly based on the members’ relative declaration order. The
first member (which, in a union type, is the discriminator) has ID 0,
the second ID 1, the third ID 2, and so on.

As described in OMG IDL for X-Types, these implicit ID assignments can
be overridden by using the "ID" annotation interface. The equivalent
definition of this type is as follows:

::

    @annotation ID {
        attribute ui32 value;
    };

Optional members
^^^^^^^^^^^^^^^^

The FIWARE Middleware IDL allows to declare a member optional, applying
the "Optional" annotation. The definitions is as follows:

::

    @annotation Optional {
        attribute boolean value default true;
    };

The CDR marshalling for this optional members is defined in IDL X-Types
standard.

Key members
^^^^^^^^^^^

The FIWARE Middleware IDL allows to declare a member as part of the key,
applying the "Key" annotation. This will be needed for future pub/sub
communication using DDS. The definitions is as follows:

::

    @annotation Key {
        attribute boolean value default true;
    };

Oneway functions
^^^^^^^^^^^^^^^^

The FIWARE Middleware IDL allows to declare a function as oneway method,
applying the "Oneway" annotation. The definitions is as follows:

::

    @annotation Oneway {
        attribute boolean value default true;
    };

Asynchronous functions
^^^^^^^^^^^^^^^^^^^^^^

The FIWARE Middleware IDL allows to declare a function as asynchronous
method, applying the "Async" annotation. The definitions is as follows:

::

    @annotation Async {
        attribute boolean value default true;
    }

IDL Complete Example
--------------------

This section provides a complete example of a FIWARE Middleware IDL
file:

::

    typedef list<i32> accountList;
    // @Encrypted annotation applies to map type declaration.
    @Encrypted(mode="sha1")
    typedef map<string, i32> userAccountMap;

    // @CppMapping annotation applies to the namespace
    @CppMapping
    namespace ThiefBank {
       
       // @Authentication annotation applies to the service.
       @Authentication(mechanism="login")
       service AccountService {
          // @Security annotation applies to the structure declaration.
          @Security
          struct AccountInfo {
              i32 count;
             string user;
          };

          @Oneway
          void setAccounts(userAccountMap uamap);

          //@Encrypted annotation applies to the parameter "account".
          @Oneway
          void setAccount(string user, @Encrypted i32 account);

          //@Encrypted annotation applies to the return value.
          @Encrypted
          AccountInfo get(string user);

          //@FullEncrypted annotation applies to the operation.
          @FullEncrypted(mode="sha1")
          AccountInfo get_secured(string user);   
       };
    };

The annotations used in previous example are defined as follows:

::

    @annotation CppMapping {
       attribute boolean value default true;
    };

    @annotation Authentication {
       attribute string mechanism default "none";
    };

    @annotation Encrypted {
       attribute string mode default "sha512";
    };

    @annotation FullEncrypted {
       attribute string mode default "sha512";
    };

    @annotation Security {
       attribute boolean active default true;
    };

Appendix A: Changes from OMG IDL 3.5
------------------------------------

This section summarizes in one block all changes applied from OMG IDL
3.5 to the FIWARE Middleware IDL:

-  Modern keyword for modules. New keyword is ``namespace``. See section
   `Module Declaration <#module-declaration>`__.
-  Modern keyword for interfaces. New keyword is ``service``. See
   section `Interface Header <#interface-header>`__.
-  Modern keywords for basic types. See section `Basic
   Types <#basic-types>`__.
-  New template types. See section `Template
   Types <#template-types>`__.
-  FIWARE Middleware IDL only uses input parameters. See section
   `Parameter Declarations <#parameter-declarations>`__
-  FIWARE Middleware IDL adds annotations. See sections `Annotation
   Declaration <#annotation-declaration>`__ and `Annotation
   Application <#annotation-application>`__.

Also FIWARE Middleware IDL does **not** use and support (and therefore
ignores) several OMG IDL 3.5 constructs:

-  Import declarations. See section `Import
   Declaration <#import-declaration>`__.
-  Value declarations. See section `Value
   Declaration <#value-declaration>`__.
-  'Any' type. See section `Basic Types <#basic-types>`__.
-  Native types. See section `Native Types <#native-types>`__.
-  Context expressions. See section `Context
   Expressions <#context-expressions>`__.
-  Attribute declarations. See section `Attribute
   Declaration <#attribute-declaration>`__.
-  Repository Identity Related Declarations. See section `Repository
   Identity Related
   Declarations <#repository-identity-related-declarations>`__.
-  Event declarations. See section `Event
   Declaration <#event-declaration>`__.
-  Component declarations. See section `Component
   Declaration <#component-declaration>`__.
-  Home declarations. See section `Home
   Declaration <#home-declaration>`__.

Appendix B: FIWARE Middleware IDL Grammar
-----------------------------------------

::

    <specification> ::= <import>* <definition>+
    <definition> ::= <type_dcl> ";"
                 |   <const_dcl> ";"
                 |   <except_dcl> ";"
                 |   <interface> ";"
                 |   <module> ";"
                 |   <value> ";"
                 |   <type_id_dcl> ";"
                 |   <type_prefix_dcl> ";"
                 |   <event> ";"
                 |   <component> ";"
                 |   <home_dcl> ";"
                 |   <annotation_dcl> ";"
                 |   <annotation_appl> <definition>
    <annotation_dcl> ::= <annotation_def> ";"
                     |   <annotation_forward_dcl>
    <annotation_def> ::= <annotation_header> "{" <annotation_body> "}"
    <annotation_header> ::= "@annotation" <identifier> [<annotation_inheritance_spec>]
    <annotation_inheritance_spec> ::= ":" <scoped_name>
    <annotation_body> ::= <annotation_member>*
    <annotation_member> ::= <const_type> <simple_declarator>
                            [ "default" <const_expr> ] ";"
    <annotation_forward_dcl> ::= "@annotation" <scoped_name>
    <annotation_appl> ::= "@" <scoped_name> [ "(" [ <annotation_appl_params> ] ")" ]
    <annotation_appl_params> ::= <const_exp>
                            |   <annotation_appl_param> { "," <annotation_appl_param> }*
    <annotation_appl_param> ::= <identifier> "=" <const_exp>
    <module> ::= ("module" | "namespace") <identifier> "{" <definition> + "}"
    <interface> ::= <interface_dcl>
                |   <forward_dcl>
    <interface_dcl> ::= <interface_header> "{" <interface_body> "}"
    <forward_dcl> ::= [ "abstract" | "local" ] ("interface" | "service") <identifier>
    <interface_header> ::= [ "abstract" | "local" ] ("interface" | "service") <identifier>
                           [ <interface_inheritance_spec> ]
    <interface_body> ::= <export>*
    <export> ::= <type_dcl> ";"
              |   <const_dcl> ";"
              |   <except_dcl> ";"
              |   <attr_dcl> ";"
              |   <op_dcl> ";"
              |   <type_id_dcl> ";"
              |   <type_prefix_dcl> ";"
             |   <annotation_appl> <export>
    <interface_inheritance_spec> ::= ":" <interface_name>
                                     { "," <interface_name> }*
    <interface_name> ::= <scoped_name>
    <scoped_name> ::= <identifier>
                |   "::" <identifier>
                |   <scoped_name> "::" <identifier>
    <value> ::= ( <value_dcl> | <value_abs_dcl> | <value_box_dcl> | <value_forward_dcl>)
    <value_forward_dcl> ::= [ "abstract" ] "valuetype" <identifier>
    <value_box_dcl> ::= "valuetype" <identifier> <type_spec>
    <value_abs_dcl> ::= "abstract" "valuetype" <identifier>
                        [ <value_inheritance_spec> ]
                        "{" <export>* "}"
    <value_dcl> ::= <value_header> "{" <value_element>* "}"
    <value_header> ::= ["custom" ] "valuetype" <identifier>
                       [ <value_inheritance_spec> ]
    <value_inheritance_spec> ::= [ ":" [ "truncatable" ] <value_name>
                                 { "," <value_name> }* ]
                                 [ "supports" <interface_name>
                                 { "," <interface_name> }* ]
    <value_name> ::= <scoped_name>
    <value_element> ::= <export> | <state_member> | <init_dcl>
    <state_member> ::= ( "public" | "private" )
                       <type_spec> <declarators> ";"
    <init_dcl> ::= "factory" <identifier>
                   "(" [ <init_param_decls> ] ")"
                   [ <raises_expr> ] ";"
    <init_param_decls> ::= <init_param_decl> { "," <init_param_decl> }*
    <init_param_decl> ::= <init_param_attribute> <param_type_spec> <simple_declarator>
    <init_param_attribute> ::= "in"
    <const_dcl> ::= "const" <const_type>
                    <identifier> "=" <const_exp>
    <const_type> ::= <integer_type>
                 |   <char_type>
                 |   <wide_char_type>
                 |   <boolean_type>
                 |   <floating_pt_type>
                 |   <string_type>
                 |   <wide_string_type>
                 |   <fixed_pt_const_type>
                 |   <scoped_name>
                 |   <octet_type>
    <const_exp> ::= <or_expr>
    <or_expr> ::= <xor_expr>
               |   <or_expr> "|" <xor_expr>
    <xor_expr> ::= <and_expr>
               |   <xor_expr> "^" <and_expr>
    <and_expr> ::= <shift_expr>
               |   <and_expr> "&" <shift_expr>
    <shift_expr> ::= <add_expr>
                 |   <shift_expr> ">>" <add_expr>
                 |   <shift_expr> "<<" <add_expr>
    <add_expr> ::= <mult_expr>
               |   <add_expr> "+" <mult_expr>
               |   <add_expr> "-" <mult_expr>
    <mult_expr> ::= <unary_expr>
                |   <mult_expr> "*" <unary_expr>
                |   <mult_expr> "/" <unary_expr>
                |   <mult_expr> "%" <unary_expr>
    <unary_expr> ::= <unary_operator> <primary_expr>
                 |   <primary_expr>
    <unary_operator> ::= "-"
                     |   "+"
                     |   "~"
    <primary_expr> ::= <scoped_name>
                   |   <literal>
                   |   "(" <const_exp> ")"
    <literal> ::= <integer_literal>
              |   <string_literal>
              |   <wide_string_literal>
              |   <character_literal>
              |   <wide_character_literal>
              |   <fixed_pt_literal>
              |   <floating_pt_literal>
              |   <boolean_literal>
    <boolean_literal> ::= "TRUE"
                      |   "FALSE"
    <positive_int_const> ::= <const_exp>
    <type_dcl> ::= "typedef" <type_declarator>
               |   <struct_type>
               |   <union_type>
               |   <enum_type>
               |   "native" <simple_declarator>
               |   <constr_forward_decl>
    <type_declarator> ::= <type_spec> <declarators>
    <type_spec> ::= <simple_type_spec>
                |   <constr_type_spec>
    <simple_type_spec> ::= <base_type_spec>
                       |   <template_type_spec>
                       |   <scoped_name>
    <base_type_spec> ::= <floating_pt_type>
                     |   <integer_type>
                     |   <char_type>
                     |   <wide_char_type>
                     |   <boolean_type>
                     |   <octet_type>
                     |   <any_type>
                     |   <object_type>
                     |   <value_base_type>
    <template_type_spec> ::= <sequence_type>
                         |   <set_type>
                         |   <map_type>
                         |   <string_type>
                         |   <wide_string_type>
                         |   <fixed_pt_type>
    <constr_type_spec> ::= <struct_type>
                       |   <union_type>
                       |   <enum_type>
    <declarators> ::= <declarator> { "," <declarator> }∗
    <declarator> ::= <simple_declarator>
                 |   <complex_declarator>
    <simple_declarator> ::= <identifier>
    <complex_declarator> ::= <array_declarator>
    <floating_pt_type> ::= "float"
                       |   "double"
                       |   "long" "double"
                       |   "float32"
                       |   "float64"
                       |   "float128"
    <integer_type> ::= <signed_int>
                   |   <unsigned_int>
    <signed_int> ::= <signed_short_int>
                 |   <signed_long_int>
                 |   <signed_longlong_int>
    <signed_short_int> ::= "short"
                       |   "i16"
    <signed_long_int> ::= "long"
                      |    "i32"
    <signed_longlong_int> ::= "long" "long"
                          |   "i64"
    <unsigned_int> ::= <unsigned_short_int>
                   |   <unsigned_long_int>
                   |   <unsigned_longlong_int>
    <unsigned_short_int> ::= "unsigned" "short"
                         |   "ui16"
    <unsigned_long_int> ::= "unsigned" "long"
                        |   "ui32"
    <unsigned_longlong_int> ::= "unsigned" "long" "long"
                            |   "ui64"
    <char_type> ::= "char"
    <wide_char_type> ::= "wchar"
    <boolean_type> ::= "boolean"
    <octet_type> ::= "octet"
                 |   "byte"
    <any_type> ::= "any"
    <object_type> ::= "Object"
    <struct_type> ::= "struct" <identifier> "{" <member_list> "}"
    <member_list> ::= <member>+
    <member> ::= <type_spec> <declarators> ";"
             |   <annotation_appl> <type_spec> <declarators> ";"
    <union_type> ::= "union" <identifier> "switch"
                     "(" <switch_type_spec> ")"
                     "{" <switch_body> "}"
    <switch_type_spec> ::= <integer_type>
                       |   <char_type>
                       |   <boolean_type>
                       |   <enum_type>
                       |   <scoped_name>
    <switch_body> ::= <case> +
    <case> ::= <case_label> + <element_spec> ";"
    <case_label> ::= "case" <const_exp> ":"
                 |   "default" ":"
    <element_spec> ::= <type_spec> <declarator>
                   |   <annotation_appl> <type_spec> <declarator>
    <enum_type> ::= "enum" <identifier>
                    "{" <enumerator> { "," <enumerator> } ∗ "}"
    <enumerator> ::= <identifier>
    <sequence_type> ::= "sequence" "<" <simple_type_spec> "," <positive_int_const> ">"
                    |   "sequence" "<" <simple_type_spec> ">"
                    |   "list" "<" <simple_type_spec> "," <positive_int_const> ">"
                    |   "list" "<" <simple_type_spec> ">"
    <set_type> ::= "set" "<" <simple_type_spec> "," <positive_int_const> ">"
                    |   "set" "<" <simple_type_spec> ">"
    <map_type> ::= "map" "<" <simple_type_spec> ","
                        <simple_type_spec> "," <positive_int_const> ">"
                    |   "map" "<" <simple_type_spec> "," <simple_type_spec> ">"
    <string_type> ::= "string" "<" <positive_int_const> ">"
                  |   "string"
    <wide_string_type> ::= "wstring" "<" <positive_int_const> ">"
                       |   "wstring"
    <array_declarator> ::= <identifier> <fixed_array_size>+
    <fixed_array_size> ::= "[" <positive_int_const> "]"
    <attr_dcl> ::= <readonly_attr_spec>
               |   <attr_spec>
    <except_dcl> ::= "exception" <identifier> "{" <member>* "}"
    <op_dcl> ::= [ <op_attribute> ] <op_type_spec>
                 <identifier> <parameter_dcls>
                 [ <raises_expr> ] [ <context_expr> ]
    <op_attribute> ::= "oneway"
    <op_type_spec> ::= <param_type_spec>
                   | "void"
    <parameter_dcls> ::= "(" <param_dcl> { "," <param_dcl> } ∗ ")"
                     |   "(" ")"
    <param_dcl> ::= [<param_attribute>] <param_type_spec> <simple_declarator>
                |   [<param_attribute>] <annotation_appl>
                    <param_type_spec> <simple_declarator>
    <param_attribute> ::= "in"
                      |   "out"
                      |   "inout"
    <raises_expr> ::= "raises" "(" <scoped_name>
                      { "," <scoped_name> } ∗ ")"
    <context_expr> ::= "context" "(" <string_literal>
                       { "," <string_literal> } ∗ ")"
    <param_type_spec> ::= <base_type_spec>
                      |   <string_type>
                      |   <wide_string_type>
                      |   <scoped_name>
    <fixed_pt_type> ::= "fixed" "<" <positive_int_const> "," <positive_int_const> ">"
    <fixed_pt_const_type> ::= "fixed"
    <value_base_type> ::= "ValueBase"
    <constr_forward_decl> ::= "struct" <identifier>
                          |   "union" <identifier>
    <import> ::= "import" <imported_scope> ";"
    <imported_scope> ::= <scoped_name> | <string_literal>
    <type_id_dcl> ::= "typeid" <scoped_name> <string_literal>
    <type_prefix_dcl> ::= "typeprefix" <scoped_name> <string_literal>
    <readonly_attr_spec> ::= "readonly" "attribute" <param_type_spec>
                             <readonly_attr_declarator>
    <readonly_attr_declarator> ::= <simple_declarator> <raises_expr>
                               |   <simple_declarator>
                                   { "," <simple_declarator> }*
    <attr_spec> ::= "attribute" <param_type_spec>
                    <attr_declarator>
    <attr_declarator> ::= <simple_declarator> <attr_raises_expr>
                      |   <simple_declarator>
                          { "," <simple_declarator> }*
    <attr_raises_expr> ::= <get_excep_expr> [ <set_excep_expr> ]
                       |   <set_excep_expr>
    <get_excep_expr> ::= "getraises" <exception_list>
    <set_excep_expr> ::= "setraises" <exception_list>
    <exception_list> ::= "(" <scoped_name>
                         { "," <scoped_name> } * ")"
    <component> ::= <component_dcl>
                |   <component_forward_dcl>
    <component_forward_dcl> ::= "component" <identifier>
    <component_dcl> ::= <component_header>
                        "{" <component_body> "}"
    <component_header> ::= "component" <identifier>
                           [ <component_inheritance_spec> ]
                           [ <supported_interface_spec> ]
    <supported_interface_spec> ::= "supports" <scoped_name>
                                   { "," <scoped_name> }*
    <component_inheritance_spec> ::= ":" <scoped_name>
    <component_body> ::= <component_export>*
    <component_export> ::= <provides_dcl> ";"
                       |   <uses_dcl> ";"
                       |   <emits_dcl> ";"
                       |   <publishes_dcl> ";"
                       |   <consumes_dcl> ";"
                       |   <attr_dcl> ";"
    <provides_dcl> ::= "provides" <interface_type> <identifier>
    <interface_type> ::= <scoped_name>
                     |   "Object"
    <uses_dcl> ::= "uses" [ "multiple" ]
                   < interface_type> <identifier>
    <emits_dcl> ::= "emits" <scoped_name> <identifier>
    <publishes_dcl> ::= "publishes" <scoped_name> <identifier>
    <consumes_dcl> ::= "consumes" <scoped_name> <identifier>
    <home_dcl> ::= <home_header> <home_body>
    <home_header> ::= "home" <identifier>
                      [ <home_inheritance_spec> ]
                      [ <supported_interface_spec> ]
                      "manages" <scoped_name>
                      [ <primary_key_spec> ]
    <home_inheritance_spec> ::= ":" <scoped_name>
    <primary_key_spec> ::= "primarykey" <scoped_name>
    <home_body> ::= "{" <home_export>* "}"
    <home_export ::= <export>
                 |   <factory_dcl> ";"
                 |   <finder_dcl> ";"
    <factory_dcl> ::= "factory" <identifier>
                      "(" [ <init_param_decls> ] ")"
                      [ <raises_expr> ]
    <finder_dcl> ::= "finder" <identifier>
                     "(" [ <init_param_decls> ] ")"
                     [ <raises_expr> ]
    <event> ::= ( <event_dcl> | <event_abs_dcl> |
                <event_forward_dcl>)
    <event_forward_dcl> ::= [ "abstract" ] "eventtype" <identifier>
    <event_abs_dcl> ::= "abstract" "eventtype" <identifie
                        [ <value_inheritance_spec> ]
                        "{" <export>* "}"
    <event_dcl> ::= <event_header> "{" <value_element> * "}"
    <event_header> ::= [ "custom" ] "eventtype"
                       <identifier> [ <value_inheritance_spec> ]

Appendix C: OMG IDL 3.5 Grammar
-------------------------------

::

    <specification> ::= <import>* <definition>+
    <definition> ::= <type_dcl> ";"
                 |   <const_dcl> ";"
                 |   <except_dcl> ";"
                 |   <interface> ";"
                 |   <module> ";"
                 |   <value> ";"
                 |   <type_id_dcl> ";"
                 |   <type_prefix_dcl> ";"
                 |   <event> ";"
                 |   <component> ";"
                 |   <home_dcl> ";"
    <module> ::= "module" <identifier> "{" <definition> + "}"
    <interface> ::= <interface_dcl>
                |   <forward_dcl>
    <interface_dcl> ::= <interface_header> "{" <interface_body> "}"
    <forward_dcl> ::= [ "abstract" | "local" ] "interface" <identifier>
    <interface_header> ::= [ "abstract" | "local" ] "interface" <identifier>
                           [ <interface_inheritance_spec> ]
    <interface_body> ::= <export>*
    <export> ::= <type_dcl> ";"
              |   <const_dcl> ";"
              |   <except_dcl> ";"
              |   <attr_dcl> ";"
              |   <op_dcl> ";"
              |   <type_id_dcl> ";"
              |   <type_prefix_dcl> ";"
    <interface_inheritance_spec> ::= ":" <interface_name>
                                     { "," <interface_name> }*
    <interface_name> ::= <scoped_name>
    <scoped_name> ::= <identifier>
                |   "::" <identifier>
                |   <scoped_name> "::" <identifier>
    <value> ::= ( <value_dcl> | <value_abs_dcl> | <value_box_dcl> | <value_forward_dcl>)
    <value_forward_dcl> ::= [ "abstract" ] "valuetype" <identifier>
    <value_box_dcl> ::= "valuetype" <identifier> <type_spec>
    <value_abs_dcl> ::= "abstract" "valuetype" <identifier>
                        [ <value_inheritance_spec> ]
                        "{" <export>* "}"
    <value_dcl> ::= <value_header> "{" < value_element>* "}"
    <value_header> ::= ["custom" ] "valuetype" <identifier>
                       [ <value_inheritance_spec> ]
    <value_inheritance_spec> ::= [ ":" [ "truncatable" ] <value_name>
                                 { "," <value_name> }* ]
                                 [ "supports" <interface_name>
                                 { "," <interface_name> }* ]
    <value_name> ::= <scoped_name>
    <value_element> ::= <export> | < state_member> | <init_dcl>
    <state_member> ::= ( "public" | "private" )
                       <type_spec> <declarators> ";"
    <init_dcl> ::= "factory" <identifier>
                   "(" [ <init_param_decls> ] ")"
                   [ <raises_expr> ] ";"
    <init_param_decls> ::= <init_param_decl> { "," <init_param_decl> }*
    <init_param_decl> ::= <init_param_attribute> <param_type_spec> <simple_declarator>
    <init_param_attribute> ::= "in"
    <const_dcl> ::= "const" <const_type>
                    <identifier> "=" <const_exp>
    <const_type> ::= <integer_type>
                 |   <char_type>
                 |   <wide_char_type>
                 |   <boolean_type>
                 |   <floating_pt_type>
                 |   <string_type>
                 |   <wide_string_type>
                 |   <fixed_pt_const_type>
                 |   <scoped_name>
                 |   <octet_type>
    <const_exp> ::= <or_expr>
    <or_expr> ::= <xor_expr>
               |   <or_expr> "|" <xor_expr>
    <xor_expr> ::= <and_expr>
               |   <xor_expr> "^" <and_expr>
    <and_expr> ::= <shift_expr>
               |   <and_expr> "&" <shift_expr>
    <shift_expr> ::= <add_expr>
                 |   <shift_expr> ">>" <add_expr>
                 |   <shift_expr> "<<" <add_expr>
    <add_expr> ::= <mult_expr>
               |   <add_expr> "+" <mult_expr>
               |   <add_expr> "-" <mult_expr>
    <mult_expr> ::= <unary_expr>
                |   <mult_expr> "*" <unary_expr>
                |   <mult_expr> "/" <unary_expr>
                |   <mult_expr> "%" <unary_expr>
    <unary_expr> ::= <unary_operator> <primary_expr>
                 |   <primary_expr>
    <unary_operator> ::= "-"
                     |   "+"
                     |   "~"
    <primary_expr> ::= <scoped_name>
                   |   <literal>
                   |   "(" <const_exp> ")"
    <literal> ::= <integer_literal>
              |   <string_literal>
              |   <wide_string_literal>
              |   <character_literal>
              |   <wide_character_literal>
              |   <fixed_pt_literal>
              |   <floating_pt_literal>
              |   <boolean_literal>
    <boolean_literal> ::= "TRUE"
                      |   "FALSE"
    <positive_int_const> ::= <const_exp>
    <type_dcl> ::= "typedef" <type_declarator>
               |   <struct_type>
               |   <union_type>
               |   <enum_type>
               |   "native" <simple_declarator>
               |   <constr_forward_decl>
    <type_declarator> ::= <type_spec> <declarators>
    <type_spec> ::= <simple_type_spec>
                |   <constr_type_spec>
    <simple_type_spec> ::= <base_type_spec>
                       |   <template_type_spec>
                       |   <scoped_name>
    <base_type_spec> ::= <floating_pt_type>
                     |   <integer_type>
                     |   <char_type>
                     |   <wide_char_type>
                     |   <boolean_type>
                     |   <octet_type>
                     |   <any_type>
                     |   <object_type>
                     |   <value_base_type>
    <template_type_spec> ::= <sequence_type>
                         |   <string_type>
                         |   <wide_string_type>
                         |   <fixed_pt_type>
    <constr_type_spec> ::= <struct_type>
                       |   <union_type>
                       |   <enum_type>
    <declarators> ::= <declarator> { "," <declarator> }∗
    <declarator> ::= <simple_declarator>
                 |   <complex_declarator>
    <simple_declarator> ::= <identifier>
    <complex_declarator> ::= <array_declarator>
    <floating_pt_type> ::= "float"
                       |   "double"
                       |   "long" "double"
    <integer_type> ::= <signed_int>
                   |   <unsigned_int>
    <signed_int> ::= <signed_short_int>
                 |   <signed_long_int>
                 |   <signed_longlong_int>
    <signed_short_int> ::= "short"
    <signed_long_int> ::= "long"
    <signed_longlong_int> ::= "long" "long"
    <unsigned_int> ::= <unsigned_short_int>
                   |   <unsigned_long_int>
                   |   <unsigned_longlong_int>
    <unsigned_short_int> ::= "unsigned" "short"
    <unsigned_long_int> ::= "unsigned" "long"
    <unsigned_longlong_int> ::= "unsigned" "long" "long"
    <char_type> ::= "char"
    <wide_char_type> ::= "wchar"
    <boolean_type> ::= "boolean"
    <octet_type> ::= "octet"
    <any_type> ::= "any"
    <object_type> ::= "Object"
    <struct_type> ::= "struct" <identifier> "{" <member_list> "}"
    <member_list> ::= <member> +
    <member> ::= <type_spec> <declarators> ";"
    <union_type> ::= "union" <identifier> "switch"
                     "(" <switch_type_spec> ")"
                     "{" <switch_body> "}"
    <switch_type_spec> ::= <integer_type>
                       |   <char_type>
                       |   <boolean_type>
                       |   <enum_type>
                       |   <scoped_name>
    <switch_body> ::= <case> +
    <case> ::= <case_label> + <element_spec> ";"
    <case_label> ::= "case" <const_exp> ":"
                 |   "default" ":"
    <element_spec> ::= <type_spec> <declarator>
    <enum_type> ::= "enum" <identifier>
                    "{" <enumerator> { "," <enumerator> } ∗ "}"
    <enumerator> ::= <identifier>
    <sequence_type> ::= "sequence" "<" <simple_type_spec> "," <positive_int_const> ">"
                    |   "sequence" "<" <simple_type_spec> ">"
    <string_type> ::= "string" "<" <positive_int_const> ">"
                  |   "string"
    <wide_string_type> ::= "wstring" "<" <positive_int_const> ">"
                       |   "wstring"
    <array_declarator> ::= <identifier> <fixed_array_size>+
    <fixed_array_size> ::= "[" <positive_int_const> "]"
    <attr_dcl> ::= <readonly_attr_spec>
               |   <attr_spec>
    <except_dcl> ::= "exception" <identifier> "{" <member>* "}"
    <op_dcl> ::= [ <op_attribute> ] <op_type_spec>
                 <identifier> <parameter_dcls>
                 [ <raises_expr> ] [ <context_expr> ]
    <op_attribute> ::= "oneway"
    <op_type_spec> ::= <param_type_spec>
                   | "void"
    <parameter_dcls> ::= "(" <param_dcl> { "," <param_dcl> } ∗ ")"
                     |   "(" ")"
    <param_dcl> ::= <param_attribute> <param_type_spec> <simple_declarator>
    <param_attribute> ::= "in"
                      |   "out"
                      |   "inout"
    <raises_expr> ::= "raises" "(" <scoped_name>
                      { "," <scoped_name> } ∗ ")"
    <context_expr> ::= "context" "(" <string_literal>
                       { "," <string_literal> } ∗ ")"
    <param_type_spec> ::= <base_type_spec>
                      |   <string_type>
                      |   <wide_string_type>
                      |   <scoped_name>
    <fixed_pt_type> ::= "fixed" "<" <positive_int_const> "," <positive_int_const> ">"
    <fixed_pt_const_type> ::= "fixed"
    <value_base_type> ::= "ValueBase"
    <constr_forward_decl> ::= "struct" <identifier>
                          |   "union" <identifier>
    <import> ::= "import" <imported_scope> ";"
    <imported_scope> ::= <scoped_name> | <string_literal>
    <type_id_dcl> ::= "typeid" <scoped_name> <string_literal>
    <type_prefix_dcl> ::= "typeprefix" <scoped_name> <string_literal>
    <readonly_attr_spec> ::= "readonly" "attribute" <param_type_spec>
                             <readonly_attr_declarator>
    <readonly_attr_declarator> ::= <simple_declarator> <raises_expr>
                               |   <simple_declarator>
                                   { "," <simple_declarator> }*
    <attr_spec> ::= "attribute" <param_type_spec>
                    <attr_declarator>
    <attr_declarator> ::= <simple_declarator> <attr_raises_expr>
                      |   <simple_declarator>
                          { "," <simple_declarator> }*
    <attr_raises_expr> ::= <get_excep_expr> [ <set_excep_expr> ]
                       |   <set_excep_expr>
    <get_excep_expr> ::= "getraises" <exception_list>
    <set_excep_expr> ::= "setraises" <exception_list>
    <exception_list> ::= "(" <scoped_name>
                         { "," <scoped_name> } * ")"
    <component> ::= <component_dcl>
                |   <component_forward_dcl>
    <component_forward_dcl> ::= "component" <identifier>
    <component_dcl> ::= <component_header>
                        "{" <component_body> "}"
    <component_header> ::= "component" <identifier>
                           [ <component_inheritance_spec> ]
                           [ <supported_interface_spec> ]
    <supported_interface_spec> ::= "supports" <scoped_name>
                                   { "," <scoped_name> }*
    <component_inheritance_spec> ::= ":" <scoped_name>
    <component_body> ::= <component_export>*
    <component_export> ::= <provides_dcl> ";"
                       |   <uses_dcl> ";"
                       |   <emits_dcl> ";"
                       |   <publishes_dcl> ";"
                       |   <consumes_dcl> ";"
                       |   <attr_dcl> ";"
    <provides_dcl> ::= "provides" <interface_type> <identifier>
    <interface_type> ::= <scoped_name>
                     |   "Object"
    <uses_dcl> ::= "uses" [ "multiple" ]
                   < interface_type> <identifier>
    <emits_dcl> ::= "emits" <scoped_name> <identifier>
    <publishes_dcl> ::= "publishes" <scoped_name> <identifier>
    <consumes_dcl> ::= "consumes" <scoped_name> <identifier>
    <home_dcl> ::= <home_header> <home_body>
    <home_header> ::= "home" <identifier>
                      [ <home_inheritance_spec> ]
                      [ <supported_interface_spec> ]
                      "manages" <scoped_name>
                      [ <primary_key_spec> ]
    <home_inheritance_spec> ::= ":" <scoped_name>
    <primary_key_spec> ::= "primarykey" <scoped_name>
    <home_body> ::= "{" <home_export>* "}"
    <home_export ::= <export>
                 |   <factory_dcl> ";"
                 |   <finder_dcl> ";"
    <factory_dcl> ::= "factory" <identifier>
                      "(" [ <init_param_decls> ] ")"
                      [ <raises_expr> ]
    <finder_dcl> ::= "finder" <identifier>
                     "(" [ <init_param_decls> ] ")"
                     [ <raises_expr> ]
    <event> ::= ( <event_dcl> | <event_abs_dcl> |
                <event_forward_dcl>)
    <event_forward_dcl> ::= [ "abstract" ] "eventtype" <identifier>
    <event_abs_dcl> ::= "abstract" "eventtype" <identifie
                        [ <value_inheritance_spec> ]
                        "{" <export>* "}"
    <event_dcl> ::= <event_header> "{" <value_element> * "}"
    <event_header> ::= [ "custom" ] "eventtype"
                       <identifier> [ <value_inheritance_spec> ]
