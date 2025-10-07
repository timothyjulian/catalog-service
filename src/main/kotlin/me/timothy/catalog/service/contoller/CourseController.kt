package me.timothy.catalog.service.contoller

import me.timothy.catalog.service.dto.CourseDTO
import me.timothy.catalog.service.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/courses")
class CourseController(
    val courseService: CourseService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody courseDTO: CourseDTO) : CourseDTO {
        return courseService.addCourse(courseDTO)
    }

}