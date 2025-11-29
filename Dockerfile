# Multi-stage build
FROM eclipse-temurin:21-jdk-jammy as builder
WORKDIR /workspace/app

# Копируем Maven Wrapper и исходники
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Проверка на соответствие названия проекта
RUN if ! grep -q "balance-service" pom.xml; then \
      echo "❌ ERROR: Wrong project!"; \
      exit 1; \
    fi



# Даем права на выполнение mvnw
RUN chmod +x mvnw

# Собираем JAR
RUN ./mvnw clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/app/target/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]