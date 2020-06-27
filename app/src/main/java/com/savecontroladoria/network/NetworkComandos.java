package com.savecontroladoria.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import com.savecontroladoria.activity.AgendaActivity;
import com.savecontroladoria.activity.ComandosActivity;
import com.savecontroladoria.activity.ComandosActivityRV;
import com.savecontroladoria.eventBus.ListenerEB;
import com.savecontroladoria.util.Utils;
import com.savecontroladoria.util.VerificaConnection;

import de.greenrobot.event.EventBus;

/**
 * Created by ADMIN on 15/10/2015.
 */
public class NetworkComandos {
    private static NetworkComandos instance;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private String url;
    private String autentication;
    public static final String TAG = "NetworkComandos";

    public NetworkComandos(Context c) {
        mContext = c;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized NetworkComandos getInstance(Context c) {
        if (instance == null) {
            instance = new NetworkComandos(c.getApplicationContext());
        }
        return (instance);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return (mRequestQueue);
    }

    public <T> void addRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public void execute(final String filtro) {
        final String params = filtro;
        final String autentication = Links.getAutentication();
        EventBus eventBus = EventBus.getDefault();
        ListenerEB msg = new ListenerEB();
        msg.setClasseDestino(ComandosActivityRV.class + "");
        msg.setMensagem("PROGRESSBARENABLE");
        eventBus.post(msg);
        if (VerificaConnection.isConnectedWifi(mContext)) {
            String ssid = Utils.getCurrentSsid(mContext).replaceAll("\"", "");

            if (ssid.equals("Seu Madruga")) {
                url = Links.getUrlinterna();

            } else {
                url = Links.getUrlexterna();
            }
        } else {
            if (VerificaConnection.isConnected(mContext)) {
                url = Links.getUrlexterna();
            } else {
                Toast.makeText(mContext, "Sem conexão com Internet", Toast.LENGTH_LONG).show();
            }
        }

        //"Basic YWRtaW46ZXNwODI2Ng=="
        Log.d("NetworkCom", "auth: " + autentication);
        /*TAREFA APRESENTACAO DE DENUNCIAS*/


        final StringRequest json = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response.equals("retorno ok")) {
                    Toast.makeText(mContext, "Realizado com sucesso!", Toast.LENGTH_LONG).show();
                    EventBus eventBus = EventBus.getDefault();
                    ListenerEB msg = new ListenerEB();
                    msg.setClasseDestino(ComandosActivityRV.class + "");
                    msg.setMensagem("PROGRESSBARDISABLE");
                    msg.setRetorno("ok");
                    msg.setComando(params);
                    eventBus.post(msg);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Sem conexão com servidor! "+error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG,error.getCause()+"ERROR"+error.getMessage());
                EventBus eventBus = EventBus.getDefault();
                ListenerEB msg = new ListenerEB();
                msg.setClasseDestino(ComandosActivityRV.class + "");
                msg.setMensagem("PROGRESSBARDISABLE");
                msg.setRetorno("erro");
                msg.setComando(params);
                eventBus.post(msg);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "text/html");
                headers.put("Authorization", autentication);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "text/html; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return params.getBytes();
            }

        };
        json.setTag("getEnvio");
        json.setRetryPolicy(new DefaultRetryPolicy(25000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequestQueue(json);
       // Log.d(TAG,"oque foi enviado: "+json);

    }

}
