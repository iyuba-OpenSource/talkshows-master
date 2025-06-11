package com.iyuba.iyubamovies.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import android.util.Log;

import com.iyuba.iyubamovies.database.opinf.ImoviesDetailInf;
import com.iyuba.iyubamovies.network.result.ImoviesDetailData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iyuba on 2018/1/18.
 */

public class ImoviesDetailOp implements ImoviesDetailInf{
    private ImoviesDbhelper dbhelper;
    public ImoviesDetailOp(@NonNull ImoviesDbhelper dbhelper){
        this.dbhelper = dbhelper;
    }


    @Override
    public void insertImoviesDetails(List<ImoviesDetailData> datas, String id, String seriseid, String type) {
        if(datas==null)
            return;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ImoviesDetailData data:datas) {
                ContentValues values = new ContentValues();
                values.put(SERISEID, seriseid);
                values.put(ID, id);
                values.put(TYPE, type);
                values.put(ENDTIMING, data.getEndTiming());
                values.put(IDINDEX, data.getIdIndex());
                values.put(SENTENCE, data.getSentence());
                values.put(SENTENCE_CN, data.getSentence_cn());
                values.put(PARAID, data.getParaId());
                values.put(TIMING, data.getTiming());
                db.replace(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.toString();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    @Override
    public List<ImoviesDetailData> getImoviesDetailsData(String id, String seriseid, String type) {
        List<ImoviesDetailData> imoviesDetails = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            Log.e("select--","select * from "+TABLE_NAME+" where "+ID+"="+id+" and "+SERISEID+"="+seriseid+
                    " and "+TYPE+"="+type);
            cursor = db.rawQuery("select "+ENDTIMING+","+IDINDEX+","+PARAID+","+SENTENCE+","+SENTENCE_CN+","+TIMING
                            +" from "+TABLE_NAME+" where "+ID+"="+id+" and "+SERISEID+"="+seriseid+
                    " and "+TYPE+"= '"+type+"'",
                new String[]{});
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                ImoviesDetailData data = new ImoviesDetailData();
                data.setEndTiming(cursor.getFloat(cursor.getColumnIndex(ENDTIMING))+"");
                data.setIdIndex(cursor.getInt(cursor.getColumnIndex(IDINDEX))+"");
                data.setParaId(cursor.getInt(cursor.getColumnIndex(PARAID))+"");
                data.setSentence(cursor.getString(cursor.getColumnIndex(SENTENCE)));
                data.setSentence_cn(cursor.getString(cursor.getColumnIndex(SENTENCE_CN)));
                data.setTiming(cursor.getFloat(cursor.getColumnIndex(TIMING))+"");
                imoviesDetails.add(data);
            }

        }catch (Exception e){
            e.toString();
            Log.e("getdetails","-exception-");
        }finally {
            db.close();
        }
        return imoviesDetails;
    }
}
