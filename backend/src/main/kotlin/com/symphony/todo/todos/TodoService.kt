package com.symphony.todo.todos

import org.springframework.stereotype.Service

@Service
class TodoService(
	private val repository: TodoRepository,
) {
	fun listTodos(): List<Todo> = repository.findAll()

	fun createTodo(request: CreateTodoRequest): Todo =
		repository.create(title = request.title.trim())

	fun updateTodo(id: Long, request: UpdateTodoRequest): Todo {
		val existing = repository.findById(id) ?: throw TodoNotFoundException(id)
		val updated = existing.copy(
			title = request.title?.trim() ?: existing.title,
			completed = request.completed ?: existing.completed,
		)

		return repository.update(updated)
	}

	fun deleteTodo(id: Long) {
		if (!repository.deleteById(id)) {
			throw TodoNotFoundException(id)
		}
	}
}
