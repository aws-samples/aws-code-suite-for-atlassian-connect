+++
title = "Container Orchestration"
chapter = false
weight = 1
+++

## Introduction

In this module, we're going to deploy applications using [Amazon Elastic Container Service (Amazon ECS)](http://aws.amazon.com/ecs/) to orchestrate our containers on top of [AWS Fargate](http://aws.amazon.com/fargate/).

{{% notice tip %}}
[AWS Fargate](http://aws.amazon.com/fargate/) is a compute engine for Amazon ECS that allows you to run containers without having to manage servers or clusters. With AWS Fargate, you no longer have to provision, configure, and scale clusters of virtual machines to run containers.
{{% /notice %}}

### Getting Started with Amazon ECS using AWS Fargate

Before we get started, here are some definitions you need to understand in order to deploy your application when creating your first Amazon ECS cluster.

| Object | Cluster |
| ------ | ------- |
| Cluster | Logical grouping of tasks and services. Infrastructure may be shared between tasks and services running on the same cluster.
| Task Definition | Blueprint for our application. Defines attributes such as CPU and memory requirements, networking configuration, and container definitions.
| Container Definition | Configuration for a container to run as part of our task. Defines attributes of the container including port mappings, resources requirements, environment variables, etc.
| Service | Maintains a specified number of running simultaneous instances of a task definition in an ECS cluster.

You'll deploy a service via Amazon ECS using AWS Fargate as the launch type. The Fargate launch type allows you to run your containerized applications without the need to provision and manage the back-end infrastructure. Amazon ECS also can launch tasks and services using the EC2 launch type which runs containerized applications on Amazon EC2 instances that you manage. Amazon ECS is the orchestration service responsible for running docker containers and AWS Fargate is the underlying compute platform where the containers will run.
