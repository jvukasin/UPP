package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.services.CasopisService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAccessProvera implements JavaDelegate {

    @Autowired
    CasopisService casopisService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Long casopisID = (Long) execution.getVariable("izabraniCasopisID");

        Casopis c = casopisService.findOneById(casopisID);
        if(c.getClanarina().equals("autori")) {
            execution.setVariable("openaccess", true);
        } else {
            execution.setVariable("openaccess", false);
        }

    }
}
