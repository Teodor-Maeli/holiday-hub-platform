FROM gradle:7.6-jdk AS build
WORKDIR /application
COPY . /application
RUN gradle clean build

FROM amazoncorretto:17-alpine as run
WORKDIR /application
COPY --from=build /application/build/libs/*.jar /application/platform.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","platform.jar"]