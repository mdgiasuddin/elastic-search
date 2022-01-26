package com.example.elasticsearchdemo.repositories;

import com.example.elasticsearchdemo.documents.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PersonRepository extends ElasticsearchRepository<Person, String> {

    List<Person> findByName(String name);
}
