#!/bin/sh

./mvnw "$@" &&
    exec java -jar target/kotlin-rational-0-SNAPSHOT-jar-with-dependencies.jar
