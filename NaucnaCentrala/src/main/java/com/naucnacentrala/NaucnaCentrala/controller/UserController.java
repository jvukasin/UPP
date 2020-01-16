package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.dto.FormFieldsDTO;
import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.dto.UserInfoDTO;
import com.naucnacentrala.NaucnaCentrala.model.*;
import com.naucnacentrala.NaucnaCentrala.security.TokenUtils;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    KorisnikService korisnikService;

    @GetMapping(path = "/get", produces = "application/json")
    public @ResponseBody FormFieldsDTO get() {
        //provera da li korisnik sa id-jem pera postoji
        //List<User> users = identityService.createUserQuery().userId("pera").list();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("registracija");

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        return new FormFieldsDTO(task.getId(), pi.getId(), properties);
    }

    @PostMapping(path = "/post/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity post(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        try {
            validateData(dto);
        } catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "registration", dto);
        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/verify/{processId}/{username}", method = RequestMethod.GET)
    public void verifyMail(@PathVariable String processId, @PathVariable String username) {
        byte[] actualByte1 = Base64.getDecoder().decode(username);
        byte[] actualByte2 = Base64.getDecoder().decode(processId);
        String praviUsername = new String(actualByte1);
        String praviProces = new String(actualByte2);

        runtimeService.setVariable(praviProces, "potvrdio", true);
        runtimeService.setVariable(praviProces, "korisnik", praviUsername);
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserInfoDTO> getUser(HttpServletRequest request){
        String username = korisnikService.getUsernameFromRequest(request);
        UserInfoDTO ui = new UserInfoDTO();
        if(username != "" && username != null) {
            User u = (User) korisnikService.findOneByUsername(username);
            if(u instanceof Admin){
                u = (Admin) u;
                ui.setRole("Admin");
            } else if (u instanceof Autor) {
                ui.setRole("Autor");
            } else if (u instanceof Recenzent) {
                ui.setRole("Recenzent");
            } else if (u instanceof Urednik) {
                ui.setRole("Urednik");
            }
            ui.setUsername(u.getUsername());
            return new ResponseEntity<UserInfoDTO>(ui, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    private HashMap<String, Object> mapListToDto(List<FormSubmissionDTO> list)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormSubmissionDTO temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }


    private void validateData(List<FormSubmissionDTO> data) throws Exception {
        ArrayList<User> korisnici = (ArrayList) korisnikService.findAll();
        for (FormSubmissionDTO f : data) {
            if(f.getFieldId().equals("email")) {
                if(!mailOk(f.getFieldValue(), korisnici)) throw new Exception("Nevalidan email");
            } else if (f.getFieldId().equals("username")) {
                if(!userOk(f.getFieldValue(), korisnici)) throw new Exception("Nevalidan username");
            } else if (f.getFieldId().equals("password")) {
                if(!passOk(f.getFieldValue())) throw new Exception("Nevalidan password");
            }
        }
    }

    private boolean mailOk(String mail, ArrayList<User> korisnici) {
        for(User k : korisnici) {
            if(k.getEmail().equals(mail)) {
                return false;
            }
        }
        if(mail.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        Matcher matcher = pattern.matcher(mail);

        return matcher.matches();
    }

    private boolean userOk(String username, ArrayList<User> korisnici) {
        for(User k : korisnici) {
            if(k.getUsername().equals(username)) {
                return false;
            }
        }
        if(username.isEmpty()) {
            return false;
        }
        if(username.contains(";") || username.contains(">") || username.contains("<")) {
            return false;
        }
        for(Character c : username.toCharArray()) {
            if (Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean passOk(String pass) {
        if(pass.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
        Matcher matcher = pattern.matcher(pass);

        return matcher.matches();
    }
}
