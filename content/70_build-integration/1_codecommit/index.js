console.log('Starting getCommitId...');

const AWS = require('aws-sdk');
const codecommit = new AWS.CodeCommit({ apiVersion: '2015-04-13' });
const request = require('request');
const baseURL = process.env.BASE_URL

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
            }
        });           
    });
};
