package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.Recenzent;
import com.naucnacentrala.NaucnaCentrala.model.Urednik;
import com.naucnacentrala.NaucnaCentrala.services.CasopisService;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IzmeniOdbor implements JavaDelegate {

    @Autowired
    CasopisService casopisService;

    @Autowired
    KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> odbor = (List<FormSubmissionDTO>)execution.getVariable("odborIzmena");
        Long casopisID = (Long) execution.getVariable("casopisID");
        Casopis casopis = casopisService.findOneById(casopisID);

        ArrayList<Urednik> uredniciList = new ArrayList<>();
        ArrayList<Recenzent> recenzentiList = new ArrayList<>();

        for (FormSubmissionDTO formField : odbor) {
            if (formField.getFieldId().equals("uredniciIzmena")) {
                String username = formField.getFieldValue();
                Urednik u = (Urednik) korisnikService.findOneByUsername(username);
                u.setCasopis(casopis);
                uredniciList.add(u);
            }
            if (formField.getFieldId().equals("recenzentiIzmena")) {
                String username = formField.getFieldValue();
                Recenzent r = (Recenzent) korisnikService.findOneByUsername(username);
                recenzentiList.add(r);
            }
        }

        casopis.setRecenzenti(recenzentiList);
        casopis.setUredniciNO(uredniciList);
        casopisService.save(casopis);
    }
}
