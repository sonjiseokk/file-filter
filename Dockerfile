# =================================================
# 1. 빌드 스테이지 (Build Stage)
# JDK를 사용하여 애플리케이션을 빌드합니다.
# =================================================
FROM eclipse-temurin:17-jdk-jammy as builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 관련 파일들을 먼저 복사하여 의존성 캐싱을 활용합니다.
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# 소스 코드 복사
COPY src ./src

# Gradle 실행 권한 부여 및 빌드 실행
# bootJar 태스크를 사용하여 실행 가능한 JAR 파일만 생성합니다.
RUN chmod +x ./gradlew && ./gradlew bootJar

# =================================================
# 2. 실행 스테이지 (Final Stage)
# 빌드된 JAR 파일을 JRE 기반의 경량 이미지로 옮겨 실행합니다.
# =================================================
FROM eclipse-temurin:17-jre-jammy

# 작업 디렉토리 설정
WORKDIR /app

# 보안을 위해 루트가 아닌 별도의 사용자 및 그룹 생성
RUN groupadd --system --gid 1001 spring && \
    useradd --system --uid 1001 --gid spring spring

# 빌드 스테이지에서 생성된 JAR 파일을 복사
# JAR 파일 이름은 build.gradle의 설정에 따라 달라질 수 있습니다.
COPY --from=builder /app/build/libs/file-filter-0.0.1-SNAPSHOT.jar app.jar

# 애플리케이션 디렉토리 및 파일의 소유권을 spring 사용자로 변경
RUN chown -R spring:spring /app

# spring 사용자로 전환
USER spring

# 컨테이너가 리스닝할 포트 설정 (Spring Boot 기본 포트)
EXPOSE 8080

# 컨테이너 실행 명령어
# 외부의 설정 파일을 /app/config/application.yaml 경로로 마운트하는 것을 가정합니다.
# Spring Boot는 이 경로의 설정 파일을 자동으로 로드하여 실행됩니다.
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/config/application.yaml"]
