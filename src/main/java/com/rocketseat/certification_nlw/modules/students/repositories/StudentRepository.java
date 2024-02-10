package com.rocketseat.certification_nlw.modules.students.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rocketseat.certification_nlw.modules.students.entities.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, UUID> {
    //faz parte do "StudentEntity", então não precisa usar uma "querry"
    //retornando um "Optional" para não aparecer "null", caso não tenha student cadastrado
    public Optional<StudentEntity> findByEmail(String email);
}
