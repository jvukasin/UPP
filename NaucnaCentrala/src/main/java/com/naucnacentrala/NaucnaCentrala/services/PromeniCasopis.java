package com.naucnacentrala.NaucnaCentrala.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class PromeniCasopis implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String noviNaziv = (String) execution.getVariable("nazivIzmena");
        String noviIssn = (String) execution.getVariable("issnIzmena");
        String novaNaplata = (String) execution.getVariable("naplata_clanarineIzmena");

        execution.setVariable("naziv", noviNaziv);
        execution.setVariable("issn", noviIssn);
        execution.setVariable("naplata_clanarine", novaNaplata);

    }
}
