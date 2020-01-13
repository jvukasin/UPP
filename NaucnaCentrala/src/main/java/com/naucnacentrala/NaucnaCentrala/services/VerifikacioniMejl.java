package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.model.NaucnaOblast;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VerifikacioniMejl implements JavaDelegate {

    @Autowired
    KorisnikService servis;

    @Autowired
    NOService naucServis;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> registration = (List<FormSubmissionDTO>)execution.getVariable("registration");
        User k = new User();
        ArrayList<NaucnaOblast> no = new ArrayList<>();
        ArrayList<NaucnaOblast> oblastiIzBaze = (ArrayList) naucServis.findAll();
        for (FormSubmissionDTO formField : registration) {
            if (formField.getFieldId().equals("username")) {
                k.setUsername(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("ime")) {
                k.setIme(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("prezime")) {
                k.setPrezime(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("grad")) {
                k.setGrad(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("drzava")) {
                k.setDrzava(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("titula")) {
                k.setTitula(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("email")) {
                k.setEmail(formField.getFieldValue());
            }
            if (formField.getFieldId().equals("password")) {
                String salt = BCrypt.gensalt();
                String pass = formField.getFieldValue();
                String hashedPass = BCrypt.hashpw(pass, salt);
                k.setPassword(hashedPass);
            }
            if (formField.getFieldId().equals("nauc_oblasti")) {
                for(NaucnaOblast n : oblastiIzBaze) {
                    if(n.getNaziv().equalsIgnoreCase(formField.getFieldValue())) {
                        no.add(n);
                        break;
                    }
                }
            }
        }

            k.setAktivan(false);
            k.setNaucneOblasti(no);
            servis.save(k);

            execution.setVariable("potvrdio", false);

            servis.sendNotificationSync(execution.getProcessInstanceId() ,k);
    }
}