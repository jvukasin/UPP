package com.naucnacentrala.NaucnaCentrala.dto;

public class AccessClanarinaDTO {

    private boolean openAccess;
    private boolean imaClanarinu;

    public AccessClanarinaDTO() {}
    public AccessClanarinaDTO(boolean openAccess, boolean imaClanarinu) {
        this.openAccess = openAccess;
        this.imaClanarinu = imaClanarinu;
    }

    public boolean isOpenAccess() {
        return openAccess;
    }

    public void setOpenAccess(boolean openAccess) {
        this.openAccess = openAccess;
    }

    public boolean isImaClanarinu() {
        return imaClanarinu;
    }

    public void setImaClanarinu(boolean imaClanarinu) {
        this.imaClanarinu = imaClanarinu;
    }
}
