package com.savecontroladoria.objetos;

/**
 * Created by ADMIN on 28/04/2017.
 */

public class Usuario {

    public String email;
    public String adm;
    public String acessoJob;
    public String acessoAcao;
    public String nomeusu;
    public String uid;
    public String dhintegracao;

    public Usuario() {
    }

    public Usuario(String nomeusu,String email, String adm, String acessoJob, String acessoAcao, String uid) {
        this.nomeusu = nomeusu;
        this.email = email;
        this.adm = adm;
        this.acessoJob = acessoJob;
        this.acessoAcao = acessoAcao;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdm() {
        return adm;
    }

    public void setAdm(String adm) {
        this.adm = adm;
    }

    public String getAcessoJob() {
        return acessoJob;
    }

    public void setAcessoJob(String acessoJob) {
        this.acessoJob = acessoJob;
    }

    public String getAcessoAcao() {
        return acessoAcao;
    }

    public void setAcessoAcao(String acessoAcao) {
        this.acessoAcao = acessoAcao;
    }

    public String getNomeusu() {
        return nomeusu;
    }

    public void setNomeusu(String nomeusu) {
        this.nomeusu = nomeusu;
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
