package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.dto.AccessClanarinaDTO;
import com.naucnacentrala.NaucnaCentrala.dto.FormFieldsDTO;
import com.naucnacentrala.NaucnaCentrala.dto.FormSubmissionDTO;
import com.naucnacentrala.NaucnaCentrala.dto.TaskDTO;
import com.naucnacentrala.NaucnaCentrala.model.*;
import com.naucnacentrala.NaucnaCentrala.repository.KoautorRepository;
import com.naucnacentrala.NaucnaCentrala.services.CasopisService;
import com.naucnacentrala.NaucnaCentrala.services.KorisnikService;
import com.naucnacentrala.NaucnaCentrala.services.NOService;
import com.naucnacentrala.NaucnaCentrala.services.NaucniRadService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sun.awt.image.ImageAccessException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/rad")
public class NaucniRadController {
    @Autowired
    IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    CasopisService casopisService;

    @Autowired
    KorisnikService korisnikService;

    @Autowired
    NOService noService;

    @Autowired
    NaucniRadService naucniRadService;

    @Autowired
    KoautorRepository koautorRepository;

    @GetMapping(path = "/get", produces = "application/json")
    public @ResponseBody
    FormFieldsDTO get() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("obrada_teksta");
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        List<Casopis> casopisi = casopisService.findAll();

        for(FormField field : properties) {
            if(field.getId().equals("casopis")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(Casopis c: casopisi){
                    enumType.getValues().put(c.getId().toString(), c.getNaziv().concat(", ").concat(c.getIssn()));
                }
            }
        }

