Advanced Middleware Publication Subscription API Specification
==============================================================

**Date: 01th November 2015**

- Version: `0.3.0 <#>`_
- Latest version: :doc:`latest <Middleware_PUBSUB_API_Specification>`

Editors:

-  `eProsima - The Middleware
   Experts <http://www.eprosima.com/index.php/en/>`_
-  `DFKI - German Research Center for Artificial
   Intelligence <http://www.dfki.de/>`_
-  `ZHAW - School of Engineering
   (ICCLab) <http://blog.zhaw.ch/icclab>`_

Copyright © 2013-2015 by eProsima, DFKI, ZHAW. All Rights Reserved

--------------

Abstract
--------

This document presents a proposal for a publication-subscription API.
The goal of this document is to define the basic principles of a new
KIARA Publication-Subscription framework.

The API proposed in this document will be very user-friendly in order to
allow FI-WARE users to easily migrate to KIARA's Publisher-Subscriber
framework. The presented API should allow users to set up a publisher or
a subscriber with very few calls and with little knowledge of the
technology behind the framework.

Status of this Document
-----------------------

+-------------------+-------------------+
| **Date**          | **Description**   |
+===================+===================+
| 29-April-2015     | Release 0.2.0     |
+-------------------+-------------------+
| 01-November-2015  | Release 0.3.0     |
+-------------------+-------------------+

Conformance
-----------

The conformance clause identifies which clauses of the specification are
mandatory (or conditionally mandatory) and which are optional in order
for an implementation to claim conformance to the specification.

Note: For conditionally mandatory clauses, the conditions must be
specified.

Reference Material
------------------

The following normative documents contain provisions which, through
reference in this text, constitute provisions of this specification. For
dated references, subsequent amendments to, or revisions of, any of
these publications do not apply.

API Overview
------------

This section enumerates and describes the classes provided by the KIARA
Publication-Subscription API.

The Publication-Subscription framework is a messaging pattern by which
the information producers, in this case called publishers, publish
messages characterized in a specific manner. On the other hand, the
information consumers, called subscribers, state their interest in a
certain type of messages. This pattern provides a larger flexibility and
scalability compared to other messaging paradigms.

This pattern also allows for message filtering, and the most common way
of doing so is by using a topic-based filter, where each publisher or
subscriber produces or consumes information of a specific topic. Each
topic has a unique name in the network, as well as a specific data type
associated with it.

The main objective of this API is to allow users to easily have access
to the basics of a Publication-Subscription messaging framework,
providing them with a friendly interface and lowering the learning
curve.

Main classes
~~~~~~~~~~~~

There are four main classes in this API:

org.fiware.kiara.ps.participant.Participant
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Singleton class representing a node in the network. It might have
multiple publishers and subscribers associated to it. This object acts
as a supervisor of this node in the network. All the methods in this
class will be static, since the constructor will be hidden to the user.

**Functions:**

-  **registerTopicDataType:** 
	This function is used to register new TopicDataType objects in a Participant (network node). The registration acts as a contract defining which data types a publisher is able to understand. It allows the creation of publishers and subscribers that use the registered TopicDataType.

-  **createPublisher:** 
	Function to create a new Publisher for a specific TopicDataType object and associate it with a listener.

-  **createSubscriber:** 
	Function to create a new Subscriber for a specific TopicDataType object and associate it with a listener.

-  **removePubisher:** 
	This function removes a Publisher from this Participant.

-  **removeSubscriber:** 
	This function removes a Subscriber from this Participant.

-  **registerType:** 
	This function is used to register a TopicDataType in this participant.

org.fiware.kiara.ps.topic.TopicDataType
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This interface describes the serialization and deserialization procedures of the selected data type. It also defines how the key is obtained from the data object in case the topic is a keyed topic.

**Functions:**

-  **serialize**: 
	This function defines the signature of the serialization functions for every TopicDataType object existing in the framework.

-  **deserialize**: 
	This function defines the signature of the deserialization functions for every TopicDataType object existing in the framework.

-  **createData**: 
	This function returns a new object of the type represented by the TopicDataType.

-  **getKey**: 
	This function is used to return the IntanceHandle object representing the 16-Byte key of the TopicDataType.

