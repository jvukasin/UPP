package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.model.Recenzent;
import com.naucnacentrala.NaucnaCentrala.model.Urednik;
import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.repository.jpa.UserRepository;
import com.naucnacentrala.NaucnaCentrala.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class KorisnikService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TokenUtils tokenUtils;

    //klasa Environment ocitava vrednosti iz application.properties fajla
    @Autowired
    private Environment env;

    @Autowired
    UserRepository korRepo;

    public List<User> findAll() {
        return korRepo.findAll();
    }

    public User findOneByUsername(String username) {
        return korRepo.findOneByUsername(username);
    }

    public User save(User user) {
        return korRepo.save(user);
    }

    public void remove(String username) {
        korRepo.deleteById(username);
    }

    public void sendMail(User user, String subject, String tekstPoruke) throws MailException  {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom(env.getProperty("spring.mail.username"));
        mail.setSubject(subject);

        mail.setText(tekstPoruke);

        javaMailSender.send(mail);
    }

    public void sendNotificationSync(String procesId, User user) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom(env.getProperty("spring.mail.username"));
        mail.setSubject("Verifikacija naloga");

        String encUser = Base64.getEncoder().encodeToString(user.getUsername().getBytes());
        String encProces = Base64.getEncoder().encodeToString(procesId.getBytes());

        String path = "https://localhost:8601/users/verify/" + encProces + "/" + encUser;

        mail.setText("Zdravo " + user.getIme() + ",\n\n Molimo vas da kliknete na sledeći link kako biste verifikovali vaš nalog: "+ path);

        javaMailSender.send(mail);
    }

    public String getUsernameFromRequest(HttpServletRequest request) {
        String authToken = tokenUtils.getToken(request);
        if (authToken == null) {
            return null;
        }
        String username = tokenUtils.getUsernameFromToken(authToken);
        return username;
    }

    public String getUsernameFromRequestAndToken(HttpServletRequest request, TokenUtils tokenUtils) {
        String authToken = tokenUtils.getToken(request);
        if (authToken == null) {
            return null;
        }
        String username = tokenUtils.getUsernameFromToken(authToken);
        return username;
    }

    public ArrayList<User> findRecenzente() {
        ArrayList<User> recenzenti = new ArrayList<>();
        ArrayList<User> users = (ArrayList) korRepo.findAll();
        for(User u : users) {
            if(u instanceof Recenzent) {
                recenzenti.add((Recenzent) u);
            }
        }
        return recenzenti;
    }

    public ArrayList<User> findUrednike(String username) {
        ArrayList<User> urednici = new ArrayList<>();
        ArrayList<User> users = (ArrayList) korRepo.findAll();
        for(User u : users) {
            if(u instanceof Urednik) {
                if(!u.getUsername().equals(username)) {
                    urednici.add((Urednik) u);
                }
            }
        }
        return urednici;
    }

}
