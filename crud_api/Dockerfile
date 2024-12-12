FROM maven:3.8.5-openjdk-17 as build
WORKDIR /crud-api
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk
WORKDIR /crud-api
COPY --from=build /crud-api/target/*.jar app.jar
EXPOSE 6000
ENTRYPOINT ["java", "-jar", "app.jar"]