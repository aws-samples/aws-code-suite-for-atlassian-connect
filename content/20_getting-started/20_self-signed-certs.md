+++
title = "Self-signed SSL certificate"
chapter = false
weight = 20
+++

You will need to have a certificate issued by a valid certificate authority if you intend to deploy the application and register this to your Jira instance. This module will guide you through the steps to generate a self-signed certificate and import this to [AWS Certificate Manager](https://aws.amazon.com/certificate-manager) (ACM). If you are already using ACM to provision, manage, and deploy public and private Secure Sockets Layer/Transport Layer Security (SSL/TLS) certificates then you can skip this section and [request a public certificate](https://docs.aws.amazon.com/acm/latest/userguide/gs-acm-request-public.html). Otherwise, you can proceed and go through the steps of generating a self-signed certificate which you can later upload to ACM.

## Generate your private key and public certificate

```bash
cd ~/environment/
openssl req -newkey rsa:2048 -nodes -keyout key.pem -x509 -days 365 -out certificate.pem
```

{{% notice info %}}
The result should look like what's below:
{{% /notice %}}

<pre>
Generating a 2048 bit RSA private key
......+++
...........................+++
writing new private key to 'key.pem'
-----
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [XX]:US
State or Province Name (full name) []:Washington
Locality Name (eg, city) [Default City]:Seattle
Organization Name (eg, company) [Default Company Ltd]:
Organizational Unit Name (eg, section) []:
Common Name (eg, your name or your server's hostname) []:amazonaws.com
Email Address []:
</pre>

## Import your certificate using the AWS CLI

```bash
aws acm import-certificate --certificate file://certificate.pem \
  --private-key file://key.pem
```

{{% notice info %}}
If the import-certificate command is successful, it returns the [Amazon Resource Name (ARN)](https://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html) of the imported certificate.
{{% /notice %}}

<pre>
{
    "CertificateArn": "arn:aws:acm:us-west-2:***********:certificate/********-****-****"
}
</pre>

{{% notice warning %}}
As of the publish date for this workshop, [Jira Software Cloud](https://support.atlassian.com/jira-software-cloud/) **does not** support self-signed certificates. You will need to obtain a valid certificate in order to register your application. Only **HTTPS** is supported for both development and production environments.
{{% /notice %}}