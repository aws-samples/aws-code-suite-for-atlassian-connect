+++
title = "Creating the pipeline"
chapter = false
weight = 10
+++

## Getting Started

### Steps to Create the Pipeline

First, let's make sure we are working in the correct directory:

```bash
cd ~/environment/aws-atlassian-connect/content/60_pipeline/
```

Next, from your Cloud9 IDE, let's launch the CloudFormation template:

```bash
aws cloudformation create-stack \
  --stack-name atlassian-connect-cicd --template-body=file://pipeline.yaml \
   --parameters ParameterKey=ProjectName,ParameterValue=atlassian-connect \
  --capabilities CAPABILITY_IAM
```

Wait for the CloudFormation template to successfully deploy.

```bash
until [[ `aws cloudformation describe-stacks \
  --stack-name "cicd" --query "Stacks[0].[StackStatus]" \
  --output text` == "CREATE_COMPLETE" ]]; \
  do  echo "The stack is NOT in a state of CREATE_COMPLETE at `date`"; \
  sleep 30; done && echo "The Stack is built at `date` - Please proceed"
```

Obtain the clone URL for our repository. We will pass the output of our CLI query to an ENVIRONMENT VARIABLE for later use.

```bash
CloneURL=$(aws codecommit get-repository \
  --repository-name atlassian-connect \
  --query 'repositoryMetadata.cloneUrlHttp' \
  --output=text)
```

### Cloning your CodeCommit Repo

When your Cloud9 environment was provisioned, it should also have automatically cloned the GitHub repository for this lab. We will want to push the contents of that repo to our CodeCommit repo. From your Cloud9 IDE change directories to your locally clone copy of the lab content:

```bash
cd ~/environment/atlassian-connect/
```

Before we are able to clone our CodeCommit repo, we will need to configure our credentials allow HTTP:

```bash
git config --global credential.helper '!aws codecommit credential-helper $@'
```

```bash
git config --global credential.UseHttpPath true
```

Next, we will add CodeCommit as a remote repo.

```bash
git remote add codecommit $CloneURL
```

We are almost ready to commit. Let's configure git with our email and name:

```bash
git config --global user.email " your_email "
```

```bash
git config --global user.name " your_name "
```

Now we are finally ready for our initial commit. Type the following:

```bash
git status
```

```bash
git add .
```

```bash
git commit -am "initial"
```

```bash
git push --force-with-lease codecommit master
```

This should create a branch called **master** in your CodeCommit repo and push the contents of our lab. You should see the following results in your Cloud9 console:

<pre>
Counting objects: 457, done.
Compressing objects: 100% (283/283), done.
Writing objects: 100% (457/457), 11.56 MiB | 15.70 MiB/s, done.
Total 457 (delta 153), reused 457 (delta 153)
To https://git-codecommit.us-west-2.amazonaws.com/v1/repos/aws_pet_store_repo
 * [new branch]      master -> master
</pre>

You can also see the same results by navigating to the [CodeCommit console](https://console.aws.amazon.com/codecommit) where you will find results similar to these:

image::cicd-04.png[cicd]

### Build Process

Remember that our pipeline has been configured to watch for any changes to CodeCommit. When a change is detected it will trigger the pipeline and the build process will commence.

You can also trigger the process by clicking the **Release change** button from the [CodePipeline console](https://console.aws.amazon.com/codepipeline)

image::cicd-05.png[cicd]

Once triggered, you should see the various stages go through the workflow from the [CodePipeline console](https://console.aws.amazon.com/codepipeline). For example:

image::cicd-06.png[cicd]

You can also view additional details for the build process by navigating to the [CodeBuild console](https://console.aws.amazon.com/codebuild) where you will find messages for the various stages defined.

image::cicd-07.png[cicd]

A complete log of the events is also detailed for you in this console.

image::cicd-08.png[cicd]

### Deploy Process

The final stage in our pipeline is to deploy the new docker image into our Fargate cluster. As part of the process, an **imagedefinitions.json** file is generated which contains the path to the newly created docker image(s) stored in ECR.

This file will then be used in Fargate's task definition to pull the **latest** image containing your recent code changes.

So, now you should be able to confirm your containers are running by navigating to your [Fargate console](https://us-west-2.console.aws.amazon.com/ecs/home?#/clusters/petstore-workshop/services/petstore/details) and see the following:

image::cicd-09.png[cicd]
