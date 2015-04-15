KIARA Installation and Admin Guide
======================================================

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

## System Requirements
This  section describes the basic requirements of KIARA Advanced Middleware and how to install them.

### Hardware Requirements
The hardware requirements depend on the application to be developed. Any hardware running Java JRE/JDK 7 or later is supported.

This [Oracle JDK Webpage](http://docs.oracle.com/javase/8/docs/technotes/guides/install/windows_system_requirements.html) provides specific minimal hardware requirements (Disk, Memory, CPU).

### Software Requirements
Runtime Systems:

* Any Java SE JRE 7 or later distribution (OpenJDK, Oracle Java SE or IBM Java SDK)

Development Systems:
* Any Java SE JDK 7 or later distribution (OpenJDK, Oracle Java SE or IBM Java SDK)
* Build Tools (gradle or maven) and/or IDE (Eclipse, IntelliJ IDEA, Netbeans, ...)

### Operating System support:
Any Operating system running Java JRE/JDK 7 or later is supported. 

KIARA is tested on Windows (7), Linux (Ubuntu 14.04/Fedora 19) and OS X (10.9/10.10).

## Installation of the Java JDK
Please follow the installation instructions of the respective Java distribution:
* [OpenJDK](http://openjdk.java.net/install/) (Default on most Linux-Systems)
* [Oracle Java SE JDK](http://docs.oracle.com/javase/8/docs/technotes/guides/install) (Linux, Windows, OS X, Solaris)
* [IBM Java SE JDK](http://www-01.ibm.com/support/knowledgecenter/#!/SSYKE2_7.0.0/welcome/welcome_javasdk_version71.html) (Linux, Windows, AIX, z/OS) 

To verify that the installation is correct please open a terminal/shell/command line interface (cmd.exe, sh/bash/zsh) and check, that the java command is executable:
```
$ java -version
java version "1.7.0_65"
OpenJDK Runtime Environment (IcedTea 2.5.3) (7u71-2.5.3-0ubuntu0.14.04.1)
OpenJDK 64-Bit Server VM (build 24.65-b04, mixed mode)
```
If the java command is not found, please make sure, that the `<java_home>/bin` directory is in your `PATH` environment variable and the `JAVA_HOME` environment variable is set (see troubleshooting instructions on the [Oracle Website](http://docs.oracle.com/javase/8/docs/technotes/guides/install/windows_jdk_install.html#BABGDJFH)).

## Installation of Build Tools
On development systems developers should use a build tool to compile, test, package and deploy applications. In the Java (JVM) world the most commonly used tools are [Gradle](http://www.gradle.org) and [Apache Maven](http://maven.apache.org). An alternative approach is to use the built in build-management of IDEs like Eclipse, IntelliJ IDEA or Netbeans.

In the following section covers the Installation of these tools.

### Gradle
[Gradle](http://www.gradle.org) is the newest and most flexible build tool for Java. It provides every good and detailed [documentation](http://www.gradle.org/documentation) and [tutorials](http://www.gradle.org/docs/current/userguide/tutorials.html) to get developers started. Official installation instructions are [here](http://www.gradle.org/docs/current/userguide/installation.html).

The official way to install Gradle is to [download](http://www.gradle.org/downloads) and unpack the binary ZIP file to a common directory, set the environment variable `GRADLE_HOME` and add the bin directory to your `PATH` environment variable.

#### Windows installation
Download newest gradle-x.x.x-all.zip from http://www.gradle.org/downloads.
Open CLI (cmd.exe)
```
unzip ~\Download\gradle-x.x.x-all.zip -d "C:\Program Files"
setx GRADLE_HOME "C:\Program Files\gradle-x.x.x" /M
setx PATH "%PATH%;%GRADLE_HOME%\bin" /M
```
/M sets the value on a machine level, which means for all users.
The values are stored permanently and will be available in any new cmd.exe session.

#### Unix (Linux / OS X / Solaris / FreeBSD) manual installation
Download newest `gradle-x.x.x-all.zip` from http://www.gradle.org/downloads.

Open a shell:
```
$ sudo unzip ~/gradle-x.x.x-all.zip -d /usr/share/
$ sudo ln -s /usr/share/gradle-x.x.x /usr/share/gradle
```

Open `~/.profile` (single user) or `/etc/profile` (all users) and add the following lines:
``` bash
export GRADLE_HOME=/usr/share/gradle
export PATH=$PATH:$GRADLE_HOME/bin
```

#### Unix (Linux / OS X / Solaris / FreeBSD) installation using gvm
An alternative and simpler option to install gradle for a single user is to use the [Groovy enVironment Manager (gvm)](http://gvmtool.net) to install and update Gradle. You need the commands/packages curl and unzip to be installed on your system.

Open shell:
```
$ curl -s get.gvmtool.net | bash
… follow instructions
$ gvm install gradle
```
See gvm help to get more infos about other options of gvm, like updating or switching between different versions. 


#### Verify installation
Open a new shell or cmd.exe session and test if gradle is available:
```
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
```

### Apache Maven
[Apache Maven](http://maven.apache.org) is a very common build tool in the Java/JVM world and is very well known for its dependency management  and its [central artifact repository (mavencentral)](http://search.maven.org). Find the documentation and tutorials on the [main page](http://maven.apache.org). Installation instructions and downloads are [here](http://maven.apache.org/download.cgi).

The official way to install Maven is to [download](http://maven.apache.org/download.cgi) and unpack the binary ZIP file to a common directory, set the environment variable `M2_HOME` and add the bin directory to your `PATH` environment variable.
The official way to install Maven is to download and unpack the binary ZIP file to a common directory, set the environment variable `M2_HOME` and add the bin directory to your `PATH` environment variable.

#### Windows
Follow process in the [install instructions](http://books.sonatype.com/mvnex-book/reference/installation-sect-maven-install.html).

#### Unix (Linux / OS X / Solaris / FreeBSD) manual installation
Download newest apache-maven-x.x.x-bin.zip from <http://maven.apache.org/download.html>.
Open shell:
```
$ sudo unzip ~/apache-maven-x.x.x-bin.zip -d /usr/share/
$ sudo ln -s apache-maven-x.x.x /usr/share/maven
```

Open `~/.profile` (single user) or `/etc/profile` (all users) and add the following lines:
```
export M2_HOME=/usr/share/maven
export PATH=$PATH:$M2_HOME/bin
```

#### Unix (Linux / OS X / Solaris / FreeBSD) installation using package manager
An alternative option to install maven is to use the package manager of the unix system. 

* on DEB based systems (Debian,Ubuntu,...) 
  `$ sudo apt-get install maven` (this is a quite outdated version 3.0.x)
* on RPM based systems (RedHat,CentOS,Fedora,...) exists no official package (use above manual instructions).
* on OS X you can install Maven using a packet manager for OS X like Homebrew or MacPorts.
  Because the packages are usually compiled during installation you need to install Xcode beforehand. This is recommended especially, if you already have Xcode installed or you would like to install also other common unix packages. 
  Homebrew (<http://brew.sh>): 
  `$ brew install maven`
  MacPorts (<http://www.macports.org/install.php>):
  `$ port install maven2`

##### Verify installation
Open a new shell or cmd.exe session and test if maven is available:
```
$ mvn -version
Apache Maven 3.2.3 (33f8c3e1027c3ddde99d3cdebad2656a31e8fdf4; 2014-08-11T22:58:10+02:00)
Maven home: /usr/local/Cellar/maven/3.2.3/libexec
Java version: 1.8.0_20, vendor: Oracle Corporation
Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0_20.jdk/Contents/Home/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "10.10.2", arch: "x86_64", family: "mac"
```

### Integraded Development Environments (IDE)
To install your IDE please check the webpage of your prefered IDE product:

* [Eclipse](http://eclipse.org)
* [IntelliJ IDEA](https://www.jetbrains.com/idea/)
* [Netbeans](https://netbeans.org)

These IDEs typically integrate well with Gradle and Apache Maven using plugins. Alternatively you have to copy the KIARA libraries manually to the library folder of your project and add them to your classpath.

### Installation of the kiaragen tool
The kiaragen tool is part of the KIARA components available on Maven Central. Depending on your build tool kiaragen can be easily integrated or it can be called with a shell/batch script.

If you are using Maven or an IDE you can download an executable jar file of kiaragen from the [KIARA Maven-Central](http://search.maven.org/#search|ga|1|g:org.fiware.kiara) repository, or you can find it in a standalone distribution available online.

#### On Windows:
* Download the file kiaragen-x.x.x-capsule.jar from Maven Central
* Rename it to kiaragen-x.x.x.jar and place it in a directory in the windows 
  execute path or place it to your project directory (e.g. subdirectory lib).
* To make the execution simpler you can also download the kiaragen.bat script 
  from the kiaragen project (https://github.com/FIWARE-Middleware/kiaragen) and copy it into the scripts directory.
* Now the tool can be called using: kiaragen.bat when the scripts folder in 
  the installation dir is in the execute path or with a relative path `./scripts/kiaragen.bat` for project local installations

#### On Linux / OS X:
* Download the file `kiaragen-x.x.x-capsule.jar` from Maven Central
* Rename it to `kiaragen-x.x.x.jar` and place it in a directory of your shells
  execute path (e.g. `/usr/local/bin`). Alternatively you can also add it to your project dir and call it with a relative path (./scripts/kiaragen.sh).
* To make the execution simpler you can also download the kiaragen.sh script
  from the kiaragen project (https://github.com/FIWARE-Middleware/kiaragen) and copy it into the scripts directory.
* Now the tool can be called using: kiaragen.sh when the scripts folder in the
  installation dir is in the execute path or with a relative path `./scripts/kiaragen.sh` for project local installations executable flag is lost while downloading, you can set it again using `chmod a+x kiaragen`

## KIARA components

The KIARA components (libraries) are usually delivered together with the the developed application and do not have to be installed separately. 

## Setting up the development environment
In this section it is explained how to set up your development environment and configure your project to use KIARA Advanced Middleware. We support the most common build tools for Java projects, which are:
* Gradle 
* Apache Maven 

All Java Integrated Development environments like Eclipse, InteliJ IDEA, Netbeans, etc. provide support for one of these tools.
Please check the Installation Manual for instructions how to install the required plugins and import your KIARA project.

### Gradle
#### Set up the basic project structure
If you do not yet have a project you can setup the basic structure using the gradle init plugin:
```shell
$ mkdir calculator 
$ cd calculator
$ gradle init --type java-library
```
This will create a basic directory structure for your source and test code and create a commented `build.gradle` file for a Java application. 

Additionally the gradle wrapper is set up, which allows developers to execute gradle tasks without installing the gradle tool globally.

#### Configure your Gradle project to use KIARA
To use KIARA in your project you have to extend your build.gradle file:
```gradle
apply plugin: 'java'

sourceCompatibility = 1.7
version = '1.0'

// In this section you declare where to find the dependencies of your project
repositories {
  mavenCentral()
}

// In this section declare the dependencies for your production and test code
dependencies {
    compile group: 'org.fiware.kiara', name: 'kiara', version: '0.2.0'
    compile group: 'org.slf4j', name: 'slf4j-api', version: '1.7.7'
    testCompile group: 'junit', name: 'junit', version: '4.11'
}
```

The KIARA artefacts are available on the Maven Central repository. So you have to make sure, `mavenCentral() is part of your repositories section.

To include the KIARA artefacts you have to add the `kiara` main library to the dependencies section. All the depending libraries will be added automatically to your project.

The following is a typical file structure for a gradle project using KIARA:
```
.
├── build                                       // generated files
│   ├── classes                                 // compiled classes
│   │   └── main
│   │       └── com
│   │           └── example
│   │               ├── CalculatorClient.class
│   │               ├── Calculator.class
│   │               ├── CalculatorAsync.class
│   │               ├── CalculatorClient.class
│   │               ├── CalculatorProxy.class
│   │               ├── CalculatorServantExample.class
│   │               ├── CalculatorServantImpl.class
│   │               └── CalculatorServer.class
│   ├── generated-src                           // generated support classes 
│   │   └── kiara
│   │       └── com
│   │           └── example
│   │               ├── Calculator.java
│   │               ├── CalculatorAsync.java
│   │               ├── CalculatorClient.java
│   │               ├── CalculatorProxy.java
│   │               ├── CalculatorServant.java
│   │               └── CalculatorServantExample.java
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
    │               ├── CalculatorClientMain.java  // client start code
    │               ├── CalculatorServerMain.java  // server start code
    │               └── CalculatorServantImpl.java // servant impl.
    └── test
        └── java
```
Some basic gradle tasks:  
`./gradlew build`     → builds all classes and run tests  
`./gradlew jar`       → creates the application jar  
`./gradlew clean`     → cleans up your project  
`./gradlew tasks`     → shows all available tasks  
 
### Maven
#### Set up the basic project structure
If you do not yet have a maven project you can setup the basic structure using the archetype plugin:
```shell
$ mvn archetype:generate \
 -DgroupId=mw.kiara \
 -DartifactId=calculator \
 -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```
This will create a basic directory structure for your source and test code and create a commented `pom.xml` file for a Java application. 

#### Configure your maven project to use KIARA
To use KIARA in your project you have to extend your project configuration file (`pom.xml`):
```pom
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>calculator</artifactId>
  <packaging>jar</packaging>
  <version>1.0</version>

  <name>KIARA Calculator Demo</name>
  <url>http://kiarademo.fiware.org</url>

  <build>
    <plugins>
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
          <version>3.1</version>
            <configuration>
              <source>1.7</source>
              <target>1.7</target>
            </configuration>
          </plugin>
      </plugins>
  </build>

  <dependencies>
    <dependency>
      <groupId>org.fiware.kiara</groupId>
      <artifactId>kiara</artifactId>
      <version>0.2.0</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```

The KIARA artefacts are available on the Maven Central Repository. 
To include the KIARA libraries you have to add the `kiara` main library to the dependencies section. All the depending libraries will be added automatically to your project.  

The following is a typical file structure for a maven project using KIARA:
```tree
.
├── pom.xml                                   // maven project config (pom.xml)
├── src                                       // source files
│   ├── main
│   │   ├── idl                               // IDL definitions for KIARA
│   │   │   └── CurrencyConverter.idl
│   │   └── java                              // application code
│   │       └── com
│   │           └── example
│   │               ├── CalculatorClientMain.java  // client start code
│   │               ├── CalculatorServerMain.java  // server start code
│   │               └── CalculatorServantImpl.java // servant impl.
│   └── test
│       └── java
└── target                                    // generated files
    ├── Calculator-1.0.jar                    // packaged application
    ├── classes                               // compiled classes
    │   └── com
    │       └── example
    │           ├── CalculatorClientMain.class
    │           ├── Calculator.class
    │           ├── CalculatorAsync.class
    │           ├── CalculatorClient.class
    │           ├── CalculatorProxy.class
    │           ├── CalculatorServantExample.class
    │           ├── CalculatorServantImpl.class
    │           └── CalculatorServerMain.class
    ├── generated-src                  // generated support classes of kiaragen
    │       └── com
    │           └── example
    │               ├── Calculator.java
    │               ├── CalculatorAsync.java
    │               ├── CalculatorClient.java
    │               ├── CalculatorProxy.java
    │               ├── CalculatorServant.java
    │               └── CalculatorServantExample.java
    ├── maven-archiver
    │   └── pom.properties
    ├── maven-status
    │   └── maven-compiler-plugin
    └── test-classes
```

Some basic mvn goals:  
`mvn compile` → builds all classes  
`mvn test`    → builds all classes and run tests  
`mvn package` → creates the application package  
`mvn clean`   → cleans up your project  


