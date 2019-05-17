# AWS Lambda Custom Runtime Local API [ ![](https://img.shields.io/circleci/project/github/rdbatch02/lambda-runtime-kotlin-native/master.svg?style=flat) ](https://circleci.com/gh/rdbatch02/aws-lambda-runtime-local)
AWS Lambda Runtime API for local testing of custom runtimes

This project runs an HTTP API that is intended to emulate the [AWS Lambda Runtime Interface](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-api.html) to enable local testing of custom runtimes.

## Running

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
docker run -p 9000:9000 c1phr/aws-lambda-runtime-local
```

Run on alternate port

```bash
docker run -p 8080:8080 -e RUNTIME_PORT=8080 c1phr/aws-lambda-runtime-local
```

## Usage

This utility will expose endpoints that mirror the AWS Lambda Runtime interface:

```text
GET http://localhost:9000/2018-06-01/runtime/invocation/next
POST http://localhost:9000/2018-06-01/runtime/invocation/$REQUEST_ID/response
POST http://localhost:9000/2018-06-01/runtime/invocation/$REQUEST_ID/error
POST http://localhost:9000/2018-06-01/runtime/init/error
```

In addition to an endpoint that allows you to stage request events for your runtime to invoke:

```text
POST http://localhost:9000/event/next
```

The runtime will serve events that have been POSTed to the `/event/next` endpoint sequentially each time the `/invocation/next` endpoint is called (or will return an empty response when there is no pending event). 
Invocation requests will include headers to emulate the Lambda Runtime interface as well, and expects that Request IDs are included with calls to `/response` and `/error` just as the Lambda service would.

## Environment Configuration

The following configuration options can be set to modify the behavior of the local runtime. Either JVM arguments or environment variables can be used depending on the environment (JVM vs Docker).

| Effect                            | JVM Argument       | Environment Variable |
|-----------------------------------|--------------------|----------------------|
| Set Runtime Port                  | runtime.port=8080  | RUNTIME_PORT=8080    |
| Enable Request/Response Debugging | runtime.debug=true | RUNTIME_DEBUG=true   |
