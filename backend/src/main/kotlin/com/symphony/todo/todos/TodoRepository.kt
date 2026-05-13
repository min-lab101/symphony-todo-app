package com.symphony.todo.todos

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

interface TodoRepository {
	fun findAll(): List<Todo>
	fun findById(id: Long): Todo?
	fun create(title: String): Todo
	fun update(todo: Todo): Todo
	fun deleteById(id: Long): Boolean
}

@Repository
class InMemoryTodoRepository : TodoRepository {
	private val nextId = AtomicLong(1)
	private val todos = ConcurrentHashMap<Long, Todo>()

	override fun findAll(): List<Todo> = todos.values.sortedBy { it.id }

	override fun findById(id: Long): Todo? = todos[id]

	override fun create(title: String): Todo {
		val todo = Todo(
			id = nextId.getAndIncrement(),
			title = title,
			completed = false,
		)
		todos[todo.id] = todo
		return todo
	}

	override fun update(todo: Todo): Todo {
		todos[todo.id] = todo
		return todo
	}

	override fun deleteById(id: Long): Boolean = todos.remove(id) != null
}
