FROM openjdk:17
EXPOSE 8084
ADD target/Delivery-0.0.1-SNAPSHOT.jar Delivery-docker.jar
ENTRYPOINT ["java", "-jar", "Delivery-docker.jar"]