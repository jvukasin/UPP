package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifikacijaKomentariAutoru implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String autor = (String) execution.getVariable("autor");
        User a = korisnikService.findOneByUsername(autor);
        String subject = "Naucna Centrala - notifikacija o radu";
        String poruka = "Zdravo " + a.getIme() + ",\n\nVaš rad \"" + execution.getVariable("konacan_naslov") + "\" se može prihvatiti uz izmene.\n " +
                "Potrebno je da pročitate komentare i izvršite izmene u narednih 48 sati. U suprotnom će rad biti odbijen.";
        korisnikService.sendMail(a, subject, poruka);
    }
}
