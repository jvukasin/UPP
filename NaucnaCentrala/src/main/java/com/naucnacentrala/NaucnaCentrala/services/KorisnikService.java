package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.model.Korisnik;
import com.naucnacentrala.NaucnaCentrala.repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.List;

@Service
public class KorisnikService {

    @Autowired
    private JavaMailSender javaMailSender;

    //klasa Environment ocitava vrednosti iz application.properties fajla
    @Autowired
    private Environment env;

    @Autowired
    KorisnikRepository korRepo;

    public List<Korisnik> findAll() {
        return korRepo.findAll();
    }

    public Korisnik findOneByUsername(String username) {
        return korRepo.findOneByUsername(username);
    }

    public Korisnik save(Korisnik user) {
        return korRepo.save(user);
    }

    public void remove(String username) {
        korRepo.deleteById(username);
    }

    public void sendNotificationSync(String procesId, Korisnik user) throws MailException, InterruptedException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom(env.getProperty("spring.mail.username"));
        mail.setSubject("Verifikacija naloga");

        String encUser = Base64.getEncoder().encodeToString(user.getUsername().getBytes());
        String encProces = Base64.getEncoder().encodeToString(procesId.getBytes());

        String path = "http://localhost:4200/verified/" + encProces + "/" + encUser;

        mail.setText("Zdravo " + user.getIme() + ",\n\n Molimo vas da kliknete na sledeći link kako biste verifikovali vaš nalog: "+ path);

        javaMailSender.send(mail);
    }


}
