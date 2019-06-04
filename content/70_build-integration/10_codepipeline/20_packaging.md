+++
title = "AWS SAM"
chapter = false
weight = 20
+++

## Packaging and deploying the function

Now that we have reviewed our function and understand what it does, we are ready to package and deploy this into our AWS account. As with our previous module on CodeCommit, we will again use SAM. First, let's begin by making sure we are working in the correct directory. Let's go to our [AWS Cloud9](https://aws.amazon.com/cloud9/) instance and from the terminal switch to this modules directory.

```bash
cd ~/environment/aws-code-suite-for-atlassian-connect/lambda-functions/get-execution-id/
```

{{% notice info %}}
This directory should contain the following contents:
{{% /notice %}}

<pre>
.
├── getExecutionId.js
├── index.js
└── package.json
</pre>

The file named `index.js` is our main Lambda function or entry point. In this example, we also have a second function named `getExecutionId.js` which we are calling from main. Next, we have `package.json` which is our manifest for external dependencies used in our function. In this case, we are using `require`. Lastly, have our SAM template as `template.yml`. We also have a hidden file named `.npmignore`. Before we start building, we will need an **S3 bucket** to store our artifacts. Since we already completed this using the **AWS CLI** from our **Cloud9** instance by calling [s3api](https://docs.aws.amazon.com/cli/latest/reference/s3api/) in the previous module, we will reference that same bucket. Now let's build & package our function by running both commands: [sam build](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-cli-command-reference-sam-build.html) and [sam package](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/sam-cli-command-reference-sam-package.html). 

```bash
sam build && sam package --output-template-file package.yml --s3-bucket my-bucket
```

{{% notice note %}}
Remember to replace `my-bucket` with the name of the bucket you created before running the command above.
{{% /notice %}}

{{% notice info %}}
Upon successful completion, you will see the following results:
{{% /notice %}}

<pre>
2019-06-04 18:30:41 Building resource 'LambdaFunction'
2019-06-04 18:30:41 Running NodejsNpmBuilder:NpmPack
2019-06-04 18:30:41 Running NodejsNpmBuilder:CopyNpmrc
2019-06-04 18:30:41 Running NodejsNpmBuilder:CopySource
2019-06-04 18:30:41 Running NodejsNpmBuilder:NpmInstall
2019-06-04 18:30:42 Running NodejsNpmBuilder:CleanUpNpmrc

Build Succeeded

Built Artifacts  : .aws-sam/build
Built Template   : .aws-sam/build/template.yaml

Commands you can use next
=========================
[*] Invoke Function: sam local invoke
[*] Package: sam package --s3-bucket <yourbucket>
    
Uploading to 4ea9324944d790f7e927b8f0bf3fa037  2054 / 2054.0  (100.00%)
Successfully packaged artifacts and wrote output template to file package.yml.
Execute the following command to deploy the packaged template
aws cloudformation deploy --template-file /home/ec2-user/environment/jira-codesuite/lambda-functions/get-execution-id/package.yml --stack-name <YOUR STACK NAME>
</pre>

This process will create a `package.yml` template based on the contents defined in `template.yml` but will also include the artifacts of your Lambda function packaged to your S3 bucket. These will be defined in the `CodeUri` parameter as an S3 URL. Now, we are ready to deploy our package. We will run the following:

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