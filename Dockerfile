# 1. Use an official base image with Maven and JDK for building
FROM maven:3.9.9-eclipse-temurin-23 AS build

# 2. Set working directory for build
WORKDIR /build

# 3. Copy the source code into the container
COPY . .

# 4. Run Maven build if /target is empty
RUN if [ ! -d "target" ] || [ -z "$(ls -A target)" ]; then mvn clean package; fi

# 5. Use a smaller runtime image for the app
FROM eclipse-temurin:23-jdk-alpine

# 6. Set working directory for runtime
WORKDIR /app

# 7. Copy the built JAR file from the build stage
COPY --from=build /build/target//categorymanagement-0.0.1-SNAPSHOT.war app.war

# 8. Expose the port
EXPOSE 8080

# 9. Run the JAR file
ENTRYPOINT ["java", "-jar", "app.war"]

## 1. Use an official base image with Java 23
#FROM eclipse-temurin:23-jdk-alpine
#
## 2. Set working directory
#WORKDIR /app
#
## 3. Copy the Spring Boot WAR file into the container
#COPY target/categorymanagement-0.0.1-SNAPSHOT.war app.war
#
## 4. Expose the port
#EXPOSE 8080
#
## 5. Run the WAR file
#ENTRYPOINT ["java", "-jar", "app.war"]
