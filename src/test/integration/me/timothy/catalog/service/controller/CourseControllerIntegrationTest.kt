package me.timothy.catalog.service.controller

import me.timothy.catalog.service.dto.CourseDTO
import me.timothy.catalog.service.repository.CourseRepository
import me.timothy.catalog.service.util.courseEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
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

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        val courseEntityList = courseEntityList()
        courseRepository.saveAll(courseEntityList)
    }

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

    @Test
    fun retrieveAllCourses() {
        val returnResult =
            webTestClient.get().uri("/v1/courses").exchange().expectStatus().isOk.expectBodyList(CourseDTO::class.java)
                .returnResult().responseBody

        println("coursesDTO $returnResult")
        Assertions.assertEquals(returnResult!!.size, 3)
    }
}