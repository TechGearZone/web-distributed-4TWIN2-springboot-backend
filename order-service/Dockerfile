FROM openjdk:17
EXPOSE 8082
COPY target/order-service-0.0.1-SNAPSHOT.jar order-service.jar
ENTRYPOINT ["java", "-jar", "/order-service.jar"]