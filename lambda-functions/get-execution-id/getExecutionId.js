console.log('Starting getExecutionId.js');

const AWS = require('aws-sdk');
const codepipeline = new AWS.CodePipeline({ apiVersion: '2015-07-09' });

var pipelineName = process.env.PIPELINE_NAME;

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