package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.model.Recenzent;
import com.naucnacentrala.NaucnaCentrala.model.Urednik;
import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.repository.UserRepository;
import com.naucnacentrala.NaucnaCentrala.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

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

    public void sendNotificationSync(String procesId, User user) throws MailException, InterruptedException {
//        SimpleMailMessage mail = new SimpleMailMessage();
//        mail.setTo(user.getEmail());
//        mail.setFrom(env.getProperty("spring.mail.username"));
//        mail.setSubject("Verifikacija naloga");

        String encUser = Base64.getEncoder().encodeToString(user.getUsername().getBytes());
        String encProces = Base64.getEncoder().encodeToString(procesId.getBytes());

        String path = "http://localhost:8080/users/verify/" + encProces + "/" + encUser;

//        mail.setText("Zdravo " + user.getIme() + ",\n\n Molimo vas da kliknete na sledeći link kako biste verifikovali vaš nalog: "+ path);
//
//        javaMailSender.send(mail);

        // change accordingly
        final String username = env.getProperty("spring.mail.username");

        // change accordingly
        final String password = env.getProperty("spring.mail.password");

        // or IP address
        final String host = "localhost";

        // Get system properties
        Properties props = new Properties();

        // enable authentication
        props.put("mail.smtp.auth", host);

        // enable STARTTLS
        props.put("mail.smtp.starttls.enable", "true");

        // Setup mail server
        props.put("mail.smtp.host", "smtp.gmail.com");

        // TLS Port
        props.put("mail.smtp.port", "587");

        // creating Session instance referenced to
        // Authenticator object to pass in
        // Session.getInstance argument
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {

                    //override the getPasswordAuthentication method
                    protected PasswordAuthentication
                    getPasswordAuthentication() {

                        return new PasswordAuthentication(username,
                                password);
                    }
                });

        try {

            // compose the message
            // javax.mail.internet.MimeMessage class is
            // mostly used for abstraction.
            Message message = new MimeMessage(session);

            // header field of the header.
            message.setFrom(new InternetAddress("flylivedrive@gmail.com"));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail()));
            message.setSubject("Verifikacija naloga");
            message.setText("Zdravo " + user.getIme() + ",\n\n Molimo vas da kliknete na sledeći link kako biste verifikovali vaš nalog: "+ path);

            Transport.send(message);         //send Message

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
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

    public ArrayList<User> findUrednike() {
        ArrayList<User> urednici = new ArrayList<>();
        ArrayList<User> users = (ArrayList) korRepo.findAll();
        for(User u : users) {
            if(u instanceof Urednik) {
                urednici.add((Urednik) u);
            }
        }
        return urednici;
    }

}
