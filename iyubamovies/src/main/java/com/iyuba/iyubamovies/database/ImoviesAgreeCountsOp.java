package com.iyuba.iyubamovies.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import android.util.Log;

import com.iyuba.iyubamovies.database.opinf.ImoviesAgreeCountInf;

/**
 * Created by iyuba on 2018/1/22.
 */

public class ImoviesAgreeCountsOp implements ImoviesAgreeCountInf {

    private ImoviesDbhelper dbhelper;
    public ImoviesAgreeCountsOp(@NonNull ImoviesDbhelper dbhelper){
        this.dbhelper = dbhelper;
    }
    @Override
    public boolean isClickZAN(String uid, String id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{USERID,CID},USERID+" = ? and "+CID+" = ?"
                ,new String[]{uid,id},null,null,null);
        if(cursor.getCount()>0)
        {
            Log.e("userinfo_database",cursor.getCount()+"");
            if (cursor != null) {
                cursor.close();
            }
            return true;
        }
        else {
            if (cursor != null) {
                cursor.close();
            }
            return false;
        }
    }

    @Override
    public void savaclickZan(String uid, String id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERID,uid);
        values.put(CID,id);
        db.insert(TABLE_NAME,null,values);
        db.close();
    }
}
