FROM springci/graalvm-ce:java11-0.12.x

WORKDIR /app

COPY target/goals-service-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]
