package me.timothy.catalog.service.repository

import me.timothy.catalog.service.entity.Course
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<Course, Int> {
}