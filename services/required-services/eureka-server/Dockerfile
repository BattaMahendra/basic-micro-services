FROM openjdk:22-jdk-slim
WORKDIR /app
COPY build/libs/eureka-server.jar eureka-server.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","eureka-server.jar"]
