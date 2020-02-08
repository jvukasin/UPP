package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.client.RegistrationClient;
import com.naucnacentrala.NaucnaCentrala.dto.CasopisDTO;
import com.naucnacentrala.NaucnaCentrala.dto.KPRegistrationDTO;
import com.naucnacentrala.NaucnaCentrala.services.KPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "kp")
public class KPController {

    @Autowired
    KPService kpService;

    @RequestMapping(value = "/createPlan/{magazineId}", method = RequestMethod.GET)
    public ResponseEntity<?> sendKPCreatePlan(@PathVariable("magazineId") long magazineId){
        return new ResponseEntity<>(kpService.createPlan(magazineId), HttpStatus.OK);
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity registerSeller(@RequestBody CasopisDTO magazineDTO){
        return new ResponseEntity<>(kpService.initRegistration(magazineDTO), HttpStatus.OK);
    }

    @RequestMapping(value = "/registration/review", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity reviewRegistrationSeller(@RequestBody CasopisDTO magazineDTO){
        return new ResponseEntity<>(kpService.reviewRegistrationSeller(magazineDTO), HttpStatus.OK);
    }

    @RequestMapping(value = "/registration/status", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity registerSeller(@RequestBody KPRegistrationDTO kprDTO){
        kpService.changeRegistrationStatus(kprDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/getPlans/{magazineId}", method = RequestMethod.GET)
    public ResponseEntity<?> getBillingPlans(@PathVariable("magazineId") long magazineId){
        return new ResponseEntity<>(kpService.getMagazinePlans(magazineId), HttpStatus.OK);
    }

}
