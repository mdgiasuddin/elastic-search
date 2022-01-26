package com.example.elasticsearchdemo.controllers;

import com.example.elasticsearchdemo.services.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/index")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IndexController {

    private final IndexService indexService;

    @PostMapping("/recreate")
    public void recreateAllIndices() {
        indexService.recreateIndices(true);
    }
}
