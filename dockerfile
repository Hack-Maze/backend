# FROM maven:3.9.7-amazoncorretto-17-debian as build

# WORKDIR /build

# COPY pom.xml .
# COPY src ./src

# # Build the application using Maven
# RUN mvn clean package -DskipTests

 
FROM eclipse-temurin:17-jre-alpine AS final

ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser


# Copy the executable from the "package" stage.
# COPY  --from=build /build/target/app-0.0.1-SNAPSHOT.jar app.jar
# Set the command to run the application
COPY /target/app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 4444

CMD ["java", "-jar", "app.jar"]

