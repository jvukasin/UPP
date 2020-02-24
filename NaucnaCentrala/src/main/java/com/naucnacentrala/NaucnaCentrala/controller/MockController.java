package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.CoordsFromAdressUtil;
import com.naucnacentrala.NaucnaCentrala.dto.FormFieldsDTO;
import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.dto.NaucniRadDTO;
import com.naucnacentrala.NaucnaCentrala.dto.UserInfoDTO;
import com.naucnacentrala.NaucnaCentrala.model.*;
import com.naucnacentrala.NaucnaCentrala.services.CasopisService;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import com.naucnacentrala.NaucnaCentrala.services.NaucniRadService;
import com.naucnacentrala.NaucnaCentrala.services.elasticSearch.ReviewerESService;
import com.naucnacentrala.NaucnaCentrala.services.elasticSearch.SciencePaperESService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(value = "mock")
public class MockController {

    @Autowired
    IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    CasopisService casopisService;

    @Autowired
    KorisnikService korisnikService;

    @Autowired
    NaucniRadService naucniRadService;

    @Autowired
    SciencePaperESService sciencePaperESService;

    @Autowired
    ReviewerESService reviewerESService;


    @GetMapping(path = "/getForm/{procesId}", produces = "application/json")
    public @ResponseBody
    FormFieldsDTO uplataClanarineForm(@PathVariable String procesId) {

        ProcessInstance subprocess = runtimeService.createProcessInstanceQuery().superProcessInstanceId(procesId).singleResult();

        Task task = taskService.createTaskQuery().processInstanceId(subprocess.getId()).singleResult();

        String pid = task.getProcessInstanceId();

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        return new FormFieldsDTO(task.getId(), pid, properties);
    }

    @PostMapping(path = "/uplatiClanarinu/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity uplatiClanarinu(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId, HttpServletRequest request) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        String username = korisnikService.getUsernameFromRequest(request);
        FormSubmissionDTO formField = dto.get(0);
        Long casopisID = (Long) runtimeService.getVariable(processInstanceId, "izabraniCasopisID");

        Casopis c = casopisService.findOneById(casopisID);
        Clanarina clan = new Clanarina();
        clan.setUsername(username);
        List<Clanarina> lista = c.getKorisniciSaClanarinom();
        lista.add(clan);
        casopisService.save(c);

        formService.submitTaskForm(taskId, map);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/savePaper/{paperID}", method = RequestMethod.POST, produces = "application/json")
    private ResponseEntity<NaucniRadDTO> save(@PathVariable("paperID") Long id) throws UnsupportedEncodingException {
        NaucniRad sciencePaper = naucniRadService.findOneById(id);
        // sacuvaj u elastic-u rad
        SciencePaperES sciencePaperES = new SciencePaperES();
        String text = sciencePaperESService.parsePDF(sciencePaper);
        sciencePaperES.setText(text);
        sciencePaperES.setId(sciencePaper.getId().toString());
        sciencePaperES.setKeyTerms(sciencePaper.getKeyTerm());
        sciencePaperES.setTitle(sciencePaper.getTitle());
        sciencePaperES.setPaperAbstract(sciencePaper.getPaperAbstract());
        sciencePaperES.setScienceField(sciencePaper.getScienceField().getNaziv());
        sciencePaperES.setMagazineName(sciencePaper.getMagazine().getNaziv());
        sciencePaperES.setFilePath(naucniRadService.getPath(sciencePaper.getId()));
        Random rand = new Random();
        int prefix = rand.nextInt(1000) + 1;
        int suffix = rand.nextInt(1000) + 1;
        sciencePaperES.setDoi("10." + prefix + "/" + suffix);

        String autori="";
        autori+=sciencePaper.getAutor().getIme() + " " + sciencePaper.getAutor().getPrezime();
        for(Koautor coAuthor : sciencePaper.getKoautori()){
            autori+=", "+coAuthor.getIme()+" "+coAuthor.getPrezime();
        }
        sciencePaperES.setAuthor(autori);

        sciencePaperES = sciencePaperESService.save(sciencePaperES);

        NaucniRadDTO sciencePaperDTO = new NaucniRadDTO();
        sciencePaperDTO.setId(Long.parseLong(sciencePaperES.getId()));
        sciencePaperDTO.setTitle(sciencePaperES.getTitle());
        sciencePaperDTO.setKeyTerm(sciencePaperES.getKeyTerms());
        sciencePaperDTO.setPaperAbstract(sciencePaperES.getPaperAbstract());

        return new ResponseEntity<>(sciencePaperDTO, HttpStatus.OK);
    }
    @RequestMapping(value = "/getPaper/{sciencePaperId}", method = RequestMethod.GET, produces = "application/json")
    private ResponseEntity<NaucniRadDTO> getPaper(@PathVariable("sciencePaperId") String id){
        SciencePaperES sciencePaperES = sciencePaperESService.findOneById(id);
        NaucniRadDTO sciencePaperDTO = new NaucniRadDTO();
        sciencePaperDTO.setId(Long.parseLong(sciencePaperES.getId()));
        sciencePaperDTO.setTitle(sciencePaperES.getTitle());
        sciencePaperDTO.setKeyTerm(sciencePaperES.getKeyTerms());
        sciencePaperDTO.setPaperAbstract(sciencePaperES.getPaperAbstract());
        return new ResponseEntity<>(sciencePaperDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/saveReviewers", method = RequestMethod.POST, produces = "application/json")
    private ResponseEntity<List<UserInfoDTO>> saveReviewers() throws UnsupportedEncodingException {
        ArrayList<User> recenzenti = korisnikService.findRecenzente();
        List<UserInfoDTO> ret = new ArrayList<>();
        for(User reviewer: recenzenti) {
            ReviewerES reviewerES = new ReviewerES();
            reviewerES.setScienceFields(reviewer.getNaucneOblasti());
            reviewerES.setFirstName(reviewer.getIme());
            reviewerES.setLastName(reviewer.getPrezime());
            reviewerES.setEmail(reviewer.getEmail());
            reviewerES.setId(reviewer.getUsername());
            CoordsFromAdressUtil coords = new CoordsFromAdressUtil(reviewer.getGrad());
            reviewerES.setLocation(new GeoPoint(coords.getLat(), coords.getLon()));
            reviewerESService.save(reviewerES);
            UserInfoDTO dto = new UserInfoDTO();
            dto.setUsername(reviewer.getUsername());
            ret.add(dto);
        }

        return new ResponseEntity<>(ret, HttpStatus.OK);
    }




    private HashMap<String, Object> mapListToDto(List<FormSubmissionDTO> list)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormSubmissionDTO temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }
}
