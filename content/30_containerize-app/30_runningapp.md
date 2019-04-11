+++
title = "Running the container"
chapter = false
weight = 30
+++

To run the application we will execute the following Docker Compose commands:

```bash
docker-compose build atlassian-connect
```

Run the application container in the foreground and live stream the logs to stdout. If you hit an error hit **Ctrl+C**, make updates to the Dockerfile and re-build the container using **docker-compose** as described below.

```bash
docker-compose up atlassian-connect
```

To preview the application you will need to click *Preview* from the top menu of the Cloud9 environment. This will open a new window and pre-populate the full URL to your preview domain. The application outputs a JSON payload similar to this:

<pre>
{
  "key": "jira-data-provider-aws-addon",
  "baseUrl": "http://localhost",
  "name": "AWS Code Suite & Jira Software Cloud Integration",
  "description": "Example AWS & JSWC Integration",
  "vendor": {
    "name": "Amazon Web Services",
    "url": "https://aws.amazon.com"
},  
  "authentication": {
    "type": "jwt"
  },
  "lifecycle": {
    "installed": "/installed",
    "uninstalled": "/uninstalled"
  },
  "modules": {
    "jiraDevelopmentTool": {
        "url": "https://aws.amazon.com",
        "application": {
            "value": "AWS CodeCommit"
        },
        "logoUrl": "https://a0.awsstatic.com/libra-css/images/site/touch-icon-ipad-144-smile.png",
        "capabilities": [
            "commit"
        ],
        "name": {
            "value": "AWS CodeCommit"
        },
        "key": "codecommit-integration"
    },        
    "jiraBuildInfoProvider": {
        "homeUrl": "https://aws.amazon.com",
        "logoUrl": "https://a0.awsstatic.com/libra-css/images/site/touch-icon-ipad-144-smile.png",
        "documentationUrl": "https://docs.aws.amazon.com/index.html?nc2=h_ql_doc",
        "actions": {},
        "name": {
            "value": "AWS CodeBuild"
        },
        "key": "codebuild-integration"
    }
},
  "scopes": [
    "READ",
    "WRITE",
    "DELETE"
  ]
}
</pre>
