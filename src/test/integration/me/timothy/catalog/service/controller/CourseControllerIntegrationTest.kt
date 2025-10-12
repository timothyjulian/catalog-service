package me.timothy.catalog.service.controller

import me.timothy.catalog.service.dto.CourseDTO
import me.timothy.catalog.service.entity.Course
import me.timothy.catalog.service.repository.CourseRepository
import me.timothy.catalog.service.repository.InstructorRepository
import me.timothy.catalog.service.util.courseEntityList
import me.timothy.catalog.service.util.instructorEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntegrationTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        val instructorEntity = instructorEntity()
        instructorRepository.save(instructorEntity)

        val courseEntityList = courseEntityList(instructorEntity)
        courseRepository.saveAll(courseEntityList)
    }

    @Test
    fun addCourse() {

        val instructorEntity = instructorRepository.findAll().first()

        val courseDTO = CourseDTO(null, "Test Course", "Tim", instructorEntity.id)

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


    @Test
    fun retrieveAllCourses_ByName() {

        val uri =
            UriComponentsBuilder.fromUriString("/v1/courses").queryParam("course_name", "SpringBoot").toUriString()

        val returnResult =
            webTestClient.get().uri(uri).exchange().expectStatus().isOk.expectBodyList(CourseDTO::class.java)
                .returnResult().responseBody

        println("coursesDTO $returnResult")
        Assertions.assertEquals(returnResult!!.size, 2)
    }

    @Test
    fun updateCourse() {
        val instructorEntity = instructorRepository.findAll().first()
        val course = Course(
            null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development",
            instructorEntity
        )
        courseRepository.save(course)


        val courseDTO = CourseDTO(
            null,
            "Build RestFul APis using SpringBoot and Kotlin 999", "Development",
            instructorEntity.id
        )

        val responseBody = webTestClient.put().uri("/v1/courses/{courseId}", course.id).bodyValue(courseDTO)
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
        val instructorEntity = instructorRepository.findAll().first()
        val course = Course(
            null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development",
            instructorEntity
        )
        courseRepository.save(course)

        webTestClient.delete().uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus().isNoContent
    }


}