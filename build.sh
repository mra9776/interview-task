#! /usr/bin/env bash

# TODO: check if jdk15 was already available
PATH=/opt/jdk-15.0.2/bin/:"$PATH"
JAVA_HOME=/opt/jdk-15.0.2

java -version
./mvnw package || exit 1

timeout 3 java -jar target/interview-task-0.0.1-SNAPSHOT.jar
