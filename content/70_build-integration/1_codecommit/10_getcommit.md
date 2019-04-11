+++
title = "AWS Lambda function"
chapter = false
weight = 10
+++

## Reviewing the function

Our AWS Lambda function is written in [Node.js](https://nodejs.org/dist/latest-v11.x/docs/api/) and leverages the [AWS SDK for JavaScript in Node.js](https://docs.aws.amazon.com/AWSJavaScriptSDK/latest/index.html) as well as an [npm](https://www.npmjs.com/package/request) module: [Request - Simplified HTTP client](https://www.npmjs.com/package/request).

The beginning of our code will import these modules and define a few variables. Among these will be the endpoint for our Atlassian Connect App which we will pass as an [environment variable](https://docs.aws.amazon.com/lambda/latest/dg/env_variables.html), `BASE_URL`, in our Lambda function. 

<pre>
console.log('Starting getCommitId...');

const AWS = require('aws-sdk');
const codecommit = new AWS.CodeCommit({ apiVersion: '2015-04-13' });
const request = require('request');
const baseURL = process.env.BASE_URL
</pre>

The next section of our code will define the [AWS Lambda Function Handler](https://docs.aws.amazon.com/lambda/latest/dg/nodejs-prog-model-handler.html). AWS Lambda invokes your Lambda function via a handler object. A handler represents the name of your Lambda function and serves as the entry point that AWS Lambda uses to execute your function code. We will enumerate the event data and define a few variables such as `commitId` and `repository` which we will then pass to [getCommit](https://docs.aws.amazon.com/AWSJavaScriptSDK/latest/AWS/CodeCommit.html#getCommit-property) method in order to pull additional information about a commit, including commit message and committer information. We will then declare `responseBody` and using `JSON.stringify()` convert this data to JSON.

<pre>
exports.handler = function(event, context) {

    (event.Records || []).forEach(function(rec) {
        var details = rec.codecommit.references[0];
        var commitId = details.commit;
        var repository = rec.eventSourceARN.split(":")[5];
        var params = {
            commitId: commitId,
            repositoryName: repository
        };

        codecommit.getCommit(params, function(err, data) {
            if (err) {
                console.log(err, err.stack);
                var message = "Error getting commit data for commitId: " + commitId;
                console.log(message);
                context.fail(message);
            } else {
                var details = data.commit;
                var commitMessage = data.commit.message;
                var authorName = data.commit.author.name;
                var authorEmail = data.commit.author.email;
                var authorTimestamp = data.commit.author.date;
                var responseBody = JSON.stringify({
                    repositories: [
                        {
                            name: repository,
                            commits: [
                                {
                                    id: commitId,
                                    message: commitMessage,
                                    author: {
                                        name: authorName,
                                        email: authorEmail
                                    },
                                    authorTimestamp: authorTimestamp
                                }
                            ]
                        }
                    ]
                });
                console.log("Response body:\n", responseBody);
                context.succeed(details);
</pre>

The last section of our code will declare a few more variables for preparing our payload and pass these to `request` which will **POST** the data to our Atlassian Connect app.

<pre>
var send = {
    method: 'POST',
    url: baseURL,
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    },
    body: responseBody
};

request(send, function(error, response, body) {
    console.log('error:', error);
    console.log('statusCode:', response && response.statusCode);
    console.log('body:', body);
});
</pre>

{{% notice note %}}
In the code block referenced above, note that we are passing our environment variable `baseURL` to `send`. In order for this to complete successfully, you will need to update this value within your Lambda function before it triggers. Otherwise, the value will be `null` and the payload will not be sent.
{{% /notice %}}