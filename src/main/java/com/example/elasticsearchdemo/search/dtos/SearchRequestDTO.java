package com.example.elasticsearchdemo.search.dtos;

import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;

@Data
public class SearchRequestDTO {
    private List<String> fields;
    private String searchTerm;
    private String sortBy;
    private SortOrder sortOrder;
}
