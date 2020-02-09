package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifikacijaIzborRec implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String kome = (String) execution.getVariable("ko_bira_rec");
        User a = korisnikService.findOneByUsername(kome);
        String subject = "Notifikacija o novom radu";
        String poruka = "Zdravo " + a.getIme() + ",\n\nPotrebno je izabrati recenzente za rad \"" + execution.getVariable("konacan_naslov") + "\".";
        korisnikService.sendMail(a, subject, poruka);
    }
}
