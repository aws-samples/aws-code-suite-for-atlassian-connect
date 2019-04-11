---
title: "AWS Code Suite integration to Atlassian Jira Software Cloud"
chapter: true
weight: 1
---

# AWS Code Suite integration to Atlassian Jira Software Cloud

## Welcome

<p style='text-align: left;'>
    In this workshop, you will learn how to customize and build your own version of the Atlassian Connect Add-on Spring Boot application. The modules contained in this workshop will provide you with step-by-step instructions for building a JAR. You will also learn how to package the application as a Docker image and deploy it to AWS Fargate. Lastly, you will also setup a complete CI/CD pipeline using AWS CodePipeline, AWS CodeCommit and AWS CodeBuild. Once built and deployed you will be able to track your entire pipeline within Atlassian Jira Software Cloud (JSWC) through our AWS Lambda based integrations which push relevant details about the status of the pipeline in near real-time.
</p>

## Sample reference architecture

<p style='text-align: left;'>
    At the conclusion of this workshop, you will end up with various AWS services provisioned in your AWS account. The following diagram illustrates some of these services and is intended as a sample reference architecture.
</p>

![Reference Architecture](/images/jira-ref-arch.png)

## Workshop flow

<p style='text-align: left;'>
    Each section or module contained in this workshop is designed to guide you through each step of the process to build the architecture referenced above. This is accomplished by using AWS Cloud 9 as our starting point along with a `git clone` of the content from our repository. Everything you need is provided to you including sample code, AWS CloudFormation templates, and detailed instructions. We will be using the AWS CLI from our Cloud9 instance to deploy the CloudFormation templates and build out our environment. The diagram below illustrates this workflow.
</p>

![Workshop Flow](/images/workshop-flow.png)

{{% notice warning %}}
<p style='text-align: left;'>
The examples and sample code provided in this workshop are intended to be consumed as instructional content. These will help you understand how various AWS services can be architected to build a solution while demonstrating best practices along the way. These examples are not intended for use in production environments.
</p>
{{% /notice %}}