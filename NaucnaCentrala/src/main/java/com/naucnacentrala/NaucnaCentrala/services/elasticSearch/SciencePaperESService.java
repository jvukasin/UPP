package com.naucnacentrala.NaucnaCentrala.services.elasticSearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naucnacentrala.NaucnaCentrala.dto.SciencePaperESDTO;
import com.naucnacentrala.NaucnaCentrala.dto.SimpleSearchDTO;
import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.model.SciencePaperES;
import com.naucnacentrala.NaucnaCentrala.repository.elasticSearch.SciencePaperESRepository;
import com.naucnacentrala.NaucnaCentrala.services.NaucniRadService;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SciencePaperESService {

    @Autowired
    private SciencePaperESRepository sciencePaperESRepo;

    @Autowired
    private NaucniRadService naucniRadService;

    public SciencePaperES save(SciencePaperES sciencePaperES){
        return sciencePaperESRepo.save(sciencePaperES);
    }


    public SciencePaperES findOneById(String id) {
        return sciencePaperESRepo.findOneById(id);
    }

    public String parsePDF(NaucniRad naucniRad) throws UnsupportedEncodingException {
        String path = naucniRadService.getPath(naucniRad.getId());
        File pdf = new File(path);
        String text = null;
        try {
            System.out.println("*******************************************");
            System.out.println("Parsiranje PDF-a");
            System.out.println("Path: " + path);
            System.out.println("*******************************************");
            PDFParser parser = new PDFParser(new RandomAccessFile(pdf, "r"));
            parser.parse();
            text = getText(parser);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public String getText(PDFParser parser) {
        try {
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(parser.getPDDocument());
            return text;
        } catch (IOException e) {
            System.out.println("Greksa pri konvertovanju dokumenta u pdf");
        }
        return null;
    }


    //SEARCH

    public List<SciencePaperESDTO> searchQuery(SimpleSearchDTO dto, boolean isPhrase) throws JsonProcessingException {
        String query = "";
        if (!isPhrase) {
            query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : {\n" +
                    "            \"" + dto.getField() + "\" : {\n" +
                    "                \"query\" : \"" + dto.getValue() + "\"\n" +
                    "            }\n" +
                    "           \n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"highlight\" : {\n" +
                    "        \"fields\" : {\n" +
                    "            \"" + dto.getField() + "\" : {\n" +
                    "            \t\"type\":\"plain\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
        } else if (isPhrase) {
            query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match_phrase\" : {\n" +
                    "            \"" + dto.getField() + "\" : {\n" +
                    "                \"query\" : \"" + dto.getValue() + "\"\n" +
                    "         \n" +
                    "            }\n" +
                    "           \n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"highlight\" : {\n" +
                    "        \"fields\" : {\n" +
                    "            \"" + dto.getField() + "\" : {\n" +
                    "            \t\"type\":\"plain\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
        }


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonquery = objectMapper.readTree(query);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<JsonNode> request = new HttpEntity<>(jsonquery);
        String url = "http://localhost:9200/magazine/sciencePaper/_search?pretty";
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode locatedNode = rootNode.path("hits").path("hits");
        List<SciencePaperESDTO> retVal = getRetVal(locatedNode, dto.getField());


        return retVal;
    }


    public List<SciencePaperESDTO> getRetVal(JsonNode node,String highlight) {
        List<SciencePaperESDTO> retVal=new ArrayList<>();
        for(int i=0;i<node.size();i++){
            SciencePaperESDTO dto = new SciencePaperESDTO();
            Long paperId=Long.parseLong(node.get(i).path("_source").path("id").asText());
            NaucniRad rd = naucniRadService.findOneById(paperId);
            dto.setPrice(rd.getPrice());
            dto.setCurrency(rd.getCurrency());
            dto.setAuthor(node.get(i).path("_source").path("author").asText());
            dto.setId(paperId);
            dto.setMagazineName(node.get(i).path("_source").path("magazineName").asText());
            if(rd.getMagazine().getClanarina().equals("autori")) {
                dto.setOpenAcess(true);
            } else {
                dto.setOpenAcess(false);
            }
            dto.setTitle(node.get(i).path("_source").path("title").asText());
            dto.setPaperAbstract(node.get(i).path("_source").path("paperAbstract").asText());
            System.out.println(node.get(i).path("highlight").path(highlight).toString());
            String highText="";
            for(int j=0;j<node.get(i).path("highlight").path(highlight).size();j++){
                highText+=node.get(i).path("highlight").path(highlight).get(j).asText()+"...";
            }
            dto.setHighlight(highText);
            retVal.add(dto);
        }
        return retVal;

    }
}
