+++
title = "Installing Java 8 and Maven"
chapter = false
weight = 10
+++

We will need to compile our [Spring Boot](https://spring.io/projects/spring-boot) app as part of this workshop. To do so, we have to upgrade our version of Java from the default Java 7 package that ships with Cloud9 to Java 8. We will also need to install [Apache Maven](https://maven.apache.org/).

## Install Java 8

```bash
sudo yum install -y java-1.8.0-openjdk-devel
```

{{% notice info %}}
The result should look like what's below:
{{% /notice %}}

<pre>
Loaded plugins: priorities, update-motd, upgrade-helper
1060 packages excluded due to repository priority protections
Resolving Dependencies

Install  1 Package (+3 Dependent packages)

Installed:
  java-1.8.0-openjdk-devel.x86_64 1:1.8.0.191.b12-0.42.amzn1

Dependency Installed:
  java-1.8.0-openjdk.x86_64 1:1.8.0.191.b12-0.42.amzn1
  java-1.8.0-openjdk-headless.x86_64 1:1.8.0.191.b12-0.42.amzn1
  lksctp-tools.x86_64 0:1.0.10-7.7.amzn1
  
Complete!
</pre>

## Configure Cloud9 to use Java 8

```bash
sudo alternatives --config java
```

{{% notice info %}}
The result should look like what's below:
{{% /notice %}}

<pre>
There are 2 programs which provide 'java'.

  Selection    Command
-----------------------------------------------
*+ 1           /usr/lib/jvm/jre-1.7.0-openjdk.x86_64/bin/java
   2           /usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/java

Enter to keep the current selection[+], or type selection number: 2
</pre>

## Remove Java 7

```bash
sudo yum remove -y java-1.7.0-openjdk-devel
```

{{% notice info %}}
The result should look like what's below:
{{% /notice %}}

<pre>
Loaded plugins: priorities, update-motd, upgrade-helper
Resolving Dependencies

Remove  1 Package

Removed:
  java-1.7.0-openjdk-devel.x86_64 1:1.7.0.201-2.6.16.0.78.amzn1
Complete!
</pre>

## Verify you are running Java 8

```bash
java -version
```

{{% notice info %}}
The result should look like what's below:
{{% /notice %}}

<pre>
openjdk version "1.8.0_191"
OpenJDK Runtime Environment (build 1.8.0_191-b12)
OpenJDK 64-Bit Server VM (build 25.191-b12, mixed mode)
</pre>

Let's make a `bin` folder where the binaries will be stored and change our directory to the new location.

```bash
mkdir ~/bin
cd ~/bin
```

Download Maven from the [Apache Maven Project](https://maven.apache.org) and add to our `$PATH`

```bash
wget -qO- http://apache.mirrors.pair.com/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.tar.gz | tar xzv -C ~/bin
echo "export PATH=~/bin/apache-maven-3.6.0/bin:${PATH}" >> ~/.bashrc
```

## Confirm Maven is installed

```bash
mvn -v
```

{{% notice info %}}
The result should look like what's below:
{{% /notice %}}

<pre>
Apache Maven 3.6.0
Maven home: /home/ec2-user/bin/apache-maven-3.6.0
Java version: 1.8.0_191, vendor: Oracle Corporation, runtime: /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.191.b12-0.42.amzn1.x86_64/jre
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "4.14.97-74.72.amzn1.x86_64", arch: "amd64", family: "unix"
</pre>
