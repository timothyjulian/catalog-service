package me.timothy.catalog.service.service

import me.timothy.catalog.service.dto.InstructorDTO
import me.timothy.catalog.service.entity.Instructor
import me.timothy.catalog.service.repository.InstructorRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class InstructorService(val instructorRepository: InstructorRepository) {
    fun createInstructor(instructorDTO: InstructorDTO): InstructorDTO {
        val instructorEntity = instructorDTO.let { Instructor(it.id, it.name) }
        instructorRepository.save(instructorEntity)

        return instructorEntity.let { InstructorDTO(it.id, it.name) }
    }

    fun findByInstructorId(instructorId: Int) : Optional<Instructor> {
        return instructorRepository.findById(instructorId)
    }


}
