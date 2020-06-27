package com.savecontroladoria.objetos;

/**
 * Created by ADMIN on 21/05/2017.
 */

public class Status {
    private  Integer codstatus;
    private  String descrstatus;
    private  String status;
    private  String uid;
    private  String dhintegracao;


    public Integer getCodstatus() {
        return codstatus;
    }

    public void setCodstatus(Integer codstatus) {
        this.codstatus = codstatus;
    }

    public String getDescrstatus() {
        return descrstatus;
    }

    public void setDescrstatus(String descrstatus) {
        this.descrstatus = descrstatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDhintegracao() {
        return dhintegracao;
    }

    public void setDhintegracao(String dhintegracao) {
        this.dhintegracao = dhintegracao;
    }
}
