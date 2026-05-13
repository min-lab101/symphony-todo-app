package com.symphony.todo.todos

import jakarta.validation.constraints.NotBlank

data class Todo(
	val id: Long,
	val title: String,
	val completed: Boolean,
)

data class CreateTodoRequest(
	@field:NotBlank
	val title: String,
)

data class UpdateTodoRequest(
	val title: String?,
	val completed: Boolean?,
)
