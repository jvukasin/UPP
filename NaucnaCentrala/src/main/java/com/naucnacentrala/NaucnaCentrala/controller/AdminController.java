package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.dto.FormFieldsDTO;
import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.dto.TaskDTO;
import com.naucnacentrala.NaucnaCentrala.repository.UserRepository;
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
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    KorisnikService korisnikService;

    @GetMapping(path = "/get/tasks", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDTO>> get() {

        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("admin").list();
        List<TaskDTO> dtos = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
            dtos.add(t);
        }

        return new ResponseEntity(dtos,  HttpStatus.OK);
    }

    @GetMapping(path = "/task/claim/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDTO getForm(@PathVariable String taskId, HttpServletRequest request) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String username = korisnikService.getUsernameFromRequest(request);
        taskService.claim(taskId, username);
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        return new FormFieldsDTO(task.getId(), processInstanceId, properties);
    }

    @PostMapping(path = "/potvrdaRecenzenta/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity post(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String prihvati;
        for (FormSubmissionDTO formField : dto) {
            if(formField.getFieldId().equals("prihvatiRec")) {
                prihvati = formField.getFieldValue();
                if(prihvati == "true") {
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
