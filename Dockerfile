FROM gradle:7.6-jdk AS build
COPY / ./
RUN gradle clean build

FROM amazoncorretto:17-alpine as run
VOLUME /tmp
COPY --from=build build/libs/*.jar platform.jar
ENTRYPOINT ["java","-jar","/platform.jar"]