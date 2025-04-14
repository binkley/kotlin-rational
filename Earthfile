VERSION 0.8
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /code
# Save Maven downloads across targets and runs of earthly
# This adds a layer to the image
CACHE --id maven-cache --persist maven-cache

# build builds and tests with Maven, and saves the target/ directory
build:
    COPY mvnw .
    COPY .mvn .mvn
    COPY pom.xml .
    COPY config config
    COPY src src
    RUN --mount type=cache,id=maven-cache,target=/root/.m2 --secret OWASP_NVD_API_KEY ./mvnw --batch-mode --no-transfer-progress clean verify site

    # For CI so that GitHub can copy artifacts:
    # Just copy everything rather than maintain a whitelist of files/dirs.
    SAVE ARTIFACT --keep-ts target AS LOCAL target

# run runs the demo program with Maven, building if needed
run:
    FROM +build
    COPY run .
    RUN --mount type=cache,id=maven-cache,target=/root/.m2 ./run
