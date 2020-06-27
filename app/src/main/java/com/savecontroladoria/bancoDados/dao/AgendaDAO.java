package com.savecontroladoria.bancoDados.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import com.savecontroladoria.bancoDados.DB;
import com.savecontroladoria.objetos.Agendamento;


/**
 * Created by ADMIN on 20/10/2015.
 */
public class AgendaDAO {

    private static String table_name = "TABAGE";
    private static Context ctx;
    private static String[] columns = {"CODHORA", "DESCRHORA", "SEGUNDA", "TERCA", "QUARTA", "QUINTA", "SEXTA",
            "SABADO", "DOMINGO","ONOFF","ACAO","HORA","MINUTO","datetime('now','localtime') AS DHATUAL",
            "DHINTEGRACAO", "_id","UID"};
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");

    public AgendaDAO(Context ctx) {
        this.ctx = ctx;
    }

    public boolean insert(JSONObject jsonObject) {
        boolean retorno = false;
        SQLiteDatabase db = new DB(ctx).getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues ctv = new ContentValues();
            ctv.put("CODHORA", jsonObject.getInt("codhora"));
            ctv.put("DESCRHORA", jsonObject.getString("descrhora"));
            ctv.put("SEGUNDA", jsonObject.getString("segunda"));
            ctv.put("TERCA", jsonObject.getString("terca"));
            ctv.put("QUARTA", jsonObject.getString("quarta"));
            ctv.put("QUINTA", jsonObject.getString("quinta"));
            ctv.put("SEXTA", jsonObject.getString("sexta"));
            ctv.put("SABADO", jsonObject.getString("sabado"));
            ctv.put("DOMINGO", jsonObject.getString("domingo"));
            ctv.put("ONOFF", jsonObject.getString("onoff"));
            ctv.put("HORA", jsonObject.getInt("hora"));
            ctv.put("MINUTO", jsonObject.getInt("minuto"));
            ctv.put("ACAO", jsonObject.getInt("acao"));
            ctv.put("UID", jsonObject.getString("uid"));
            ctv.put("DHULTEXEC", "1999-01-01");
            ctv.put("DHINTEGRACAO", dateFormat.format(Calendar.getInstance().getTime()));
            retorno = (db.insert(table_name, null, ctv) > 0);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
        return retorno;

    }

