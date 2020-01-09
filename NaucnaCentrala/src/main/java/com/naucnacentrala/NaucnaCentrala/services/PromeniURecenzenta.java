package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.model.Korisnik;
import com.naucnacentrala.NaucnaCentrala.repository.KorisnikRepository;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromeniURecenzenta implements JavaDelegate {

    @Autowired
    KorisnikRepository korisnikRepo;

    @Autowired
    IdentityService identityService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String korisnik = (String) execution.getVariable("korisnik");
        identityService.createMembership(korisnik, "recenzenti");

        Korisnik k = korisnikRepo.findOneByUsername(korisnik);
        k.setUloga("recenzent");
        k = korisnikRepo.save(k);
    }
}
