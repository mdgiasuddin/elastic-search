package com.example.elasticsearchdemo.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.elasticsearchdemo.repositories")
@ComponentScan(basePackages = "com.example.elasticsearchdemo")
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch.url}")
    private String elasticsearchUrl;

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo(elasticsearchUrl)
                .build();

        return RestClients.create(configuration).rest();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
