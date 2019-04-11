+++
title = "Testing the application"
chapter = false
weight = 1
+++

## Introduction

Up until now, we have been going through various steps to setup our environment. Creating accounts, installing dependencies, and other necessary steps to make sure we progress through the modules without any issues. Now, we are ready to begin working on the code and the deploying the infrastructure that will support this. However, before we dive in, let's talk about the [Atlassian Connect](https://developer.atlassian.com/display/AC/Atlassian+Connect) based add-on. Specifically, some considerations you will need to take when running this in a development environment as a proof of concept.

In the [Creating your environment](https://atlassian.awsworkshop.io/20_getting-started/1_gettingstarted.html) module, you cloned the corresponding [GitHub repository](https://github.com/aws-samples/aws-code-suite-for-atlassian-connect) for this workshop to your [AWS Cloud9](https://aws.amazon.com/cloud9/) instance. In those contents, you will find some source code for an implementation of the [atlassian-connect-spring-boot](https://bitbucket.org/atlassian/atlassian-connect-spring-boot) which serves as the foundation for our add-on. that source contains a [Spring Boot](http://projects.spring.io/spring-boot/) starter for building Atlassian Connect add-ons for [JIRA Software](https://developer.atlassian.com/cloud/jira/software/). The contents can be found in your local path of `./aws-code-suite-for-atlassian-connect/content/30_containerize-app/app/src` and contain a predefined [JSON add-on descriptor](https://developer.atlassian.com/cloud/jira/platform/app-descriptor/) `atlassian-connect.json` that includes modules for [AWS CodeCommit](https://aws.amazon.com/codecommit/) and [AWS CodePipeline](https://aws.amazon.com/codepipeline/). These are mapped to the [Development Tool](https://developer.atlassian.com/cloud/jira/software/modules/development-tool/) and [Build](https://developer.atlassian.com/cloud/jira/software/modules/build/) modules, respectively. Therefore, we need not generate a fresh project using a [Maven archetype](https://maven.apache.org/guides/mini/guide-creating-archetypes.html).

### Externalized Configuration

One feature of Spring Boot is that we can [externalize configurations](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) allowing us to work on the application code in different environments. Our sample code contains a YAML properties file (`application.yml`) with a few key environment variables such as `SERVER_PORT`, `BASE_URL`, `SECURITY_LOGIN` and `SECURITY_PASSWORD`.

{{% notice info %}}
The file should look similar to this:
{{% /notice %}}

<pre>
server.port: ${SERVER_PORT:8080}

addon:
  key: jira-data-provider-aws-addon
  base-url: http://localhost:${BASE_URL:8080}

logging.level.com.atlassian.connect.spring: DEBUG

security.ignored: /jwt-myself,/oauth2-myself,/relay/**
security:
  login: ${SECURITY_LOGIN:user}
  password: ${SECURITY_PASSWORD:password}
</pre>

This will prove useful later on when we run the application locally as well as deploy this as a container.

## Preparing to run the add-on

For these steps, we are going to create an *ssh* tunnel to our Cloud9 instance and run the application locally while passing a few environment variables. Then, we will register the application in Jira.

### Networking

A critical piece of this architecture involves registerig your add-on with your Jira instance. The steps for this will be a lengthy and will involve taking some time early on to plan accordingly. Jira requires **HTTPS** for all communications between the add-on applications and your cloud tenant. This will be true even if you enable development mode. Additionally, only valid SSL certificates issues from an external Certificate Authority will be acknowledged. Otherwise, your application will fail to register. As a result, we will use a third-party service, [ngrok](https://ngrok.com) to solve for this when testing from our Cloud9 IDE. Since ngrok exposes local servers behind NATs and firewalls to the public internet over secure tunnels and routes through its cloud service which accepts traffic on a public address and relays that traffic through to the ngrok process running on your machine and then on to the local address you specified, we can work around this for the purpose of our excercise. We will install the [ngrok npm module](https://www.npmjs.com/package/ngrok) on our Cloud9 instance by running the following:

```bash
npm install ngrok -g
```

Once that installation completes successfully, we will run a second command:

```bash
ngrok http 8080
```

This will start ngrok on our Cloud9 instance and listen on port 8080 and you should see results similar to this:

<pre>
Session Status                online
Session Expires               7 hours, 57 minutes
Version                       2.3.25
Region                        United States (us)
Web Interface                 http://127.0.0.1:4040
Forwarding                    http://YOUR_NGROK_SUBDOMAIN.ngrok.io -> http://localhost:8080
Forwarding                    https://YOUR_NGROK_SUBDOMAIN.ngrok.io -> http://localhost:8080
Connections                   ttl     opn     rt1     rt5     p50     p90
                              0       0       0.00    0.00    0.00    0.00
</pre>

Take special note of the *forwarding* URLs. You will need the *HTTPS* ngrok provided for the next steps.

{{% notice warning %}}
Leave the tab running ngrok open for the duration of this workshop. Closing the tab, entering `CTRL C`, or allowing your Cloud9 instance to sleep will cause ngrok to shutdown and you will need to repeat this step. Your URL will change each time you run ngrok.
{{% /notice %}}

### Starting the application

First, let's make sure we are working in the correct directory where our `pom.xml` is located. Run the following:

```bash
cd ~/environment/aws-atlassian-connect/content/30_containerize-app/app/
```

Next, we are going to define a variable called *ngrokURL* and assign the value of our ngrok HTTPS URL from our tab where the application is running.

```bash
ngrokURL=https://<yoursubdomain>.ngrok.io
```

Next, we can copy and paste the following command to our Cloud9 terminal. This will start the add-on application and pass a few runtime arguments to set values for the externalized parameters we defined in our `application.yml` file.

```bash
mvn spring-boot:run -Drun.arguments=\
"--addon.base-url=$ngrokURL",\
"--security.login=$(aws secretsmanager get-secret-value --secret-id AtlassianAPIUser --query SecretString)",\
"--security.password=$(aws secretsmanager get-secret-value --secret-id AtlassianAPIToken --query SecretString)"
```

Once the application is up and running, you will see something similar to this:
![App Up](/images/ngrok-01.png)

{{% notice info %}}
Note that the value of our BASE_URL reflects the ngrok URL from our running instance. We are also able to preview the running application by clicking on the URL provided in the top right corner of our Cloud9 environment.
{{% /notice %}}

### Enable Development Mode

We are now ready to register the application. To do so, you will need to login to your Jira instance. This should be something similar to `https://<YOUR_ATLAS_SUBDOMAIN>.atlassian.net`. Once authenticated, follow these steps:

1. Go to Jira Settings:
![Jira settings](/images/atlassian-05.png)

2. Select Apps:
![Apps](/images/atlassian-06.png)

3. Select Manage Apps:
![Manage Apps](/images/atlassian-07.png)

4. Select Settings:
![Settings](/images/atlassian-08.png)

5. Enable Private Listings & Development Mode:
![Dev Mode](/images/atlassian-09.png)

Now we are ready to upload the app!

### Register

1. Click on Upload:
![Upload](/images/atlassian-10.png)

2. Enter BASE URL:
![URL](/images/atlassian-11.png)

3. Confirm application installation:
![Install](/images/atlassian-12.png)

Congratulations! You successfully registered the application!

### Uninstall

If at any point, you need to uninstall a registered application, you may do so by going back to the Jira Settings->Manage Apps menu and selecting the `Uninstall` option for the respective app.
![Uninstall](/images/atlassian-13.png)