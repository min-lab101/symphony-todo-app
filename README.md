# Symphony Todo App

A small todo app designed to test OpenAI Symphony.

## Structure

- backend: Kotlin + Spring Boot
- apps/web: React + Vite web app
- packages/shared: shared TypeScript API client and types
- mobile: planned React Native app
- docs: API and architecture notes

## Development

Install Java 21 and Node.js 22 or newer.

Run the backend:

```bash
cd backend
./gradlew bootRun
```

Run the web app:

```bash
npm install
npm run web:dev
```

The web app runs on http://localhost:5173 and proxies `/api` requests to
http://localhost:8080.

Check the frontend:

```bash
npm run typecheck
npm run web:build
```

Check the backend:

```bash
cd backend
./gradlew test
```
