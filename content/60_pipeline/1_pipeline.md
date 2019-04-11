+++
title = "Understanding CICD"
chapter = false
weight = 1
+++

## Introduction

We will use [AWS CloudFormation](https://aws.amazon.com/cloudformation) to create our environment and provision various AWS resources. We will use  [AWS CodeCommit](https://aws.amazon.com/codecommit) as our fully managed [source control](https://aws.amazon.com/devops/source-control) service,  [AWS CodePipeline](https://aws.amazon.com/codepipeline) as our [continuous integration](https://aws.amazon.com/devops/continuous-integration) and [continuous delivery](https://aws.amazon.com/devops/continuous-delivery) service, as well as [AWS CodeBuild](https://aws.amazon.com/codebuild) as our fully managed build service.

We will launch the CloudFormation template **pipeline.yaml** in order to automate much of the provisioning and configuring or our pipeline.

## AWS CodePipeline Workflow

A pipeline models our workflow from end to end. Within our pipeline we can have stages, and you can think of stages as groups of actions. An action or a plug-in is what acts upon the current revision that is moving through your pipeline. This is where the actual work happens in your pipeline. Stages can then be connected by transitions and in our console we represent these by an arrow between each stage. Our pipeline will consist of *three* stages:

![CICD](/images/cicd-01.png)

The *Source* stage monitors for changes to our source code repository. When a change is made, we will transition to the following stage. In this case, our *Build* stage. Here we will use CodeBuild to build a new docker image. The process will follow the same workflow we previously ran when we went through the *Containerize Application* module. We will build our JAR as part of a multi-stage docker build and push a new version of this image to our [Amazon Elastic Container Registry](https://aws.amazon.com/ecr). These various phases are defined within our *BuildSpec* which will be found in the *buildspec.yml* in the source code directory. A sample of this file is below:

<pre>
BuildSpec: |
  version: 0.2
  phases:
    pre_build:
      commands:
        - echo Logging in to Amazon ECR...
        - aws --version
        - $(aws ecr get-login --region $AWS_DEFAULT_REGION --no-include-email)
        - REPOSITORY_URI=$(aws ecr describe-repositories --repository-name atlassian-connect --query=repositories[0].repositoryUri --output=text)
        - COMMIT_HASH=$(echo $CODEBUILD_RESOLVED_SOURCE_VERSION | cut -c 1-7)
        - IMAGE_TAG=${COMMIT_HASH:=latest}
        - PWD=$(pwd)
    build:
      commands:
        - echo Build started on `date`
        - echo Building the Docker image...
        - cd content/30_containerize-app
        - docker build -t $REPOSITORY_URI:latest .
        - docker tag $REPOSITORY_URI:latest $REPOSITORY_URI:$IMAGE_TAG
    post_build:
      commands:
        - echo Build completed on `date`
        - echo Pushing the Docker images...
        - docker push $REPOSITORY_URI:latest
        - docker push $REPOSITORY_URI:$IMAGE_TAG
        - echo Writing image definitions file...
        - echo Source DIR ${CODEBUILD_SRC_DIR}
        - printf '[{"name":"atlassian-connect-ecs-atlassian-connect","imageUri":"%s"}]' $REPOSITORY_URI:$IMAGE_TAG > ${CODEBUILD_SRC_DIR}/imagedefinitions.json
</pre>

Once our new Docker image has been successfully built and stored in ECR, we transition to our final stage where we deploy the image to our AWS Fargate cluster. During the *Deploy* stage, we will then consume the **imagedefinitions.json** output from the *post_build* process to sping up a new container using our newly created image into our existing cluster.

