package com.example.elasticsearchdemo.services;

import com.example.elasticsearchdemo.utils.ResourceUtil;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

import static com.example.elasticsearchdemo.constants.Indices.VEHICLE_INDEX;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IndexService {

    private final RestHighLevelClient restHighLevelClient;
    private final ResourceUtil resourceUtil;

    private final List<String> INDICES_TO_CREATE = Arrays.asList(VEHICLE_INDEX);
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void tryToCreateIndices() {
        recreateIndices(false);
    }

    public void recreateIndices(final boolean deleteExisting) {

        final String setting = resourceUtil.loadAsString("static/es-setting.json");

        for (final String indexName : INDICES_TO_CREATE) {
            try {
                boolean indexExists = restHighLevelClient.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);

                if (indexExists) {
                    if (!deleteExisting)
                        continue;

                    restHighLevelClient.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
                }

                final CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
                indexRequest.settings(setting, XContentType.JSON);

                final String mappings = resourceUtil.loadAsString("static/mappings/" + indexName + ".json");
                if (mappings != null)
                    indexRequest.mapping(mappings, XContentType.JSON);

                restHighLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
