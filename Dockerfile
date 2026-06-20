FROM eclipse-temurin:26-jdk AS builder

WORKDIR /app

COPY . .

RUN chmod +x gradlew || true
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:26-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]