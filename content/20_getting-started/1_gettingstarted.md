+++
title = "Creating your environment"
chapter = false
weight = 1
+++

{{% notice warning %}}
You are responsible for the cost of the AWS services used while running this workshop in your AWS account.
{{% /notice %}}

In order for you to succeed in this workshop, you will need to run through a few steps in order to properly setup and configure your environment. These steps will include provisioning some services, installing some tools, and downloading some dependencies as well. We will begin with [AWS Cloud9](https://aws.amazon.com/cloud9/). Technically, you should be able to complete many of the steps in these modules if you have a properly configured terminal. However, in order to avoid the *"works on my machine"* response you've surely experienced at some point in your career, I strongly encourage you to proceed with launching Cloud9.

{{% notice tip %}}
[AWS Cloud9](https://aws.amazon.com/cloud9/) is a cloud-based integrated development environment (IDE) that lets you write, run, and debug your code with just a browser. It includes a code editor, debugger, and terminal. Cloud9 comes prepackaged with essential tools for popular programming languages, including JavaScript, Python, PHP, and more, so you don’t need to install files or configure your development machine to start new projects.
{{% /notice %}}

## Launch AWS Cloud9

There are a couple of ways you can provision Cloud9. The first, is through the [AWS Management Console](https://aws.amazon.com/console/) where you would need to have a properly configured [Amazon Virtual Private Cloud (Amazon VPC)](https://aws.amazon.com/vpc/) and later extend the default size of your [Amazon Elastic Block Store (Amazon EBS)](https://aws.amazon.com/ebs/) from the default size of 8GB. The second, is by simply deploying the [AWS Quick Start](https://aws.amazon.com/quickstart/) for [AWS Cloud9 Cloud-Based IDE](https://aws.amazon.com/quickstart/architecture/aws-cloud9-ide/) which does all of this for you with a single-click deployment. I recommend launching the Quick Start:

[Deploy into a new VPC](https://fwd.aws/xVvND)

The deployment process takes approximately 30 minutes to complete. In the meantime, you can review the [deployment guide](https://aws-quickstart.s3.amazonaws.com/quickstart-cloud9-ide/doc/aws-cloud9-cloud-based-ide.pdf) while you wait.

## Configure AWS Cloud9

If you opted to launch the AWS Quick Start, you may skip this section. Otherwise, if you deployed Cloud9 through other means and have an IAM Instance Profile, you will need to associate that with the Cloud9 environment.

```
aws ec2 associate-iam-instance-profile \
  --instance-id $(aws ec2 describe-instances \
    --filters Name=tag:Name,Values="*cloud9*" \
    --query Reservations[0].Instances[0].InstanceId \
    --output text) --iam-instance-profile Name="cl9-workshop-role"
```

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
