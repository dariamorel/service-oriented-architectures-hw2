### Запустить swagger

```shell
cd openapi
docker compose up
```

Open API можно посмотреть на http://localhost:8080/.

### Запустить кодогенерацию по Open API

```shell
./gradlew clean build
```

### Запустить сервис

```shell
docker compose up -d
./gradlew bootRun
```

Взаимодействовать с сервисом можно либо из консоли, либо из Swagger по адресу http://localhost:8080/swagger-ui.html.

Посмотреть на бд Postgre SQL можно на pgAdmin по адресу http://localhost:5050.

Параметры pgAdmin:
	- email: admin@example.com
	- password: admin

Параметры сервера:
	- Host name/address: postgres
	- Port: 5432
	- Maintenance database: demo_db
	- Username: postgres
	- Password: postgres