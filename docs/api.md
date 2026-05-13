# API

Base URL: http://localhost:8080

## GET /health

Response:
```json
{
  "status": "ok"
}
```

## GET /todos

Response:
```json
[
  {
    "id": 1,
    "title": "Write Symphony workflow",
    "completed": false
  }
]
```

## POST /todos

Request:
```json
{
  "title": "Write Symphony workflow"
}
```

Response: `201 Created`
```json
{
  "id": 1,
  "title": "Write Symphony workflow",
  "completed": false
}
```

Validation error response: `400 Bad Request`
```json
{
  "message": "Invalid request"
}
```

## PATCH /todos/{id}

Request:
```json
{
  "title": "Write project workflow",
  "completed": true
}
```

All request fields are optional.

Response:
```json
{
  "id": 1,
  "title": "Write project workflow",
  "completed": true
}
```

Not found response: `404 Not Found`
```json
{
  "message": "Todo not found: 1"
}
```

## DELETE /todos/{id}

Response: `204 No Content`
