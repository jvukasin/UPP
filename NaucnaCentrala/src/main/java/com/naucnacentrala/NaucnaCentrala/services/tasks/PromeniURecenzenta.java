package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.Recenzent;
import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromeniURecenzenta implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Autowired
    IdentityService identityService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String korisnik = (String) execution.getVariable("korisnik");
        identityService.createMembership(korisnik, "recenzenti");

        User k = korisnikService.findOneByUsername(korisnik);
        Recenzent r = new Recenzent(k);
        korisnikService.remove(k.getUsername());
        r = (Recenzent) korisnikService.save(r);
    }
}
