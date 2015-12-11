KIARA Installation and Administration Guide
===========================================

**Date: 10th October 2015**

- Version: `0.4.0 <#>`__
- Latest version: :doc:`latest <Installation_and_Admin_Guide>`

Editors:

-  `eProsima - The Middleware
   Experts <http://www.eprosima.com/index.php/en/>`__
-  `DFKI - German Research Center for Artificial
   Intelligence <http://www.dfki.de/>`__
-  `ZHAW - School of Engineering
   (ICCLab) <http://blog.zhaw.ch/icclab>`__

Copyright © 2013-2015 by eProsima, DFKI, ZHAW. All Rights Reserved

--------------

Introduction
------------

KIARA Advanced Middleware is a Java based communication middleware for modern, efficient and secure applications. It is an implementation of the FIWARE Advanced Middleware Generic Enabler.

This first release focuses on the basic features of RPC communication:

-  Modern Interface Definition Language (IDL) with a syntax based on the Corba IDL.
-  Easy to use and extensible Application Programmer Interface (API).
-  IDL derived operation mode providing Stubs and Skeletons for RPC Client/Server implementations.
-  Synchronous and Asynchronous function calls.

Later versions will include additional features like:

-  Application derived and Mapped operation mode providing dynamic declaration of functions and data type mapping.
-  Advanced security features like field encryption and authentication.
-  Additional communication patterns like publish/subscribe.

KIARA Advanced Middleware is essentially a library which is incorporated into the developed applications, the requirements are rather minimal. In particular it requires no service running in the background.

System Requirements
-------------------

This section describes the basic requirements of KIARA Advanced Middleware and how to install them.

Hardware Requirements
~~~~~~~~~~~~~~~~~~~~~

The hardware requirements depend on the application to be developed. Any hardware running Java JRE/JDK 7 or later is supported.

This `Oracle JDK Webpage <http://docs.oracle.com/javase/8/docs/technotes/guides/install/windows_system_requirements.html>`__ provides specific minimal hardware requirements (Disk, Memory, CPU).

Software Requirements
~~~~~~~~~~~~~~~~~~~~~

Runtime Systems:

-  Any Java SE JRE 7 or later distribution (OpenJDK, Oracle Java SE or IBM Java SDK)

Development Systems:

-  Any Java SE JDK 7 or later distribution (OpenJDK, Oracle Java SE or IBM Java SDK)
-  Build Tools (gradle or maven) and/or IDE (Eclipse, IntelliJ IDEA, Netbeans, ...)

Operating System support:
~~~~~~~~~~~~~~~~~~~~~~~~~

Any Operating system running Java JRE/JDK 7 or later is supported.

KIARA is tested on Windows (7), Linux (Ubuntu 14.04/Fedora 19) and OS X (10.9/10.10).

Installation
------------

Installation of the Java JDK
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Please follow the installation instructions of the respective Java distribution:

-  `OpenJDK <http://openjdk.java.net/install/>`__ (Default on most Linux-Systems)
-  `Oracle Java SE JDK <http://docs.oracle.com/javase/8/docs/technotes/guides/install>`__ (Linux, Windows, OS X, Solaris)
-  `IBM Java SE JDK <http://www-01.ibm.com/support/knowledgecenter/#!/SSYKE2_7.0.0/welcome/welcome_javasdk_version71.html>`__ (Linux, Windows, AIX, z/OS)

To verify that the installation is correct please open a terminal/shell/command line interface (cmd.exe, sh/bash/zsh) and check, that the java command is executable:

::

    $ java -version
    java version 1.7.0_65
    OpenJDK Runtime Environment (IcedTea 2.5.3) (7u71-2.5.3-0ubuntu0.14.04.1)
    OpenJDK 64-Bit Server VM (build 24.65-b04, mixed mode)

If the java command is not found, please make sure, that the ``<java_home>/bin`` directory is in your ``PATH`` environment variable and the ``JAVA_HOME`` environment variable is set (see troubleshooting instructions on the `Oracle Website <http://docs.oracle.com/javase/8/docs/technotes/guides/install/windows_jdk_install.html#BABGDJFH>`__).

