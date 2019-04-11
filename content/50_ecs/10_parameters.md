+++
title = "Setting parameters"
chapter = false
weight = 10
+++

Our CloudFormation template depends on a few parameters which will need to be either provided manually if running the template through the AWS management console, or set as environment variables which we can pass to the CLI.

Let's start with the ECR Image URI. This is the path to the image we created in the previous module and will be used by ECS to launch our container. We will define a variable called **ECRImageURI** and assign to this the value from our CLI query. To do so, run the following command:

```bash
ECRImageURI=$(aws ecr describe-repositories \
--repository-name atlassian-connect \
--query=repositories[0].repositoryUri \
--output=text):latest
```

It's a good idea to validate this worked before we continue. Let's do so by running the following:

```bash
echo $ECRImageURI
```

{{% notice info %}}
You should receive a response similar to the following output:
{{% /notice %}}

<pre>
123456789012.dkr.ecr.us-west-2.amazonaws.com/atlassian-connect:latest
</pre>

Next, let's retrieve our secrets and pass those along as environmen variables as well for use later on when we launch our template. Run the following commands:

```bash
JiraUser=$(aws secretsmanager get-secret-value \
--secret-id AtlassianAPIUser \
--query SecretString \
--output text)
```

```bash
JiraPass=$(aws secretsmanager get-secret-value \
--secret-id AtlassianAPIToken \
--query SecretString \
--output text)
```
