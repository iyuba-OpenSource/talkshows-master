package com.iyuba.iyubamovies.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import android.util.Log;

import com.iyuba.iyubamovies.database.opinf.ImoviesOneDataInf;
import com.iyuba.iyubamovies.model.OneSerisesData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iyuba on 2018/1/16.
 */

public class ImoviesOneSerisesOp implements ImoviesOneDataInf{

    private ImoviesDbhelper dbhelper;
    public ImoviesOneSerisesOp(@NonNull ImoviesDbhelper dbhelper){
        this.dbhelper = dbhelper;
    }

    @Override
    public void saveSerises(List<OneSerisesData> datas) {
        if(datas==null||datas.size()==0)
            return;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (OneSerisesData data:datas){
                ContentValues values = new ContentValues();
                values.put(SERIESID,data.getSerisesid());
                values.put(CATEGORY,data.getCategory());
                values.put(CREATETIME,data.getCreateTime());
                values.put(DESCCN,data.getDescCn());
                values.put(FLAG,data.getFlag());
                values.put(READCOUNT,data.getReadCount());
                values.put(OTHER,data.getOther());
                values.put(OTHER1,data.getOther1());
                values.put(CATEGORYNAME,data.getCategoryName());
                values.put(ID,data.getId());
                values.put(PIC,data.getPic());
                values.put(TITLE,data.getTitle());
                values.put(TITLECN,data.getTitle_cn());
                values.put(SOUND,data.getSound());
                values.put(TYPE,data.getType());
                values.put(ISDOWNLOAD,data.getIsDownload());
                values.put(ISFINISHDL,data.getIsFinishDownLoad());
                db.replace(TABLE_NAME,null,values);
            }
            Log.e("saveSerisesize",datas.size()+"");
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    @Override
    public List<OneSerisesData> getSerisesBySerisesId(String serisesid) {
        List<OneSerisesData> datas = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from "+TABLE_NAME
                    +" where "+SERIESID+"="+serisesid+" ORDER BY id DESC",new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                OneSerisesData data = new OneSerisesData();
                data.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                data.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
                data.setSerisesid(cursor.getString(cursor.getColumnIndex(SERIESID)));
                data.setDescCn(cursor.getString(cursor.getColumnIndex(DESCCN)));
                data.setFlag(cursor.getString(cursor.getColumnIndex(FLAG)));
                data.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                data.setOther(cursor.getString(cursor.getColumnIndex(OTHER)));
                data.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
                data.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
                data.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
                data.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLECN)));
                data.setSound(cursor.getString(cursor.getColumnIndex(SOUND)));
                data.setReadCount(cursor.getString(cursor.getColumnIndex(READCOUNT)));
                data.setType (cursor.getString(cursor.getColumnIndex(TYPE)));
                data.setId(cursor.getInt(cursor.getColumnIndex(ID))+"");
                data.setIsDownload(cursor.getInt(cursor.getColumnIndex(ISDOWNLOAD))+"");
                data.setIsFinishDownLoad(cursor.getInt(cursor.getColumnIndex(ISFINISHDL))+"");
                datas.add(data);

            }
            Log.e("getSerisesize",datas.size()+"--serisesid"+serisesid);
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null)
                cursor.close();
            db.close();
        }
        return datas;
    }

    @Override
    public void saveSerise(OneSerisesData data) {
        if(data==null)
            return;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {
                ContentValues values = new ContentValues();
                values.put(SERIESID,data.getSerisesid());
                values.put(CATEGORY,data.getCategory());
                values.put(CREATETIME,data.getCreateTime());
                values.put(DESCCN,data.getDescCn());
                values.put(FLAG,data.getFlag());
                values.put(READCOUNT,data.getReadCount());
                values.put(OTHER,data.getOther());
                values.put(OTHER1,data.getOther1());
                values.put(CATEGORYNAME,data.getCategoryName());
                values.put(ID,data.getId());
                values.put(PIC,data.getPic());
                values.put(TITLE,data.getTitle());
                values.put(TITLECN,data.getTitle_cn());
                values.put(SOUND,data.getSound());
                values.put(TYPE,data.getType());
                values.put(ISDOWNLOAD,data.getIsDownload());
                values.put(ISFINISHDL,data.getIsFinishDownLoad());
                db.replace(TABLE_NAME,null,values);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }

    }

    @Override
    public int countofSeriseid(String serisesid) {
        int catCount = 0;
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " +
                    TABLE_NAME + " where " + SERIESID + "=" + serisesid, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    catCount = cursor.getInt(0);
                }
                Log.e("seriseid-count",catCount+"");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return catCount;

    }

    @Override
    public void updateSerisesDownloadState(String download,String isfinishdl,String id,String seriseid) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        try {
            db.execSQL("update "+TABLE_NAME+" set "+ISDOWNLOAD+"="+download+","+ISFINISHDL+"="+isfinishdl+" where id="+id+" and "+SERIESID+"="+seriseid);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
    }

    @Override
    public List<OneSerisesData> getAllSerisesByDownLoadState() {
        List<OneSerisesData> datas = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from "+TABLE_NAME
                    +" where "+ISDOWNLOAD+" = 1 "+"ORDER BY id DESC",new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                OneSerisesData data = new OneSerisesData();
                data.setCategory(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                data.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
                data.setSerisesid(cursor.getString(cursor.getColumnIndex(SERIESID)));
                data.setDescCn(cursor.getString(cursor.getColumnIndex(DESCCN)));
                data.setFlag(cursor.getString(cursor.getColumnIndex(FLAG)));
                data.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                data.setOther(cursor.getString(cursor.getColumnIndex(OTHER)));
                data.setOther1(cursor.getString(cursor.getColumnIndex(OTHER1)));
                data.setPic(cursor.getString(cursor.getColumnIndex(PIC)));
                data.setCategoryName(cursor.getString(cursor.getColumnIndex(CATEGORYNAME)));
                data.setTitle_cn(cursor.getString(cursor.getColumnIndex(TITLECN)));
                data.setSound(cursor.getString(cursor.getColumnIndex(SOUND)));
                data.setReadCount(cursor.getString(cursor.getColumnIndex(READCOUNT)));
                data.setType (cursor.getString(cursor.getColumnIndex(TYPE)));
                data.setId(cursor.getInt(cursor.getColumnIndex(ID))+"");
                data.setIsDownload(cursor.getInt(cursor.getColumnIndex(ISDOWNLOAD))+"");
                data.setIsFinishDownLoad(cursor.getInt(cursor.getColumnIndex(ISFINISHDL))+"");
                datas.add(data);
            }
            Log.e("getSerisesize",datas.size()+"--download");
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null)
                cursor.close();
            db.close();
        }
        return datas;
    }
}
