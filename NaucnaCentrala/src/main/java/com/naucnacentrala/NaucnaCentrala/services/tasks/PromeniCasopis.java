package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import com.naucnacentrala.NaucnaCentrala.services.CasopisService;
import com.naucnacentrala.NaucnaCentrala.services.NOService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromeniCasopis implements JavaDelegate {

    @Autowired
    CasopisService casopisService;

    @Autowired
    NOService naucService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> casopis = (List<FormSubmissionDTO>)execution.getVariable("ispravka");
        Long casopisID = (Long) execution.getVariable("casopisID");
        Casopis cas = casopisService.findOneById(casopisID);
        ArrayList<NaucnaOblast> no = new ArrayList<>();
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
            if (formField.getFieldId().equals("naucneIzmena")) {
                List<NaucnaOblast> oblastiIzBaze = naucService.findAll();
                for(NaucnaOblast n : oblastiIzBaze) {
                    if(n.getSifra().equals(Long.parseLong(formField.getFieldValue()))) {
                        no.add(n);
                        break;
                    }
                }
            }
        }
        if(!no.isEmpty()) {
            cas.setNaucneOblasti(no);
        }
        cas = casopisService.save(cas);

        execution.setVariable("naziv", cas.getNaziv());
        execution.setVariable("issn", cas.getIssn());
        execution.setVariable("naplata_clanarine", cas.getClanarina());
    }
}
