package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.client.RegistrationClient;
import com.naucnacentrala.NaucnaCentrala.dto.*;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class KPService {

    private final String REG_STATUS_CALLBACK_URL = "https://localhost:8600/kp/registration/status";

    @Autowired
    CasopisService magazineService;

    @Autowired
    RegistrationClient registrationClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OrderObjectService orderObjectService;

    @Autowired
    KorisnikService korisnikService;


    public KPRegistrationDTO initRegistration(CasopisDTO magazineDTO) {

        Casopis m = magazineService.findOneById(magazineDTO.getId());

        // ako postoji seller id za tu instancu magazina, znaci da vec postoji registracija, uspesna ili neuspesna
        if (m.getSellerId() != null) {
            return reviewRegistration(m);
        }

        KPRegistrationDTO kprDTO = new KPRegistrationDTO();
        kprDTO.setRegistrationStatusCallbackUrl(this.REG_STATUS_CALLBACK_URL);

        kprDTO = registrationClient.initRegistration(kprDTO);

        m.setSellerId(kprDTO.getSellerId());

        magazineService.save(m);

        return kprDTO;
    }

    public void changeRegistrationStatus(KPRegistrationDTO kprDTO) {
        Casopis m = magazineService.findBySellerId(kprDTO.getSellerId());
        // isStatus lmao fucking shit but who cares
        m.setRegistered(kprDTO.isStatus());
        magazineService.save(m);
        System.out.println("Magazine: " + m.getId() + " registration success: " + m.isRegistered());

    }

    public KPRegistrationDTO reviewRegistrationSeller(CasopisDTO magazineDTO) {
        Casopis m = magazineService.findOneById(magazineDTO.getId());
        return reviewRegistration(m);
    }

    private KPRegistrationDTO reviewRegistration(Casopis m) {
        KPRegistrationDTO kprDTO = new KPRegistrationDTO();
        kprDTO.setRegistrationStatusCallbackUrl(this.REG_STATUS_CALLBACK_URL);
        kprDTO.setSellerId(m.getSellerId());
        kprDTO = registrationClient.reviewRegistration(kprDTO);
        return kprDTO;
    }

    public StringDTO createPlan(long magId) {
        Casopis m = magazineService.findOneById(magId);
        List<NaucniRad> radovi = m.getSciencePapers();
        double amount = 0;
        for(NaucniRad rad : radovi) {
            amount += rad.getPrice();
        }
        amount = amount * 0.9;
        String currency = "USD";
        if(!radovi.isEmpty()) {
            currency = radovi.get(0).getCurrency();
        }
        double roundAmount = Math.round(amount * 100.0) / 100.0;
        CasopisInfoDTO magazineDTO = new CasopisInfoDTO(m.getNaziv(), m.getIssn(), currency, roundAmount, m.getSellerId());
        ResponseEntity response = restTemplate.postForEntity("https://localhost:8500/sellers/sellers/createPlan", new HttpEntity<>(magazineDTO),
                String.class);
        StringDTO text = new StringDTO((String) response.getBody());
        return text;
    }

    public String getMagazinePlans(long magId) {
        Casopis m = magazineService.findOneById(magId);
        ResponseEntity response = restTemplate.getForEntity("https://localhost:8500/sellers/sellers/getPlans/" + m.getSellerId(),
                String.class);
        StringDTO text = new StringDTO((String) response.getBody());

        return text.getHref();
    }

    public List<AgreementDTO> getUserAgreements(HttpServletRequest request) {
        String korisnik = korisnikService.getUsernameFromRequest(request);

        ResponseEntity response = restTemplate.getForEntity("https://localhost:8500/paypal-service/paypal/getUserAgreements/" + korisnik,
                AgreementDTO[].class);
        List<AgreementDTO> lista = (List<AgreementDTO>) response.getBody();
        return lista;
    }
}