org.fiware.kiara.ps.publisher.Publisher
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This class is the one used to describe a data publisher inside a
specific node. It has multiple parameters grouped into a single
PublisherAttributes object, which will be detailed later.

It also provides functions to easily change and manipulate the
parameters of the publisher, as well as a function to send data over the
wire.

**Functions:**

-  **getAttributes:** 
	This function is used to retrieve the PublisherAttributes object contained within this class.

-  **setAttributes:** 
	This function is used to set the PublisherAttributes object inside this class.

-  **write:** 
	This function is the one used to send data through the network. Its function is to publish information about a specific topic which the publisher is able to use.

-  **destroy**: 
	This function is used to delete all the entities associated to this publisher.

org.fiware.kiara.ps.subscriber.Subscriber
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This class is similar to the Publisher class. All the parameters
associated with each subscriber are aggregated into a single
SubscriberAttributes object that can be retrieved and changed by using
its accessor functions.

**Functions:**

-  **getAttributes:** 
	This function is used to retrieve the SubscriberAttributes object contained within this class.

-  **setAttributes:** 
	This function is used to set the SubscriberAttributes object inside this class.

-  **readNextData:** 
	This function is used to retrieve an unread CacheChange with the data sent through the network. The data received belongs to a topic the subscriber has subscribed to.

-  **takeNextData**: 
	This function is used to retrieve and remove the next unread CacheChange with data receiver over the wire.

-  **waitForMessage:** 
	This method blocks the execution thread until a message is received. This message can be retrieved then using the **read** function.

-  **destroy**: 
	This function is used to delete all the entities associated to this subscriber.

Secondary classes
~~~~~~~~~~~~~~~~~

This classes are those necessary for the main ones described before. In this group are included the classes that are used to deliberately specify a certain behaviour for an event that happened in the publisher or the subscriber side.

org.fiware.kiara.ps.listeners.PublisherListener
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This interface is designed to be implemented for those classes that ought to manage certain events in the publisher side. It defines a set of methods that can be overwritten by the user to specify the behaviour of the publisher when certain events occur. An example of this would be a new subscriber that has been discovered.

**Functions:**

-  **onPublicationMatched:** 
	This function is the one that will be called when the data published by a publisher matches with the subscriber for a specific topic. The MatchingInfo class provided as a parameter gives the user information about the matched subscriber.

org.fiware.kiara.ps.listeners.SubscriberListener
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This interface is similar to the PublisherListener interface described above, but in this case it defines a set of methods used to specify the subscriber's behaviour. An example of this would be a new message that has been received.

**Functions:**

-  **onSubscriptionMatched:** 
	This method's objective is the same as the one described in the PublisherListener class, but in this case, it will be executed when a new subscription matches with the data a publisher is publishing.

-  **onNewDataMessage:** 
	This function will be executed when a new message is received by the subscriber.

Auxiliary classes
~~~~~~~~~~~~~~~~~

org.fiware.kiara.ps.utils.InstanceHandle
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This class contains the serialized data of a specific message the user is sending or receiving through the network. It provides functions to retrieve the information of such message.

**Functions:** None

org.fiware.kiara.ps.attributes.TopicAttributes
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This structure contains all the different attributes of a Topic. These attributes will include the topic name, the data type name and the topic kind.

**Attributes:**

-  **topicKind:** 
	This attribute represents the kind of the Topic (with key or without key)

-  **topicName:** 
	This attribute represents the topic name.

-  **topicDataTypeName:** 
	This attribute represents the name of the data type for a specific topic.

-  **historyQos:** 
	This attribute represents the History QoS.

-  **resourceLimitQos:** 
	This attribute represents the limit of the resources for this topic.

org.fiware.kiara.ps.attributes.PublisherAttributes
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This structure contains all the different attributes of a publisher. These attributes will include the topic attributes (topic name, topic data type, etc), as well as the list of locators, times and writer QoS.

**Attributes:**

-  **topic:** 
	Object instance of TopicAttributes. It holds all the attributes of the Topic.

-  **wqos:** 
	Represents the Qualities of Service associated to the Writer.

