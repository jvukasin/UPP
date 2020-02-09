package com.naucnacentrala.NaucnaCentrala.services.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZabeleziDaJeRecenzirao implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String recenzirao = (String) execution.getVariable("JedRec");
        List<String> gotovi = (List<String>) execution.getVariable("gotovi");
        gotovi.add(recenzirao);
        execution.setVariable("gotovi", gotovi);
    }
}
