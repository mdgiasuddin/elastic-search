package com.example.elasticsearchdemo.repositories.elastic;

import com.example.elasticsearchdemo.documents.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PersonRepository extends ElasticsearchRepository<Person, String> {
}