-  **times:** 
	Time values associated to the Publisher entity.

-  **unicastLocatorList:** 
	List of unicast locators representing different Endpoints.

-  **multicastLocatorList:** 
	List of multicast locators representing different Endpoints.

**Functions:**

-  **getUserDefinedID:** 
	Returns the user defined identifier for this object.

-  **setUserDefinedID:** 
	Sets the user defined identifier for this object.

-  **getEntityId:** 
	Returns the EntityID that uses this attributes class.

-  **setEntityId:** 
	Sets the EntityID that uses this attributes class.

org.fiware.kiara.ps.attributes.SubscriberAttributes
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This structure contains all the different attributes of subscriber. These attributes will include the topic attributes (name, topic data type, etc), as well as the list of locators, times and reader QoS.

**Attributes:**

-  **topic:** 
	Object instance of TopicAttributes. It holds all the attributes of the Topic.

-  **rqos:** 
	Represents the Qualities of Service associated to the Reader.

-  **times:** 
	Time values associated to the Subscriber entity.

-  **unicastLocatorList:** 
	List of unicast locators representing different Endpoints.

-  **multicastLocatorList:** 
	List of multicast locators representing different Endpoints.

-  **expectsInlineQos**: 
	This attribute defines whether or not the Subscriber will expect inline QoS.

**Functions:**

-  **getUserDefinedID:** 
	Returns the user defined identifier for this object.

-  **setUserDefinedID:** 
	Sets the user defined identifier for this object.

-  **getEntityId:** 
	Returns the EntityID that uses this attributes class.

-  **setEntityId:** 
	Sets the EntityID that uses this attributes class.

org.fiware.kiara.ps.utils.SampleInfo
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This class contains information about each particular message, for example the publisher who originated it or a timestamp of its creation time. The GUID of the writer is related to the node from where the information comes from.

**Functions:**

-  **getWriterGUID:** 
	This function is used to obtain the GUID of the writer who originated a specific message.

-  **getTimestamp:** 
	This function returns the timestamp value indicating the exact time when the message was created.

org.fiware.kiara.ps.utils.MatchingInfo
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This class informs the user of whether the event is a matching or an un-matching event and also the global identifier of the remote endpoint.

**Functions:**

-  **getMatchingStatus:** 
	This function allows the users to know the matching status of a specific endpoint with the risen event.
-  **getRemoteEndpointGUID:** 
	This function returns the endpoint's unique identifier.

org.fiware.kiara.ps.common.GUID
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This class represents a unique identifier of a node in the network. It is formed by two members, a prefix (12 Bytes) and an entity ID (4 Bytes).

**Functions:**

-  **getEntityId**: 
	This function returns the EntityId associated to the GUID.
-  **getGUIDPrefix**: 
	This function returns the GUIDPrefix associated to the GUID.

.. note::
	There are some classes that do not appear yet in this document, and this is because their definition is too long (they will be included in an external annex)

	A few examples of these classes are:                                                                           

	-  QoS related classes: QoSList, ReaderQos, WriterQos and QoSPolicy (and all its subclasses).
	-  SerializableDataType: Interface between Serializable objects defined in KIARA and TopicDataTypes.
	-  History classes: HistoryCache, SubscriberHistory and PublisherHistory      


API Description
---------------

This section details the classes of this API and all their methods.

Main classes
~~~~~~~~~~~~

org.fiware.kiara.ps.participant.Participant
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The public methods of this class are listed below:

**Attributes**
**************

- None

