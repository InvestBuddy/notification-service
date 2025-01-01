# Use JDK 21 as the base image
FROM openjdk:21-jdk

# Set working directory
WORKDIR /app

# Copy the JAR file and rename it to notification.jar
COPY target/notification-service-1.0-SNAPSHOT.jar notification-service.jar
# Expose port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "notification-service.jar"]
