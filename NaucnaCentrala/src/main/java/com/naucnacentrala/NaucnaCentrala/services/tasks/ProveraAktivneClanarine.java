package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.Clanarina;
import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.services.CasopisService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveraAktivneClanarine  implements JavaDelegate {

    @Autowired
    CasopisService casopisService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String autor = (String) execution.getVariable("autor");
        Long casopisID = (Long) execution.getVariable("izabraniCasopisID");

        Casopis c = casopisService.findOneById(casopisID);
        boolean ima = false;
        for(Clanarina cl : c.getKorisniciSaClanarinom()) {
            if(cl.getUsername().equals(autor)) {
                execution.setVariable("imaclanarinu", true);
                ima = true;
                break;
            }
        }
        if(!ima) {
            execution.setVariable("imaclanarinu", false);
        }
    }
}
