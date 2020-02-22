package com.naucnacentrala.NaucnaCentrala.services.tasks;

import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.repository.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProveraRegistracije implements JavaDelegate {

    @Autowired
    UserRepository korisnikRepo;

    private String username;
    private String email;
    private boolean recenzent = false;
    private boolean postoji = false;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        if (execution.getVariable("dalje") == null) {
            execution.setVariable("dalje", false);
        }
        List<FormSubmissionDTO> registration = (List<FormSubmissionDTO>)execution.getVariable("registration");
        ArrayList<User> korisnici = (ArrayList) korisnikRepo.findAll();
        for (FormSubmissionDTO formField : registration) {
            if(formField.getFieldId().equals("username")) {
                username = formField.getFieldValue();
                execution.setVariable("korisnik", username);
            }
            if(formField.getFieldId().equals("email")) {
                email = formField.getFieldValue();
            }
            if(formField.getFieldId().equals("recenzent")) {
                if(formField.getFieldValue() == "") {
                    recenzent = false;
                } else {
                    recenzent = true;
                }
            }
        }

        for(User k : korisnici) {
            if(k.getEmail().equals(email) || k.getUsername().equals(username)) {
                postoji = true;
                break;
            }
        }

        if(postoji) {
            execution.setVariable("dalje", false);
        } else {
            execution.setVariable("dalje", true);
            execution.setVariable("recenzent", recenzent);
        }
    }
}
