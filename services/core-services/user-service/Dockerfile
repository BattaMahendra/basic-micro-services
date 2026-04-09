FROM openjdk:22-jdk-slim
WORKDIR /app
COPY build/libs/user-service.jar user-service.jar
EXPOSE 801
ENTRYPOINT ["java","-jar","user-service.jar"]
