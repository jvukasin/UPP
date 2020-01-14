package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.dto.FormFieldsDTO;
import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
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

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping(path = "/get", produces = "application/json")
    public @ResponseBody
    FormFieldsDTO get() {
        //provera da li korisnik sa id-jem pera postoji
        //List<User> users = identityService.createUserQuery().userId("pera").list();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("kreiranje_casopisa");

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        return new FormFieldsDTO(task.getId(), pi.getId(), properties);
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

    private HashMap<String, Object> mapListToDto(List<FormSubmissionDTO> list)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormSubmissionDTO temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }

}
