package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import com.naucnacentrala.NaucnaCentrala.services.NaucniRadService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifikacijaRadPrihvacen implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Autowired
    NaucniRadService naucniRadService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String autor = (String) execution.getVariable("autor");
        User a = korisnikService.findOneByUsername(autor);
        String subject = "Naucna Centrala - notifikacija";
        String poruka = "Zdravo " + a.getIme() + ",\n\nVaš rad sa naslovom \"" + execution.getVariable("konacan_naslov") + "\" je prihvaćen!";
        korisnikService.sendMail(a, subject, poruka);
    }
}