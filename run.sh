#!/bin/sh

jar=target/kotlin-rational-1.0.1-SNAPSHOT-jar-with-dependencies.jar

test -e $jar || ./mvnw package

exec java -jar $jar
