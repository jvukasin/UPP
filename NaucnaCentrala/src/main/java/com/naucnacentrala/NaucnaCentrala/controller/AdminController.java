package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.dto.FormFieldsDTO;
import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.dto.TaskDTO;
import com.naucnacentrala.NaucnaCentrala.repository.KorisnikRepository;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

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

    @GetMapping(path = "/get/tasks", produces = "application/json")
    public @ResponseBody
    ResponseEntity<List<TaskDTO>> get() {

        ProcessInstance processInstance =
		        runtimeService.createProcessInstanceQuery()
		            .processDefinitionKey("registracija")
		            .active() // we only want the unsuspended process instances
		            .list().get(0);

//        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        List<TaskDTO> dtos = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
            dtos.add(t);
        }

        return new ResponseEntity(dtos,  HttpStatus.OK);
    }

    @GetMapping(path = "/get/form", produces = "application/json")
    public @ResponseBody
    FormFieldsDTO getForm() {
        //provera da li korisnik sa id-jem pera postoji
        //List<User> users = identityService.createUserQuery().userId("pera").list();
        ProcessInstance pi =
                runtimeService.createProcessInstanceQuery()
                        .processDefinitionKey("registracija")
                        .active() // we only want the unsuspended process instances
                        .list().get(0);

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        //UZMI TASK ZA ADMINA

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        return new FormFieldsDTO(task.getId(), pi.getId(), properties);
    }

    @PostMapping(path = "/potvrdaRecenzenta/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity post(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String prihvati;
        for (FormSubmissionDTO formField : dto) {
            if(formField.getFieldId().equals("prihvatiRec")) {
                prihvati = formField.getFieldValue();
                if(prihvati == "asd") {
                    runtimeService.setVariable(processInstanceId, "prihvatiRec", true);
                } else {
                    runtimeService.setVariable(processInstanceId, "prihvatiRec", false);
                }
                break;
            }
        }
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
