
FROM eclipse-temurin:17-jre-alpine AS final

COPY app-0.0.1-SNAPSHOT.jar app.jar

ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser

RUN  chown appuser:appuser app.jar

USER appuser
 
# Set the command to run the application

EXPOSE 4444
 
CMD ["java", "-jar", "app.jar"]

