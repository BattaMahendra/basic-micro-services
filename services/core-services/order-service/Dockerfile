FROM openjdk:22-jdk-slim
WORKDIR /app
COPY build/libs/order-service.jar order-service.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","order-service.jar"]
