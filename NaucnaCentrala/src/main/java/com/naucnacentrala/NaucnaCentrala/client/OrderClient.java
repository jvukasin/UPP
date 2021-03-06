package com.naucnacentrala.NaucnaCentrala.client;

import com.naucnacentrala.NaucnaCentrala.dto.InitOrderRequestDTO;
import com.naucnacentrala.NaucnaCentrala.dto.InitOrderResponseDTO;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.model.OrderObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderClient {

//    private final static String returnUrl = "https://localhost:8601/orders/finalize";
//    private final static String sellersUrl = "https://localhost:8500/sellers/active-order/init";

    //LAN
    private final static String returnUrl = "https://192.168.43.86:8601/orders/finalize";
    private final static String sellersUrl = "https://192.168.43.124:8500/sellers/active-order/init";

    @Autowired
    RestTemplate restTemplate;

    public InitOrderResponseDTO initOrder(Casopis magazine, OrderObject orderObject) {
        InitOrderRequestDTO initOrderRequestDTO = new InitOrderRequestDTO(orderObject.getId(), magazine.getNaziv(), "USD",
                magazine.getSellerId(), orderObject.getAmount(), this.returnUrl, orderObject.getOrderType(), orderObject.getOrderStatus(), orderObject.getUserId());

        HttpEntity<InitOrderRequestDTO> httpEntity = new HttpEntity<>(initOrderRequestDTO);
        ResponseEntity<InitOrderResponseDTO> responseEntity = restTemplate.postForEntity(this.sellersUrl, httpEntity, InitOrderResponseDTO.class);
        return responseEntity.getBody();
    }

    public InitOrderResponseDTO initPaperOrder(NaucniRad paper, OrderObject orderObject) {
        InitOrderRequestDTO initOrderRequestDTO = new InitOrderRequestDTO(orderObject.getId(), paper.getTitle(), paper.getCurrency(),
                paper.getMagazine().getSellerId(), orderObject.getAmount(), this.returnUrl, orderObject.getOrderType(), orderObject.getOrderStatus(), orderObject.getUserId());

        HttpEntity<InitOrderRequestDTO> httpEntity = new HttpEntity<>(initOrderRequestDTO);
        ResponseEntity<InitOrderResponseDTO> responseEntity = restTemplate.postForEntity(this.sellersUrl, httpEntity, InitOrderResponseDTO.class);
        return responseEntity.getBody();
    }
}