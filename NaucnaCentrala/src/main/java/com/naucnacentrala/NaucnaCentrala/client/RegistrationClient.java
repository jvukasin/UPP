package com.naucnacentrala.NaucnaCentrala.client;

import com.naucnacentrala.NaucnaCentrala.dto.KPRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RegistrationClient {

    @Autowired
    RestTemplate restTemplate;

    private final String INIT_REG_ENDPOINT = "https://localhost:8500/sellers/sellers/register/init";
    private final String INIT_REG_REVIEW_ENDPOINT = "https://localhost:8500/sellers/sellers/register/review";


    public KPRegistrationDTO initRegistration(KPRegistrationDTO kprDTO) {

        ResponseEntity<KPRegistrationDTO> kpResponse = restTemplate.postForEntity(this.INIT_REG_ENDPOINT,
                new HttpEntity<>(kprDTO), KPRegistrationDTO.class);

        return kpResponse.getBody();
    }

    public KPRegistrationDTO reviewRegistration(KPRegistrationDTO kprDTO) {

        ResponseEntity<KPRegistrationDTO> kpResponse = restTemplate.postForEntity(this.INIT_REG_REVIEW_ENDPOINT,
                new HttpEntity<>(kprDTO), KPRegistrationDTO.class);

        return kpResponse.getBody();
    }

}