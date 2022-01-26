package com.example.elasticsearchdemo.controllers;

import com.example.elasticsearchdemo.documents.Vehicle;
import com.example.elasticsearchdemo.search.dtos.SearchRequestDTO;
import com.example.elasticsearchdemo.services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public boolean indexVehicle(@RequestBody @Validated Vehicle vehicle) {
        return vehicleService.indexVehicle(vehicle);
    }

    @GetMapping("/{id}")
    public Vehicle getVehicleById(@PathVariable String id) {
        return vehicleService.getVehicleById(id);
    }

    @PostMapping("/search")
    public List<Vehicle> searchVehicles(@RequestBody @Validated SearchRequestDTO requestDTO) {
        return vehicleService.searchVehicle(requestDTO);
    }

    @GetMapping("/search/date/{createDate}")
    public List<Vehicle> searchVehicles(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date createDate) {
        return vehicleService.searchVehicle(createDate);
    }

    @PostMapping("/search/complex/{createDate}")
    public List<Vehicle> searchVehicles(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date createDate, @RequestBody @Validated SearchRequestDTO requestDTO) {
        return vehicleService.searchVehicle(requestDTO, createDate);
    }

    @PostMapping("/new/dummy")
    public void createDummyVehicle() throws ParseException {
        vehicleService.addVehicleList();
    }
}
