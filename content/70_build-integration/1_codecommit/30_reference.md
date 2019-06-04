+++
title = "Reference Mappings"
chapter = false
weight = 30
+++

## Required Fields

The following table maps the [Jira Development Information](https://developer.atlassian.com/cloud/jira/software/rest/#api-group-Development-Information) APIs with respective AWS APIs for [GetRepository](https://docs.aws.amazon.com/codecommit/latest/APIReference/API_GetRepository.html) and [GetCommit](https://docs.aws.amazon.com/codecommit/latest/APIReference/API_GetCommit.html).

<table>
    <div align="left">
        <tr>
            <th>Atlassian</th>
            <th></th>
            <th>AWS</th>
            <th></th>
        </tr>
        <tr>
            <th><a href="https://developer.atlassian.com/cloud/jira/software/rest/#api-group-Development-Information">repositories</th>
            <th></th>
            <th></th>
            <th></th>
        </tr>
        <tr>
            <td><b>name</b></td>
            <td>The name of this repository.</td>
            <td><a href="https://docs.aws.amazon.com/codecommit/latest/APIReference/API_RepositoryMetadata.html#CodeCommit-Type-RepositoryMetadata-repositoryName">repositoryName</td>
            <td>The name of the repository to which the commit was made.</td>
        </tr>
        <tr>
            <td><b>url</b></td>
            <td>The URL of this repository.</td>
            <td><a href="https://docs.aws.amazon.com/codecommit/latest/APIReference/API_RepositoryMetadata.html#CodeCommit-Type-RepositoryMetadata-cloneUrlHttp">cloneUrlHttp</td>
            <td>The URL to use for cloning the repository over HTTPS.</td>
        </tr>
        <tr>
            <th><a href="https://developer.atlassian.com/cloud/jira/software/rest/#api-group-Development-Information">commits</th>
            <th></th>
            <th></th>
            <th></th>
        </tr>
        <tr>
            <td><b>id</b></td>
            <td>The ID of this entity. Will be used for cross entity linking.</td>
            <td><a href="https://docs.aws.amazon.com/codecommit/latest/APIReference/API_Commit.html#CodeCommit-Type-Commit-commitId">commitID</td>
            <td>The commit ID. Commit IDs are the full SHA of the commit.</td>
        </tr>
        <tr>
            <td><b>issueKeys</b></td>
            <td>List of issues keys that this entity is associated with. They must be valid JIRA issue keys.</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td><b>updateSequenceId</b></td>
            <td>An ID used to apply an ordering to updates for this entity in the case of out-of-order receipt of update requests.</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td><b>hash</b></td>
            <td>The hash of the commit.</td>
            <td><a href="https://docs.aws.amazon.com/codecommit/latest/APIReference/API_Commit.html#CodeCommit-Type-Commit-commitId">commitID</td>
            <td>The commit ID. Commit IDs are the full SHA of the commit.</td>
        </tr>
        <tr>
            <td><b>message</b></td>
            <td>The commit message.</td>
            <td><a href="https://docs.aws.amazon.com/codecommit/latest/APIReference/API_Commit.html#CodeCommit-Type-Commit-message">message</td>
            <td>The commit message associated with the specified commit.</td>
        </tr>
        <tr>
            <td><b>author</b></td>
            <td>The author of this commit.</td>
            <td><a href="https://docs.aws.amazon.com/codecommit/latest/APIReference/API_Commit.html#CodeCommit-Type-Commit-author">author</td>
            <td>Information about the author of the specified commit.</td>
        </tr>
        <tr>
            <td><b>fileCount</b></td>
            <td>The total number of files added, removed, or modified by this commit.</td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td><b>authorTimestamp</b></td>
            <td>The author timestamp of this commit.</td>
            <td><a href="https://docs.aws.amazon.com/codecommit/latest/APIReference/API_UserInfo.html#CodeCommit-Type-UserInfo-date">date</td>
            <td>The date when the specified commit was commited.</td>
        </tr>
        <tr>
            <td><b>displayId</b></td>
            <td>Shortened identifier for this commit, used for display.</td>
            <td><a href="https://docs.aws.amazon.com/codecommit/latest/APIReference/API_Commit.html#CodeCommit-Type-Commit-commitId">commitID</td>
            <td>The commit ID. Commit IDs are the full SHA of the commit.</td>
        </tr>
    </div>
</table>
