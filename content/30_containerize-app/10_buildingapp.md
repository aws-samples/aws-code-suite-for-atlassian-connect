+++
title = "Packaging the application"
chapter = false
weight = 10
+++

This is a sample implementation of Atlassian's Connect Add-on. The application is built using the [Atlassian Connect Spring Boot framework](https://bitbucket.org/atlassian/atlassian-connect-spring-boot/src/master). It allows exercising the Jira Software Data Provider APIs to execute any requests to Jira Rest API on behalf of the Connect Add-on. In this module, you will compile the application source code. You will then build a docker image that contains the Spring Boot application as a JAR.

To get started, switch to the `app` folder within this repository.

```bash
cd ~/environment/aws-atlassian-connect/content/30_containerize-app/app/
```

Compile and package your code.

```bash
mvn clean package
```

{{% notice info %}}
If successfully built you should see the **BUILD SUCCESS** message as below.
{{% /notice %}}

<pre>
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  02:10 min
[INFO] Finished at: 2019-03-06T00:39:44Z
[INFO] ------------------------------------------------------------------------
</pre>
