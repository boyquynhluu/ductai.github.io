# Sử dụng image Java 11 chính thức từ Docker Hub
#FROM openjdk:11-jdk-slim
FROM openjdk:17-jdk-slim

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Copy file JAR vào thư mục làm việc trong container
COPY target/qlsv-0.0.1-SNAPSHOT.jar /app/qlsv-0.0.1-SNAPSHOT.jar

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "/app/qlsv-0.0.1-SNAPSHOT.jar"]