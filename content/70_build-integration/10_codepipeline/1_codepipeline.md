+++
title = "Introduction"
chapter = false
weight = 1
+++

## Store build information

The second integration point I'd like to cover is that of our [continuous delivery](https://aws.amazon.com/devops/continuous-delivery/) service. In this case, we are working with [AWS CodePipelie](https://aws.amazon.com/codecommit/) and will leverage the *jiraBuildInfoProvider* module defined in the `atlassian-connect.json` add-on descriptor located in the `./app/src/main/resources/` directory of our application.

{{% notice tip %}}
[AWS CodePipeline](https://aws.amazon.com/codepipeline/) is a fully managed [continuous delivery](https://aws.amazon.com/devops/continuous-delivery/) service that helps you automate your release pipelines for fast and reliable application and infrastructure updates. CodePipeline automates the build, test, and deploy phases of your release process every time there is a code change, based on the release model you define.
{{% /notice %}}

Our goal with this integration is to update Jira with the status of our build and deployment stages. We can accomplish this by creating a [CloudWatch Events Rule](https://docs.aws.amazon.com/AmazonCloudWatch/latest/events/Create-CloudWatch-Events-Rule.html) that triggers on an event. In this case, state changes in our pipeline that will invoke our Lambda functions. These functions will query the [AWS CodePipeline API](https://docs.aws.amazon.com/codepipeline/latest/APIReference/Welcome.html) and gather a few pieces of information to report on the status of our Build and Deploy stages for our respective commit. The data will then **POST** to our Atlassian Connect App which will in turn authenticate with our Jira Software Cloud (JSWC) tenant. The following diagram illustrates this workflow:

![CodePipeline](/images/codepipeline-01.png)