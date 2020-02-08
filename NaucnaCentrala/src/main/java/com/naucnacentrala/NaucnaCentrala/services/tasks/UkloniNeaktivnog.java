package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UkloniNeaktivnog implements JavaDelegate {

    @Autowired
    KorisnikService servis;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> registration = (List<FormSubmissionDTO>)execution.getVariable("registration");
        String username = "";
        for (FormSubmissionDTO formField : registration) {
            if(formField.getFieldId().equals("username")) {
                username = formField.getFieldValue();
            }
        }
        servis.remove(username);
    }
}