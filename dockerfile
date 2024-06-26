FROM eclipse-temurin:17-jre-alpine AS final

# Copy the JAR file into the image

ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/apphome" \
    --shell "/sbin/nologin" \
    --uid "${UID}" \
    appuser


WORKDIR /apphome

# Copy the JAR file into the user's home directory
COPY app-0.0.1-SNAPSHOT.jar app.jar


# Switch to the appuser
USER appuser

# Expose port 4444
EXPOSE 4444

# Set the working directory to the user's home directory and run the application
CMD ["java", "-jar", "app.jar"]
