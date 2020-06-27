package com.savecontroladoria.bancoDados.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.savecontroladoria.bancoDados.DB;
import com.savecontroladoria.objetos.Agendamento;
import com.savecontroladoria.objetos.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;


/**
 * Created by ADMIN on 20/10/2015.
 */
public class UsuarioDAO {

    private static String table_name = "TABUSU";
    private static Context ctx;
    private static String[] columns = {"NOMEUSU", "EMAIL", "ACESSOACAO","ACESSOJOB","ADM","UID",
            "DHINTEGRACAO", "_id"};
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");

    public UsuarioDAO(Context ctx) {
        this.ctx = ctx;
    }

    public boolean insert(Usuario usuario) {
        boolean retorno = false;
        SQLiteDatabase db = new DB(ctx).getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues ctv = new ContentValues();
            ctv.put("NOMEUSU", usuario.getNomeusu());
            ctv.put("EMAIL", usuario.getEmail());
            ctv.put("ACESSOACAO", usuario.getAcessoAcao());
            ctv.put("ACESSOJOB", usuario.getAcessoJob());
            ctv.put("ADM", usuario.getAdm());
            ctv.put("UID", usuario.getUid());
            ctv.put("DHINTEGRACAO", dateFormat.format(Calendar.getInstance().getTime()));
            retorno = (db.insert(table_name, null, ctv) > 0);
        }  finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
        return retorno;

    }

    public boolean updateUsuario(Usuario jo) {
        SQLiteDatabase db = new DB(ctx).getWritableDatabase();
        db.beginTransaction();
        boolean retorno = false;
        try {
            ContentValues ctv2 = new ContentValues();
            ctv2.put("NOMEUSU", jo.getNomeusu());
            ctv2.put("EMAIL", jo.getEmail());
            ctv2.put("ADM", jo.getAdm());
            ctv2.put("ACESSOACAO", jo.getAcessoAcao());
            ctv2.put("ACESSOJOB", jo.getAcessoJob());
            ctv2.put("DHINTEGRACAO", dateFormat.format(Calendar.getInstance().getTime()));
            retorno = (db.update(table_name, ctv2, "UID=?", new String[]{jo.getUid()}) > 0);
        }  finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
        return retorno;
    }


    public Usuario getUsuario(String uid) {
        SQLiteDatabase db = new DB(ctx).getReadableDatabase();
        db.beginTransaction();
        try {
            Cursor rs = db.query(table_name, columns, "UID=?", new String[]{uid.toString()}, null, null, null);

            Usuario vo = new Usuario();

            if (rs.moveToNext()) {
                vo.setNomeusu(rs.getString(rs.getColumnIndex("NOMEUSU")));
                vo.setEmail(rs.getString(rs.getColumnIndex("EMAIL")));
                vo.setAcessoAcao(rs.getString(rs.getColumnIndex("ACESSOACAO")));
                vo.setAcessoJob(rs.getString(rs.getColumnIndex("ACESSOJOB")));
                vo.setAdm(rs.getString(rs.getColumnIndex("ADM")));
                vo.setDhintegracao(rs.getString(rs.getColumnIndex("DHINTEGRACAO")));
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

    public Integer usuExiste(String uid) {
        SQLiteDatabase db = new DB(ctx).getReadableDatabase();
        Integer count = 0 ;
        db.beginTransaction();
        try {
            Cursor c = db.query(table_name, new String[]{"COUNT(1) AS COUNT"}, "UID=?", new String[]{uid.toString()}, null, null, null);
            if (c.moveToFirst()) {
                count = c.getInt(c.getColumnIndex("COUNT"));
            }
            c.close();
            return count;
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }

    public LinkedList<Usuario> getUsuarios() {
        SQLiteDatabase db = new DB(ctx).getReadableDatabase();
        db.beginTransaction();
        try {
            Cursor rs = db.query(table_name, columns, null, null, null, null, null);
            LinkedList<Usuario> lista = new LinkedList<Usuario>();
            while (rs.moveToNext()) {
                Usuario vo = new Usuario();
                vo.setNomeusu(rs.getString(rs.getColumnIndex("NOMEUSU")));
                vo.setAdm(rs.getString(rs.getColumnIndex("ADM")));
                vo.setAcessoAcao(rs.getString(rs.getColumnIndex("ACESSOACAO")));
                vo.setAcessoJob(rs.getString(rs.getColumnIndex("ACESSOJOB")));
                vo.setUid(rs.getString(rs.getColumnIndex("UID")));
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

}