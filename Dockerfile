FROM adoptopenjdk/openjdk11:alpine-jre
ADD aws-lambda-runtime-local-*.jar aws-lambda-runtime-local.jar
ENTRYPOINT java -jar aws-lambda-runtime-local.jar