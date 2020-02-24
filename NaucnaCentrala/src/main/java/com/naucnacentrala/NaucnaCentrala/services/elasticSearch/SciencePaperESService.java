package com.naucnacentrala.NaucnaCentrala.services.elasticSearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naucnacentrala.NaucnaCentrala.dto.AdvancedSearchDTO;
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
import org.springframework.http.HttpStatus;
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

    public List<SciencePaperESDTO> searchQuery(SimpleSearchDTO dto) throws JsonProcessingException {
        boolean isPhrase = false;
        if(dto.getValue().startsWith("\"") && dto.getValue().endsWith("\"")) {
            isPhrase = true;
            dto.setValue(removePhrase(dto.getValue()));
        }
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

    public List<SciencePaperESDTO> advancedSearchQuery(AdvancedSearchDTO dto) throws JsonProcessingException {
        String matchOrMatchPhrase;
        String mustOrShould = "";

        if(dto.getCheckAndOr().equals("AND")) {
            mustOrShould = "\"must\" : [\n" ;
        } else if (dto.getCheckAndOr().equals("OR")) {
            mustOrShould = "\"should\" : [\n";
        }

        if(!dto.getMagazineName().equals("")) {
            matchOrMatchPhrase = checkIfPhrase(dto.getMagazineName());
            if(matchOrMatchPhrase == "match_phrase") {
                dto.setMagazineName(removePhrase(dto.getMagazineName()));
            }
            mustOrShould+="{ \"" + matchOrMatchPhrase + "\" : { \"magazineName\" : \""+dto.getMagazineName()+"\" } },";
        }

        if(!dto.getTitle().equals("")) {
            matchOrMatchPhrase = checkIfPhrase(dto.getTitle());
            if(matchOrMatchPhrase == "match_phrase") {
                dto.setTitle(removePhrase(dto.getTitle()));
            }
            mustOrShould+="{ \"" + matchOrMatchPhrase + "\" : { \"title\" : \""+dto.getTitle()+"\" } },";
        }

        if(!dto.getKeyTerms().equals("")){
            matchOrMatchPhrase = checkIfPhrase(dto.getKeyTerms());
            if(matchOrMatchPhrase == "match_phrase") {
                dto.setKeyTerms(removePhrase(dto.getKeyTerms()));
            }
            mustOrShould+="{ \"" + matchOrMatchPhrase + "\" : { \"keyTerms\" : \""+dto.getKeyTerms()+"\" } },";
        }

        if(!dto.getAuthor().equals("")){
            matchOrMatchPhrase = checkIfPhrase(dto.getAuthor());
            if(matchOrMatchPhrase == "match_phrase") {
                dto.setAuthor(removePhrase(dto.getAuthor()));
            }
            mustOrShould+="{ \"" + matchOrMatchPhrase + "\" : { \"author\" : \""+dto.getAuthor()+"\" } },";
        }

        if(!dto.getText().equals("")){
            matchOrMatchPhrase = checkIfPhrase(dto.getText());
            if(matchOrMatchPhrase == "match_phrase") {
                dto.setText(removePhrase(dto.getText()));
            }
            mustOrShould+="{ \"" + matchOrMatchPhrase + "\" : { \"text\" : \""+dto.getText()+"\" } },";
        }

        if(!dto.getScienceField().equals("")){
            matchOrMatchPhrase = checkIfPhrase(dto.getScienceField());
            if(matchOrMatchPhrase == "match_phrase") {
                dto.setScienceField(removePhrase(dto.getScienceField()));
            }
            mustOrShould+="{ \"" + matchOrMatchPhrase + "\" : { \"scienceField\" : \""+dto.getScienceField()+"\" } },";
        }

        mustOrShould=mustOrShould.substring(0,  mustOrShould.length() - 1);
        mustOrShould+="],";

        String query="{\n" +
                "  \"query\": {\n" +
                "    \"bool\" : {\n";

        query+=mustOrShould;

        query+="\"boost\" : 1.0\n" +
                "    }\n" +
                "  },\n" +
                "\"highlight\" : {\n" +
                "        \"fields\" : {\n" +
                "            \"magazineName\" : {" +
                "               \t\"type\":\"plain\"\n" +
                "},\n" +
                "        \"title\" : {" +
                "           \t\"type\":\"plain\"\n" +
                "},\n" +
                "        \"keyTerms\" : {" +
                "             \t\"type\":\"plain\"\n" +
                "},\n" +
                "       \"text\" : {" +
                "               \t\"type\":\"plain\"\n" +
                "},\n" +
                "        \"author\" : {" +
                "               \t\"type\":\"plain\"\n" +
                "},\n" +
                "       \"scienceField\" : {" +
                "               \t\"type\":\"plain\"\n" +
                "}\n" +
                "        }\n" +
                "    }"+
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonquery = objectMapper.readTree(query);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<JsonNode> request = new HttpEntity<>(jsonquery);
        String url = "http://localhost:9200/magazine/sciencePaper/_search?pretty";
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode locatedNode = rootNode.path("hits").path("hits");
        List<SciencePaperESDTO> retVal = getRetValAdvanced(locatedNode);

        return retVal;
    }

    public String removePhrase(String data) {
        String bezNavodnika = data.substring(1);
        String bezNavodnikaFinal = bezNavodnika.substring(0, bezNavodnika.length() - 1);
        return  bezNavodnikaFinal;
    }

    public String checkIfPhrase(String data) {
        if(data.startsWith("\"") && data.endsWith("\"")) {
            return "match_phrase";
        }
        return "match";
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

    public List<SciencePaperESDTO> getRetValAdvanced(JsonNode node) {
        List<SciencePaperESDTO> retVal=new ArrayList<>();
        for(int i=0;i<node.size();i++){
            SciencePaperESDTO dto=new SciencePaperESDTO();
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
            String highText=node.get(i).path("highlight").toString();

            highText=highText.replace("\"","");
            highText=highText.replace("{"," ");
            highText=highText.replace("}"," ");
            highText=highText.replace("["," ");
            highText=highText.replace("]"," ");
            highText=highText.replace("\r\n"," ");
            dto.setHighlight(highText);
            retVal.add(dto);
        }
        return retVal;
    }
}
