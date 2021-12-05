FROM openjdk:11 
EXPOSE 8080
COPY target/api-gateway-service-0.0.1-SNAPSHOT.jar api-gateway-service-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "api-gateway-service-0.0.1-SNAPSHOT.jar"]