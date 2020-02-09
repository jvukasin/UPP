package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KorekcijaPrijaveRada implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String autor = (String) execution.getVariable("autor");
        User a = korisnikService.findOneByUsername(autor);
        String subject = "Naucna Centrala - notifikacija o korekciji prijave rada";
        String poruka = "Zdravo " + a.getIme() + ",\n\nVaš rad sa naslovom \"" + execution.getVariable("konacan_naslov") + "\" zahteva korekciju priloženog dokumenta.\n" +
                "Rok za korekciju je 24 sata nakon čega se rad odbija.";
        korisnikService.sendMail(a, subject, poruka);
    }
}
