[![Known Vulnerabilities](https://snyk.io/test/github/aws-samples/aws-code-suite-for-atlassian-connect/badge.svg?targetFile=/content/30_containerize-app/app/pom.xml)](https://snyk.io/test/github/aws-samples/aws-code-suite-for-atlassian-connect?targetFile=/content/30_containerize-app/app/pom.xml)

# AWS Code Suite & Atlassian Connect

In this workshop, you will learn how to customize and build your own version of the Atlassian Connect Add-on Spring Boot application. The modules contained in this workshop will provide you with step-by-step instructions for building a JAR. You will also learn how to package the application as a Docker image and deploy it to AWS Fargate. Lastly, you will also setup a complete CI/CD pipeline using AWS CodePipeline, AWS CodeCommit and AWS CodeBuild. Once built and deployed you will be able to track your entire pipeline within Atlassian Jira Software Cloud (JSWC) through our AWS Lambda based integrations which push relevant details about the status of the pipeline in near real-time.

## Generating the workshop static site pages with Hugo

### Install Hugo

macOS: `brew install hugo`

Windows: `choco install hugo -confirm`

Visit https://gohugo.io/getting-started/installing/ for detailed instructions.


### Clone this repo

`git clone git@github.com:aws-samples/aws-code-suite-for-atlassian-connect.git`

### Install node packages

`cd aws-code-suite-for-atlassian-connect`

`npm install`

### Run Hugo locally

`npm run server`

### View site locally

Visit http://localhost:1313/ to see the site.