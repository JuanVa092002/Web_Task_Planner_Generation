# syntax=docker/dockerfile:1

FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw
COPY src src
RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:21-jre-jammy AS runner
WORKDIR /app
RUN useradd -r -u 1001 appuser
COPY --from=builder /app/target/taskplanner-0.0.1-SNAPSHOT.jar app.jar
RUN chown appuser:appuser app.jar
USER appuser
ENV PORT=10000
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "app.jar"]
