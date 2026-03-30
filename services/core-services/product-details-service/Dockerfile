FROM openjdk:22-jdk-slim
WORKDIR /app
COPY build/libs/product-details-service.jar product-details-service.jar
EXPOSE 808
ENTRYPOINT ["java","-jar","product-details-service.jar"]
