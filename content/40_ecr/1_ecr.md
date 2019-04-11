+++
title = "Creating the registry"
chapter = false
weight = 1
+++

In this module, we're going to take our already containerized application and push this to [Amazon Elastic Container Registry (ECR)](https://aws.amazon.com/ecr/).

{{% notice tip %}}
[Amazon Elastic Container Registry (ECR)](https://aws.amazon.com/ecr/) is a fully-managed Docker container registry that makes it easy for developers to store, manage, and deploy Docker container images. Amazon ECR is integrated with [Amazon Elastic Container Service (ECS)](https://aws.amazon.com/ecs), simplifying your development to production workflow.
{{% /notice %}}

## Deploy ECR Repositories

We will first deploy our CloudFormation template which provisions our ECR Repositories. But first, let's make sure we are in the correct directory by running:

```bash
cd ~/environment/aws-atlassian-connect/content/40_ecr/
```

Next, let's deploy the CloudFormation template using the **AWS CLI** tool by running the following command:

```bash
aws cloudformation create-stack \
  --stack-name "atlassian-connect-ecr" \
  --template-body file://ecr.yaml \
  --parameters ParameterKey=ProjectName,ParameterValue=atlassian-connect \
  --capabilities CAPABILITY_NAMED_IAM
```

Now, let's wait for the template to finish deploying and monitor its status by running the following command:

```bash
until [[ `aws cloudformation describe-stacks --stack-name "atlassian-connect-ecr" --query "Stacks[0].[StackStatus]" --output text` == "CREATE_COMPLETE" ]]; do  echo "The stack is NOT in a state of CREATE_COMPLETE at `date`";   sleep 30; done && echo "The Stack is built at `date` - Please proceed"
```
