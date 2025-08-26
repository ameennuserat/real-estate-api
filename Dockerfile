# Stage 1: Build the application with Maven
FROM maven:3.8-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .


RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]