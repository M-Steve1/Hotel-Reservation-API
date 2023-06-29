FROM eclipse-temurin:17-jdk-alpine
VOLUME /temp
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--server.port=8083"]
EXPOSE 8083