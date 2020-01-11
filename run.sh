#!/bin/sh

./mvnw clean verify \
    && java -jar target/kotlin-rational-0-SNAPSHOT.jar
