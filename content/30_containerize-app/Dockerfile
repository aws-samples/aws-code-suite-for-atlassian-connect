FROM maven:3.6.0-jdk-8 AS build

# set working directory
WORKDIR /usr/app

# copy pom.xml
COPY ./app/pom.xml /usr/app/pom.xml

# install the dependencies for caching
RUN mvn -T 4C -T 4 dependency:go-offline

# copy the application code
COPY ./app /usr/app

# package the application
RUN mvn clean package -T 4C -T 2 -Dmaven.test.skip=true

# create our Atlassian Connect Add-on application server
FROM openjdk:8-slim AS application

# copy and deploy the war file from build layer to application layer
COPY --from=build /usr/app/target/jira-data-provider-sample-addon-1.0-SNAPSHOT.jar /usr/app/app.jar

#expose application port
EXPOSE 8080

# start application
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]
