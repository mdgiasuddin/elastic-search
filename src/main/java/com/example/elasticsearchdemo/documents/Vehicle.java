package com.example.elasticsearchdemo.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {

    private String id;
    private String number;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;
}
