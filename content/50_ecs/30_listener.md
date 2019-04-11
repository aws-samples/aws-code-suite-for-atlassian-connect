+++
title = "Adding SSL to the listener"
chapter = false
weight = 30
+++

Our CloudFormation template that provisioned our ECS cluster also provisioned a network load balancer. That load balancer was configured with a `TCP` protocal listner on port `8080`. However, before we can register our app in Jira we will need to allow for `HTTPS`. For production workloads, it is recommended that you use a proper certficiate issued from a valid Certificate Authority. 

{{% notice tip %}}
This is where services such as [AWS Certificate Manager](https://aws.amazon.com/certificate-manager) lets you easily provision, manage, and deploy public and private Secure Sockets Layer/Transport Layer Security (SSL/TLS) certificates for use with AWS services.
{{% /notice %}}

Earlier in the workshop, we generated a self-signed certificate and uploaded this to AWS Certficiate Manager. For the purpose of this example, this will suffice. Now that we have our cluster up and running, we will take this self-signed cert and attach it to a new TLS listener. To do this, we will use the AWS CLI. The following command will create the listener and pass in the values needed from the resources we have previously created. Run the following from your Cloud9 IDE:

```bash
aws elbv2 create-listener --load-balancer-arn \
$(aws elbv2 describe-load-balancers \
--query LoadBalancers[0].LoadBalancerArn --output text) \
--protocol TLS --port 443 --certificates \
CertificateArn=$(aws acm list-certificates \
--query CertificateSummaryList[0].CertificateArn --output text) \
--ssl-policy ELBSecurityPolicy-2016-08 --default-actions \
Type=forward,TargetGroupArn=$(aws elbv2 describe-target-groups \
--query TargetGroups[0].TargetGroupArn --output text)
```

{{% notice info %}}
If successfully created, you should see output similar to the output below:
{{% /notice %}}

<pre>
{
    "Listeners": [
        {
            "Protocol": "TLS", 
            "DefaultActions": [
                {
                    "TargetGroupArn": "arn:aws:elasticloadbalancing:us-west-2:123456789012:targetgroup/atlassian-connect-ecs-target/b8ce7fa3f5fa7211", 
                    "Type": "forward"
                }
            ], 
            "SslPolicy": "ELBSecurityPolicy-2016-08", 
            "Certificates": [
                {
                    "CertificateArn": "arn:aws:acm:us-west-2:123456789012:certificate/f11b8ccd-3d65-4533-8c54-bcf8972455eb"
                }
            ], 
            "LoadBalancerArn": "arn:aws:elasticloadbalancing:us-west-2:123456789012:loadbalancer/net/atlas-Publi-1NU0KBFFQU8GU/46285272d2eb179d", 
            "Port": 443, 
            "ListenerArn": "arn:aws:elasticloadbalancing:us-west-2:123456789012:listener/net/atlas-Publi-1NU0KBFFQU8GU/46285272d2eb179d/abd555c815f133ca"
        }
    ]
}
</pre>

Lastly, we can test this by opening a new tab in our browser and pointing to `HTTPS://` and the public DNS name of our load balancer. We can find this by quering with the following command:

```bash
aws elbv2 describe-load-balancers --query LoadBalancers[0].DNSName --output text
```

As with the previous like examples, we should expect to see a JSON payload in the body.
