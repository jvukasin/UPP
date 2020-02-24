package com.naucnacentrala.NaucnaCentrala.controller.elasticSearch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        boolean isPhrase = false;
        List<SciencePaperESDTO> retVal;

        if(dto.getValue().startsWith("\"") && dto.getValue().endsWith("\"")) {
            isPhrase = true;
            String bezNavodnika = dto.getValue().substring(1);
            String bezNavodnikaFinal = bezNavodnika.substring(0, bezNavodnika.length() - 1);
            dto.setValue(bezNavodnikaFinal);
        }

        retVal = sciencePaperESService.searchQuery(dto, isPhrase);

        return new ResponseEntity(retVal, HttpStatus.OK);
    }



}
