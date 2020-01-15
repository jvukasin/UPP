package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromeniCasopis implements JavaDelegate {

    @Autowired
    CasopisService casopisService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> casopis = (List<FormSubmissionDTO>)execution.getVariable("ispravka");
        Long casopisID = (Long) execution.getVariable("casopisID");
        Casopis cas = casopisService.findOneById(casopisID);

        for (FormSubmissionDTO formField : casopis) {
            if(formField.getFieldId().equals("nazivIzmena")) {
                cas.setNaziv(formField.getFieldValue());
            }
            if(formField.getFieldId().equals("issnIzmena")) {
                cas.setIssn(formField.getFieldValue());
            }
            if(formField.getFieldId().equals("naplata_clanarineIzmena")) {
                cas.setClanarina(formField.getFieldValue());
            }
        }
        cas = casopisService.save(cas);

        execution.setVariable("naziv", cas.getNaziv());
        execution.setVariable("issn", cas.getIssn());
        execution.setVariable("naplata_clanarine", cas.getClanarina());
    }
}
