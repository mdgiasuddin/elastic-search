package com.example.elasticsearchdemo.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import static com.example.elasticsearchdemo.constants.Indices.PERSON_INDEX;

@Data
@Document(indexName = PERSON_INDEX)
@Setting(settingPath = "static/es-setting.json")
public class Person {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String name;
}
