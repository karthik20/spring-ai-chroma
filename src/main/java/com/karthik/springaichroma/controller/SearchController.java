package com.karthik.springaichroma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.karthik.springaichroma.model.SearchResult;
import com.karthik.springaichroma.service.ChromaService;



@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

    @Autowired
    private ChromaService chromaService;

    @GetMapping
    public List<SearchResult> search(final @RequestParam("query") String query) {
        return chromaService.performSearch(query);
    }

}