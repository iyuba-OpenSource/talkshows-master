package com.iyuba.talkshow.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.iyuba.module.toolbox.DBUtil;
import com.iyuba.talkshow.BuildConfig;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.model.University;
import com.iyuba.talkshow.injection.ApplicationContext;
import com.iyuba.talkshow.util.FileUtils;
import com.iyuba.talkshow.util.MyGsonTypeAdapterFactory;

import javax.inject.Inject;


public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "kouyu_show.db";
    private static final int DATABASE_VERSION = 10;
    private Context mContext;

    @Inject
    public DbOpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(Db.VoaTable.CREATE);
            db.execSQL(Db.VoaTextTable.CREATE);
            db.execSQL(Db.RecordTable.CREATE);
            db.execSQL(Db.CollectTable.CREATE);
            db.execSQL(Db.UniversityTable.CREATE);
            db.execSQL(Db.ThumbTable.CREATE);
            db.execSQL(Db.DownloadTable.CREATE);
            db.execSQL(Db.SeriesTable.CREATE);
            db.execSQL(Db.VoaSoundTable.CREATE);

            insertUniversities(db);
            executeVoasSql(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void insertUniversities(SQLiteDatabase db) {
        JsonParser jsonParser = new JsonParser();
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        JsonArray jsonArray = (JsonArray) jsonParser
                .parse(FileUtils.getReaderFromRaw(mContext, R.raw.universities));
        TypeAdapter<University> typeAdapter = University.typeAdapter(gson);

        for (JsonElement jsonElement : jsonArray) {
            University university = typeAdapter.fromJsonTree(jsonElement);
            db.insert(Db.UniversityTable.TABLE_NAME, null,
                    Db.UniversityTable.toContentValues(university));
        }
    }

    public void executeVoasSql(SQLiteDatabase db) {
        FileUtils.executeAssetsSQL(mContext, db, "voa.sql");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                onCreate(db);
                db.execSQL("ALTER TABLE  '" + Db.RecordTable.TABLE_NAME + "'  ADD 'score' TEXT ");
            case 2:
                db.execSQL("ALTER TABLE  '" + Db.VoaTable.TABLE_NAME + "'  ADD 'series' INTEGER ");
                db.execSQL(Db.SeriesTable.CREATE);
            case 3:
                db.execSQL("ALTER TABLE  '" + Db.RecordTable.TABLE_NAME + "'  ADD 'audio' TEXT ");
            case 5:
                db.execSQL(Db.VoaSoundTable.CREATE);
            case 6:
                executeVoasSql(db);
                try {
                    db.delete(Db.VoaTable.TABLE_NAME, Db.VoaTable.COLUMN_SERIES + " = ?", new String[] {"201"});
                    db.delete(Db.VoaTable.TABLE_NAME, Db.VoaTable.COLUMN_SERIES + " = ?", new String[] {"203"});
                    db.delete(Db.VoaTable.TABLE_NAME, Db.VoaTable.COLUMN_SERIES + " = ?", new String[] {"204"});
                } catch (Exception var2) { }
            case 7:
                if (!DBUtil.isColumnExist(db, Db.VoaSoundTable.TABLE_NAME, Db.VoaSoundTable.COLUMN_WORDS)) {
                    db.execSQL("ALTER TABLE  '" + Db.VoaSoundTable.TABLE_NAME + "'  ADD 'words' TEXT ");
                }
            case 8:
                //先执行video字段增加，避免数据写入时存在问题
                //删除原来的数据
                db.execSQL("DELETE FROM '"+Db.VoaTable.TABLE_NAME+"'");
                //增加video参数
                if (!DBUtil.isColumnExist(db,Db.VoaTable.TABLE_NAME,Db.VoaTable.COLUMN_VIDEO)){
                    db.execSQL("ALTER TABLE '"+ Db.VoaTable.TABLE_NAME+"' ADD 'video' TEXT ");
                }
                //执行预存数据存储
                executeVoasSql(db);
            case 9:
                //优化原来的DownloadTable数据表，增加部分内容进行处理
                if (!DBUtil.isColumnExist(db,Db.DownloadTable.TABLE_NAME,Db.DownloadTable.COLUMN_ID)){
                    db.execSQL("ALTER TABLE '"+Db.DownloadTable.TABLE_NAME+"' ADD '"+Db.DownloadTable.COLUMN_ID+"' TEXT ");
                }
                if (!DBUtil.isColumnExist(db,Db.DownloadTable.TABLE_NAME,Db.DownloadTable.COLUMN_UID)){
                    db.execSQL("ALTER TABLE '"+Db.DownloadTable.TABLE_NAME+"' ADD '"+Db.DownloadTable.COLUMN_UID+"' INTEGER ");
                }
                if (!DBUtil.isColumnExist(db,Db.DownloadTable.TABLE_NAME,Db.DownloadTable.COLUMN_AUDIO_PATH)){
                    db.execSQL("ALTER TABLE '"+Db.DownloadTable.TABLE_NAME+"' ADD '"+Db.DownloadTable.COLUMN_AUDIO_PATH+"' TEXT ");
                }
                if (!DBUtil.isColumnExist(db,Db.DownloadTable.TABLE_NAME,Db.DownloadTable.COLUMN_VIDEO_PATH)){
                    db.execSQL("ALTER TABLE '"+Db.DownloadTable.TABLE_NAME+"' ADD '"+Db.DownloadTable.COLUMN_VIDEO_PATH+"' TEXT ");
                }
        }
    }
}