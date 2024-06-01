# 1단계: 빌드 단계
FROM gradle:7.3.3-jdk17 AS build
WORKDIR /app

# Gradle 빌드에 필요한 소스와 build.gradle, settings.gradle, Gradle Wrapper를 복사
COPY build.gradle settings.gradle gradlew* ./
COPY gradle ./gradle
COPY src ./src

# Gradle Wrapper를 사용하여 애플리케이션을 빌드
RUN ./gradlew build -x test

# 2단계: 실행 단계
FROM openjdk:17.0.2-slim
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=build /app/build/libs/*.jar /app/app.jar

## JVM 플래그 및 애플리케이션 실행
#CMD ["java", "-Dspring.profiles.active=prod", "-jar", "/app/app.jar"]
# JVM 플래그 및 애플리케이션 실행
CMD ["java", "-jar", "/app/app.jar"]

# 필요시 주석 처리된 jvmFlags 사용
# CMD ["java", "-Dspring.profiles.active=prod", "-Xms128m", "-Xmx128m", "-jar", "/app/app.jar"]