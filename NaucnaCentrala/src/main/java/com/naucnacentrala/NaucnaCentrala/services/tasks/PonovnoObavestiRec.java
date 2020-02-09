package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PonovnoObavestiRec implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String username = (String) execution.getVariable("novi_rec");
        User a = korisnikService.findOneByUsername(username);
        execution.setVariable("JedRec", username);
        String subject = "Naucna Centrala - notifikacija o recenziranju rada";
        String poruka = "Zdravo " + a.getIme() + ",\n\nRad sa nazivom \"" + execution.getVariable("konacan_naslov") + "\" čeka Vašu recenziju.";
        korisnikService.sendMail(a, subject, poruka);
    }
}
