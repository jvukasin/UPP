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
public class NotifikacijaNijeIspravio implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Autowired
    NaucniRadService naucniRadService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long radID = (Long) execution.getVariable("radID");
        NaucniRad rad = naucniRadService.findOneById(radID);
        naucniRadService.delete(rad);

        String autor = (String) execution.getVariable("autor");
        User a = korisnikService.findOneByUsername(autor);
        String subject = "Naucna Centrala - notifikacija o radu";
        String poruka = "Zdravo " + a.getIme() + ",\n\nVreme predviđeno za ispravku Vašeg rada \"" + execution.getVariable("konacan_naslov") + "\" je isteklo. Vaš rad je odbijen.";
        korisnikService.sendMail(a, subject, poruka);
    }
}
