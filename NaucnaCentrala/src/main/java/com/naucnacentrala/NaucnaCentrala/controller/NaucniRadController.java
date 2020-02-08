package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.dto.FormFieldsDTO;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import com.naucnacentrala.NaucnaCentrala.services.CasopisService;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import com.naucnacentrala.NaucnaCentrala.services.NOService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/rad")
public class NaucniRadController {
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

    @GetMapping(path = "/get", produces = "application/json")
    public @ResponseBody
    FormFieldsDTO get() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("obrada_teksta");

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        List<Casopis> casopisi = casopisService.findAll();

        for(FormField field : properties) {
            if(field.getId().equals("casopis")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(Casopis c: casopisi){
                    enumType.getValues().put(c.getId().toString(), c.getNaziv().concat(", ").concat(c.getIssn()));
                }
            }
        }

        return new FormFieldsDTO(task.getId(), pi.getId(), properties);
    }

}
