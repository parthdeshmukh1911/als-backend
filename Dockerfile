# Start with Maven for building
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set working directory inside build stage
WORKDIR /app

# Copy pom and source files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# ---- Runtime stage ----
FROM openjdk:17-jdk-slim

# Working directory inside container
WORKDIR /app

# Copy jar from builder image
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8081

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
