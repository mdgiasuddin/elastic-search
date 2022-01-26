package com.example.elasticsearchdemo.controllers;

import com.example.elasticsearchdemo.documents.Person;
import com.example.elasticsearchdemo.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonController {

    private final PersonService personService;

    @PostMapping
    public void savePerson(@RequestBody @Validated Person person) {
        personService.savePerson(person);
    }

    @GetMapping("/{id}")
    public Person findPersonById(@PathVariable String id) {
        return personService.findPersonById(id);
    }

    @GetMapping("/name/{name}")
    public List<Person> findPersonByName(@PathVariable String name) {
        return personService.findPersonByName(name);
    }
}
