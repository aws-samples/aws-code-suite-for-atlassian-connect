+++
title = "Introduction"
chapter = false
weight = 1
+++

## Store development information

The first integration point I'd like to cover is that of our [source control](https://aws.amazon.com/devops/source-control/) service. In this case, we are working with [AWS CodeCommit](https://aws.amazon.com/codecommit/) and will leverage the *jiraDevelopmentTool* module defined in the `atlassian-connect.json` add-on descriptor located in the `./app/src/main/resources/` directory of our application.

{{% notice tip %}}
[AWS CodeCommit](https://aws.amazon.com/codecommit/) is a fully-managed [source control](https://aws.amazon.com/devops/source-control/) service that hosts secure Git-based repositories. It makes it easy for teams to collaborate on code in a secure and highly scalable ecosystem. CodeCommit eliminates the need to operate your own source control system or worry about scaling its infrastructure. You can use CodeCommit to securely store anything from source code to binaries, and it works seamlessly with your existing Git tools.
{{% /notice %}}

Our goal with this integration is to update Jira the moment a developer pushes a commit to your private git repository. We can accomplish this by by creating an [AWS CodeCommit trigger for AWS Lambda function](https://docs.aws.amazon.com/codecommit/latest/userguide/how-to-notify-lambda.html). Our Lambda function will query the [AWS CodeCommit API](https://docs.aws.amazon.com/codecommit/latest/APIReference/Welcome.html) and gather information such as the commit ID, commiter's name, email address, log message, and date. The data will then **POST** to our Atlassian Connect App which will in turn authenticate with our Jira Software Cloud (JSWC) tenant. The following diagram illustrates this workflow:

![CodeCommit](/images/codecommit-01.png)