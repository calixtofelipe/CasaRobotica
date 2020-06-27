package com.savecontroladoria.network;

import android.util.Base64;

/**
 * Created by ADMIN on 19/04/2017.
 */

public class Links {
    private final static String userName = "[usuario_acesso_arduino]";
    private final static String password = "[usuario_acesso_arduino]";
    private final static String autentication = "Basic " + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.NO_WRAP);
    private final static String urlinterna = "http://192.168.0.24:5874/";
    private final static String urlinternacenario2 = "http://192.168.0.25:5874/";
    private final static String urlexterna = "[urlexterna]:5874/";
    private final static String urlexternacenario2 = "[urlexterna]:5875/";
//savecontroladoria.ddns.net no ip


    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getAutentication() {
        return autentication;
    }

    public static String getUrlinterna() {
        return urlinterna;
    }

    public static String getUrlexterna() {
        return urlexterna;
    }

    public static String getUrlinternacenario2() {
        return urlinternacenario2;
    }

    public static String getUrlexternacenario2() {
        return urlexternacenario2;
    }
}
