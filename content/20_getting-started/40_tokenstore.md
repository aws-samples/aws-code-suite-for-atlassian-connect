+++
title = "Secure token storage"
chapter = false
weight = 40
+++

Now, we are going to store our token in [AWS Secrets Manager](https://aws.amazon.com/secrets-manager) for protection and seamless integration with other AWS services. We will need this in subsequet steps where we automate the build process using [AWS CodeBuild](https://aws.amazon.com/codebuild) and depend on passing this token. To do so we will run through a couple of steps that may seem redundant, but makes it easier to copy and paste stuff.

{{% notice tip %}}
[AWS Secrets Manager](https://aws.amazon.com/secrets-manager) helps you protect secrets needed to access your applications, services, and IT resources. The service enables you to easily rotate, manage, and retrieve database credentials, API keys, and other secrets throughout their lifecycle. 
{{% /notice %}}

{{% notice note %}}
[AWS Systems Manager](https://aws.amazon.com/systems-manager) also provides secure, hierarchical storage for configuration data management and secrets management through its [parameter store](https://docs.aws.amazon.com/systems-manager/latest/userguide/systems-manager-paramstore.html).
{{% /notice %}}

First, let's assign the value of the token we copied to our clipboard to a local environment variable on our Cloud9 IDE. At the prompt, set `TempToken=` and **paste** the token you copied to your clipboard using `Command-V` or `CTRL-V` so that it looks similar to this:

<pre>
TempToken=UC2RDeQalishTxClTywI5218
</pre>

Next, run the following command:

```bash
aws secretsmanager create-secret \
--name "AtlassianAPIToken" \
--description "Atlassian API Token" \
--secret-string "$TempToken"
```

{{% notice info %}}
If successful, you should see output similar to what's below:
{{% /notice %}}

<pre>
{
    "VersionId": "f8a8fed6-05cd-41f3-aa1c-8fb9c0684873", 
    "Name": "AtlassianAPIToken", 
    "ARN": "arn:aws:secretsmanager:us-west-2:123456789012:secret:AtlassianAPIToken-mf8rZW"
}
</pre>

Next, let's set our Atlassian username to a local environment variable. At the prompt, set `TempUser=` and provide the email address associated with your Atlassian account so that it looks similar to this:

<pre>
TempUser=me@company.com
</pre>

Next, simply run the following command:

```bash
aws secretsmanager create-secret \
--name "AtlassianAPIUser" \
--description "Atlassian API User" \
--secret-string "$TempUser"
```

{{% notice info %}}
If successful, you should see output similar to what's below:
{{% /notice %}}

<pre>
{
    "VersionId": "033ff5f3-967f-4642-b206-01d39aca575d",
    "Name": "AtlassianAPIUser",
    "ARN": "arn:aws:secretsmanager:us-west-2:123456789012:secret:AtlassianAPIUser-pMXi9h"
}
</pre>