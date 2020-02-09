package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotifikacijaORecenziranju implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> recenzenti = (List<FormSubmissionDTO>) execution.getVariable("koRecenziraDTO");
        List<String> recenzirosi = new ArrayList<>();

        for (FormSubmissionDTO formField : recenzenti) {
            String username = formField.getFieldValue();
            recenzirosi.add(username);
            User a = korisnikService.findOneByUsername(username);
            String subject = "Naucna Centrala - notifikacija o recenziranju rada";
            String poruka = "Zdravo " + a.getIme() + ",\n\nRad sa nazivom \"" + execution.getVariable("konacan_naslov") + "\" čeka Vašu recenziju.\n" +
                    "Recenziju je potrebno uraditi u roku od 48 sati, u suprotnom će biti izabran novi recenzent.";
            korisnikService.sendMail(a, subject, poruka);
        }
        execution.setVariable("koRecenzira", recenzirosi);
    }
}
