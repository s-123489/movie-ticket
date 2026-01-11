FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/movie-service.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-XX:MaxMetaspaceSize=128m", "-jar", "app.jar"]