**Public Operations**
*********************
+-------------------------+------------------+------------------------+--------------+
| **Name**                | **Parameters**   | **Returns/Type**       | **Raises**   |
+=========================+==================+========================+==============+
| registerTopicDataType   |                  | boolean                |              |
+-------------------------+------------------+------------------------+--------------+
|                         | dataType         | TopicDataType<T>       |              |
+-------------------------+------------------+------------------------+--------------+
| createPublisher         |                  | Publisher<T>           |              |
+-------------------------+------------------+------------------------+--------------+
|                         | attributes       | PublisherAttributes    |              |
+-------------------------+------------------+------------------------+--------------+
|                         | listener         | PublisherListener      |              |
+-------------------------+------------------+------------------------+--------------+
|                         | topic            | TopicDataType<T>       |              |
+-------------------------+------------------+------------------------+--------------+
| createSubscriber        |                  | Subscriber<T>          |              |
+-------------------------+------------------+------------------------+--------------+
|                         | attributes       | SubscriberAttributes   |              |
+-------------------------+------------------+------------------------+--------------+
|                         | listener         | SubscriberListener     |              |
+-------------------------+------------------+------------------------+--------------+
|                         | topic            | TopicDataType<T>       |              |
+-------------------------+------------------+------------------------+--------------+
| removePublisher         |                  | boolean                |              |
+-------------------------+------------------+------------------------+--------------+
|                         | publisher        | Publisher<T>           |              |
+-------------------------+------------------+------------------------+--------------+
| removeSubscriber        |                  | boolean                |              |
+-------------------------+------------------+------------------------+--------------+
|                         | subscriber       | Subscriber<T>          |              |
+-------------------------+------------------+------------------------+--------------+
| registerType            |                  | boolean                |              |
+-------------------------+------------------+------------------------+--------------+
|                         | type             | TopicDataType<T>       |              |
+-------------------------+------------------+------------------------+--------------+

org.fiware.kiara.ps.topic.TopicDataType
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The public methods of this class are listed below:


**Attributes**
**************

- None

**Public Operations**
*********************

+---------------+------------------+---------------------+---------------+
| **Name**      | **Parameters**   | **Returns/Type**    | **Raises**    |
+===============+==================+=====================+===============+
| serialize     |                  | void                | IOException   |
+---------------+------------------+---------------------+---------------+
|               | payload          | SerializedPayload   |               |
+---------------+------------------+---------------------+---------------+
|               | object           | T                   |               |
+---------------+------------------+---------------------+---------------+
| deserialize   |                  | T                   | IOException   |
+---------------+------------------+---------------------+---------------+
|               | payload          | SerializerPayload   |               |
+---------------+------------------+---------------------+---------------+
| createData    |                  | T                   |               |
+---------------+------------------+---------------------+---------------+
| getKey        |                  | InstanceHandle      | IOException   |
+---------------+------------------+---------------------+---------------+
|               | object           | T                   |               |
+---------------+------------------+---------------------+---------------+


org.fiware.kiara.ps.publisher.Publisher
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The public methods of this class are listed below:

**Attributes**
**************

- None

**Public Operations**
*********************
+-----------------+------------------+-----------------------+----------------------+
| **Name**        | **Parameters**   | **Returns/Type**      | **Raises**           |
+=================+==================+=======================+======================+
| getAttributes   |                  | Attributes            |                      |
+-----------------+------------------+-----------------------+----------------------+
| setAttributes   |                  | void                  | PublisherException   |
+-----------------+------------------+-----------------------+----------------------+
|                 | attributes       | PublisherAttributes   |                      |
+-----------------+------------------+-----------------------+----------------------+
| write           |                  | boolean               | PublisherException   |
+-----------------+------------------+-----------------------+----------------------+
|                 | data             | TopicDataType<T>      |                      |
+-----------------+------------------+-----------------------+----------------------+
| destroy         |                  | void                  |                      |
+-----------------+------------------+-----------------------+----------------------+

org.fiware.kiara.ps.subscriber.Subscriber
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The public methods of this class are listed below:

**Attributes**
**************

- None

**Public Operations**
*********************

+------------------+------------------+-----------------------+-----------------------+
| **Name**         | **Parameters**   | **Returns/Type**      | **Raises**            |
+==================+==================+=======================+=======================+
| getAttributes    |                  | Attributes            |                       |
+------------------+------------------+-----------------------+-----------------------+
| setAttributes    |                  | void                  | SubscriberException   |
+------------------+------------------+-----------------------+-----------------------+
|                  | attributes       | SubscriberException   |                       |
+------------------+------------------+-----------------------+-----------------------+
| waitForMessage   |                  | void                  | SubscriberException   |
+------------------+------------------+-----------------------+-----------------------+
| readNextData     |                  | boolean               |                       |
+------------------+------------------+-----------------------+-----------------------+
|                  | info             | SampleInfo            |                       |
+------------------+------------------+-----------------------+-----------------------+
| takeNextData     |                  | boolean               |                       |
+------------------+------------------+-----------------------+-----------------------+
|                  | info             | SampleInfo            |                       |
+------------------+------------------+-----------------------+-----------------------+
| destroy          |                  | void                  |                       |
+------------------+------------------+-----------------------+-----------------------+


