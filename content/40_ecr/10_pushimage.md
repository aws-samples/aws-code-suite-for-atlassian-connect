+++
title = "Push your image"
chapter = false
weight = 10
+++

Before we can deploy our application to an orchestrator, we need to push our images to [Amazon Elastic Container Registry (ECR)](https://aws.amazon.com/ecr/). Let's log into your Amazon ECR registry using the helper provided by the AWS CLI.

```bash
eval $(aws ecr get-login --no-include-email)
```

Use the AWS CLI to get information about the Amazon ECR repository that was created.

```bash
aws ecr describe-repositories --repository-name atlassian-connect
```

{{% notice info %}}
The result should look like what's below:
{{% /notice %}}

<pre>
{
    "repositories": [
        {
            "registryId": "123456789012",
            "repositoryName": "atlassian-connect",
            "repositoryArn": "arn:aws:ecr:us-west-2:123456789012:repository/atlassian-connect",
            "createdAt": 1533757748.0,
            "repositoryUri": "123456789012.dkr.ecr.us-west-2.amazonaws.com/atlassian-connect"
        }
    ]
}
</pre>

Verify that your Docker images exist by running the docker images command.

```bash
docker images
```

{{% notice info %}}
The result should look like what's below:
{{% /notice %}}

<pre>
REPOSITORY                              TAG                 IMAGE ID            CREATED             SIZE
30_containerize-app_atlassian-connect   latest              59bc69dee955        10 minutes ago      283MB
maven                                   3.6.0-jdk-8         5f598a546cec        2 weeks ago         634MB
openjdk                                 8-slim              ca76a0748b8a        2 weeks ago         243MB
</pre>

Tag the local docker images with the locations of the remote ECR repositories we created using our **CloudFormation** template.

```bash
docker tag 30_containerize-app_atlassian-connect:latest $(aws ecr describe-repositories --repository-name atlassian-connect --query=repositories[0].repositoryUri --output=text):latest
```

Once the images have been tagged, push them to the remote repository.

```bash
docker push $(aws ecr describe-repositories --repository-name atlassian-connect --query=repositories[0].repositoryUri --output=text):latest
```

{{% notice info %}}
You should see the Docker images being pushed with an output similar to this:
{{% /notice %}}

<pre>
The push refers to repository [123456789012.dkr.ecr.us-west-2.amazonaws.com/atlassian-connect]
f498ac9136cb: Pushed
a3048adbec06: Pushed
9d5ebd9c1415: Pushed
85858bc611de: Pushed
073477592611: Pushed
6744ca1b1190: Pushed
latest: digest: sha256:ca39b6107978303706aac0f53120879afcd0d4b040ead7f19e8581b81c19ecea size: 3243
</pre>

With the images pushed to Amazon ECR we are ready to deploy them to our orchestrator.