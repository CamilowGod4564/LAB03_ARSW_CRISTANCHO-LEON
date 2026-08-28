# El informe esta en un archivo llamado INFORME
# Laboratorio #4 — REST API Blueprints

Aplicación REST de planos construida con Java 21 y Spring Boot 3.3.x.

## Requisitos

- Java 21
- Maven 3.9+
- Docker y Docker Compose (opcional, para PostgreSQL)

## Ejecutar localmente

1. Inicia PostgreSQL con las credenciales configuradas en `application.properties`, o levanta todo con Docker.
2. Ejecuta la aplicación:

```bash
mvn clean install
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080/api/v1/blueprints`.

## Ejecutar con Docker

```bash
docker compose up --build
```

Para detener los contenedores sin eliminar los datos:

```bash
docker compose down
```

## Endpoints

| Método | Ruta | Resultado exitoso |
| --- | --- | --- |
| GET | `/api/v1/blueprints` | `200 OK` |
| GET | `/api/v1/blueprints/{author}` | `200 OK` |
| GET | `/api/v1/blueprints/{author}/{name}` | `200 OK` |
| POST | `/api/v1/blueprints` | `201 Created` |
| PUT | `/api/v1/blueprints/{author}/{name}/points` | `202 Accepted` |

Todas las respuestas usan el formato:

```json
{
  "code": 200,
  "message": "Success",
  "data": {}
}
```

Ejemplos de uso:

```bash
curl -s http://localhost:8080/api/v1/blueprints
curl -s http://localhost:8080/api/v1/blueprints/john
curl -s http://localhost:8080/api/v1/blueprints/john/house
curl -i -X POST http://localhost:8080/api/v1/blueprints -H "Content-Type: application/json" -d '{"author":"john","name":"kitchen","points":[{"x":1,"y":1},{"x":2,"y":2}]}'
curl -i -X PUT http://localhost:8080/api/v1/blueprints/john/kitchen/points -H "Content-Type: application/json" -d '{"x":3,"y":3}'
```

## Filtros de puntos

De forma predeterminada se usa `IdentityFilter`. Los perfiles `redundancy` y `undersampling` son excluyentes y seleccionan un único filtro:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=redundancy
mvn spring-boot:run -Dspring-boot.run.profiles=undersampling
```

- `redundancy`: elimina puntos duplicados consecutivos.
- `undersampling`: conserva los puntos de índices pares.

Para Docker, define `SPRING_PROFILES_ACTIVE=redundancy` o `SPRING_PROFILES_ACTIVE=undersampling` en el servicio `app`.

## Documentación y pruebas

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Pruebas: `mvn test`

La persistencia principal usa PostgreSQL mediante JPA. Las pruebas usan H2 en memoria con el perfil `test`.