Secondary classes
~~~~~~~~~~~~~~~~~

org.fiware.kiara.ps.publisher.PublisherListener
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The public methods of this class are listed below:

**Attributes**
**************

- None

**Public Operations**
*********************

+------------------------+------------------+--------------------+----------------------+
| **Name**               | **Parameters**   | **Returns/Type**   | **Raises**           |
+========================+==================+====================+======================+
| onPublicationMatched   |                  | void               | PublisherException   |
+------------------------+------------------+--------------------+----------------------+
|                        | info             | MatchingInfo       |                      |
+------------------------+------------------+--------------------+----------------------+
|                        | pub              | Publisher          |                      |
+------------------------+------------------+--------------------+----------------------+


org.fiware.kiara.ps.subscriber.SubscriberListener
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The public methods of this class are listed below:

**Attributes**
**************

- None

**Public Operations**
*********************

+-------------------------+------------------+--------------------+-----------------------+
| **Name**                | **Parameters**   | **Returns/Type**   | **Raises**            |
+=========================+==================+====================+=======================+
| onSubscriptionMatched   |                  | void               | SubscriberException   |
+-------------------------+------------------+--------------------+-----------------------+
|                         | info             | MatchingInfo       |                       |
+-------------------------+------------------+--------------------+-----------------------+
|                         | sub              | Subscriber         |                       |
+-------------------------+------------------+--------------------+-----------------------+
| onNewDataMessage        |                  | void               | SubscriberException   |
+-------------------------+------------------+--------------------+-----------------------+
|                         | sub              | Subscriber         |                       |
+-------------------------+------------------+--------------------+-----------------------+


Auxiliary classes
~~~~~~~~~~~~~~~~~

org.fiware.kiara.ps.utils.InstanceHandle
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The public methods of this class are listed below:

**Attributes**
**************

- None

**Public Operations**
*********************

- None


org.fiware.kiara.ps.attributes.TopicAttributes
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The public methods of this class are listed below:

**Attributes**
**************

+---------------------+---------------------------+              |
| **Name**            | **Type**                  |              |
+=====================+===========================+              |
| topicKind           | TopicKind                 |              |
+---------------------+---------------------------+              |
| topicName           | String                    |              |
+---------------------+---------------------------+              |
| topicDataTypeName   | String                    |              |
+---------------------+---------------------------+              |
| historyQos          | HistoryPolicyQos          |              |
+---------------------+---------------------------+              |
| resourceLimitQos    | ResourceLimitsQosPolicy   |              |
+---------------------+---------------------------+              |

**Public Operations**
*********************

- None

org.fiware.kiara.ps.attributes.PublisherAttributes
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The public methods of this class are listed below:

**Attributes**
**************

+------------------------+-------------------+                                  |
| **Name**               | **Type**          |                                  |
+========================+===================+                                  |
| topic                  | TopicAttributes   |                                  |
+------------------------+-------------------+                                  |
| wqos                   | WriterQos         |                                  |
+------------------------+-------------------+                                  |
| times                  | WriterTimes       |                                  |
+------------------------+-------------------+                                  |
| unicastLocatorList     | LocatorList       |                                  |
+------------------------+-------------------+                                  |
| multicastLocatorList   | LocatorList       |                                  |
+------------------------+-------------------+                                  |

**Public Operations**
*********************

+--------------------+------------------+--------------------+--------------+
| **Name**           | **Parameters**   | **Returns/Type**   | **Raises**   |
+====================+==================+====================+==============+
| getUserDefinedID   |                  | short              |              |
+--------------------+------------------+--------------------+--------------+
| setUserDefinedID   |                  | void               |              |
+--------------------+------------------+--------------------+--------------+
|                    | userDefinedID    | short              |              |
+--------------------+------------------+--------------------+--------------+
| getEntityID        |                  | short              |              |
+--------------------+------------------+--------------------+--------------+
| setEntityID        |                  | void               |              |
+--------------------+------------------+--------------------+--------------+
|                    | entityID         | short              |              |
+--------------------+------------------+--------------------+--------------+


