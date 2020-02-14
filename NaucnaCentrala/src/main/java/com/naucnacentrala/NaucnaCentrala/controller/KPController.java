package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.client.RegistrationClient;
import com.naucnacentrala.NaucnaCentrala.dto.AgreementDTO;
import com.naucnacentrala.NaucnaCentrala.dto.CasopisDTO;
import com.naucnacentrala.NaucnaCentrala.dto.KPRegistrationDTO;
import com.naucnacentrala.NaucnaCentrala.services.KPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @RequestMapping(value = "/getPlans/{sellerId}", method = RequestMethod.GET)
    public ResponseEntity<?> getBillingPlans(@PathVariable("sellerId") long sellerId){
        return new ResponseEntity<>(kpService.getMagazinePlans(sellerId), HttpStatus.OK);
    }

    @RequestMapping(value = "/getUserAgreements", method = RequestMethod.GET)
    public ResponseEntity<List<AgreementDTO>> getUserAgreements(HttpServletRequest request){
        return new ResponseEntity<List<AgreementDTO>>(kpService.getUserAgreements(request), HttpStatus.OK);
    }

    @RequestMapping(value = "/cancelAgreement/{agrID}/{sellerID}", method = RequestMethod.GET)
    public ResponseEntity<String> cancelAgreement(@PathVariable("agrID") long agrID, @PathVariable("sellerID") long sellerID){
        return new ResponseEntity<String>(kpService.cancelAgreement(agrID, sellerID), HttpStatus.OK);
    }

}
