
all: local-all-up

local-all-up: local-dev-up local-boot-up

local-dev-up:
	docker compose up -d

local-dev-down:
	docker compose down

local-boot-up:
	./gradlew bootRun
