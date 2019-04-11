console.log('Starting getStatus.js');

const AWS = require('aws-sdk');
const codepipeline = new AWS.CodePipeline({ apiVersion: '2015-07-09' });

const getExecutionId = require('./getExecutionId.js');

var pipelineName = process.env.PIPELINE_NAME;
var pipelineExecutionId = getExecutionId();
console.log('ExecutionID: ', pipelineExecutionId);

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