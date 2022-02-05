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

    @PostMapping("/all/save")
    public List<Person> saveAllPerson(@RequestBody @Validated List<Person> people) {
        return personService.saveAllPerson(people);
    }

    @GetMapping
    public List<Person> getAllPerson() {
        return personService.getAllPerson();
    }

    @DeleteMapping("/all")
    public void deleteAllPerson() {
        personService.deleteAllPerson();
    }

    @DeleteMapping("/index/delete")
    public String deleteIndex() {
        return personService.deletePersonIndex();
    }

    @GetMapping("/{id}")
    public Person findPersonById(@PathVariable String id) {
        return personService.findPersonById(id);
    }

    @GetMapping("/search/{firstName}/{age}")
    public List<Person> searchByMultiFields(@PathVariable String firstName, @PathVariable int age) {
        return personService.searchByMultiFields(firstName, age);
    }

    @GetMapping("/search/{firstName}")
    public List<Person> searchByInput(@PathVariable String firstName) {
        return personService.searchByInput(firstName);
    }

    @GetMapping("/search/same/{firstName}")
    public List<Person> searchSame(@PathVariable String firstName) {
        return personService.searchBySameValue(firstName);
    }
}
