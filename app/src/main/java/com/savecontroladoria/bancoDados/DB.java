package com.savecontroladoria.bancoDados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by ADMIN on 20/10/2015.
 */
public class DB extends SQLiteOpenHelper {
    private static int version = 1;
    private static String dbName = "RobotiCasadb.db";
    private static String createage = "CREATE TABLE IF NOT EXISTS [TABAGE]  ([_id] integer PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT," +
            "  [DESCRHORA] VARCHAR2(100), " +
            "  [CODHORA] INTEGER, " +
            "  [DOMINGO] VARCHAR2(2), " +
            "  [SEGUNDA] VARCHAR2(2), " +
            "  [TERCA] VARCHAR2(2), " +
            "  [QUARTA] VARCHAR2(2), " +
            "  [QUINTA] VARCHAR2(2), " +
            "  [SEXTA] VARCHAR2(2), " +
            "  [SABADO] VARCHAR2(2), " +
            "  [ONOFF] VARCHAR2(2), " +
            "  [ACAO] VARCHAR2(2), " +
            "  [UID] VARCHAR2(200), " +
            "  [HORA] INTEGER, " +
            "  [MINUTO] INTEGER, " +
            "  [DHULTEXEC] DATE, "+
            "  [DHINTEGRACAO] DATETIME);";
    private static String createusu = "CREATE TABLE IF NOT EXISTS [TABUSU]  ([_id] integer PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT," +
            "  [NOMEUSU] VARCHAR2(200), " +
            "  [UID] VARCHAR2(200), " +
            "  [EMAIL] VARCHAR2(200), " +
            "  [ADM] VARCHAR2(2), " +
            "  [ACESSOACAO] VARCHAR2(2), " +
            "  [ACESSOJOB] VARCHAR2(2), " +
            "  [DHINTEGRACAO] DATETIME);";

    private static String createstatus = "CREATE TABLE IF NOT EXISTS [TABSTS]  ([_id] integer PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT," +
            "  [CODSTATUS] VARCHAR2(200), " +
            "  [DESCRSTATUS] VARCHAR2(200), " +
            "  [STATUS] VARCHAR2(2), " +
            "  [UID] VARCHAR2(200), " +
            "  [DHINTEGRACAO] DATETIME);";


    public DB(Context ctx) {
        super(ctx, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createage);
        db.execSQL(createusu);
        db.execSQL(createstatus);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("LOGCALIXTO", "VERSAO NOVA" + newVersion + "VERSAO ANTIGA" + oldVersion);
        //       db.execSQL(sqldelete);
        //     db.execSQL(sql);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("LOGCALIXTO", "DOWNVERSAO NOVA" + newVersion + "DONWVERSAO ANTIGA" + oldVersion);
    }
}