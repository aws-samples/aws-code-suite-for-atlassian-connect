+++
title = "AWS SAM"
chapter = false
weight = 20
+++

## Packaging and deploying the function

Now that we have reviewed our function and understand what it does, we are ready to package and deploy this into our AWS account. There are a few ways to accomplish this: (1) AWS Console, (2) AWS CloudFormation, or (3) AWS SAM. For these examples, we will use SAM.

{{% notice tip %}}
The [AWS Serverless Application Model (SAM)](https://aws.amazon.com/serverless/sam/) is an open-source framework for building serverless applications. It provides shorthand syntax to express functions, APIs, databases, and event source mappings. With just a few lines per resource, you can define the application you want and model it using YAML. During deployment, SAM transforms and expands the SAM syntax into AWS CloudFormation syntax, enabling you to build serverless applications faster.
{{% /notice %}}

Earlier in this workshop, we made sure that our Cloud9 environment was setup to use the latest version of the [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-reference.html#serverless-sam-cli). We will be running a few commands to build our Lambda source code and generate deployment artifacts. When complete, we will package these and store them in an [Amazon S3](https://aws.amazon.com/s3/) bucket and lastly deploy using [AWS CloudFormation](https://aws.amazon.com/cloudformation/).

First, let's begin by making sure we are working in the correct directory. Let's go to our [AWS Cloud9](https://aws.amazon.com/cloud9/) instance and from the terminal switch to this modules directory.

```bash
cd ~/environment/aws-code-suite-for-atlassian-connect/lambda-functions/get-commit-id/
```

{{% notice info %}}
This directory should contain the following contents:
{{% /notice %}}

<pre>
.
├── index.js
├── package.json
└── template.yml
</pre>

The file named `index.js` is our Lambda function. Next, we have `package.json` which is our manifest for external dependencies used in our function. In this case, we are using `require`. Lastly, have our SAM template as `template.yml`. We also have a hidden file named `.npmignore` which follows the same format as `.gitignore`. This file will allow us to exclude certain contents within the source code directory that we do not wish to package along with our function. For example, our SAM templates. While this does not negatively impact the functionality of our code, leaving it is not exactly a best practice. We want to keep things as clean and organized as possible. You can open these files in Cloud9 and examine them at your leisure.

Before we start building, let's create an **S3 bucket** to store our artifacts. We will do this using the **AWS CLI** from our **Cloud9** instance by calling [s3api](https://docs.aws.amazon.com/cli/latest/reference/s3api/).

```bash
aws s3api create-bucket --bucket my-bucket  --region us-west-2 --create-bucket-configuration LocationConstraint=us-west-2
```

{{% notice note %}}
Before running the command above, you will need to replace `my-bucket` with another name. Keep in mind that S3 bucket names are globally unique. If you do not choose a unique name then you will collide with an already existing bucket and the process will fail. For this workshop we are working in the us-west-2 region. However, if you opt to use a different region, also note that you will need to update this accordingly.
{{% /notice %}}

{{% notice info %}}
Upon successful creation, you will see the following response:
{{% /notice %}}

<pre>
{
    "Location": "http://my-bucket.s3.amazonaws.com/"
}
</pre>

Now that we have our S3 bucket created, we are ready to build & package our function. We will run two commands for this: [sam build](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-cli-command-reference-sam-build.html) and [sam package](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-cli-command-reference-sam-package.html). The **sam build** command iterates through the functions in your application, looks for a manifest file (such as package.json) that contains the dependencies, and automatically creates deployment artifacts that you can deploy to Lambda using the **sam package** command. The **sam package** command is an alias for [aws cloudformation package](https://docs.aws.amazon.com/cli/latest/reference/cloudformation/package.html) which packages the local artifacts (local paths) that your AWS CloudFormation template references. The command uploads local artifacts, such as source code for an AWS Lambda function to an S3 bucket.

```bash
sam build && sam package --output-template-file package.yml --s3-bucket my-bucket
```

{{% notice note %}}
Before running the command above, you will need to replace `my-bucket` with the name of the bucket you created.
{{% /notice %}}

{{% notice info %}}
Upon successful completion, you will see the following results:
{{% /notice %}}

<pre>
2019-04-06 04:59:23 Building resource 'LambdaFunction'
2019-04-06 04:59:23 Running NodejsNpmBuilder:NpmPack
2019-04-06 04:59:24 Running NodejsNpmBuilder:CopyNpmrc
2019-04-06 04:59:24 Running NodejsNpmBuilder:CopySource
2019-04-06 04:59:24 Running NodejsNpmBuilder:NpmInstall
2019-04-06 04:59:27 Running NodejsNpmBuilder:CleanUpNpmrc

Build Succeeded

Built Artifacts  : .aws-sam/build
Built Template   : .aws-sam/build/template.yaml

Commands you can use next
=========================
[*] Invoke Function: sam local invoke
[*] Package: sam package --s3-bucket <yourbucket>
    
Uploading to 9bc2fd97e797a86cba9c7d60f02dd399  1315337 / 1315337.0  (100.00%)
Successfully packaged artifacts and wrote output template to file package.yml.
Execute the following command to deploy the packaged template
aws cloudformation deploy --template-file /home/ec2-user/environment/test-sam/package.yml --stack-name <YOUR STACK NAME>
</pre>

This process will create a `package.yml` template based on the contents defined in `template.yml` but will also include the artifacts of your Lambda function packaged to your S3 bucket. These will be defined in the `CodeUri` parameter as an S3 URL. Now, we are ready to deploy our package. To do this, we will use [sam deploy](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-cli-command-reference-sam-deploy.html). **sam deploy** is an alias for [aws cloudformation deploy](http://docs.aws.amazon.com/cli/latest/reference/cloudformation/deploy/index.html) and will deploy your AWS SAM application package through a specified AWS CloudFormation template. We will run the following:

```bash
sam deploy --template-file package.yml --stack-name <STACK_NAME> --capabilities CAPABILITY_IAM
```

{{% notice note %}}
Before running the command above, you will need to replace `<STACK_NAME>` with a name for your stack.
{{% /notice %}}

{{% notice info %}}
Upon successful completion, you will see the following results:
{{% /notice %}}

<pre>
Waiting for changeset to be created..
Waiting for stack create/update to complete
Successfully created/updated stack - STACK_NAME
</pre>