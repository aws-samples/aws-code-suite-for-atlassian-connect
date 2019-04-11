+++
title = "Installing docker-compose"
chapter = false
weight = 30
+++

For these examples, we will use the tool [docker-compose](https://docs.docker.com/compose/reference/overview/) to simulate a full-stack development environment that consists of multiple containers communicating with each other.

Download the docker-compose binary by running the following command:

```bash
sudo curl -kLo ~/bin/docker-compose https://github.com/docker/compose/releases/download/1.22.0/docker-compose-$(uname -s)-$(uname -m)
sudo chmod +x ~/bin/docker-compose
```

{{% notice info %}}
The result should look like what's below:
{{% /notice %}}

<pre>
% Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                Dload  Upload   Total   Spent    Left  Speed
100   617    0   617    0     0   2924      0 --:--:-- --:--:-- --:--:--  2924
100 11.2M  100 11.2M    0     0  5878k      0  0:00:01  0:00:01 --:--:-- 8896k
</pre>
