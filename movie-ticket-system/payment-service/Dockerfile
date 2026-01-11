FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/payment-service.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "-XX:MaxMetaspaceSize=128m", "-jar", "app.jar"]
