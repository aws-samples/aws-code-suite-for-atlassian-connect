+++
title = "Creating your environment"
chapter = false
weight = 1
+++

{{% notice warning %}}
You are responsible for the cost of the AWS services used while running this workshop in your AWS account.
{{% /notice %}}

In order for you to succeed in this workshop, we need you to run through a few steps to properly setup and configure your environment.

## Launch AWS Cloud9

[Provision an AWS Cloud9](https://console.aws.amazon.com/cloudformation/home?region=us-west-2#/stacks/create/review?stackName=AWSModernizationWorkshop&templateURL=https://s3-us-west-2.amazonaws.com/modernization-workshop-west-2/create-environment/templates/workshop_env_master.yaml) instance.

## Configure AWS Cloud9

Following that we'll need to turn off the Managed Temporary credentials.

The Cloud9 IDE needs to use the assigned IAM Instance profile. Open the *AWS Cloud9* menu, go to *Preferences*, go to *AWS Settings*, and disable *AWS managed temporary credentials* as depicted in the diagram here:

![Cloud9 Managed Credentials](/images/cloud9-credentials.png)

## Update and install some tools

The first step is to update the **AWS CLI**, **AWS SAM CLI**, as well as `pip` and a range of pre-installed packages.

```bash
sudo yum update -y && \
curl -O https://bootstrap.pypa.io/get-pip.py && \
python3 get-pip.py --user && \
pip3 install --upgrade --user awscli pip aws-sam-cli
```

Next, let's verify we have the correct versions by running the following command:

```bash
python3 --version && \
pip3 --version && \
aws --version && \
sam --version
```

{{% notice info %}}
The result should look like what's below:
{{% /notice %}}

<pre>
Python 3.6.8
pip 19.0.3 from /home/ec2-user/.local/lib/python3.6/site-packages/pip (python 3.6)
aws-cli/1.16.128 Python/2.7.16 Linux/4.14.104-78.84.amzn1.x86_64 botocore/1.12.118
SAM CLI, version 0.14.2
</pre>

## Configure the AWS CLI Environment

After you have the installed the latest **AWS CLI** and `pip` we need to configure our environment.

```bash
aws configure set region us-west-2
```

## Clone the source repository for this workshop

Now we want to clone the repository that contains all the content and files you need to complete this workshop.

```bash
cd ~/environment && \
git clone https://github.com/aws-samples/aws-code-suite-for-atlassian-connect.git
```
