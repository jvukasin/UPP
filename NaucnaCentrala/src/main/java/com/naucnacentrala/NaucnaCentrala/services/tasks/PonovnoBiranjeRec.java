package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PonovnoBiranjeRec implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String koNijeOdradio = (String) execution.getVariable("JedRec");
        String urednik = (String) execution.getVariable("ko_bira_rec");
        User ur = korisnikService.findOneByUsername(urednik);
        User recen = korisnikService.findOneByUsername(koNijeOdradio);
        String formiranRec = recen.getIme() + " " + recen.getPrezime() + "(" + koNijeOdradio + ")";
        String subject = "Naucna Centrala - notifikacija o izboru novog recenzenta";
        String poruka = "Zdravo " + ur.getIme() + ",\n\nRecenzent " + formiranRec + " nije izvršio/izvršila recenziju rada \"" + execution.getVariable("konacan_naslov") + "\".\n" +
                "Potrebno je izabrati novog recenzenta.";
        korisnikService.sendMail(ur, subject, poruka);
    }
}
