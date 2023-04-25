# Use a base image with Java 11 installed
FROM adoptopenjdk/openjdk11:jre-11.0.13_8-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container
COPY build/libs/geico-assist-1.0-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]