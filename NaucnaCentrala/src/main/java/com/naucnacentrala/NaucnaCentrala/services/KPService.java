package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.client.RegistrationClient;
import com.naucnacentrala.NaucnaCentrala.dto.*;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.Clanarina;
import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.repository.jpa.ClanarinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class KPService {

//    private final String REG_STATUS_CALLBACK_URL = "https://localhost:8601/kp/registration/status";
//    private final String returnUrl = "https://localhost:8500";

    //LAN
    private final String REG_STATUS_CALLBACK_URL = "https://192.168.43.86:8601/kp/registration/status";
    private final String returnUrl = "https://192.168.43.124:8500";

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

    @Autowired
    CasopisService casopisService;

    @Autowired
    ClanarinaRepository clanarinaRepository;


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
        ResponseEntity response = restTemplate.postForEntity(this.returnUrl + "/sellers/sellers/createPlan", new HttpEntity<>(magazineDTO),
                String.class);
        StringDTO text = new StringDTO((String) response.getBody());
        return text;
    }

    public String getMagazinePlans(long selId) {
        ResponseEntity response = restTemplate.getForEntity(this.returnUrl + "/sellers/sellers/getPlans/" + selId,
                String.class);
        StringDTO text = new StringDTO((String) response.getBody());

        return text.getHref();
    }

    public List<AgreementDTO> getUserAgreements(HttpServletRequest request) {
        String korisnik = korisnikService.getUsernameFromRequest(request);

        ResponseEntity response = restTemplate.getForEntity(this.returnUrl + "/paypal-service/paypal/getUserAgreements/" + korisnik,
                AgreementListDTO.class);
        AgreementListDTO al = (AgreementListDTO) response.getBody();
        List<AgreementDTO> lista = al.getAgreements();
        return lista;
    }

    public String cancelAgreement(long agrID, long sellerID) {
        ResponseEntity response = restTemplate.getForEntity(this.returnUrl + "/paypal-service/paypal/cancelAgreement/" + agrID,
                String.class);
        String ret = (String) response.getBody();
        if(ret.equals("done")) {
            Casopis c = casopisService.findBySellerId(sellerID);
            Clanarina clan = clanarinaRepository.findByAgrAndCas(c.getId(), agrID);
            if(clan != null) {
                clanarinaRepository.delete(clan);
            }
        }
        return ret;
    }
}
