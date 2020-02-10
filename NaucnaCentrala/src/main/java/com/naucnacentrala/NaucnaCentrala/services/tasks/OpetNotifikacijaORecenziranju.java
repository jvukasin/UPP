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
public class OpetNotifikacijaORecenziranju implements JavaDelegate {

        @Autowired
        KorisnikService korisnikService;

        @Override
        public void execute (DelegateExecution execution) throws Exception {
            List<String> recenz = (List<String>) execution.getVariable("koRecenzira");
            List<String> prazna = new ArrayList<>();

            execution.setVariable("gotovi", prazna);
            execution.setVariable("komentari_za_urednike", prazna);
            execution.setVariable("komentari_za_autore", prazna);
            execution.setVariable("predlog_za_rad", prazna);


            for (String r : recenz) {
                User a = korisnikService.findOneByUsername(r);
                String subject = "Naucna Centrala - notifikacija o recenziranju rada";
                String poruka = "Zdravo " + a.getIme() + ",\n\nRad sa nazivom \"" + execution.getVariable("konacan_naslov") + "\" čeka Vašu ponovnu recenziju.\n" +
                        "Recenziju je potrebno uraditi u roku od 48 sati, u suprotnom će biti izabran novi recenzent.";
                korisnikService.sendMail(a, subject, poruka);
            }
        }
}
