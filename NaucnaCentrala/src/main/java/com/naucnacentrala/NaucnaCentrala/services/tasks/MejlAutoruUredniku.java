package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MejlAutoruUredniku implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String autor = (String) execution.getVariable("autor");
        String GU = (String) execution.getVariable("glavniUrednik");
        User at = korisnikService.findOneByUsername(autor);
        User gu = korisnikService.findOneByUsername(GU);
        String path = "http://localhost:4202";
        String subject = "Notifikacija o novom radu";
        String poruka1 = "Zdravo " + at.getIme() + ",\n\n Novi rad sa naslovom \"" + execution.getVariable("konacan_naslov") + "\" je poslat na uvid glavnom uredniku.\n Na Naucnu Centralu možete otići putem ovog linka: "+ path;
        korisnikService.sendMail(at, subject, poruka1);

        String poruka2 = "Zdravo " + gu.getIme() + ",\n\n Novi rad sa naslovom \"" + execution.getVariable("konacan_naslov") + "\" je poslat na uvid glavnom uredniku.\n Na Naucnu Centralu možete otići putem ovog linka: "+ path;
        korisnikService.sendMail(gu, subject, poruka2);
    }
}
