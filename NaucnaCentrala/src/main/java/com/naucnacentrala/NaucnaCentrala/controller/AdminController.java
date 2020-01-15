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
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping(path = "/get/recTasks", produces = "application/json")
    @PreAuthorize("hasAuthority('RECENZENTI_TASK')")
    public @ResponseBody ResponseEntity<List<TaskDTO>> getRecTasks() {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("admin").list();
        List<TaskDTO> dtos = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            if(task.getName().equals("Potvrda recenzenta")) {
                TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
                dtos.add(t);
            }
        }

        return new ResponseEntity(dtos,  HttpStatus.OK);
    }

    @GetMapping(path = "/get/urdTasks", produces = "application/json")
//    @PreAuthorize("hasAuthority('RECENZENTI_TASK')")
    public @ResponseBody ResponseEntity<List<TaskDTO>> getUrdTasks() {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("admin").list();
        List<TaskDTO> dtos = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            if(task.getName().equals("Admin provera") && task.getAssignee() == null) {
                TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
                dtos.add(t);
            }
        }

        return new ResponseEntity(dtos,  HttpStatus.OK);
    }

    @GetMapping(path = "/task/claim/{taskId}", produces = "application/json")
    public @ResponseBody FormFieldsDTO getRecForm(@PathVariable String taskId, HttpServletRequest request) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        if(task.getAssignee() == null) {
            String username = korisnikService.getUsernameFromRequest(request);
            taskService.claim(taskId, username);
        }
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

    @PostMapping(path = "/potvrdaCasopisa/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postCasopis(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        String potvrdio = "";
        for (FormSubmissionDTO formField : dto) {
            if(formField.getFieldId().equals("aktiviraj_casopis")) {
                potvrdio = formField.getFieldValue();
                if(potvrdio == "true") {
                    runtimeService.setVariable(processInstanceId, "aktiviraj_casopis", true);
                } else {
                    runtimeService.setVariable(processInstanceId, "aktiviraj_casopis", false);
                }
                break;
            }
        }
        formService.submitTaskForm(taskId, map);

        if(potvrdio != "true") {
            Task ispravkaTask = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
            String glUrednik = (String) runtimeService.getVariable(processInstanceId, "glavniUrednik");
            ispravkaTask.setAssignee(glUrednik);
            taskService.saveTask(ispravkaTask);
        }

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
