FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/ticket-service.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-XX:MaxMetaspaceSize=128m", "-jar", "app.jar"]
