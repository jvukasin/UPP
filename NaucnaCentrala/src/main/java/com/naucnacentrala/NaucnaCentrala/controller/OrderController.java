package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.dto.CasopisDTO;
import com.naucnacentrala.NaucnaCentrala.dto.FinalizeOrderDTO;
import com.naucnacentrala.NaucnaCentrala.dto.InitOrderResponseDTO;
import com.naucnacentrala.NaucnaCentrala.dto.NaucniRadDTO;
import com.naucnacentrala.NaucnaCentrala.services.OrderObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    OrderObjectService orderObjectService;

    @RequestMapping(value = "/magazine/init", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitOrderResponseDTO> initOrder(@RequestBody CasopisDTO magazineDTO, HttpServletRequest request){
        return new ResponseEntity(orderObjectService.create(magazineDTO, request), HttpStatus.OK);
    }

    @RequestMapping(value = "/magazine/initSub", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitOrderResponseDTO> initSub(@RequestBody CasopisDTO magazineDTO, HttpServletRequest request){
        return new ResponseEntity(orderObjectService.createSub(magazineDTO, request), HttpStatus.OK);
    }

    @RequestMapping(value = "/scPaper/init", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitOrderResponseDTO> initPaper(@RequestBody NaucniRadDTO paperDTO, HttpServletRequest request){
        return new ResponseEntity(orderObjectService.createPaper(paperDTO, request), HttpStatus.OK);
    }

    @RequestMapping(value = "/finalize", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity finalizeOrder(@RequestBody FinalizeOrderDTO foDTO){
        orderObjectService.finalizeOrder(foDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}
