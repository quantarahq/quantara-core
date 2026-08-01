FROM maven:3-eclipse-temurin-26 AS build
WORKDIR /workspace
COPY backend/pom.xml .
RUN mvn -q -B dependency:go-offline
COPY backend/src src
RUN mvn -q -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
