package com.example.elasticsearchdemo.search.utils;

import com.example.elasticsearchdemo.search.dtos.SearchRequestDTO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SearchUtil {

    public SearchRequest buildSearchRequest(final String indexName, final SearchRequestDTO requestDTO) {
        try {

            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .postFilter(getQueryBuilder(requestDTO));

            if (requestDTO.getSortBy() != null) {
                builder = builder.sort(requestDTO.getSortBy(), requestDTO.getSortOrder() == null ? SortOrder.ASC : requestDTO.getSortOrder());
            }
            SearchRequest request = new SearchRequest(indexName);
            request.source(builder);

            return request;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public QueryBuilder getQueryBuilder(final SearchRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        if (requestDTO.getFields().isEmpty()) {
            return null;
        }

        if (requestDTO.getFields().size() > 1) {
            MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(requestDTO.getSearchTerm())
                    .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
                    .operator(Operator.OR);

            requestDTO.getFields().forEach(queryBuilder::field);

            return queryBuilder;
        }

        return requestDTO.getFields()
                .stream()
                .findFirst()
                .map(field -> QueryBuilders.matchQuery(field, requestDTO.getSearchTerm())
                        .operator(Operator.OR))
                .orElse(null);
    }

    public SearchRequest buildSearchRequest(final String indexName, final String field, final Date date) {
        try {
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .postFilter(getRangeQueryBuilder(field, date));

            SearchRequest request = new SearchRequest(indexName);
            request.source(builder);

            return request;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public QueryBuilder getRangeQueryBuilder(final String field, final Date date) {
        return QueryBuilders.rangeQuery(field).gte(date);
    }

    public SearchRequest buildSearchRequest(final String indexName, final SearchRequestDTO requestDTO, final Date createdDate) {
        try {

            QueryBuilder rangeQuery = getRangeQueryBuilder("createdDate", createdDate);
            QueryBuilder matchQuery = getQueryBuilder(requestDTO);

            final BoolQueryBuilder boolQuery = new BoolQueryBuilder()
                    .must(rangeQuery)
                    .mustNot(matchQuery);

            // from index to size for pagination
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .from(2)
                    .size(2)
                    .postFilter(boolQuery);

            if (requestDTO.getSortBy() != null) {
                builder = builder.sort(requestDTO.getSortBy(), requestDTO.getSortOrder() == null ? SortOrder.ASC : requestDTO.getSortOrder());
            }

            SearchRequest request = new SearchRequest(indexName);
            request.source(builder);

            return request;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
