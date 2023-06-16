# Scalajs backend app

## Routes

### GET /todos

Returns a list of todos tasks

_Response example:_

```json
[
  {
    "id": 1,
    "title": "Todo 1",
    "completed": false
  },
  {
    "id": 2,
    "title": "Todo 2",
    "completed": false
  },
  {
    "id": 3,
    "title": "Todo 3",
    "completed": false
  }
]
```

### GET /todos/:id

Returns a todo task

_Response example:_

```json
{
  "id": 1,
  "title": "Todo 1",
  "completed": false
}
```

### POST /todos?title=string

Creates a todo task

_Response example:_

```json
{
  "id": 1,
  "title": "Todo 1",
  "completed": false
}
```

### PUT /todos/:id?title=string

Updates a todo task by changing the title field

_Response example:_

```json
{
  "id": 1,
  "title": "Todo 1",
  "completed": false
}
```

### POST /todos/:id/completed

Updates a todo task by changing the completed status

_Response example:_

```json
{
  "id": 1,
  "title": "Todo 1",
  "completed": true
}
```

### DELETE /todos/:id

Deletes a todo task

_Response example:_

```text
Task 1 has been deleted
```

### DELETE /todos/completed

Deletes all completed todo tasks

_Response example:_

```text
All completed tasks have been deleted
```
