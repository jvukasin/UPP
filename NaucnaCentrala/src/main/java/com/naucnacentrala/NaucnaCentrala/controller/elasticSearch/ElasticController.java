package com.naucnacentrala.NaucnaCentrala.controller.elasticSearch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naucnacentrala.NaucnaCentrala.dto.AdvancedSearchDTO;
import com.naucnacentrala.NaucnaCentrala.dto.SciencePaperESDTO;
import com.naucnacentrala.NaucnaCentrala.dto.SimpleSearchDTO;
import com.naucnacentrala.NaucnaCentrala.services.elasticSearch.SciencePaperESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@SuppressWarnings("Duplicates")
@RequestMapping(path = "/elastic")
public class ElasticController {

    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SciencePaperESService sciencePaperESService;

    @PostMapping(path = "/simpleSearch", produces = "application/json")
    public @ResponseBody
    ResponseEntity simpleSearch(@RequestBody SimpleSearchDTO dto) throws Exception {
        return new ResponseEntity(sciencePaperESService.searchQuery(dto), HttpStatus.OK);
    }

    @PostMapping(path = "/advancedSearch", produces = "application/json")
    public @ResponseBody
    ResponseEntity advancedSearch(@RequestBody AdvancedSearchDTO dto) throws Exception {
        return new ResponseEntity(sciencePaperESService.advancedSearchQuery(dto), HttpStatus.OK);
    }


}
