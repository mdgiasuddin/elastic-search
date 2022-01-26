package com.example.elasticsearchdemo.services;

import com.example.elasticsearchdemo.documents.Vehicle;
import com.example.elasticsearchdemo.search.dtos.SearchRequestDTO;
import com.example.elasticsearchdemo.search.utils.SearchUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.elasticsearchdemo.constants.Indices.VEHICLE_INDEX;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper;
    private final RestHighLevelClient highLevelClient;
    private final SearchUtil searchUtil;

    public boolean indexVehicle(final Vehicle vehicle) {
        try {
            final String vehicleAsString = objectMapper.writeValueAsString(vehicle);

            final IndexRequest request = new IndexRequest(VEHICLE_INDEX);
            request.id(vehicle.getId());
            request.source(vehicleAsString, XContentType.JSON);

            IndexResponse response = highLevelClient.index(request, RequestOptions.DEFAULT);
            return response != null && response.status().equals(RestStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public Vehicle getVehicleById(String id) {
        try {
            final GetResponse documentFields = highLevelClient.get(new GetRequest(VEHICLE_INDEX, id), RequestOptions.DEFAULT);
            if (documentFields == null || documentFields.isSourceEmpty()) {
                return null;
            }

            return objectMapper.readValue(documentFields.getSourceAsString(), Vehicle.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Vehicle> searchVehicle(final SearchRequestDTO requestDTO) {
        final SearchRequest searchRequest = searchUtil.buildSearchRequest(VEHICLE_INDEX, requestDTO);

        return getVehicleListBySearch(searchRequest);
    }

    public List<Vehicle> searchVehicle(final Date date) {
        final SearchRequest searchRequest = searchUtil.buildSearchRequest(VEHICLE_INDEX, "createdDate", date);

        return getVehicleListBySearch(searchRequest);
    }

    public List<Vehicle> searchVehicle(final SearchRequestDTO requestDTO, final Date date) {
        final SearchRequest searchRequest = searchUtil.buildSearchRequest(VEHICLE_INDEX, requestDTO, date);

        return getVehicleListBySearch(searchRequest);
    }

    public List<Vehicle> getVehicleListBySearch(final SearchRequest searchRequest) {
        if (searchRequest == null) {
            log.error("Failed to build search request!");
            return Collections.emptyList();
        }

        try {
            final SearchResponse response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            final SearchHit[] searchHits = response.getHits().getHits();

            final List<Vehicle> vehicleList = new ArrayList<>(searchHits.length);

            for (SearchHit hit : searchHits) {
                vehicleList.add(objectMapper.readValue(hit.getSourceAsString(), Vehicle.class));
            }

            return vehicleList;

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void addVehicleList() throws ParseException {
        List<Vehicle> vehicleList = Arrays.asList(
                new Vehicle("5", "BBCCDD", "Toyota", new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-05")),
                new Vehicle("6", "BBMMDD", "New Toyota", new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-05")),
                new Vehicle("7", "QZCCDD", "Prado", new SimpleDateFormat("yyyy-MM-dd").parse("2019-08-05")),
                new Vehicle("8", "BBCMND", "New Prado", new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-01")),
                new Vehicle("9", "KLMCDD", "Noah Gold", new SimpleDateFormat("yyyy-MM-dd").parse("2019-03-08")),
                new Vehicle("10", "BBIYUD", "New Noah Gold", new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-05")),
                new Vehicle("11", "BBIJUH", "New Yamaha", new SimpleDateFormat("yyyy-MM-dd").parse("2019-04-05")),
                new Vehicle("12", "BBIBUD", "New Sujuki Zixer", new SimpleDateFormat("yyyy-MM-dd").parse("2019-05-12"))
        );

        for (Vehicle vehicle : vehicleList) {
            indexVehicle(vehicle);
        }
    }
}
