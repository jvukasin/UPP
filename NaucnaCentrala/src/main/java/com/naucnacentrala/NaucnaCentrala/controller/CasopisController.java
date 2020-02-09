package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.dto.CasopisDTO;
import com.naucnacentrala.NaucnaCentrala.dto.FormFieldsDTO;
import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.dto.TaskDTO;
import com.naucnacentrala.NaucnaCentrala.model.*;
import com.naucnacentrala.NaucnaCentrala.services.CasopisService;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import com.naucnacentrala.NaucnaCentrala.services.NOService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.impl.form.type.StringFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/casopis")
public class CasopisController {

    @Autowired
    IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    KorisnikService korisnikService;

    @Autowired
    CasopisService casopisService;

    @Autowired
    NOService noService;

    @RequestMapping(value = "/casopisi/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<CasopisDTO> getSpecificC(@PathVariable("id") long id){
        return new ResponseEntity<>(casopisService.findOneDto(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/casopisi", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CasopisDTO>> getAllC(){
        return new ResponseEntity<>(casopisService.findAllDto(), HttpStatus.OK);
    }

    @GetMapping(path = "/get", produces = "application/json")
    public @ResponseBody
    FormFieldsDTO get() {
        //provera da li korisnik sa id-jem pera postoji
        //List<User> users = identityService.createUserQuery().userId("pera").list();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("kreiranje_casopisa");

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        List<NaucnaOblast> naucneOblasti = noService.findAll();

        for(FormField field : properties) {
            if(field.getId().equals("nauc_oblasti")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(NaucnaOblast no: naucneOblasti){
                    enumType.getValues().put(no.getSifra().toString(), no.getNaziv());
                }
            }
        }

        return new FormFieldsDTO(task.getId(), pi.getId(), properties);
    }

    @GetMapping(path = "/get/casopisTasks", produces = "application/json")
//    @PreAuthorize("hasAuthority('RECENZENTI_TASK')")
    public @ResponseBody ResponseEntity<List<TaskDTO>> getUrTasks(HttpServletRequest request) {

        String username = korisnikService.getUsernameFromRequest(request);
        List<Task> tasks = taskService.createTaskQuery().taskName("Ispravka podnetog casopis").taskAssignee(username).list();
        List<TaskDTO> dtos = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
            dtos.add(t);
        }

        return new ResponseEntity(dtos,  HttpStatus.OK);
    }

    @GetMapping(path = "/get/ispravkaForm/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDTO getIspravkaForm(@PathVariable String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        Long casopisID = (Long) runtimeService.getVariable(processInstanceId, "casopisID");
        Casopis c = casopisService.findOneById(casopisID);

        List<NaucnaOblast> nauc_obl = c.getNaucneOblasti();
        List<NaucnaOblast> sveNaucne = noService.findAll();

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        for(FormField field : properties) {
            if (field.getId().equals("izabrane_naucne")) {
                EnumFormType enumType = (EnumFormType) field.getType();
                for (NaucnaOblast n : nauc_obl) {
                    enumType.getValues().put(n.getSifra().toString(), n.getNaziv());
                }
            } else if (field.getId().equals("naucneIzmena")) {
                EnumFormType enumType = (EnumFormType) field.getType();
                for (NaucnaOblast n : sveNaucne) {
                    enumType.getValues().put(n.getSifra().toString(), n.getNaziv());
                }
            }
        }

        return new FormFieldsDTO(task.getId(), processInstanceId, properties);
    }

    @PostMapping(path = "/posaljiIspravljeniCasopis/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postEditCasopis(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        runtimeService.setVariable(processInstanceId, "ispravka", dto);

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/post/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity post(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId, HttpServletRequest request) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        String username = korisnikService.getUsernameFromRequest(request);

        runtimeService.setVariable(processInstanceId, "casopis", dto);
        runtimeService.setVariable(processInstanceId, "glavniUrednik", username);

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/get/UredRec/{procesId}", produces = "application/json")
    public @ResponseBody
    FormFieldsDTO getUredRecForm(@PathVariable String procesId, HttpServletRequest request) {
        Task task = taskService.createTaskQuery().processInstanceId(procesId).singleResult();

        String pid = task.getProcessInstanceId();
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        String user = korisnikService.getUsernameFromRequest(request);
        ArrayList<User> recenzentiBaza = korisnikService.findRecenzente();
        ArrayList<User> uredniciBaza = korisnikService.findUrednike(user);

        Long casopisID = (Long) runtimeService.getVariable(pid, "casopisID");
        Casopis c = casopisService.findOneById(casopisID);

        List<NaucnaOblast> nauc_obl = c.getNaucneOblasti();

        ArrayList<User> recenzenti = pronadjiIh(recenzentiBaza, nauc_obl);
        ArrayList<User> urednici = pronadjiIh(uredniciBaza, nauc_obl);

        if(task.getName().equals("Dodaj urednike i recenzente")) {
            for(FormField field : properties) {
                if(field.getId().equals("urednici")){
                    EnumFormType enumType = (EnumFormType) field.getType();
                    for(User urednik: urednici){
                        String prikaz = urednik.getIme() + " " + urednik.getPrezime() + "(" + urednik.getUsername() + ")";
                        enumType.getValues().put(urednik.getUsername(), prikaz);
                    }
                }
                if(field.getId().equals("recenzenti")){
                    EnumFormType enumType = (EnumFormType) field.getType();
                    for(User recenzent: recenzenti){
                        String prikaz = recenzent.getIme() + " " + recenzent.getPrezime() + "(" + recenzent.getUsername() + ")";
                        enumType.getValues().put(recenzent.getUsername(), prikaz);
                    }
                }
            }
        }
        if(task.getName().equals("Izmeni urednike i recenzente")) {
            for(FormField field : properties) {
                if(field.getId().equals("uredniciIzmena")){
                    EnumFormType enumType = (EnumFormType) field.getType();
                    for(User urednik: urednici){
                        String prikaz = urednik.getIme() + " " + urednik.getPrezime() + "(" + urednik.getUsername() + ")";
                        enumType.getValues().put(urednik.getUsername(), prikaz);
                    }
                }
                if(field.getId().equals("recenzentiIzmena")){
                    EnumFormType enumType = (EnumFormType) field.getType();
                    for(User recenzent: recenzenti){
                        String prikaz = recenzent.getIme() + " " + recenzent.getPrezime() + "(" + recenzent.getUsername() + ")";
                        enumType.getValues().put(recenzent.getUsername(), prikaz);
                    }
                }
            }

            List<Recenzent> izabraniR = c.getRecenzenti();
            List<Urednik> izabraniU = c.getUredniciNO();
            String pretRe = c.getRecenzenti().get(0).getUsername();
            String pretUr = c.getUredniciNO().get(0).getUsername();

            for(Recenzent r : c.getRecenzenti()) {
                if(!pretRe.equals(r.getUsername())) {
                    pretRe = pretRe + ", " + r.getUsername();
                }
            }
            for(Urednik u : c.getUredniciNO()) {
                if(!pretUr.equals(u.getUsername())) {
                    pretUr = pretUr + ", " + u.getUsername();
                }
            }
            FormField urd = casopisService.prethodniUred(pretUr);
            FormField rcz = casopisService.prethodniRec(pretRe);

            properties.add(0, urd);
            properties.add(2, rcz);
        }

        return new FormFieldsDTO(task.getId(), pid, properties);
    }

    @PostMapping(path = "/post/UredRec/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity postUredRec(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        runtimeService.setVariable(processInstanceId, "odbor", dto);

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/post/UredRecIzmena/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity postUredRecIzmena(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        runtimeService.setVariable(processInstanceId, "odborIzmena", dto);

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private HashMap<String, Object> mapListToDto(List<FormSubmissionDTO> list)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormSubmissionDTO temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }

    private ArrayList<User> pronadjiIh(ArrayList<User> useri, List<NaucnaOblast> oblasti) {
        ArrayList<User> ret = new ArrayList<>();

        for(User rec : useri) {
            for(NaucnaOblast nRec : rec.getNaucneOblasti()) {
                for(NaucnaOblast n : oblasti) {
                    if(nRec.getNaziv().equals(n.getNaziv())) {
                        if(!ret.contains(rec)) {
                            ret.add(rec);
                            break;
                        }
                    }
                }
            }
        }

        return ret;
    }

}
