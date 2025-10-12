package me.timothy.catalog.service.repository

import me.timothy.catalog.service.entity.Instructor
import org.springframework.data.repository.CrudRepository

interface InstructorRepository : CrudRepository<Instructor, Int> {
}