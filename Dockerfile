FROM amazoncorretto:17

WORKDIR /app

COPY target/restaurant-choice-service-0.0.1-SNAPSHOT.jar /app/restaurant-choice-service-0.0.1-SNAPSHOT.jar

EXPOSE 8080

# command to run on container start
CMD ["java", "-jar", "restaurant-choice-service-0.0.1-SNAPSHOT.jar"]