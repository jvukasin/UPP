package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.dto.*;
import com.naucnacentrala.NaucnaCentrala.services.OrderObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    OrderObjectService orderObjectService;

    @RequestMapping(value = "/casopis/init", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitOrderResponseDTO> initOrder(@RequestBody CasopisDTO casopis, HttpServletRequest request){
        return new ResponseEntity(orderObjectService.create(casopis, request), HttpStatus.OK);
    }

    @RequestMapping(value = "/casopis/initSub", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitOrderResponseDTO> initSub(@RequestBody CasopisDTO casopis, HttpServletRequest request){
        return new ResponseEntity(orderObjectService.createSub(casopis, request), HttpStatus.OK);
    }

    @RequestMapping(value = "/scPaper/init", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitOrderResponseDTO> initPaper(@RequestBody NaucniRadDTO rad, HttpServletRequest request){
        return new ResponseEntity(orderObjectService.createPaper(rad, request), HttpStatus.OK);
    }

    @RequestMapping(value = "/finalize", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity finalizeOrder(@RequestBody FinalizeOrderDTO foDTO){
        orderObjectService.finalizeOrder(foDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/userOrders", method = RequestMethod.GET)
    public ResponseEntity<List<OrderObjectDTO>> userOrders(HttpServletRequest request){
        return new ResponseEntity(orderObjectService.getUserOrders(request), HttpStatus.OK);
    }

}