    public boolean deleteItem(Integer codhora) {
        SQLiteDatabase db = new DB(ctx).getWritableDatabase();
        db.execSQL("VACUUM");
        db.beginTransaction();
        try {
            Cursor cr = db.rawQuery("SELECT * FROM TABAGE", null);
            Integer newseq = 0;
            if (cr.move(codhora+1)) {
                newseq = cr.getInt(cr.getColumnIndex("CODHORA"));
            }
            cr.close();
            return (db.delete(table_name, "CODHORA=?", new String[]{newseq.toString()}) > 0);
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }

    public boolean deleteAgendaUID(String uid) {
        SQLiteDatabase db = new DB(ctx).getWritableDatabase();
        db.execSQL("VACUUM");
        db.beginTransaction();
        try {
            return (db.delete(table_name, "UID=?", new String[]{uid}) > 0);
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }

    public boolean updateAgenda(JSONObject jo) {
        SQLiteDatabase db = new DB(ctx).getWritableDatabase();
        db.beginTransaction();
        boolean retorno = false;
        try {
            ContentValues ctv2 = new ContentValues();
            ctv2.put("DESCRHORA", jo.getInt("descrhora"));
            ctv2.put("SEGUNDA", jo.getString("segunda"));
            ctv2.put("TERCA", jo.getString("terca"));
            ctv2.put("QUARTA", jo.getString("quarta"));
            ctv2.put("QUINTA", jo.getString("quinta"));
            ctv2.put("SEXTA", jo.getString("sexta"));
            ctv2.put("SABADO", jo.getString("sabado"));
            ctv2.put("DOMINGO", jo.getString("domingo"));
            ctv2.put("ONOFF", jo.getString("onoff"));
            ctv2.put("DHINTEGRACAO", dateFormat.format(Calendar.getInstance().getTime()));
            retorno = (db.update(table_name, ctv2, "CODHORA=?", new String[]{jo.getString("CODHORA")}) > 0);
        } catch (JSONException e) {
            e.printStackTrace();

        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
        return retorno;
    }


    public Agendamento getAgenda(Integer codagenda) {
        SQLiteDatabase db = new DB(ctx).getReadableDatabase();
        db.beginTransaction();
        try {
            Cursor rs = db.query(table_name, columns, "CODHORA=?", new String[]{codagenda.toString()}, null, null, null);

            Agendamento vo = new Agendamento();

            if (rs.moveToNext()) {
                vo.setDescrHora(rs.getString(rs.getColumnIndex("DESCRHORA")));
                vo.setSegunda(rs.getString(rs.getColumnIndex("SEGUNDA")));
                vo.setTerca(rs.getString(rs.getColumnIndex("TERCA")));
                vo.setQuarta(rs.getString(rs.getColumnIndex("QUARTA")));
                vo.setQuinta(rs.getString(rs.getColumnIndex("QUINTA")));
                vo.setSexta(rs.getString(rs.getColumnIndex("SEXTA")));
                vo.setSabado(rs.getString(rs.getColumnIndex("SABADO")));
                vo.setDomingo(rs.getString(rs.getColumnIndex("DOMINGO")));
                vo.setOnoff(rs.getString(rs.getColumnIndex("ONOFF")));
                vo.setDhintegracao(rs.getString(rs.getColumnIndex("dhintegracao")));
            }
            rs.close();
            return vo;
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }


    public Integer count() {
        SQLiteDatabase db = new DB(ctx).getReadableDatabase();
        db.beginTransaction();
        try {
            Cursor c = db.query(table_name, null, null, null, null, null, null);
            if (c.moveToFirst()) {
                c.getCount();
            }
            c.close();
            return c.getCount();
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }

    public LinkedList<Agendamento> getAgendamentos() {
        SQLiteDatabase db = new DB(ctx).getReadableDatabase();
        db.beginTransaction();
        try {
            Cursor rs = db.query(table_name, columns, null, null, null, null, null);
            LinkedList<Agendamento> lista = new LinkedList<Agendamento>();
            while (rs.moveToNext()) {
                Agendamento vo = new Agendamento();
                vo.setId(rs.getInt(rs.getColumnIndex("_id")));
                vo.setCodHora(rs.getInt(rs.getColumnIndex("CODHORA")));
                vo.setDescrHora(rs.getString(rs.getColumnIndex("DESCRHORA")));
                vo.setSegunda(rs.getString(rs.getColumnIndex("SEGUNDA")));
                vo.setTerca(rs.getString(rs.getColumnIndex("TERCA")));
                vo.setQuarta(rs.getString(rs.getColumnIndex("QUARTA")));
                vo.setQuinta(rs.getString(rs.getColumnIndex("QUINTA")));
                vo.setSexta(rs.getString(rs.getColumnIndex("SEXTA")));
                vo.setSabado(rs.getString(rs.getColumnIndex("SABADO")));
                vo.setDomingo(rs.getString(rs.getColumnIndex("DOMINGO")));
                vo.setOnoff(rs.getString(rs.getColumnIndex("ONOFF")));
                vo.setAcao(rs.getString(rs.getColumnIndex("ACAO")));
                vo.setDhintegracao(rs.getString(rs.getColumnIndex("DHINTEGRACAO")));
                lista.add(vo);
            }
            rs.close();
            return lista;
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }

    }

    public LinkedList<Agendamento> getJobAgenda() {
        SQLiteDatabase db = new DB(ctx).getReadableDatabase();
        db.beginTransaction();
        try {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            String filtrodia = "vazio";
            switch (day) {
                case Calendar.SUNDAY:
                    filtrodia = "DOMINGO";
                    break;
                case Calendar.MONDAY:
                    filtrodia = "SEGUNDA";
                    break;
                case Calendar.TUESDAY:
                    filtrodia = "TERCA";
                    break;
                case Calendar.WEDNESDAY:
                    filtrodia = "QUARTA";
                    break;
                case Calendar.THURSDAY:
                    filtrodia = "QUINTA";
                    break;
                case Calendar.FRIDAY:
                    filtrodia = "SEXTA";
                    break;
                case Calendar.SATURDAY:
                    filtrodia = "SABADO";
                    break;
                default:
                    Log.d("AgendaDAO","Este não é um dia válido!");
            }
            Cursor rs = db.query(table_name, columns, filtrodia+"= 'S' AND " +
                    "CAST(SUBSTR(DHULTEXEC,1,4)||SUBSTR(DHULTEXEC,6,2)||SUBSTR(DHULTEXEC,9,2) AS INTEGER) < " +
                    "CAST(substr(datetime('now','localtime'),1,4)||substr(datetime('now','localtime'),6,2)||substr(datetime('now','localtime'),9,2) AS INTEGER) AND " +
                    "CAST(substr(datetime('now','localtime'),1,4)||substr(datetime('now','localtime'),6,2)||substr(datetime('now','localtime'),9,2)" +
                    "||CASE WHEN LENGTH(HORA)=1 THEN '0'||HORA ELSE HORA END||CASE WHEN LENGTH(MINUTO)=1 THEN '0'||MINUTO ELSE MINUTO END AS INTEGER) > " +
                    "CAST(substr(datetime(DHINTEGRACAO,'localtime'),1,4)||substr(datetime(DHINTEGRACAO,'localtime'),6,2)||substr(datetime(DHINTEGRACAO,'localtime'),9,2)||substr(datetime(DHINTEGRACAO,'localtime'),12,2)||substr(datetime(DHINTEGRACAO,'localtime'),15,2) AS INTEGER) " +
                    "AND CAST(substr(datetime('now','localtime'),1,4)||substr(datetime('now','localtime'),6,2)||substr(datetime('now','localtime'),9,2)" +
                    "||substr(datetime('now','localtime'),12,2)||substr(datetime('now','localtime'),15,2) AS INTEGER) - " +
                    "CAST(substr(date('now','localtime'),1,4)||substr(date('now','localtime'),6,2)||substr(date('now','localtime'),9,2) " +
                    "||CASE WHEN LENGTH(HORA)=1 THEN '0'||HORA ELSE HORA END " +
                    "||CASE WHEN LENGTH(MINUTO)=1 THEN '0'||MINUTO ELSE MINUTO END AS INTEGER) > 0", null, null, null, null);
            LinkedList<Agendamento> lista = new LinkedList<Agendamento>();
            while (rs.moveToNext()) {
                Agendamento vo = new Agendamento();
                vo.setId(rs.getInt(rs.getColumnIndex("_id")));
                vo.setCodHora(rs.getInt(rs.getColumnIndex("CODHORA")));
                vo.setAcao(rs.getString(rs.getColumnIndex("ACAO")));
                vo.setOnoff(rs.getString(rs.getColumnIndex("ONOFF")));
                vo.setHora(rs.getString(rs.getColumnIndex("HORA")));
                vo.setMinuto(rs.getString(rs.getColumnIndex("MINUTO")));
                vo.setDhatual(rs.getString(rs.getColumnIndex("DHATUAL")));
                lista.add(vo);
            }
            rs.close();
            return lista;
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }

    }

    public boolean updateDhExeAgenda(String id) {
        SQLiteDatabase db = new DB(ctx).getWritableDatabase();
        db.beginTransaction();
        boolean retorno = false;
        try {
            ContentValues ctv2 = new ContentValues();
            ctv2.put("DHULTEXEC", dtFormat.format(Calendar.getInstance().getTime()));
            retorno = (db.update(table_name, ctv2, "_id=?", new String[]{id}) > 0);
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
        return retorno;
    }
}