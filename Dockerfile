FROM adoptopenjdk/openjdk11:alpine-jre
ADD build/libs/aws-lambda-runtime-local-*.jar aws-lambda-runtime-local.jar
ENTRYPOINT java -jar aws-lambda-runtime-local.jar