package com.naucnacentrala.NaucnaCentrala.services;

import com.naucnacentrala.NaucnaCentrala.client.OrderClient;
import com.naucnacentrala.NaucnaCentrala.dto.CasopisDTO;
import com.naucnacentrala.NaucnaCentrala.dto.FinalizeOrderDTO;
import com.naucnacentrala.NaucnaCentrala.dto.InitOrderResponseDTO;
import com.naucnacentrala.NaucnaCentrala.dto.NaucniRadDTO;
import com.naucnacentrala.NaucnaCentrala.enums.Enums;
import com.naucnacentrala.NaucnaCentrala.model.Casopis;
import com.naucnacentrala.NaucnaCentrala.model.NaucniRad;
import com.naucnacentrala.NaucnaCentrala.model.OrderObject;
import com.naucnacentrala.NaucnaCentrala.repository.OrderObjectRepository;
import com.naucnacentrala.NaucnaCentrala.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class OrderObjectService {

    @Autowired
    CasopisService magazineService;

    @Autowired
    NaurcniRadService sciencePaperService;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    private OrderObjectRepository orderObjectRepo;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    KorisnikService korisnikService;

    public InitOrderResponseDTO create(CasopisDTO magazineDTO, HttpServletRequest request){
        Casopis magazine = magazineService.findOneById(magazineDTO.getId());
        OrderObject orderObject = createOrderObject(magazine, request);
        orderObject.setOrderType(Enums.OrderType.ORDER_CASOPIS);
        orderObject = orderObjectRepo.save(orderObject);

        return orderClient.initOrder(magazine, orderObject);
    }

    public InitOrderResponseDTO createSub(CasopisDTO magazineDTO, HttpServletRequest request){
        Casopis magazine = magazineService.findOneById(magazineDTO.getId());
        OrderObject orderObject = createOrderObject(magazine, request);
        orderObject.setOrderType(Enums.OrderType.ORDER_SUBSCRIPTION);
        orderObject = orderObjectRepo.save(orderObject);

        return orderClient.initOrder(magazine, orderObject);
    }

    public InitOrderResponseDTO createPaper(NaucniRadDTO paperDTO, HttpServletRequest request) {
        NaucniRad paper = sciencePaperService.findOneById(paperDTO.getId());
        OrderObject orderObject = new OrderObject();
        orderObject.setSciencePaper(paper);
        orderObject.setUserId(korisnikService.getUsernameFromRequestAndToken(request, tokenUtils));
        orderObject.setAmount(paperDTO.getPrice());
        orderObject.setOrderStatus(Enums.OrderStatus.PENDING);
        orderObject.setOrderType(Enums.OrderType.ORDER_RAD);
        orderObject = orderObjectRepo.save(orderObject);

        return orderClient.initPaperOrder(paper, orderObject);
    }

    private OrderObject createOrderObject(Casopis m, HttpServletRequest request) {
        OrderObject oo = new OrderObject();
        oo.setMagazine(m);
        oo.setUserId(korisnikService.getUsernameFromRequestAndToken(request, tokenUtils));
        oo.setAmount(calculateAmount(m));
        oo.setOrderStatus(Enums.OrderStatus.PENDING);
        return oo;
    }


    private double calculateAmount(Casopis magazine) {
        double amount = 0;
        for (NaucniRad sciencePaper : magazine.getSciencePapers()) {
            amount = amount + sciencePaper.getPrice();
        }
        amount = amount * 0.95;
        return Math.round(amount * 100.0) / 100.0;
    }

    public void finalizeOrder(FinalizeOrderDTO foDTO) {
        OrderObject o = orderObjectRepo.findById(foDTO.getNcOrderId()).get();
        o.setOrderStatus(foDTO.getOrderStatus());
        orderObjectRepo.save(o);

    }
}
