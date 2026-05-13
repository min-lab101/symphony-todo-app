package com.symphony.todo.common

import com.symphony.todo.todos.TodoNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

	@ExceptionHandler(TodoNotFoundException::class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	fun handleTodoNotFound(exception: TodoNotFoundException): ErrorResponse =
		ErrorResponse(message = exception.message ?: "Todo not found")

	@ExceptionHandler(MethodArgumentNotValidException::class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	fun handleValidationError(): ErrorResponse =
		ErrorResponse(message = "Invalid request")
}

data class ErrorResponse(
	val message: String,
)
