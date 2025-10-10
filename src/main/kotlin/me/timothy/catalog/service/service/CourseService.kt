package me.timothy.catalog.service.service

import me.timothy.catalog.service.dto.CourseDTO
import me.timothy.catalog.service.entity.Course
import me.timothy.catalog.service.exception.CourseNotFoundException
import me.timothy.catalog.service.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(
    val courseRepository: CourseRepository
) {

    companion object : KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO {
        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category)
        }

        courseRepository.save(courseEntity)

        logger.info("Saved course is: $courseEntity")

        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category)
        }
    }

    fun retrieveAllCourses(): List<CourseDTO> {
        return courseRepository.findAll().map { course -> CourseDTO(course.id, course.name, course.category) }
    }

    fun updateCourse(courseId: Int, courseDTO: CourseDTO): CourseDTO {
        val existingCourse = courseRepository.findById(courseId)

        return if (existingCourse.isPresent) {
            existingCourse.get().let {
                it.name = courseDTO.name
                it.category = courseDTO.category

                courseRepository.save(it)

                CourseDTO(it.id, it.name, it.category)
            }
        } else{
            throw CourseNotFoundException("No course found for: $courseId")
        }
    }

}
