+++
title = "AWS Lambda function"
chapter = false
weight = 10
+++

## Reviewing the function

Our AWS Lambda function is written in [Node.js](https://nodejs.org/dist/latest-v11.x/docs/api/) and leverages the [AWS SDK for JavaScript in Node.js](https://docs.aws.amazon.com/AWSJavaScriptSDK/latest/index.html). Specifically, we are leveraging the AWS CodePipeline [API Reference](https://docs.aws.amazon.com/codepipeline/latest/APIReference/Welcome.html). I have broken this out into two scripts: one that calls [GetPipelineExecution](https://docs.aws.amazon.com/codepipeline/latest/APIReference/API_GetPipelineExecution.html) to return the **pipelineExecutionId**, and the other which uses that ID of that pipeline execution to get the state by calling the [GetPipelineState](https://docs.aws.amazon.com/codepipeline/latest/APIReference/API_GetPipelineState.html).

Let's take a look at the code that gets our execution ID first. The beggining of our code will import the AWS SDK as well as our AWS CodePipeline name which we will pass as an [environment variable](https://docs.aws.amazon.com/lambda/latest/dg/env_variables.html), `PIPELINE_NAME`, in our Lambda function.

<pre>
console.log('Starting getExecutionId.js');

const AWS = require('aws-sdk');
const codepipeline = new AWS.CodePipeline({ apiVersion: '2015-07-09' });

var pipelineName = process.env.PIPELINE_NAME;
</pre>

The next section of our code will define the [AWS Lambda Function Handler](https://docs.aws.amazon.com/lambda/latest/dg/nodejs-prog-model-handler.html). AWS Lambda invokes your Lambda function via a handler object. A handler represents the name of your Lambda function and serves as the entry point that AWS Lambda uses to execute your function code. We will enumerate the event data and define `pipelineExecutionId` for records that match build stage name.

<pre>
module.exports = function(event, context) {

    var params = {
        name: pipelineName
    };

    codepipeline.getPipelineState(params, function(err, data) {
        if (err) {
            console.log(err, err.stack);
            var message = "Error getting data for pipeline: " + pipelineName;
            console.log(message);
            context.fail(message);
        } else {
            console.log(data);
            if (data.stageStates.length > 0) {
                for (let stage of data.stageStates) {
                    let stageName = stage.stageName;
                    if (stageName === 'Build') {
                        let pipelineExecutionId = stage.latestExecution.pipelineExecutionId;
                        let executionStatus = stage.latestExecution.status;
                        console.log('Stage: ', stageName, 'with Execution Id: ', pipelineExecutionId, 'completed with status: ', executionStatus);
                        return pipelineExecutionId;
                    }
                }
            }
            context.succeed(data.stageStates.message);
        }
    });

};
</pre>

Now, let's take a closer look at the code that pulls our pipeline state. The beginning of our code will again import the AWS SDK and define a few variables. Among these will be the AWS CodePipeline name as well as our execution ID which we are passing from our other function.

<pre>
console.log('Starting getStatus.js');

const AWS = require('aws-sdk');
const codepipeline = new AWS.CodePipeline({ apiVersion: '2015-07-09' });

const getExecutionId = require('./getExecutionId.js');

var pipelineName = process.env.PIPELINE_NAME;
var pipelineExecutionId = getExecutionId();
</pre>

The next section of our code will define the [AWS Lambda Function Handler](https://docs.aws.amazon.com/lambda/latest/dg/nodejs-prog-model-handler.html). Here we are calling [GetPipelineExecution](https://docs.aws.amazon.com/codepipeline/latest/APIReference/API_GetPipelineExecution.html) to get the status.

<pre>
module.exports = function(event, context) {

    var params = {
        pipelineName: pipelineName,
        pipelineExecutionId: pipelineExecutionId
    };

    codepipeline.getPipelineExecution(params, function(err, data) {
        if (err) {
            console.log(err, err.stack);
            var message = "Error getting data for execution id: " + pipelineExecutionId + "for pipeline: " + pipelineName;
            console.log(message);
            context.fail(message);
        } else {
            console.log(data);
            let status = data.pipelineExecution.status;
            console.log('Execution ID: ', pipelineExecutionId, 'completed with status: ', status);
            let artifact = data.pipelineExecution.artifactRevisions;
            console.log('Artifact: ', artifact);
            let revisionId = data.pipelineExecution.artifactRevisions[0].revisionId;
            let revisionSummary = data.pipelineExecution.artifactRevisions[0].revisionSummary;
            console.log('Commit ID: ', revisionId, 'with message: ', revisionSummary);
            context.succeed(data.pipelineExecution.message);
        }
    });

};
</pre>
