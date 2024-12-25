FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} backend-qlsv.jar

ENTRYPOINT ["java", "-jar", "backend-qlsv.jar"]

EXPOSE 8080