package com.savecontroladoria.objetos;

/**
 * Created by ADMIN on 29/04/2017.
 */

public class Acao {

    private String tipoacao;
    private boolean status;
    private String uid;
    private Integer codstatus;
    private String dhintegracao;
    private String comodo;
    public Acao(String tipoacao, boolean status, String uid, Integer codstatus, String dhintegracao, String comodo) {
        this.tipoacao = tipoacao;
        this.status = status;
        this.uid = uid;
        this.codstatus = codstatus;
        this.dhintegracao = dhintegracao;
        this.comodo = comodo;
    }

    public  Acao () {

    }

    public Acao(String tipoacao) {
        this.tipoacao = tipoacao;
    }



    public String getComodo() {
        return comodo;
    }

    public void setComodo(String comodo) {
        this.comodo = comodo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getCodstatus() {
        return codstatus;
    }

    public void setCodstatus(Integer codstatus) {
        this.codstatus = codstatus;
    }

    public String getDhintegracao() {
        return dhintegracao;
    }

    public void setDhintegracao(String dhintegracao) {
        this.dhintegracao = dhintegracao;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTipoacao() {
        return tipoacao;
    }

    public void setTipoacao(String tipoacao) {
        this.tipoacao = tipoacao;
    }
}
