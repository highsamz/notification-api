up:
	docker compose up -d

down:
	docker compose down

restart:
	docker compose down
	docker compose up -d

logs:
	docker compose logs -f

app:
	./mvnw spring-boot:run

test:
	./mvnw test

build:
	./mvnw clean package

status:
	docker compose ps