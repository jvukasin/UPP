package com.naucnacentrala.NaucnaCentrala.services.elasticSearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naucnacentrala.NaucnaCentrala.CoordsFromAdressUtil;
import com.naucnacentrala.NaucnaCentrala.dto.AdvancedSearchDTO;
import com.naucnacentrala.NaucnaCentrala.dto.ReviewerDTO;
import com.naucnacentrala.NaucnaCentrala.dto.SciencePaperESDTO;
import com.naucnacentrala.NaucnaCentrala.dto.SimpleSearchDTO;
import com.naucnacentrala.NaucnaCentrala.model.Autor;
import com.naucnacentrala.NaucnaCentrala.model.Koautor;
import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.model.SciencePaperES;
import com.naucnacentrala.NaucnaCentrala.repository.elasticSearch.SciencePaperESRepository;
import com.naucnacentrala.NaucnaCentrala.services.NaucniRadService;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.text.PDFTextStripper;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
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

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

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

    public List<ReviewerDTO> getReviewersByLocation(String taskId) throws JsonProcessingException {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String pid = task.getProcessInstanceId();

        Long radID = (Long) runtimeService.getVariable(pid, "radID");
        NaucniRad rad = naucniRadService.findOneById(radID);
        Autor autor = rad.getAutor();
        CoordsFromAdressUtil coords = new CoordsFromAdressUtil(autor.getGrad());
        GeoPoint geoPoint = new GeoPoint(coords.getLat(), coords.getLon());
        List<Koautor> koautori = rad.getKoautori();

        String query="{\n" +
                "    \"query\": {\n" +
                "        \"bool\" : {\n" +
                "           \"must_not\" : {\n"+
                "               \"geo_distance\" : {\n" +
                "                   \"distance\" : \"100km\",\n" +
                "                   \"location\" : {\n" +
                "                       \"lat\" :" + geoPoint.getLat() + ",\n" +
                "                       \"lon\" :" + geoPoint.getLon() + "\n" +
                "                   }\n" +
                "               }\n" +
                "           }\n"+
                "        }\n" +
                "    }\n"+
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonquery = objectMapper.readTree(query);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<JsonNode> request = new HttpEntity<>(jsonquery);
        String url = "http://localhost:9200/reviewers/reviewer/_search?pretty";
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        List<ReviewerDTO> retVal = this.getReviewersFromES(rootNode);  //rec van 100km
        //izbaci one koji su unutar 100km od koautora - konacna lista je lista recenzenata koji zadovoljavaju uslov
        System.out.println("VAN 100KM SIZE="+retVal.size());
        for(Koautor ka: koautori){
            CoordsFromAdressUtil kacoords = new CoordsFromAdressUtil(ka.getGrad());
            Double lat = kacoords.getLat();
            Double lon = kacoords.getLon();
            String query1="{\n" +
                    "    \"query\": {\n" +
                    "        \"bool\" : {\n" +
                    "            \"must\" : {\n" +
                    "                \"match_all\" : {}\n" +
                    "            },\n" +
                    "            \"filter\" : {\n" +
                    "                \"geo_distance\" : {\n" +
                    "                    \"distance\" : \"100km\",\n" +
                    "                    \"location\" : {\n" +
                    "                        \"lat\" : "+ lat +",\n" +
                    "                        \"lon\" : "+ lon +"\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            ObjectMapper objectMapper2 = new ObjectMapper();
            JsonNode jsonquery2 = objectMapper.readTree(query1);

            RestTemplate restTemplate2 = new RestTemplate();
            HttpEntity<JsonNode> request2 = new HttpEntity<>(jsonquery2);
            String url2 = "http://localhost:9200/reviewers/reviewer/_search?pretty";
            ResponseEntity<String> response2 = restTemplate2.postForEntity(url2, request2, String.class);
            JsonNode rootNode2 = objectMapper2.readTree(response2.getBody());
            List<ReviewerDTO> upadaju = this.getReviewersFromES(rootNode2);

            for(int rv=0; rv<retVal.size(); rv++){
                for(ReviewerDTO crdto : upadaju){
                    if(crdto.getUsername()==retVal.get(rv).getUsername() || crdto.getUsername().equals(retVal.get(rv).getUsername())){
                        retVal.remove(rv);
                    }
                }
            }
        }

        return retVal;
    }

    public List<ReviewerDTO> getReviewersMoreLikeThis(String taskId) throws JsonProcessingException, UnsupportedEncodingException {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String pid = task.getProcessInstanceId();

        Long radID = (Long) runtimeService.getVariable(pid, "radID");
        NaucniRad rad = naucniRadService.findOneById(radID);
        String parsed = parsePDF(rad);
        String parsedText = StringEscapeUtils.escapeJson(parsed);
        String query="{\n" +
                "    \"query\": {\n" +
                "        \"more_like_this\" : {\n" +
                "            \"fields\" : [\"text\"],\n" +
                "            \"like\" : \""+ parsedText +"\",\n" +
                "            \"min_term_freq\" : 4,\n" +
                "            \"max_query_terms\" : 40,\n" +
                "            \"min_doc_freq\": 2\n" +
                "        }\n" +
                "    }\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonquery = objectMapper.readTree(query);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<JsonNode> request = new HttpEntity<>(jsonquery);
        String url = "http://localhost:9200/magazine/sciencePaper/_search?pretty";
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode locatedNode = rootNode.path("hits").path("hits");
        List<SciencePaperESDTO> slicniRadovi = getRetVal(locatedNode, "text");

        //sad trazimo recenzente koji su recenzirali radove sa nadjenim naslovima
        String queryRC="{\n" +
                "  \"query\": {\n" +
                "    \"bool\" : {\n";
        queryRC += "\"should\" : [\n";

        for(SciencePaperESDTO rd : slicniRadovi) {
            queryRC += "{ \"match\" : { \"sciencePapers\" : \""+ rd.getTitle() +"\" } },";
        }
        queryRC = queryRC.substring(0, queryRC.length()-1);
        queryRC+="]";
        queryRC+="    }\n" +
                "  }\n" +
                "}";

        ObjectMapper objectMapper2 = new ObjectMapper();
        JsonNode jsonquery2 = objectMapper.readTree(queryRC);

        RestTemplate restTemplate2 = new RestTemplate();
        HttpEntity<JsonNode> request2 = new HttpEntity<>(jsonquery2);
        String url2 = "http://localhost:9200/reviewers/reviewer/_search?pretty";
        ResponseEntity<String> response2 = restTemplate2.postForEntity(url2, request2, String.class);
        JsonNode rootNode2 = objectMapper2.readTree(response2.getBody());
        List<ReviewerDTO> retVal = this.getReviewersFromES(rootNode2);

        return retVal;
    }

    public List<ReviewerDTO> getReviewersFromES(JsonNode rootNode) {
        List<ReviewerDTO> reviewers=new ArrayList<>();
        JsonNode locatedNode = rootNode.path("hits").path("hits");

        for(int i=0;i<locatedNode.size();i++){
            ReviewerDTO dto=new ReviewerDTO();
            String usrnm = locatedNode.get(i).path("_source").path("id").asText();
            dto.setUsername(usrnm);
            String ime = locatedNode.get(i).path("_source").path("firstName").asText();
            String prezime = locatedNode.get(i).path("_source").path("lastName").asText();
            dto.setName(ime + " " + prezime + "(" + usrnm + ")");
            reviewers.add(dto);
        }
        return reviewers;
    }


    //SEARCH

    public List<SciencePaperESDTO> searchQuery(SimpleSearchDTO dto) throws JsonProcessingException {
        String matchOrPhrase = "match";
        if(dto.getValue().startsWith("\"") && dto.getValue().endsWith("\"")) {
            dto.setValue(removePhrase(dto.getValue()));
            matchOrPhrase = "match_phrase";
        }
        String query = "{\n" +
                "    \"query\": {\n" +
                "        \"" + matchOrPhrase + "\" : {\n" +
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
            SciencePaperESDTO dto = getInfoFromNode(node, i);

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
            SciencePaperESDTO dto=getInfoFromNode(node, i);

            String highText=node.get(i).path("highlight").toString();

            highText=highText.replace("\"","");
            highText=highText.replace("{"," ");
            highText=highText.replace("}"," ");
            highText=highText.replace("["," ");
            highText=highText.replace("]"," ");
            highText=highText.replace("\\r\\n","");
            dto.setHighlight(highText);
            retVal.add(dto);
        }
        return retVal;
    }

    public SciencePaperESDTO getInfoFromNode(JsonNode node, int i) {
        SciencePaperESDTO dto = new SciencePaperESDTO();
        dto.setPrice(Double.parseDouble(node.get(i).path("_source").path("price").asText()));
        dto.setCurrency(node.get(i).path("_source").path("currency").asText());
        dto.setAuthor(node.get(i).path("_source").path("author").asText());
        dto.setId(Long.parseLong(node.get(i).path("_source").path("id").asText()));
        dto.setMagazineName(node.get(i).path("_source").path("magazineName").asText());
        if(node.get(i).path("_source").path("openAccess").asText().equals("yes")) {
            dto.setOpenAcess(true);
        } else {
            dto.setOpenAcess(false);
        }
        dto.setTitle(node.get(i).path("_source").path("title").asText());
        dto.setPaperAbstract(node.get(i).path("_source").path("paperAbstract").asText());
        return dto;
    }
}
