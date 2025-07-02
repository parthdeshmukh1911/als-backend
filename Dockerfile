# Use official lightweight OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy your Spring Boot JAR file to the container
COPY target/*.jar app.jar

# Expose port 8080 (default for Spring Boot)
EXPOSE 8081

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
