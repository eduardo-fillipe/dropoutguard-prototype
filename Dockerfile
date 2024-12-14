# Stage 1: Cache Maven dependencies
FROM maven:3.9.4-eclipse-temurin-21-alpine AS dependencies
WORKDIR /dropout-guard
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Stage 2: Build the application
FROM maven:3.9.4-eclipse-temurin-21-alpine AS builder
WORKDIR /dropout-guard
COPY --from=dependencies /root/.m2 /root/.m2
ADD src src
COPY pom.xml .
RUN mvn clean package -D skipTests

# Stage 3: Run the application
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /dropout-guard
COPY --from=builder /dropout-guard/target/*.jar dropout-guard.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "dropout-guard.jar"]