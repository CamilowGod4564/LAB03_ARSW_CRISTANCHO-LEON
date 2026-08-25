# Blueprints API — Guía rápida

Esta guía te explica, paso a paso, cómo levantar la aplicación usando Docker.

## 1. Abrir una terminal en la carpeta del proyecto

Abre una terminal (CMD, PowerShell, o Terminal en Mac/Linux) 
y ubícate en la carpeta donde está el archivo `docker-compose.yml`. Por ejemplo:

```bash
cd ruta/a/la/carpeta/del/proyecto
```

## 2. Levantar la aplicación

Ejecuta este comando:

```bash
docker compose up
```

Esto va a:
- Descargar la imagen de PostgreSQL (la base de datos).
- Construir la imagen de la aplicación.
- Levantar ambos contenedores conectados entre sí.
  Si quieres que se ejecute "en segundo plano" (y así poder seguir usando la terminal), agrega `-d`:

```bash
docker compose up -d
```

## 3. Verificar que todo esté corriendo

```bash
docker compose ps
```

Deberías ver algo así, con ambos servicios en estado `running` o `healthy`:

```
NAME               IMAGE          STATUS
blueprints-db      postgres:15    Up (healthy)
blueprints-app     ...            Up
```

## 4. Probar la aplicación

Una vez que ambos contenedores estén arriba, abre tu navegador en:

```
http://localhost:8080
```

Si configuraste Swagger, la documentación estará en:

```
http://localhost:8080/swagger-ui.html
```


## 6. Apagar la aplicación

Cuando quieras detener todo:

```bash
docker compose down
```

Esto detiene y elimina los contenedores, pero **no borra los datos** guardados en la base de datos (gracias al volumen `postgres_data`).

Si además quieres borrar los datos guardados (empezar desde cero):

```bash
docker compose down -v
```

## Problemas comunes

- **El puerto 8080 o 5432 ya está en uso**: cierra la otra aplicación que lo esté usando, o cambia el puerto en `docker-compose.yml` (por ejemplo `"8081:8080"`).
- **La app no conecta a la base de datos**: espera unos segundos más, Postgres puede tardar en iniciar. El `docker-compose.yml` ya tiene un `healthcheck` para esperar a que la base esté lista antes de levantar la app.
- **Hice cambios en el código y no se reflejan**: necesitas reconstruir la imagen:
```bash
  docker compose up --build
```