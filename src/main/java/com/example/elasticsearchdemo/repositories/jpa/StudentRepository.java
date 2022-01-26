package com.example.elasticsearchdemo.repositories.jpa;

import com.example.elasticsearchdemo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
