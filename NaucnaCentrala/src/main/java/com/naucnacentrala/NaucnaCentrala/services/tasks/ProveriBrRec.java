package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import com.naucnacentrala.NaucnaCentrala.model.Recenzent;
import com.naucnacentrala.NaucnaCentrala.services.CasopisService;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProveriBrRec implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Autowired
    CasopisService casopisService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long casopisID = (Long) execution.getVariable("izabraniCasopisID");
        Casopis c = casopisService.findOneById(casopisID);
        String sifraNO = (String) execution.getVariable("konacan_nauc_obl");

        List<Recenzent> recenzentiCasopis = c.getRecenzenti();
        List<Recenzent> rec = new ArrayList<>();
        for(Recenzent r : recenzentiCasopis) {
            for(NaucnaOblast no : r.getNaucneOblasti()) {
                if(no.getSifra().toString().equals(sifraNO)) {
                    rec.add(r);
                    break;
                }
            }
        }

        if(rec.size() >= 2) {
            execution.setVariable("imaDovoljnoRec", true);
        } else {
            execution.setVariable("imaDovoljnoRec", false);
        }
    }
}