Installation of Build Tools
~~~~~~~~~~~~~~~~~~~~~~~~~~~

On development systems developers should use a build tool to compile, test, package and deploy applications. In the Java (JVM) world the most commonly used tools are `Gradle <http://www.gradle.org>`__ and `Apache Maven <http://maven.apache.org>`__. An alternative approach is to use the built in build-management of IDEs like Eclipse, IntelliJ IDEA or Netbeans.

In the following section covers the Installation of these tools.

Gradle
^^^^^^

`Gradle <http://www.gradle.org>`__ is the newest and most flexible build tool for Java. It provides every good and detailed `documentation <http://www.gradle.org/documentation>`__ and `tutorials <http://www.gradle.org/docs/current/userguide/tutorials.html>`__ to get developers started. Official installation instructions are `here <http://www.gradle.org/docs/current/userguide/installation.html>`__.

The official way to install Gradle is to `download <http://www.gradle.org/downloads>`__ and unpack the binary ZIP file to a common directory, set the environment variable ``GRADLE_HOME`` and add the bin directory to your ``PATH`` environment variable.

Windows installation
""""""""""""""""""""

Download newest gradle-x.x.x-all.zip from http://www.gradle.org/downloads. 

Open CLI (cmd.exe)

::

    unzip ~\Download\gradle-x.x.x-all.zip -d C:\Program Files
    setx GRADLE_HOME C:\Program Files\gradle-x.x.x /M
    setx PATH %PATH%;%GRADLE_HOME%\bin /M

/M sets the value on a machine level, which means for all users. The values are stored permanently and will be available in any new cmd.exe session.

Unix (Linux / OS X / Solaris / FreeBSD) manual installation
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

Download newest ``gradle-x.x.x-all.zip`` from http://www.gradle.org/downloads.

Open a shell:

::

    $ sudo unzip ~/gradle-x.x.x-all.zip -d /usr/share/
    $ sudo ln -s /usr/share/gradle-x.x.x /usr/share/gradle

Open ``~/.profile`` (single user) or ``/etc/profile`` (all users) and add the following lines:

::

    export GRADLE_HOME=/usr/share/gradle
    export PATH=$PATH:$GRADLE_HOME/bin

Unix (Linux / OS X / Solaris / FreeBSD) installation using gvm
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

An alternative and simpler option to install gradle for a single user is to use the `Groovy enVironment Manager (gvm) <http://gvmtool.net>`__ to install and update Gradle. You need the commands/packages curl and unzip to be installed on your system.

Open shell:

::

    $ curl -s get.gvmtool.net | bash
    … follow instructions
    $ gvm install gradle

See gvm help to get more infos about other options of gvm, like updating or switching between different versions.

Verify installation
"""""""""""""""""""

Open a new shell or cmd.exe session and test if gradle is available:

::

    $ gradle -v
    ------------------------------------------------------------
    Gradle 2.2.1
    ------------------------------------------------------------

    Build time:   2014-11-24 09:45:35 UTC
    Build number: none
    Revision:     6fcb59c06f43a4e6b1bcb401f7686a8601a1fb4a

    Groovy:       2.3.6
    Ant:          Apache Ant(TM) version 1.9.3 compiled on December 23 2013
    JVM:          1.7.0_65 (Oracle Corporation 24.65-b04)
    OS:           Linux 3.13.0-34-generic amd64

Apache Maven
^^^^^^^^^^^^

`Apache Maven <http://maven.apache.org>`__ is a very common build tool in the Java/JVM world and is very well known for its dependency management and its `central artifact repository
(mavencentral) <http://search.maven.org>`__. Find the documentation and tutorials on the `main page <http://maven.apache.org>`__. Installation instructions and downloads are `here <http://maven.apache.org/download.cgi>`__.

The official way to install Maven is to `download <http://maven.apache.org/download.cgi>`__ and unpack the binary ZIP file to a common directory, set the environment variable ``M2_HOME`` and add the bin directory to your ``PATH`` environment variable.

Windows installation
""""""""""""""""""""

Follow process in the `install instructions <http://books.sonatype.com/mvnex-book/reference/installation-sect-maven-install.html>`__.

