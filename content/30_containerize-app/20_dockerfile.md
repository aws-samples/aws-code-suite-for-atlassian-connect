+++
title = "Building the Dockerfile"
chapter = false
weight = 20
+++

To get started, switch to the **containerize-app** folder within this repository. Then open the **Dockerfile** in your editor of choice.

```bash
cd ~/environment/aws-atlassian-connect/content/30_containerize-app/
```

## Reviewing the Dockerfile

The first `stage` of our **Dockerfile** will be to build the application. We will use the **maven:3.6.0-jdk-8** container from Docker Hub as our base image. The first `stage` of our **Dockerfile** will need to copy the code into the container, install the dependencies, then build the application using Maven.

The second `stage` of our **Dockerfile** will be to build our Atlassian Connect Add-on application server. We will need to copy the JAR file we created in the first stage to the second stage. The JAR file is built to **/usr/app/target/jira-data-provider-sample-addon-1.0-SNAPSHOT.jar** in the first stage. We will instruct **docker** to copy that JAR file to the **/usr/app/app.jar** in the new `stage`. The application will launch on boot.

Let's now review the full **Dockerfile** step-by-step and walk through the result. The first stage will be based off our **build tools** container, in this case we are using maven. This container could be a container within your organization that houses all the tools your development team needs. We alias this first stage as the `build` stage.

<pre>
FROM maven:3.6.0-jdk-8 AS build
</pre>

Next we are going to install the dependencies. We do this ahead of time so that we can leverage the Docker build cache to increase the speed of build times. To accomplish this we copy the **pom.xml** and install the dependencies. We then copy our application code and build the application. This enables developers to iterate on their code without having to install dependencies each time.

<pre>
# set working directory
WORKDIR /usr/app

# copy pom.xml
COPY ./app/pom.xml /usr/app/pom.xml

# install the dependencies for caching
RUN mvn -T 4C -T 4 dependency:go-offline
</pre>

Finally, we build the application using `maven`.

<pre>
# copy the application code
COPY ./app /usr/app

# package the application
RUN mvn clean package -T 4C -T 2 -Dmaven.test.skip=true
</pre>

Next, in the same **Dockerfile** we create a new stage called **application**. Then from the `build` stage, we copy the JAR file we compiled to the deployments folder. The JAR will launch on boot. We then expose port 8080 for our application. Lastly, we set the **ENTRYPOINT** to start the application.

<pre>
# create our Atlassian Connect Add-on application server
FROM openjdk:8-slim AS application

# copy and deploy the war file from build layer to application layer
COPY --from=build /usr/app/target/jira-data-provider-sample-addon-1.0-SNAPSHOT.jar /usr/app/app.jar

#expose application port
EXPOSE 8080

# start application
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]
</pre>

Next, we need to define how our application will run. We do this by defining the structure of our application and it's dependencies in a **docker-compose.yml** file. This file contains the complete environment required for our application.

Open the file called **docker-compose.yml** and let's review its contents
 
To start, your file should look like this:

<pre>
version: '3.4'
</pre>

The next section defines the Atlassian Connect service. We will use the **openjdk:8-slim** image available from Docker Hub. Next, we will map the application port `8080` to our machine port for easy access. Finally, we will define a few environment variables to configure our instance.

<pre>
services:
  atlassian-connect:
    build:
      context: ./
      dockerfile: Dockerfile
    ports:
      - 8080:8080
    environment:
      - 'SECURITY_LOGIN=user'
      - 'SECURITY_PASSWWORD=password'
      - 'BASE_URL=http://localhost'
      - 'SERVER_PORT=8080'
</pre>
