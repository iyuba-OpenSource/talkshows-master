package com.iyuba.iyubamovies.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;

import com.iyuba.iyubamovies.database.opinf.ImoviesListInf;
import com.iyuba.iyubamovies.model.ImoviesList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iyuba on 2017/12/23.
 */

public class ImoviesListOp implements ImoviesListInf{
    private ImoviesDbhelper dbhelper;
    public ImoviesListOp(@NonNull ImoviesDbhelper dbhelper){
        this.dbhelper = dbhelper;
    }

    @Override
    public void SavaImoviesList(ImoviesList data) {
        if(data==null)
            return;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(SERIESID,data.getSeriesid());
            values.put(CATEGORY,data.getCategory());
            values.put(CREATETIME,data.getCreateTime());
            values.put(DESCCN,data.getDescCn());
            values.put(HOTFLG,data.getHotFlg());
            values.put(KEYWORDS,data.getKeyWords());
            values.put(OTHER,data.getOther());
            values.put(OTHER1,data.getOther1());
            values.put(SERIESNAME,data.getSeriesName());
            values.put(UPDATETIME,data.getUpdateTime());
            values.put(PIC,data.getPic());
            db.replace(TABLE_NAME,null,values);
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    @Override
    public void SavaImoviesLists(List<ImoviesList> datas) {
        if(datas!=null||datas.size()>0){
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            db.beginTransaction();
            try {
                for (ImoviesList data:datas){
                    ContentValues values = new ContentValues();
                    values.put(SERIESID,data.getSeriesid());
                    values.put(CATEGORY,data.getCategory());
                    values.put(CREATETIME,data.getCreateTime());
                    values.put(DESCCN,data.getDescCn());
                    values.put(HOTFLG,data.getHotFlg());
                    values.put(KEYWORDS,data.getKeyWords());
                    values.put(OTHER,data.getOther());
                    values.put(OTHER1,data.getOther1());
                    values.put(SERIESNAME,data.getSeriesName());
                    values.put(UPDATETIME,data.getUpdateTime());
                    values.put(PIC,data.getPic());
                    values.put(SERIESNAME,data.getSeriesName());
                    db.replace(TABLE_NAME,null,values);

                }
                db.setTransactionSuccessful();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                db.endTransaction();
                db.close();
            }
        }
    }

    @Override
    public List<ImoviesList> SelectAllImoviesLists() {
        List<ImoviesList> imoviesLists = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from "+TABLE_NAME,
                    new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                ImoviesList imoviesList = new ImoviesList();
                imoviesList.setSeriesid(cursor.getString(cursor.getColumnIndex(SERIESID)));
                imoviesList.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                imoviesList.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
                imoviesList.setUpdateTime(cursor.getString(cursor.getColumnIndex(UPDATETIME)));
                imoviesList.setDescCn(cursor.getString(cursor.getColumnIndex(DESCCN)));
                imoviesList.setHotFlg(cursor.getString(cursor.getColumnIndex(HOTFLG)));
                imoviesList.setKeyWords(cursor.getString(cursor.getColumnIndex(KEYWORDS)));
                imoviesList.setOther(cursor.getString(cursor.getColumnIndex(OTHER)));
                imoviesList.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
                imoviesList.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
                imoviesList.setSeriesName(cursor.getString(cursor.getColumnIndex(SERIESNAME)));
                imoviesLists.add(imoviesList);
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return imoviesLists;
    }

    @Override
    public ImoviesList SelectImoviesList(String seriesid) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        Cursor cursor =null;
        try {
            ImoviesList imoviesList = new ImoviesList();
            cursor = db.rawQuery("select * from "+TABLE_NAME+" where "+SERIESID+"="+seriesid,
                    new String[]{});

            if(cursor.moveToFirst()){
                imoviesList.setSeriesid(cursor.getString(cursor.getColumnIndex(SERIESID)));
                imoviesList.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                imoviesList.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
                imoviesList.setUpdateTime(cursor.getString(cursor.getColumnIndex(UPDATETIME)));
                imoviesList.setDescCn(cursor.getString(cursor.getColumnIndex(DESCCN)));
                imoviesList.setHotFlg(cursor.getString(cursor.getColumnIndex(HOTFLG)));
                imoviesList.setKeyWords(cursor.getString(cursor.getColumnIndex(KEYWORDS)));
                imoviesList.setOther(cursor.getString(cursor.getColumnIndex(OTHER)));
                imoviesList.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
                imoviesList.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
                imoviesList.setSeriesName(cursor.getString(cursor.getColumnIndex(SERIESNAME)));
                cursor.close();
                return imoviesList;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null)
                cursor.close();
            db.close();
        }
        return null;
    }


}