org.fiware.kiara.ps.attributes.SubscriberAttributes
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The public methods of this class are listed below:

**Attributes**
**************

+------------------------+-------------------+                                  |
| **Name**               | **Type**          |                                  |
+========================+===================+                                  |
| topic                  | TopicAttributes   |                                  |
+------------------------+-------------------+                                  |
| rqos                   | ReaderQos         |                                  |
+------------------------+-------------------+                                  |
| times                  | ReaderTimes       |                                  |
+------------------------+-------------------+                                  |
| unicastLocatorList     | LocatorList       |                                  |
+------------------------+-------------------+                                  |
| multicastLocatorList   | LocatorList       |                                  |
+------------------------+-------------------+                                  |
| expectsInlineQos       | boolean           |                                  |
+------------------------+-------------------+                                  |

**Public Operations**
*********************

+--------------------+------------------+--------------------+--------------+
| **Name**           | **Parameters**   | **Returns/Type**   | **Raises**   |
+====================+==================+====================+==============+
| getUserDefinedID   |                  | short              |              |
+--------------------+------------------+--------------------+--------------+
| setUserDefinedID   |                  | void               |              |
+--------------------+------------------+--------------------+--------------+
|                    | userDefinedID    | short              |              |
+--------------------+------------------+--------------------+--------------+
| getEntityID        |                  | short              |              |
+--------------------+------------------+--------------------+--------------+
| setEntityID        |                  | void               |              |
+--------------------+------------------+--------------------+--------------+
|                    | entityID         | short              |              |
+--------------------+------------------+--------------------+--------------+


org.fiware.kiara.ps.utils.SampleInfo
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The public methods of this class are listed below:

**Attributes**
**************

- None

**Public Operations**
*********************

+-----------------+------------------+--------------------+--------------+
| **Name**        | **Parameters**   | **Returns/Type**   | **Raises**   |
+=================+==================+====================+==============+
| getWriterGUID   |                  | GUID               |              |
+-----------------+------------------+--------------------+--------------+
| getTimestamp    |                  | Timestamp          |              |
+-----------------+------------------+--------------------+--------------+


org.fiware.kiara.ps.utils.MatchingInfo
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The public methods of this class are listed below:

**Attributes**
**************

- None

**Public Operations**
*********************

+------------------------+------------------+--------------------+--------------+
| **Name**               | **Parameters**   | **Returns/Type**   | **Raises**   |
+========================+==================+====================+==============+
| getMatchingStatus      |                  | MatchingStatus     |              |
+------------------------+------------------+--------------------+--------------+
| geRemoteEndpointGUID   |                  | GUID               |              |
+------------------------+------------------+--------------------+--------------+


org.fiware.kiara.ps.common.GUID
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The public methods of this class are listed below:

**Attributes**
**************

- None

**Public Operations**
*********************

+-----------------+------------------+--------------------+--------------+
| **Name**        | **Parameters**   | **Returns/Type**   | **Raises**   |
+=================+==================+====================+==============+
| getEntityId     |                  | EntityId           |              |
+-----------------+------------------+--------------------+--------------+
| setEntityId     |                  | void               |              |
+-----------------+------------------+--------------------+--------------+
|                 | entityID         | EntityId           |              |
+-----------------+------------------+--------------------+--------------+
| getGUIDPrefix   |                  | GuidPrefix         |              |
+-----------------+------------------+--------------------+--------------+
| setGUIDPrefix   |                  | void               |              |
+-----------------+------------------+--------------------+--------------+
|                 | guidPrefix       | GUIDPrefix         |              |
+-----------------+------------------+--------------------+--------------+
| equals          |                  | boolean            |              |
+-----------------+------------------+--------------------+--------------+
|                 | other            | Object             |              |
+-----------------+------------------+--------------------+--------------+


