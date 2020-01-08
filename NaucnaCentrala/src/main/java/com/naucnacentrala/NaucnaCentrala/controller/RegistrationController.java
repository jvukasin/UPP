package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.dto.FormFieldsDTO;
import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.model.Korisnik;
import com.naucnacentrala.NaucnaCentrala.repository.KorisnikRepository;
import com.naucnacentrala.NaucnaCentrala.repository.NORepo;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    KorisnikRepository korRepo;

    @GetMapping(path = "/get", produces = "application/json")
    public @ResponseBody FormFieldsDTO get() {
        //provera da li korisnik sa id-jem pera postoji
        //List<User> users = identityService.createUserQuery().userId("pera").list();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("registracija");

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        for(FormField fp : properties) {
            System.out.println(fp.getId() + fp.getType());
        }

        return new FormFieldsDTO(task.getId(), pi.getId(), properties);
    }

    @PostMapping(path = "/post/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity post(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        // list all running/unsuspended instances of the process
//		    ProcessInstance processInstance =
//		        runtimeService.createProcessInstanceQuery()
//		            .processDefinitionKey("registracija")
//		            .active() // we only want the unsuspended process instances
//		            .list().get(0);

//			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);



        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "registration", dto);
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/verify/{username}", method = RequestMethod.GET)
    public void verifyMail(@PathVariable String username) {
        Korisnik k = new Korisnik();

        byte[] actualByte = Base64.getDecoder().decode(username);
        String praviUsername = new String(actualByte);

        runtimeService.setVariable("", "potvrdio", true);

        k = korRepo.findOneByUsername(praviUsername);
        k.setAktivan(true);
        k = korRepo.save(k);
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
