package me.timothy.catalog.service.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import me.timothy.catalog.service.contoller.CourseController
import me.timothy.catalog.service.dto.CourseDTO
import me.timothy.catalog.service.service.CourseService
import me.timothy.catalog.service.util.courseDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMockk: CourseService


    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(null, "Test Course", "Tim")

        every { courseServiceMockk.addCourse(any()) }.returns(courseDTO(1))

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
        every { courseServiceMockk.retrieveAllCourses() }.returnsMany(
            listOf(courseDTO(1), courseDTO(2), courseDTO(3))
        )
        val returnResult =
            webTestClient.get().uri("/v1/courses").exchange().expectStatus().isOk.expectBodyList(CourseDTO::class.java)
                .returnResult().responseBody

        println("coursesDTO $returnResult")
        Assertions.assertEquals(returnResult!!.size, 3)
    }
}