        return new FormFieldsDTO(task.getId(), pi.getId(), properties);
    }

    @GetMapping(path = "/getFormFromTask/{taskId}", produces = "application/json")
    public @ResponseBody
    FormFieldsDTO getFormFromTask(@PathVariable String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String pid = task.getProcessInstanceId();

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        return new FormFieldsDTO(task.getId(), pid, properties);
    }


    @RequestMapping(value = "/tasks/coauthor", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity addCoauthorTasks(HttpServletRequest request) {
        String username = korisnikService.getUsernameFromRequest(request);
        List<Task> tasks = taskService.createTaskQuery().taskName("Unesi koautore").taskAssignee(username).list();
        List<TaskDTO> tasksDto = new ArrayList<>();
        for (Task task : tasks) {
            TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
            tasksDto.add(t);
        }
        return new ResponseEntity<>(tasksDto, HttpStatus.OK);
    }

    @PostMapping(path = "/postKoautori/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity postKoautori(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        List<FormSubmissionDTO> formFields = dto;
        Koautor k = new Koautor();
        for(FormSubmissionDTO d : formFields) {
            if(d.getFieldId().equals("ime_koautora")) {
                k.setIme(d.getFieldValue());
            } else if (d.getFieldId().equals("email_koautora")) {
                k.setEmail(d.getFieldValue());
            }
            else if (d.getFieldId().equals("grad_koautora")) {
                k.setGrad(d.getFieldValue());
            }
            else if (d.getFieldId().equals("drzava_koautora")) {
                k.setDrzava(d.getFieldValue());
            }
        }
        Long radID = (Long) runtimeService.getVariable(processInstanceId, "radID");
        NaucniRad rad = naucniRadService.findOneById(radID);
        k.setNaucniRad(rad);
        koautorRepository.save(k);

        formService.submitTaskForm(taskId, map);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/getUrednikRadTasks", produces = "application/json")
//    @PreAuthorize("hasAuthority('UREDNICI_TASK')")
    public @ResponseBody ResponseEntity<List<TaskDTO>> getUrednikRadTasks(HttpServletRequest request) {

        String username = korisnikService.getUsernameFromRequest(request);
        List<Task> tasks = taskService.createTaskQuery().taskName("Obradjivanje rada").taskAssignee(username).list();
        List<TaskDTO> dtos = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
            dtos.add(t);
        }

        return new ResponseEntity(dtos,  HttpStatus.OK);
    }

    @GetMapping(path = "/getUrednikIzborRecTasks", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDTO>> getUrednikIzborRecTasks(HttpServletRequest request) {

        String username = korisnikService.getUsernameFromRequest(request);

        List<Task> tasks = taskService.createTaskQuery().taskName("Izbor recenzenata").taskAssignee(username).list();
        List<TaskDTO> dtos = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
            dtos.add(t);
        }

        return new ResponseEntity(dtos,  HttpStatus.OK);
    }

    @GetMapping(path = "/getRecenzentiForm/{taskId}", produces = "application/json")
    public @ResponseBody
    FormFieldsDTO getRecenzentiForm(@PathVariable String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String pid = task.getProcessInstanceId();

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        Long casopisID = (Long) runtimeService.getVariable(pid,"izabraniCasopisID");
        Casopis c = casopisService.findOneById(casopisID);
        String sifraNO = (String) runtimeService.getVariable(pid,"konacan_nauc_obl");

        List<Recenzent> recenzentiCasopis = c.getRecenzenti();
        List<Recenzent> rec = new ArrayList<>();
        for(Recenzent r : recenzentiCasopis) {
            for(NaucnaOblast no : r.getNaucneOblasti()) {
                if(no.getSifra().toString().equals(sifraNO)) {
                    rec.add(r);
                    break;
                }
            }
        }

        for(FormField field : properties) {
            if(field.getId().equals("recenzent")) {
                EnumFormType enumType = (EnumFormType) field.getType();
                for(Recenzent ree : rec){
                    String ip = ree.getIme() + " " + ree.getPrezime() + "(" + ree.getUsername() +")";
                    enumType.getValues().put(ree.getUsername(), ip);
                }
            }
        }

        return new FormFieldsDTO(task.getId(), pid, properties);
    }


    @GetMapping(path = "/getNoviRecenzentForm/{taskId}", produces = "application/json")
    public @ResponseBody
    FormFieldsDTO getNoviRecenzentForm(@PathVariable String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String pid = task.getProcessInstanceId();

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        Long casopisID = (Long) runtimeService.getVariable(pid,"izabraniCasopisID");
        Casopis c = casopisService.findOneById(casopisID);
        String sifraNO = (String) runtimeService.getVariable(pid,"konacan_nauc_obl");
        List<String> gotovi = (List<String>) runtimeService.getVariable(pid, "gotovi");

        List<Recenzent> recenzentiCasopis = c.getRecenzenti();
        List<User> rec = new ArrayList<>();

        for(Recenzent r : recenzentiCasopis) {
            for(NaucnaOblast no : r.getNaucneOblasti()) {
                if(no.getSifra().toString().equals(sifraNO)) {
                    rec.add(r);
                    break;
                }
            }
        }

        if(gotovi.size() != 0) {
            for(int i=0; i<rec.size(); i++) {
                for(String usr : gotovi) {
                    if(usr.equals(rec.get(i).getUsername())) {
                        rec.remove(i);
                    }
                }
            }
        }

        String urednik = (String) runtimeService.getVariable(pid, "ko_bira_rec");
        User ur = korisnikService.findOneByUsername(urednik);
        rec.add(ur);

        for(FormField field : properties) {
            if(field.getId().equals("novi_recenzent")) {
                EnumFormType enumType = (EnumFormType) field.getType();
                for(User ree : rec){
                    String ip = ree.getIme() + " " + ree.getPrezime() + "(" + ree.getUsername() +")";
                    enumType.getValues().put(ree.getUsername(), ip);
                }
            }
        }

        return new FormFieldsDTO(task.getId(), pid, properties);
    }

    @PostMapping(path = "/postRecenzenti/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity postRecenzenti(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        runtimeService.setVariable(processInstanceId, "koRecenziraDTO", dto);

        formService.submitTaskForm(taskId, map);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/postNoviRecenzent/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity postNoviRecenzent(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        FormSubmissionDTO field = dto.get(0);

        runtimeService.setVariable(processInstanceId, "novi_rec", field.getFieldValue());

        formService.submitTaskForm(taskId, map);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/getAutorIspravkaRadaTasks", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDTO>> getUrednikIspravkaRadaTasks(HttpServletRequest request) {

        String username = korisnikService.getUsernameFromRequest(request);
        List<Task> tasks = taskService.createTaskQuery().taskName("Korekcija rada").taskAssignee(username).list();
        List<TaskDTO> dtos = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
            dtos.add(t);
        }

        return new ResponseEntity(dtos,  HttpStatus.OK);
    }

    @GetMapping(path = "/getUrednikIzborRecOpetTasks", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDTO>> getUrednikIzborRecOpetTasks(HttpServletRequest request) {

        String username = korisnikService.getUsernameFromRequest(request);
        List<Task> tasks = taskService.createTaskQuery().taskName("Izaberi novog recenzenta").taskAssignee(username).list();
        List<TaskDTO> dtos = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
            dtos.add(t);
        }

        return new ResponseEntity(dtos,  HttpStatus.OK);
    }

    @GetMapping(path = "/getRecenzentRecenziranjeTasks", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDTO>> getRecenzentRecenziranjeTasks(HttpServletRequest request) {

        String username = korisnikService.getUsernameFromRequest(request);
        List<Task> tasks = taskService.createTaskQuery().taskName("Obavljanje recenzije").taskAssignee(username).list();
        List<TaskDTO> dtos = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
            dtos.add(t);
        }

        return new ResponseEntity(dtos,  HttpStatus.OK);
    }

    @PostMapping(path = "/postRecenzija/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity postRecenzija(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        List<String> komentariUrednici = (List<String>) runtimeService.getVariable(processInstanceId, "komentari_za_urednike");
        List<String> komentariRad = (List<String>) runtimeService.getVariable(processInstanceId, "komentari_za_autore");
        List<String> predlozi = (List<String>) runtimeService.getVariable(processInstanceId, "predlog_za_rad");
        for(FormSubmissionDTO field : dto) {
            if(field.getFieldId().equals("komentar_za_urednike")) {
                if(!field.getFieldValue().equals("")) {
                    komentariUrednici.add(field.getFieldValue());
                    runtimeService.setVariable(processInstanceId, "komentari_za_urednike", komentariUrednici);
                }
            } else if(field.getFieldId().equals("komentar_o_radu")) {
                if(!field.getFieldValue().equals("")) {
                    komentariRad.add(field.getFieldValue());
                    runtimeService.setVariable(processInstanceId, "komentari_za_autore", komentariRad);
                }
            } else if(field.getFieldId().equals("preporuka")) {
                predlozi.add(field.getFieldValue());
                runtimeService.setVariable(processInstanceId, "predlog_za_rad", predlozi);
            }
        }

        formService.submitTaskForm(taskId, map);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(path = "/getUrednikPregledaTasks", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDTO>> getUrednikPregledaTasks(HttpServletRequest request) {

        String username = korisnikService.getUsernameFromRequest(request);

        List<Task> tasks = taskService.createTaskQuery().taskName("Urednik pregleda ocene").taskAssignee(username).list();
        List<TaskDTO> dtos = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
            dtos.add(t);
        }

        return new ResponseEntity(dtos,  HttpStatus.OK);
    }

    @GetMapping(path = "/getAutorKomentariTasks", produces = "application/json")
    public @ResponseBody ResponseEntity<List<TaskDTO>> getAutorKomentariTasks(HttpServletRequest request) {

        String username = korisnikService.getUsernameFromRequest(request);

        List<Task> tasks = taskService.createTaskQuery().taskName("Prikazivanje komentara autoru").taskAssignee(username).list();
        List<TaskDTO> dtos = new ArrayList<TaskDTO>();
        for (Task task : tasks) {
            TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
            dtos.add(t);
        }

        return new ResponseEntity(dtos, HttpStatus.OK);
    }

    @GetMapping(path = "/getPregledForm/{taskId}", produces = "application/json")
    public @ResponseBody
    FormFieldsDTO getPregledForm(@PathVariable String taskId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String pid = task.getProcessInstanceId();

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        List<String> komentariUrednici = (List<String>) runtimeService.getVariable(pid, "komentari_za_urednike");
        List<String> komentariRad = (List<String>) runtimeService.getVariable(pid, "komentari_za_autore");
        List<String> predlozi = (List<String>) runtimeService.getVariable(pid, "predlog_za_rad");

        for(FormField field : properties) {
            if(field.getId().equals("posl_komentar_o_radu")) {
                EnumFormType enumType = (EnumFormType) field.getType();
                for(String temp : komentariRad) {
                    enumType.getValues().put(temp, temp);
                }
            } else if (field.getId().equals("posl_preporuka")) {
                EnumFormType enumType = (EnumFormType) field.getType();
                for(String temp : predlozi) {
                    enumType.getValues().put(temp, temp);
                }
            } else if (field.getId().equals("posl_komentar_za_urednika")) {
                EnumFormType enumType = (EnumFormType) field.getType();
                for(String temp : komentariUrednici) {
                    enumType.getValues().put(temp, temp);
                }
            }
        }

        return new FormFieldsDTO(task.getId(), pid, properties);
    }

    @GetMapping(path = "/getRadForm/{procesId}", produces = "application/json")
    public @ResponseBody
    FormFieldsDTO getRadForm(@PathVariable String procesId) {

        Task task = taskService.createTaskQuery().processInstanceId(procesId).singleResult();

        String pid = task.getProcessInstanceId();

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();

        List<NaucnaOblast> oblasti = noService.findAll();

        for(FormField field : properties) {
            if(field.getId().equals("naucna_oblast")) {
                EnumFormType enumType = (EnumFormType) field.getType();
                for(NaucnaOblast n : oblasti){
                    enumType.getValues().put(n.getSifra().toString(), n.getNaziv());
                }
            }
        }

        return new FormFieldsDTO(task.getId(), pid, properties);
    }

    @PostMapping(path = "/postIzabraniCasopis/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity postIzabraniCasopis(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId, HttpServletRequest request) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        String username = korisnikService.getUsernameFromRequest(request);
        FormSubmissionDTO formField = dto.get(0);

        runtimeService.setVariable(processInstanceId, "izabraniCasopisID", Long.parseLong(formField.getFieldValue()));
        runtimeService.setVariable(processInstanceId, "autor", username);

        formService.submitTaskForm(taskId, map);

        boolean opn = (boolean) runtimeService.getVariable(processInstanceId, "openaccess");
        boolean imacln = (boolean) runtimeService.getVariable(processInstanceId, "imaclanarinu");
        AccessClanarinaDTO d = new AccessClanarinaDTO(opn, imacln);

        return new ResponseEntity<>(d, HttpStatus.OK);
    }

    @PostMapping(path = "/postOdlukaUrednika/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity postOdlukaUrednika(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        FormSubmissionDTO formField = dto.get(0);
        String staKaze = formField.getFieldValue();
        runtimeService.setVariable(processInstanceId, "prihvati_rad", staKaze);

        formService.submitTaskForm(taskId, map);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/postRad/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity saveRad(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId, HttpServletRequest request) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        NaucniRad rad = new NaucniRad();

        List<FormSubmissionDTO> formFields = dto;

        Long id = (Long) runtimeService.getVariable(processInstanceId, "radID");
        if(id == null) {
            for(FormSubmissionDTO field : formFields) {
                if(field.getFieldId().equals("fajl")) {
                    String nazivFajlaCEO = field.getFieldValue();
                    String nazivFajla = nazivFajlaCEO.substring(12);
                    rad.setPdfName(nazivFajla);
                    break;
                }
            }
            rad = naucniRadService.save(rad);

            runtimeService.setVariable(processInstanceId, "rad", dto);
            runtimeService.setVariable(processInstanceId, "radID", rad.getId());
        } else {
            NaucniRad rad2 = naucniRadService.findOneById(id);
            for(FormSubmissionDTO field : formFields) {
                if(field.getFieldId().equals("naslov2")) {
                    runtimeService.setVariable(processInstanceId, "konacan_naslov", field.getFieldValue());
                } else if (field.getFieldId().equals("apstrakt2")) {
                    runtimeService.setVariable(processInstanceId, "konacan_apstrakt", field.getFieldValue());
                } else if (field.getFieldId().equals("kljucni_pojmovi2")) {
                    runtimeService.setVariable(processInstanceId, "konacan_kljuc_poj", field.getFieldValue());
                } else if(field.getFieldId().equals("fajl")) {
                    String nazivFajlaCEO = field.getFieldValue();
                    String nazivFajla = nazivFajlaCEO.substring(12);
                    rad2.setPdfName(nazivFajla);
                }
            }
            rad2 = naucniRadService.save(rad2);
        }

        formService.submitTaskForm(taskId, map);

        return new ResponseEntity<>(rad.getId().toString(),HttpStatus.OK);
    }

    @PostMapping(path = "/obradaRada/{taskId}", produces = "application/json")
    public @ResponseBody
    ResponseEntity obradaRada(@RequestBody List<FormSubmissionDTO> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        String relevantan;
        String formiran;

        for (FormSubmissionDTO formField : dto) {
            if(formField.getFieldId().equals("relevantan_rad")) {
                relevantan = formField.getFieldValue();
                if(relevantan == "true") {
                    runtimeService.setVariable(processInstanceId, "relevantan_rad", true);
                } else {
                    runtimeService.setVariable(processInstanceId, "relevantan_rad", false);
                }
            } else if(formField.getFieldId().equals("dobro_formiran")) {
                formiran = formField.getFieldValue();
                if(formiran == "true") {
                    runtimeService.setVariable(processInstanceId, "dobro_formiran", true);
                } else {
                    runtimeService.setVariable(processInstanceId, "dobro_formiran", false);
                }
            } else if(formField.getFieldId().equals("komentar_ur")) {
                if(formField.getFieldValue() != null) {
                    runtimeService.setVariable(processInstanceId, "ur_kom", formField.getFieldValue());
                }
            }
        }

        formService.submitTaskForm(taskId, map);

        //TODO dodaj ovde sta treba
        //vrati u responsu podatak koji treba
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/uploadFile/{radId}", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file , @PathVariable("radId") String radId) {
        NaucniRad rad = naucniRadService.findOneById(Long.parseLong(radId));
        rad = naucniRadService.savePdf(file, rad);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping(path = "/downloadFile/{procesId}")
    public @ResponseBody
    ResponseEntity downloadFile(@PathVariable String procesId) {

        Long radID = (Long) runtimeService.getVariable(procesId, "radID");

        NaucniRad rad = naucniRadService.findOneById(radID);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + rad.getPdfName() + "\"")
                .body(new ByteArrayResource(rad.getPdf()));
    }

    private HashMap<String, Object> mapListToDto(List<FormSubmissionDTO> list)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormSubmissionDTO temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }

}
