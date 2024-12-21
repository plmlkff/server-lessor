include server-lessor-back/.env

export

dev-up-postgres:
	docker-compose -f docker-compose.dev.yaml up postgres -d

dev-down-postgres:
	docker-compose -f docker-compose.dev.yaml down postgres
