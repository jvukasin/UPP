package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import com.naucnacentrala.NaucnaCentrala.model.Urednik;
import com.naucnacentrala.NaucnaCentrala.services.CasopisService;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class IzaberiUrednikaNO implements JavaDelegate {

    @Autowired
    KorisnikService korisnikService;

    @Autowired
    CasopisService casopisService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long casopisID = (Long) execution.getVariable("izabraniCasopisID");
        Casopis c = casopisService.findOneById(casopisID);
        String glavniUrednik = (String) execution.getVariable("glavniUrednik");
        String sifraNO = (String) execution.getVariable("konacan_nauc_obl");

        List<Urednik> uredniciCasopisa = c.getUredniciNO();

        List<Urednik> sadrzeNO = new ArrayList<>();
        if(uredniciCasopisa.size() == 0) {
            execution.setVariable("ko_bira_rec", glavniUrednik);
        } else {
            for(Urednik u : uredniciCasopisa) {
                for(NaucnaOblast no : u.getNaucneOblasti()) {
                    if(no.getSifra().toString().equals(sifraNO)) {
                        sadrzeNO.add(u);
                        break;
                    }
                }
            }
            if(sadrzeNO.size() == 0) {
                execution.setVariable("ko_bira_rec", glavniUrednik);
            } else {
                Random rand = new Random();
                Urednik randomIzabrani = sadrzeNO.get(rand.nextInt(sadrzeNO.size()));
                execution.setVariable("ko_bira_rec", randomIzabrani.getUsername());
            }
        }
    }
}
