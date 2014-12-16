<link href="https://raw.github.com/clownfart/Markdown-CSS/master/markdown.css" rel="stylesheet"></link>

# Contributing to KIARA

Here are some ways for you to get involved in the community:

* Get involved with the KIARA/FIWARE community on the FI-Ware [Jira](http://jira.fiware.org/issues/?jql=project%20%3D%20MIND%20AND%20resolution%20%3D%20Unresolved%20AND%20component%20%3D%20KIARA-J%20ORDER%20BY%20priority%20DESC) by responding to questions and joining the debate.
* Create [github](https://github.com/FIWARE-Middleware/KIARA/issues) tickets for bugs and new features and comment and vote on the ones that you are interested in.
* Github is for social coding: if you want to write code, we encourage contributions through pull requests from [forks of this repository](http://help.github.com/forking/).  If you
want to contribute code this way, please reference an existing issue
if there is one as well covering the specific issue you are addressing.  Always submit pull requests to the "develop" branch.
* Watch for upcoming articles on KIARA by subscribing to the [FIWARE blog](http://www.fi-ware.org/blog)


## How to upload to the Maven Central Repository
[The Maven Central Repository](http://search.maven.org) (aka. MavenCentral) allows global distribution of Software artifacts for any size of project. Since 2002 this repository is hosted by Sonatype using their Nexus Professional product management platform. 

For individual Projects the service is called ''OpenSource Software Repository Hosting' (OSSRH). It uses the Maven repository format to:

* deploy development version binaries (snapshots)
* stage release binaries
* promote release binaries to the public (the central repository)

The instructions how to register, set up projects and manage artifacts can be found in the [OSSHR Guide](http://central.sonatype.org/pages/ossrh-guide.html).

### Create a Sonatype Jira Account
The Sonatype Jira Account is required to get access to The Central Repository and upload artifacts.
An account can be associated with multiple projects.
To get access go to the [Sonatype Jira Server](https://issues.sonatype.org/) and [Sign Up](https://issues.sonatype.org/secure/Signup) for a new account. Afterwards send the 'username' of your new account to a project administrator for your project. He has to add you to the contributors list.

KIARA is [registered](https://issues.sonatype.org/browse/OSSRH-12836) for the group id `org.fiware.kiara`.

### Create a GPG signing keypair
All artifacts in the Central Repository have to be signed with a valid published GPG key. The following is a summary of the [OSSRH instructions](http://central.sonatype.org/pages/working-with-pgp-signatures.html). 

Install gpg (or the newer gpg2 release) with your OS package manager or download it from (http://www.gnupg.org/download).

* Linux - DEB: `sudo apt-get install gnupg2`
* Linux - RPM: `yum install gnupg2`
* OS X - Homebrew: `brew install gpg2`

Create a Private Key Pair
```    
$ gpg --gen-key
```

* give default values when asked for type, size, time of validity and confirm the key data.
* give your full name, email and (optional) comment
* give a good pass phrase for your private key

You can list all your keys (or secret keys):
```
$ gpg2 --list-keys
/home/jdoe/.gnupg/pubring.gpg
------------------------------
pub   1024D/C6EED57A 2010-01-13
uid                  John Doe (FIWARE) <jdoe@fiware.org>
sub   2048g/D704745C 2010-01-13
```
or secret keys:
```
$ gpg2 --list-secret-keys
/home/jdoe/.gnupg/secring.gpg
------------------------------
sec   1024D/C6EED57A 2010-01-13
uid                  John Doe (FIWARE) <jdoe@fiware.org>
ssb   2048g/D704745C 2010-01-13
```

To distribute your public key (make it available for other people) to verify files, it has to be published to a public key-server:

```
$ gpg2 --keyserver hkp://pool.sks-keyservers.net --send-keys C6EED57A
```
Now other people can import the public key to their key-chain:
```
$ gpg2 --keyserver hkp://pool.sks-keyservers.net --recv-keys C6EED57A
```

### Set up Gradle
Create the file `~/.gradle/gradle.properties` and add the following settings:
```
signing.keyId=              // key id of your GPG key e.g. 00B0AEF9
signing.password=           // password for your GPG secret key
signing.secretKeyRingFile=/Users/mach/.gnupg/secring.gpg
nexusUsername=              // username of your Sonatype Jira user
nexusPassword=              // password of your Sonatype Jira user
```
After this you can install the artefacts to the local repository (mavenLocal) using:
```
$ ./gradlew install
``` 
and upload release artifacts with:
```
$ ./gradlew uploadArchives
```
    
### Set up Maven

Create the file `~/.m2/settins.xml` and add the following settings:
```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <localRepository>${user.home}/.m2/repository</localRepository>
  <interactiveMode>true</interactiveMode>
  <usePluginRegistry>false</usePluginRegistry>
  <offline>false</offline>
  <servers>
    <server>
      <id>ossrh</id>
      <username>**YourNexusUsername**</username>
      <password>**YourNexusPassword**</password>
    </server>
  </servers>
  <profiles>
      <profile>
      <id>ossrh</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <gpg.executable>gpg2</gpg.executable>
        <gpg.passphrase>**YourPassphrase**</gpg.passphrase>
      </properties>
    </profile>
  </profiles>
</settings>
```    
After this you can install the artefacts locally using:
```
$ mvn install
```    
and upload the release artifacts with:
```
$ mvn upload -P release
```
