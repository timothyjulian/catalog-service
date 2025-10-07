package me.timothy.catalog.service.controller

import me.timothy.catalog.service.dto.CourseDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntegrationTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(null, "Test Course", "Tim")

        val responseBody =
            webTestClient.post().uri("/v1/courses").bodyValue(courseDTO).exchange().expectStatus().isCreated.expectBody(
                CourseDTO::class.java
            ).returnResult().responseBody

        Assertions.assertTrue {
            responseBody!!.id != null
        }
    }
}