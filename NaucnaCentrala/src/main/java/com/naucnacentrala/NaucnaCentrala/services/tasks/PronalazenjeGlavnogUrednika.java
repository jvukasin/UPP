package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import com.naucnacentrala.NaucnaCentrala.services.CasopisService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PronalazenjeGlavnogUrednika implements JavaDelegate  {

    @Autowired
    CasopisService casopisService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long casopisID = (Long) execution.getVariable("izabraniCasopisID");
        Casopis c = casopisService.findOneById(casopisID);
        execution.setVariable("glavniUrednik", c.getGlavniUrednik().getUsername());

        List<FormSubmissionDTO> rad = (List<FormSubmissionDTO>)execution.getVariable("rad");

        for (FormSubmissionDTO formField : rad) {
            if(formField.getFieldId().equals("naslov")) {
                execution.setVariable("konacan_naslov", formField.getFieldValue());
            }
            if(formField.getFieldId().equals("apstrakt")) {
                execution.setVariable("konacan_apstrakt", formField.getFieldValue());
            }
            if(formField.getFieldId().equals("kljucni_pojmovi")) {
                execution.setVariable("konacan_kljuc_poj", formField.getFieldValue());
            }
            if (formField.getFieldId().equals("naucna_oblast")) {
                execution.setVariable("konacan_nauc_obl", formField.getFieldValue());
            }
        }
    }
}
