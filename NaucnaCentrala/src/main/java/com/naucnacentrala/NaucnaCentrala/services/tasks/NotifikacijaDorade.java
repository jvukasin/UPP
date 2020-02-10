package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifikacijaDorade implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String autor = (String) execution.getVariable("autor");
        User a = korisnikService.findOneByUsername(autor);
        String subject = "Naucna Centrala - notifikacija o radu";
        String poruka = "Zdravo " + a.getIme() + ",\n\nVašem radu \"" + execution.getVariable("konacan_naslov") + "\" je potrebna dorada.\n " +
                "Potrebno je da izvršite doradu u narednih 24 sata. U suprotnom će rad biti odbijen.";
        korisnikService.sendMail(a, subject, poruka);
    }
}