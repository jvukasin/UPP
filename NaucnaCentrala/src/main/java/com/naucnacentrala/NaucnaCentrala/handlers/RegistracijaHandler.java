package com.naucnacentrala.NaucnaCentrala.handlers;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;

public class RegistracijaHandler implements ExecutionListener {

    @Autowired
    IdentityService identityService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        User user = (User) identityService.newUser("vlada");
        user.setEmail("cvetanovic9696@gmail.com");
        user.setPassword("vlada");
        user.setFirstName("Vladimir");
        user.setLastName("Cvetanovic");
        user.setId("vlada");
        identityService.saveUser(user);
        identityService.createMembership("admin", user.getId());


        Group group = identityService.newGroup("recenzenti");
        group.setId("recenzenti");
        group.setName("recenzenti");
        identityService.saveGroup(group);

        Group adminGroup = identityService.newGroup("admin");
        group.setId("admin");
        group.setName("admin");
        identityService.saveGroup(group);

        Group uredniciGroup = identityService.newGroup("urednici");
        group.setId("urednici");
        group.setName("urednici");
        identityService.saveGroup(group);
    }
}
