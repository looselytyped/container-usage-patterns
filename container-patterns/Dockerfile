ARG APP_ENV=builder
ARG JDK_VERSION=11.0.5-jdk
ARG JRE_VERSION=11.0.5-jdk

# ——— Base building image —————————————————————————————————————————————————————
FROM openjdk:${JDK_VERSION} as base

# ——— Get the dependencies ————————————————————————————————————————————————————
FROM base as gradle
WORKDIR /code
COPY *.gradle gradle.* gradlew ./
COPY gradle ./gradle
RUN ./gradlew build || return 0
# Alternatively ...
# RUN ./gradlew resolveDependencies

# ——— Get the source ——————————————————————————————————————————————————————————
FROM gradle as sourced
COPY spotless.* ./
COPY src/ src/

# ——— Build/test ——————————————————————————————————————————————————————————————
FROM sourced as checked
COPY src/ src/
RUN ["./gradlew", "-q", "--no-daemon", "check"]

# ——— Build/test ——————————————————————————————————————————————————————————————
FROM checked as builder
RUN ["./gradlew", "-q", "--no-daemon", "bootJar"]

# ——— Final Image —————————————————————————————————————————————————————————————
FROM openjdk:${JRE_VERSION}
EXPOSE 8080

LABEL org.label-schema.description="This is a demo of a multi-stage Dockerfile" \
  org.label-schema.docker.cmd="\
  docker run -d \
  -p 8080:8080 \
  --name <container-name> \
  <image-name>" \
  org.label-schema.docker.debug="docker exec -it <container-name> bash" \
  org.label-schema.name="multi-stage-dockerfile-demo" \
  org.label-schema.vcs-url="https://github.com/looselytyped/my-awesome-app" \
  org.label-schema.vendor="DefMacro Software LLC" \
  org.label-schema.schema-version="1.0.0"

COPY entrypoint.sh entrypoint.sh
COPY --from=builder /code/build/libs/container-patterns-0.0.1-SNAPSHOT.jar /var/app.jar

# DO NOT DO THIS
# CMD java -jar /var/app.jar

# SLIGHTLY BETTER
# CMD ["java", "-jar", "/var/app.jar"]

# ENTRYPOINT+CMD+entrypoint command
# ENTRYPOINT ["./entrypoint.sh"]
# CMD ["default"]

