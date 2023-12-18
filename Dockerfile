#build platform jar

FROM gradle:latest AS build-stage
WORKDIR /application
COPY . /application
RUN gradle clean build

#define entry point

FROM amazoncorretto:17-alpine as run-stage
WORKDIR /application
COPY --from=build-stage /application/build/libs/*.jar /application/platform.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","platform.jar"]