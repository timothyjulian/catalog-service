package me.timothy.catalog.service.repository

import me.timothy.catalog.service.util.courseEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntegrationTest {

    @Autowired
    lateinit var courseRepository: CourseRepository

    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        val courseEntityList = courseEntityList()
        courseRepository.saveAll(courseEntityList)
    }

    @Test
    fun findByNameContaining() {
        val courses = courseRepository.findByNameContaining("SpringBoot")
        println(courses)

        Assertions.assertEquals(2, courses.size)
    }

    @Test
    fun findCoursesByName() {
        val courses = courseRepository.findCoursesByName("SpringBoot")
        println(courses)

        Assertions.assertEquals(2, courses.size)
    }


    @ParameterizedTest
    @MethodSource(value = ["courseAndSize"])
    fun findCoursesByName_approach2(name: String, expectedSize: Int) {
        val courses = courseRepository.findCoursesByName(name)
        println(courses)

        Assertions.assertEquals(expectedSize, courses.size)
    }

    companion object {

        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("SpringBoot", 2),
                Arguments.arguments("Wiremock", 1)
            )
        }
    }
}