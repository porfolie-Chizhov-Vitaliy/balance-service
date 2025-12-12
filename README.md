# balance-service
Микросервис проверки баланса -  Java, Spring Boot, Kafka, Redis

## Что делает данный сервис ?
- Создает уведомление через REST API
- Проверка создание таблиц в Postgres с помощью flyway
- Сохраняет данные в Postgres
- Принимает и отправляет события в Kafka
- Предоставляет метрики для мониторинга (Prometheus)
- Swagger UI для REST API запросов ( http://localhost:8082/swagger-ui/index.html)
  
## Основные команды:
### Bash:
**Сборка ** `./mvnw clean package -DskipTests` 

**Проверить работоспособность сервиса** curl http://localhost:8082/actuator/health

### Docker:

**Ссылка на Docker image:** https://hub.docker.com/r/chizhovvm/balance-service

**Сборка образа** `docker build -t balance-service .`

**Загрузка образа**  `docker pull chizhovvm/balance-service:latest`

**Публикация в Docker Hub**
```
docker tag payment-service chizhovvm/balance-service
docker push chizhovvm/balance-service
```
