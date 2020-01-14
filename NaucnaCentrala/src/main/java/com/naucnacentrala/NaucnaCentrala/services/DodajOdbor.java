package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.NacinPlacanja;
import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DodajOdbor implements JavaDelegate {

    @Autowired
    CasopisService casopisService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> odbor = (List<FormSubmissionDTO>)execution.getVariable("odbor");
//        Casopis casopis = casopisService.findOneById()
//        for (FormSubmissionDTO formField : odbor) {
//            if (formField.getFieldId().equals("urednici")) {
//                for(NaucnaOblast n : oblastiIzBaze) {
//                    if(n.getNaziv().equalsIgnoreCase(formField.getFieldValue())) {
//                        no.add(n);
//                        break;
//                    }
//                }
//            }
//            if (formField.getFieldId().equals("recenzenti")) {
//                for(NacinPlacanja n : placanjaIzBaze) {
//                    if(n.getNaziv().equalsIgnoreCase(formField.getFieldValue())) {
//                        np.add(n);
//                        break;
//                    }
//                }
//            }
//        }
    }
}