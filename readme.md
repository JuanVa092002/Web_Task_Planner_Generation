# Planificador de Tareas

Aplicación web Full Stack para registrar, visualizar, actualizar y eliminar tareas.

- **Frontend:** HTML, Bootstrap y JavaScript (`client/`) — Vercel
- **Backend:** Java + Spring Boot (`server/`) — Render
- **Base de datos:** PostgreSQL (Neon)

## Cómo ejecutar en local

1. En `server/` copia `.env.example` a `.env` y completa las credenciales de Neon.

2. Inicia el backend:

```bash
cd server
./mvnw spring-boot:run
```

3. Abre `client/index.html` en el navegador, o sirve `client/` con cualquier static server.

API local: `http://localhost:8080/api/tasks`

## Deploy

| Parte | Plataforma | Root Directory |
| --- | --- | --- |
| Frontend | Vercel | `client` |
| Backend | Render | `server` |

El backend se despliega como **Docker** (`server/Dockerfile`). En Render configura `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD` (no subas `server/.env`).

En producción el frontend usa `https://web-task-planner-api.onrender.com`; en local sigue `http://localhost:8080`.

## Trello / Jira

[Tablero del proyecto](https://juancarlospastasvalencia.atlassian.net/jira/software/projects/PP/boards/35/backlog)

## Figma

[Wireframe del proyecto](https://www.figma.com/design/T3V2xudb4Cckh0Y6pgiSr7/Planner-Tareas-Web?node-id=4-15&t=ObOs78eYWvgdgk2w-1)
