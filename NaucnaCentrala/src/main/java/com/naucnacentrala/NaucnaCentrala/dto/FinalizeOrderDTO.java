package com.naucnacentrala.NaucnaCentrala.dto;

import com.naucnacentrala.NaucnaCentrala.enums.Enums;

import javax.persistence.Column;
import java.util.Date;

public class FinalizeOrderDTO {

    private long activeOrderId;
    private Enums.OrderStatus orderStatus;
    private long ncOrderId;
    private Date finalDate;
    private long agreementID;

    public FinalizeOrderDTO() {
    }

    public FinalizeOrderDTO(long activeOrderId, Enums.OrderStatus orderStatus, long ncOrderId, Date endDate, long agreementID) {
        this.activeOrderId = activeOrderId;
        this.orderStatus = orderStatus;
        this.ncOrderId = ncOrderId;
        this.finalDate = endDate;
        this.agreementID = agreementID;
    }

    public long getActiveOrderId() {
        return activeOrderId;
    }

    public void setActiveOrderId(long activeOrderId) {
        this.activeOrderId = activeOrderId;
    }

    public Enums.OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Enums.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public long getNcOrderId() {
        return ncOrderId;
    }

    public void setNcOrderId(long ncOrderId) {
        this.ncOrderId = ncOrderId;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public long getAgreementID() {
        return agreementID;
    }

    public void setAgreementID(long agreementID) {
        this.agreementID = agreementID;
    }
}