Unix (Linux / OS X / Solaris / FreeBSD) manual installation
"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

Download newest apache-maven-x.x.x-bin.zip from http://maven.apache.org/download.html.

Open shell:

::

    $ sudo unzip ~/apache-maven-x.x.x-bin.zip -d /usr/share/
    $ sudo ln -s apache-maven-x.x.x /usr/share/maven

Open ``~/.profile`` (single user) or ``/etc/profile`` (all users) and add the following lines:

::

    export M2_HOME=/usr/share/maven
    export PATH=$PATH:$M2_HOME/bin

Unix (Linux / OS X / Solaris / FreeBSD) installation using package manager
""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""

An alternative option to install maven is to use the package manager of the unix system.

-  on DEB based systems (Debian,Ubuntu,...) ``$ sudo apt-get install maven`` (this is a quite outdated version 3.0.x)
-  on RPM based systems (RedHat,CentOS,Fedora,...) exists no official package (use above manual instructions).
-  on OS X you can install Maven using a packet manager for OS X like Homebrew or MacPorts. Because the packages are usually compiled during installation you need to install Xcode beforehand. This is recommended especially, if you already have Xcode installed or you would like to install also other common unix packages.
   
Homebrew (http://brew.sh):
	
::
    ``$ brew install maven``
	
MacPorts (http://www.macports.org/install.php):
	
::
    ``$ port install maven2``

Verify installation
*******************

Open a new shell or cmd.exe session and test if maven is available:

::

    $ mvn -version
    Apache Maven 3.2.3 (33f8c3e1027c3ddde99d3cdebad2656a31e8fdf4; 2014-08-11T22:58:10+02:00)
    Maven home: /usr/local/Cellar/maven/3.2.3/libexec
    Java version: 1.8.0_20, vendor: Oracle Corporation
    Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0_20.jdk/Contents/Home/jre
    Default locale: en_US, platform encoding: UTF-8
    OS name: mac os x, version: 10.10.2, arch: x86_64, family: mac

Integraded Development Environments (IDE)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

To install your IDE please check the webpage of your prefered IDE
product:

-  `Eclipse <http://eclipse.org>`__
-  `IntelliJ IDEA <https://www.jetbrains.com/idea/>`__
-  `Netbeans <https://netbeans.org>`__

These IDEs typically integrate well with Gradle and Apache Maven using plugins. Alternatively you have to copy the KIARA libraries manually to the library folder of your project and add them to your classpath.

Installation of the kiaragen tool
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The kiaragen tool is part of the KIARA components available on Maven Central. Depending on your build tool kiaragen can be easily integrated or it can be called with a shell/batch script.

If you are using Maven or an IDE you can download an executable jar file of kiaragen from the `ga\|1\|g:org.fiware.kiara\|KIARA Maven-Central <http://search.maven.org/#search>`__ repository, or you
can find it in a standalone distribution available online.

On Windows:
"""""""""""

-  There are two ways of obtaining the kiaragen software:
-  Download the file ``kiaragen-x.x.x-cli.jar`` from Maven Central and place it into a directory (e.g. subdirectory kiaragen).
-  To make the execution simpler you can also download the kiaragen.bat script from the kiaragen project (https://github.com/FIWARE-Middleware/kiaragen) and copy it into the scripts directory (create if not not created yet).

    Please take into account that the script will look for the ``kiaragen-x.x.x-cli.jar`` file inside the kiaragen subdirectory.

-  Now the tool can be called using: kiaragen.bat when the scripts folder in the installation dir is in the execute path or with a relative path ``./scripts/kiaragen.bat`` for project local installations.

On Linux / OS X:
""""""""""""""""

-  Download the file ``kiaragen-x.x.x-cli.jar`` from Maven Central
-  Place it in a directory of your shells execute path (e.g. ``/usr/local/bin``). Alternatively you can also add it to your project dir and call it with a relative path (./scripts/kiaragen.sh).
-  To make the execution simpler you can also download the kiaragen.sh script from the kiaragen project (https://github.com/FIWARE-Middleware/kiaragen) and copy it into the scripts directory.

	Please take into account that the script will look for the ``kiaragen-x.x.x-cli.jar`` file inside the kiaragen subdirectory.

-  Now the tool can be called using: kiaragen.sh when the scripts folder in the installation dir is in the execute path or with a relative path ``./scripts/kiaragen.sh`` for project local installations executable flag is lost while downloading, you can set it again using ``chmod a+x kiaragen``

KIARA components
~~~~~~~~~~~~~~~~

The KIARA components (libraries) are usually delivered together with the the developed application and do not have to be installed separately.

Setting up the development environment
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

In this section it is explained how to set up your development environment and configure your project to use KIARA Advanced Middleware. We support the most common build tools for Java projects, which are:

-  Gradle
-  Apache Maven

All Java Integrated Development environments like Eclipse, InteliJ IDEA, Netbeans, etc. provide support for one of these tools.

Please check the Installation Manual for instructions how to install the required plugins and import your KIARA project.

Gradle
^^^^^^

Set up the basic project structure
""""""""""""""""""""""""""""""""""

If you do not yet have a project you can setup the basic structure using the gradle init plugin:

::

    $ mkdir calculator 
    $ cd calculator
    $ gradle init --type java-library

This will create a basic directory structure for your source and test code and create a commented ``build.gradle`` file for a Java application.

Additionally the gradle wrapper is set up, which allows developers to execute gradle tasks without installing the gradle tool globally.

Configure your Gradle project to use KIARA
""""""""""""""""""""""""""""""""""""""""""

To use KIARA in your project you have to extend your build.gradle file:

::

    apply plugin: 'java'

    sourceCompatibility = 1.7
    version = '1.0'

    // In this section you declare where to find the dependencies of your project
    repositories {
      mavenCentral()
    }

    // In this section declare the dependencies for your production and test code
    dependencies {
        compile group: 'org.fiware.kiara', name: 'KIARA', version: '0.4.0'
        compile group: 'org.slf4j', name: 'slf4j-api', version: '1.7.7'
        testCompile group: 'junit', name: 'junit', version: '4.11'
    }

The KIARA artefacts are available on the Maven Central repository. So you have to make sure, \`mavenCentral() is part of your repositories section.

To include the KIARA artefacts you have to add the ``kiara`` main library to the dependencies section. All the depending libraries will be added automatically to your project.

The following is a typical file structure for a gradle project using KIARA:

::

    .
    ├── build                                       // generated files
    │   ├── classes                                 // compiled classes
    │   │   └── main
    │   │       └── com
    │   │           └── example
    │   │               ├── Calculator.class
    │   │               ├── CalculatorAsync.class
    │   │               ├── CalculatorClient.class
    │   │               ├── CalculatorProcess.class
    │   │               ├── CalculatorProxy.class
    │   │               ├── CalculatorServant.class
    │   │               ├── CalculatorServantExample.class
    │   │               ├── ClientExample.class
    │   │               ├── IDLText.class
    │   │               └── ServerExample.class
    │   ├── generated-src                           // generated support classes 
    │   │   └── kiara
    │   │       └── com
    │   │           └── example
    │   │               ├── Calculator.java
    │   │               ├── CalculatorAsync.java
    │   │               ├── CalculatorClient.java
    │   │               ├── CalculatorProcess.java
    │   │               ├── CalculatorProxy.java
    │   │               └── CalculatorServant.java
    │   └── libs                                   
    │       └── Calculator-1.0.jar                 // packaged application
    ├── build.gradle                               // gradle build file
    ├── gradle                                     
    │   └── wrapper                                // gradle wrapper files
    │       └── ...
    ├── gradlew                                    // gradle wrapper unix script
    ├── gradlew.bat                                // gradle wrapper windows script
    ├── settings.gradle   
    └── src                                        // source files
        ├── main
        │   ├── idl                                // IDL definitions for KIARA
        │   │   └── com  
        │   │       └── example
        │   │           └── Calculator.idl  
        │   └── java                               // application code
        │       └── com
        │           └── example
        │               ├── ClientExample.java               // client start code
        │               ├── ServerExample.java               // server start code
        │               └── CalculatorServantExample.java    // servant impl.
        └── test
            └── java

| Some basic gradle tasks:
| ``./gradlew build`` → builds all classes and run tests
| ``./gradlew jar`` → creates the application jar
| ``./gradlew clean`` → cleans up your project
| ``./gradlew tasks`` → shows all available tasks

Apache Maven
^^^^^^^^^^^^

Set up the basic project structure
""""""""""""""""""""""""""""""""""

If you do not yet have a maven project you can setup the basic structure using the archetype plugin:

::

    $ mvn archetype:generate \
     -DgroupId=mw.kiara \
     -DartifactId=calculator \
     -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

This will create a basic directory structure for your source and test code and create a commented ``pom.xml`` file for a Java application.

.. raw:: mediawiki

   {{TOCright}}

Sanity Check Procedures
-----------------------

End to End testing
~~~~~~~~~~~~~~~~~~

To verify your development environment you can download and run the KIARA Calculator example application.

Download the example application from `Github <https://github.com/FIWARE-Middleware/Examples>`__. You can clone it using git or download the ZIP archive and unzip it to an empty directory.

::

    $ git clone https://github.com/FIWARE-Middleware/Examples.git KiaraCalculator
    $ cd KiaraCalculator

**Build the application**

::

    $ gradle build
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

    Total time: 1.793 secs

**Run the Server**

::

    $ gradle runServer
    :compileJava UP-TO-DATE
    :processResources UP-TO-DATE
    :classes UP-TO-DATE
    :runServer
    CalculatorServerExample
    Apr 15, 2015 6:00:32 PM io.netty.util.internal.logging.Slf4JLogger info
    INFO: [id: 0xbfb04d67] REGISTERED
    Apr 15, 2015 6:00:32 PM io.netty.util.internal.logging.Slf4JLogger info
    INFO: [id: 0xbfb04d67] BIND(/0.0.0.0:9090)
    Apr 15, 2015 6:00:32 PM io.netty.util.internal.logging.Slf4JLogger info
    INFO: [id: 0xbfb04d67, /0:0:0:0:0:0:0:0:9090] ACTIVE
    > Building 75% > :runServer

**Open an new terminal window and run the Client**

::

    $ cd KiaraCalculator
    $ gradle runClient
    :compileJava UP-TO-DATE
    :processResources UP-TO-DATE
    :classes UP-TO-DATE
    :runClient
    CalculatorClientExample

    10 + 5 = 15

    Apr 15, 2015 5:54:06 PM org.fiware.kiara.Kiara shutdown
    INFO: shutdown 2 services
    Apr 15, 2015 5:54:06 PM org.fiware.kiara.Kiara shutdown
    INFO: shutdown org.fiware.kiara.netty.NettyTransportFactory$1@880ec60
    Apr 15, 2015 5:54:11 PM org.fiware.kiara.Kiara shutdown
    INFO: shutdown org.fiware.kiara.transport.impl.Global$1@3f3afe78

    BUILD SUCCESSFUL

    Total time: 12.76 secs

The Client task should terminate with BUILD SUCCESSFUL and the Calculation should show the correct result.

List of Running Processes
~~~~~~~~~~~~~~~~~~~~~~~~~

KIARA Advanced Middleware itself do not install any kind of daemon or service. There are no running processes, but libraries to link to your applications.

Network interfaces Up & Open
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The KIARA Middleware itself does not open or provide services, therefore has no open Ports or Interfaces. Applications using KIARA can open any ports or interfaces and firewalls have to be configured accordingly.

The provided TestServer is opening and listening by default on Port 9090.

Databases
~~~~~~~~~

	N/A

Diagnosis Procedures
--------------------

Resource availability
~~~~~~~~~~~~~~~~~~~~~

This middleware requires very few resources, any typical PC should be enough to run the regular examples.

Remote Service Access
~~~~~~~~~~~~~~~~~~~~~

	N/A

Resource consumption
~~~~~~~~~~~~~~~~~~~~

Depends on your application, it can be as low of 256 Kbytes of heap space and almost zero cpu use. The amount of RAM depends on your data types size and the different persistence options, please read the user manual for more information.

I/O flows
~~~~~~~~~

	N/A
