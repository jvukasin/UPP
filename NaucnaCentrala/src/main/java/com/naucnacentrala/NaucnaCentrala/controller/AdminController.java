package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.dto.TaskDTO;
import com.naucnacentrala.NaucnaCentrala.repository.KorisnikRepository;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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
}
