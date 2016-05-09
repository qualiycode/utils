#About this Repository

This repository contains many useful utilities such as:

* CsvParser - Generic CSV parser

* CommandLineExecutor - Utility that allows shell commands execution on the local machine

* Pair - Generic pair class which contains two objects

* StringUtils - Utility that mostly used for doing operation on Strings using regular expressions

* W64Convertor - W64 encoding / decoding utility

* SftpUtils - SFTP utility class that allows SFTP Operations

* EmailClient - Email client (currently support only Gmail)

* RestClient - Generic class for working with REST API devices

* IpManager -  IPv4 & IPv6 IP manager that holds the logic of allocating IP's

* DateFormatter - Utility for converting date formats 

<br><br><br><br>
#How to add it as a Maven dependency
Since this code is not published to the central Maven repository you should use JitPack repository.

Just Add the following to your pom.xml file:

```
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
</repositories>

<dependencies>
   <dependency>
	    <groupId>com.qualiycode</groupId>
	    <artifactId>utils</artifactId>
	    <version>-SNAPSHOT</version>
   </dependency>
</dependencies>
```
