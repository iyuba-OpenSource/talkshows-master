package com.iyuba.iyubamovies.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by iyuba on 2017/12/23.
 */

public class ImoviesDbhelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "imovies_data.db";
    private static final int DB_VERSION = 3;

    private static final String IMOVIELIST_TABLE = "CREATE TABLE imovieslist " +
            "(seriesid text primary key,category text, createtime text, desccn text, hotflg text, " +
            "keywords text, other text, other1 text, seriesname text, updatetime text, pic text)";
    private static final String IMOVIEONE_TABLE = "CREATE TABLE imvonedatas " +
            "(seriesid text,id integer,category text, createtime text, desccn text, flag text,title text," +
            " titlecn text, other text, other1 text,type text, categoryname text, sound text, pic text," +
            "readcount text,isdownload integer default 0,isfinshdl integer default 0,PRIMARY KEY(seriesid,id))";
    private static final String IMOVIESERISE_DETAIL_TABLE =  "create table imovies_serise_detail (Id integer,Seriseid integer,Type text,ParaId integer,"+
            "IdIndex integer,Sentence text,Sentence_cn text,Timing float DEFAULT 0," +
                    "EndTiming float,PRIMARY KEY(id,Seriseid,type,ParaId,IdIndex))";
    public ImoviesDbhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public ImoviesDbhelper(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    public ImoviesDbhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, DB_NAME, factory, DB_VERSION, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE imovieslist " +
                "(seriesid text primary key,category text, createtime text, desccn text, hotflg text, ")
                .append("keywords text, other text, other1 text, seriesname text, updatetime text, pic text)");
//        db.execSQL(IMOVIELIST_TABLE);
//        db.execSQL(IMOVIEONE_TABLE);
//        db.execSQL(IMOVIESERISE_DETAIL_TABLE);
        db.execSQL(sql.toString());
        sql.setLength(0);
        sql.append("CREATE TABLE imvonedatas " +
                "(seriesid text,id integer,category text, createtime text, desccn text, flag text,title text,")
                .append(" titlecn text, other text, other1 text,type text, categoryname text, sound text, pic text,")
                .append("readcount text,isdownload integer default 0,isfinshdl integer default 0,PRIMARY KEY(seriesid,id))");
        db.execSQL(sql.toString());
        sql.setLength(0);
        sql.append("create table imovies_serise_detail (Id integer,Seriseid integer,Type text,ParaId integer,"+
                "IdIndex integer,Sentence text,Sentence_cn text,Timing float DEFAULT 0,");
        sql.append("EndTiming float,PRIMARY KEY(id,Seriseid,type,ParaId,IdIndex))");
        db.execSQL(sql.toString());
        sql.setLength(0);
        sql.append("create table imoviesagreecount(userid text,cid text)");
        db.execSQL(sql.toString());
        sql.setLength(0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StringBuilder sql = new StringBuilder();

        sql.setLength(0);
        sql.append("CREATE TABLE imvonedatas " +
                "(seriesid text,id integer,category text, createtime text, desccn text, flag text,title text,")
                .append(" titlecn text, other text, other1 text,type text, categoryname text, sound text, pic text,")
                .append("readcount text,isdownload integer default 0,isfinshdl integer default 0,PRIMARY KEY(seriesid,id))");
        db.execSQL(sql.toString());
    }
}
