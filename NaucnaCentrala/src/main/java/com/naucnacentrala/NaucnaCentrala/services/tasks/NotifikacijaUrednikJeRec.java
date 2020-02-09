package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotifikacijaUrednikJeRec implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String kome = (String) execution.getVariable("ko_bira_rec");
        List<String> recenzirosi = new ArrayList<>();
        recenzirosi.add(kome);
        execution.setVariable("koRecenzira", recenzirosi);

        User a = korisnikService.findOneByUsername(kome);
        String subject = "Naucna Centrala - notifikacija o recenziranju rada";
        String poruka = "Zdravo " + a.getIme() + ",\n\nZa recenziranje rada \"" + execution.getVariable("konacan_naslov") + "\" nema (dovljan broj) recenzenata.\n" +
                "Iz tog razloga potrebno je da Vi recenzirate rad.";
        korisnikService.sendMail(a, subject, poruka);
    }
}
