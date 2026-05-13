package com.symphony.todo.todos

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/todos")
class TodoController(
	private val service: TodoService,
) {
	@GetMapping
	fun listTodos(): List<Todo> = service.listTodos()

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun createTodo(@Valid @RequestBody request: CreateTodoRequest): Todo =
		service.createTodo(request)

	@PatchMapping("/{id}")
	fun updateTodo(
		@PathVariable id: Long,
		@RequestBody request: UpdateTodoRequest,
	): Todo = service.updateTodo(id, request)

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun deleteTodo(@PathVariable id: Long) {
		service.deleteTodo(id)
	}
}
