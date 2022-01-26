package com.example.elasticsearchdemo.services;

import com.example.elasticsearchdemo.documents.Person;
import com.example.elasticsearchdemo.repositories.elastic.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private final PersonRepository personRepository;

    public void savePerson(final Person person) {
        personRepository.save(person);
    }

    public Person findPersonById(final String id) {
        return personRepository.findById(id).orElse(null);
    }

    public List<Person> findPersonByName(final String name) {
        return personRepository.findByName(name);
    }
}
