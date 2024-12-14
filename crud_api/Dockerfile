# Resources:
# https://www.geeksforgeeks.org/how-to-create-a-java-docker-container/

# Building the application
# Maven version used for building
FROM maven:3.8.5-openjdk-17 as build

# Set the working directory inside the container
WORKDIR /crud-api

# Copy the Maven project descriptor file
COPY pom.xml .
# Copy the source code
COPY src ./src

# Build
RUN mvn clean package -DskipTests

# Running the application
# Set the runtime environment
FROM openjdk:17-jdk

WORKDIR /crud-api

# Copy the .JAR file from the building step
COPY --from=build /crud-api/target/*.jar app.jar

# Allow external access on port 6000
EXPOSE 6000
# Run
ENTRYPOINT ["java", "-jar", "app.jar"]
