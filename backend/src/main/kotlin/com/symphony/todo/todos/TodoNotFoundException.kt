package com.symphony.todo.todos

class TodoNotFoundException(id: Long) : RuntimeException("Todo not found: $id")
