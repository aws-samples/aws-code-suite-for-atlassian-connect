+++
title = "Deploy the cluster"
chapter = false
weight = 20
+++

We will first deploy our CloudFormation template which configures our ECS VPC, Task Definition, Container Definition, and Service. First, let's change to this modules directory by running:

```bash
cd ~/environment/aws-atlassian-connect/content/50_ecs/
```

Deploy the CloudFormation template to create your new VPC by running the following command:

```bash
aws cloudformation create-stack \
  --stack-name "atlassian-connect-ecs" \
  --template-body file://ecs.yaml \
  --parameters ParameterKey=ProjectName,ParameterValue=atlassian-connect \
  ParameterKey=ECRImageURI,ParameterValue=$ECRImageURI \
  ParameterKey=JiraUser,ParameterValue=$JiraUser \
  ParameterKey=JiraPass,ParameterValue=$JiraPass \
  ParameterKey=JiraServerPort,ParameterValue=8080 \
  --capabilities CAPABILITY_NAMED_IAM
```

Wait for the Template to finish deploying by typing the following command:

```bash
until [[ `aws cloudformation describe-stacks --stack-name "atlassian-connect-ecs" --query "Stacks[0].[StackStatus]" --output text` == "CREATE_COMPLETE" ]]; do  echo "The stack is NOT in a state of CREATE_COMPLETE at `date`";   sleep 30; done && echo "The Stack is built at `date` - Please proceed"
```

Once successfully deployed, let's query the stack for outputs. Specifically, we are interested in the network load balancer's public DNS name. In our CloudFormation template, we thought ahead and constructed this into a properly formatted URL in order to make things easier. From your Cloud9 IDE, run the following command:

```bash
aws cloudformation describe-stacks --stack-name atlassian-connect-ecs \
--query Stacks[0].Outputs[5].OutputValue --output text
```

{{% notice info %}}
Your result should look similar to this:
{{% /notice %}}

<pre>
http://atlas-Publi-1NU0KBFFQU8GU-46285272d2eb179d.elb.us-west-2.amazonaws.com:8080
</pre>

Now, we simply copy and paste this into a new tab on your browser and you should see a JSON formatted response in the body.
