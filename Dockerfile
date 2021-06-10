FROM openjdk:latest

COPY build/libs/IntegratedCA-0.0.1-SNAPSHOT.jar /app/Integratedca-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "/app/Integratedca-0.0.1-SNAPSHOT.jar"]