package com.symphony.todo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class TodoApplicationTests {

	@Autowired
	lateinit var mockMvc: MockMvc

	@Test
	fun contextLoads() {
	}

	@Test
	fun `health endpoint returns ok`() {
		mockMvc.get("/health")
			.andExpect {
				status { isOk() }
				content { json("""{"status":"ok"}""") }
			}
	}

	@Test
	fun `todo endpoints create list update and delete todos`() {
		mockMvc.post("/todos") {
			contentType = MediaType.APPLICATION_JSON
			content = """{"title":"Write Symphony workflow"}"""
		}
			.andExpect {
				status { isCreated() }
				content { json("""{"id":1,"title":"Write Symphony workflow","completed":false}""") }
			}

		mockMvc.get("/todos")
			.andExpect {
				status { isOk() }
				content { json("""[{"id":1,"title":"Write Symphony workflow","completed":false}]""") }
			}

		mockMvc.patch("/todos/1") {
			contentType = MediaType.APPLICATION_JSON
			content = """{"completed":true}"""
		}
			.andExpect {
				status { isOk() }
				content { json("""{"id":1,"title":"Write Symphony workflow","completed":true}""") }
			}

		mockMvc.delete("/todos/1")
			.andExpect {
				status { isNoContent() }
			}

		mockMvc.get("/todos")
			.andExpect {
				status { isOk() }
				content { json("""[]""") }
			}
	}

	@Test
	fun `creating a todo requires a title`() {
		mockMvc.post("/todos") {
			contentType = MediaType.APPLICATION_JSON
			content = """{"title":""}"""
		}
			.andExpect {
				status { isBadRequest() }
				content { json("""{"message":"Invalid request"}""") }
			}
	}

	@Test
	fun `updating a missing todo returns not found`() {
		mockMvc.patch("/todos/999") {
			contentType = MediaType.APPLICATION_JSON
			content = """{"completed":true}"""
		}
			.andExpect {
				status { isNotFound() }
				content { json("""{"message":"Todo not found: 999"}""") }
			}
	}
}
