package com.example.elasticsearchdemo.services;

import com.example.elasticsearchdemo.documents.Person;
import com.example.elasticsearchdemo.repositories.elastic.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.elasticsearchdemo.constants.Indices.PERSON_INDEX;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final PersonRepository personRepository;
    private final RestHighLevelClient restHighLevelClient;
    private final ObjectMapper objectMapper;

    public void savePerson(final Person person) {
        personRepository.save(person);
    }

    public Person findPersonById(final String id) {
        return personRepository.findById(id).orElse(null);
    }

    public List<Person> getAllPerson() {
        List<Person> people = new ArrayList<>();
        personRepository.findAll().forEach(people::add);

        return people;
    }

    public void deleteAllPerson() {
        personRepository.deleteAll();
    }

    public List<Person> saveAllPerson(List<Person> people) {
        List<Person> personList = new ArrayList<>();
        personRepository.saveAll(people).forEach(personList::add);

        return personList;
    }

    public String deletePersonIndex() {
        try {
            boolean indexExists = restHighLevelClient.indices().exists(new GetIndexRequest(PERSON_INDEX), RequestOptions.DEFAULT);
            restHighLevelClient.indices().delete(new DeleteIndexRequest(PERSON_INDEX), RequestOptions.DEFAULT);

            return "Index deleted successfully!";
        } catch (IOException e) {
            e.printStackTrace();

            return "Index could not be deleted! " + e.getMessage();
        }
    }

    public List<Person> searchByMultiFields(String firstName, int age) {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("firstName", firstName))
                .must(QueryBuilders.matchQuery("age", age));

        SearchSourceBuilder builder = new SearchSourceBuilder()
                .postFilter(queryBuilder);

        SearchRequest request = new SearchRequest(PERSON_INDEX);
        request.source(builder);

        return getSearchResult(request);

    }

    public List<Person> searchByInput(String input) {

        String regex = ".*" + input;

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.regexpQuery("firstName", regex));

        SearchSourceBuilder builder = new SearchSourceBuilder()
                .postFilter(queryBuilder);

        SearchRequest request = new SearchRequest(PERSON_INDEX);
        request.source(builder);

        return getSearchResult(request);

    }

    public List<Person> searchBySameValue(String input) {

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.multiMatchQuery(input).field("firstName").field("lastName"));

        SearchSourceBuilder builder = new SearchSourceBuilder()
                .postFilter(queryBuilder);

        SearchRequest request = new SearchRequest(PERSON_INDEX);
        request.source(builder);

        return getSearchResult(request);

    }

    private List<Person> getSearchResult(final SearchRequest request) {

        if (request == null) {
            log.error("Failed to build search request!");
            return Collections.emptyList();
        }

        try {
            final SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            final SearchHit[] searchHits = response.getHits().getHits();

            final List<Person> people = new ArrayList<>(searchHits.length);

            for (SearchHit hit : searchHits) {
                people.add(objectMapper.readValue(hit.getSourceAsString(), Person.class));
            }

            return people;

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
