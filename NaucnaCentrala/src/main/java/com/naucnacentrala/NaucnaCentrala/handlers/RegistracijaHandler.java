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
        List<Group> groups = identityService.createGroupQuery().groupIdIn("recenzenti", "admin", "urednici", "autori", "korisnici").list();

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

            Group autoriGroup = identityService.newGroup("autori");
            uredniciGroup.setId("autori");
            uredniciGroup.setName("autori");
            identityService.saveGroup(autoriGroup);

            Group korisnici = identityService.newGroup("korisnici");
            uredniciGroup.setId("korisnici");
            uredniciGroup.setName("korisnici");
            identityService.saveGroup(korisnici);
        }
        User temp = identityService.createUserQuery().userId("dovla").singleResult();
        if(temp == null) {
            //admin
            User user = (User) identityService.newUser("vlada");
            user.setEmail("cvetanovic9696@gmail.com");
            user.setPassword("vlada");
            user.setFirstName("Vladimir");
            user.setLastName("Cvetanovic");
            user.setId("vlada");
            identityService.saveUser(user);
            identityService.createMembership(user.getId(), "admin");
            //recenzent
            User rec = (User) identityService.newUser("milica");
            rec.setEmail("makarena@gmail.com");
            rec.setPassword("milica");
            rec.setFirstName("Milica");
            rec.setLastName("Makaric");
            rec.setId("milica");
            identityService.saveUser(rec);
            identityService.createMembership(rec.getId(), "recenzenti");
            //autor
            User autor = (User) identityService.newUser("pera");
            autor.setEmail("peraa@gmail.com");
            autor.setPassword("pera");
            autor.setFirstName("Petar");
            autor.setLastName("Petrovic");
            autor.setId("pera");
            identityService.saveUser(autor);
            identityService.createMembership(autor.getId(), "autori");
            //urednik
            User urd = (User) identityService.newUser("djo");
            urd.setEmail("djodja@gmail.com");
            urd.setPassword("djodja");
            urd.setFirstName("Nikola");
            urd.setLastName("Djordjevic");
            urd.setId("djo");
            identityService.saveUser(urd);
            identityService.createMembership(urd.getId(), "urednici");
            //korisnik
            User usr = (User) identityService.newUser("vule");
            usr.setEmail("jovic.vukasin@gmail.com");
            usr.setPassword("vule");
            usr.setFirstName("Vukasin");
            usr.setLastName("Jovic");
            usr.setId("vule");
            identityService.saveUser(usr);
            identityService.createMembership(usr.getId(), "korisnici");
        }
    }
}
