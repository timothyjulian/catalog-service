package me.timothy.catalog.service.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
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
    fun addCourse_validation() {
        val courseDTO = CourseDTO(null, "", "Tim")

        every { courseServiceMockk.addCourse(any()) }.returns(courseDTO(1))

        val responseBody = webTestClient.post().uri("/v1/courses").bodyValue(courseDTO).exchange()
            .expectStatus().isBadRequest.expectBody(String::class.java).returnResult().responseBody

        Assertions.assertEquals(responseBody, "courseDTO.name must not be blank")
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

    @Test
    fun updateCourse() {
        val courseDTO = CourseDTO(
            null,
            "Build RestFul APis using SpringBoot and Kotlin 999", "Development"
        )

        every { courseServiceMockk.updateCourse(any(), any()) }.returns(courseDTO.copy(id = 1))

        val responseBody = webTestClient.put().uri("/v1/courses/{courseId}", 1).bodyValue(courseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(
                CourseDTO::class.java
            ).returnResult()
            .responseBody

        Assertions.assertEquals("Build RestFul APis using SpringBoot and Kotlin 999", responseBody!!.name)
    }

    @Test
    fun deleteCourse() {
        every { courseServiceMockk.deleteCourse(any()) } just runs

        webTestClient.delete().uri("/v1/courses/{courseId}", 1)
            .exchange()
            .expectStatus().isNoContent
    }
}