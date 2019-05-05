# AWS Lambda Custom Runtime Local API [ ![](https://img.shields.io/circleci/project/github/rdbatch02/lambda-runtime-kotlin-native/master.svg?style=flat) ](https://circleci.com/gh/rdbatch02/aws-lambda-runtime-local)
AWS Lambda Runtime API for local testing of custom runtimes

This project runs an HTTP API that is intended to emulate the [AWS Lambda Runtime Interface](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-api.html) to enable local testing of custom runtimes.

## Usage

This utility is published as both a jar or Docker image.

### Jar
Prerequisites:
* JRE/JDK 11

Grab the [latest release](https://github.com/rdbatch02/aws-lambda-runtime-local/releases) and run:
 
 ```bash
 java -jar aws-lambda-runtime-local-<version>.jar
```

Run on alternate port

 ```bash
 java -jar -Druntime.port=8080 aws-lambda-runtime-local-<version>.jar
```
 
 ### Docker
 Dockerhub Link: [https://hub.docker.com/r/c1phr/aws-lambda-runtime-local](https://hub.docker.com/r/c1phr/aws-lambda-runtime-local)
 
 ```bash
docker run -p 9000:9000 c1phr/aws-lambda-runtime-local -p
```

Run on alternate port

```bash
docker run -p 8080:8080 -e RUNTIME_PORT=8080 c1phr/aws-lambda-runtime-local
```