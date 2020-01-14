package com.naucnacentrala.NaucnaCentrala.handlers;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistracijaHandler implements ExecutionListener {

    @Autowired
    IdentityService identityService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        List<Group> groups = identityService.createGroupQuery().groupIdIn("recenzenti", "admin", "urednici").list();

        if(groups.isEmpty()) {
            Group recenzentiGroup = identityService.newGroup("recenzenti");
            recenzentiGroup.setId("recenzenti");
            recenzentiGroup.setName("recenzenti");
            identityService.saveGroup(recenzentiGroup);

            Group adminGroup = identityService.newGroup("admin");
            adminGroup.setId("admin");
            adminGroup.setName("admin");
            identityService.saveGroup(adminGroup);

            Group uredniciGroup = identityService.newGroup("urednici");
            uredniciGroup.setId("urednici");
            uredniciGroup.setName("urednici");
            identityService.saveGroup(uredniciGroup);
        }
        User temp = identityService.createUserQuery().userId("dovla").singleResult();
        if(temp == null) {
            User user = (User) identityService.newUser("vlada");
            user.setEmail("cvetanovic9696@gmail.com");
            user.setPassword("vlada");
            user.setFirstName("Vladimir");
            user.setLastName("Cvetanovic");
            user.setId("vlada");
            identityService.saveUser(user);
            identityService.createMembership(user.getId(), "admin");
        }
    }
}
