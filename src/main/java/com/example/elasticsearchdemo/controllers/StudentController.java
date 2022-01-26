package com.example.elasticsearchdemo.controllers;

import com.example.elasticsearchdemo.model.Student;
import com.example.elasticsearchdemo.repositories.jpa.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentController {

    private final StudentRepository studentRepository;

    @GetMapping
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }
}
