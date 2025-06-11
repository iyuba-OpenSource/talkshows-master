package com.iyuba.talkshow.ui.lil.local;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.talkshow.ui.lil.local.dao.Dao_book;
import com.iyuba.talkshow.ui.lil.local.entity.Entity_book;

@Database(entities = {Entity_book.class},version = 1,exportSchema = false)
public abstract class TempRoomDb extends RoomDatabase {

    private static TempRoomDb instance;

    public static TempRoomDb getInstance() {
        if (instance == null) {
            synchronized (TempRoomDb.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(LibResUtil.getInstance().getApplication(), TempRoomDb.class, getDBName())
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return instance;
    }

    //数据库名称
    private static String getDBName() {
        //这里设置为包名最后一个+db
        String packageName = LibResUtil.getInstance().getApplication().getPackageName();
        int index = packageName.lastIndexOf(".");
        String dbName = packageName.substring(index + 1);
        return dbName + ".db";
    }

    //回调
    private static Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };


    /*************************操作类*************************/
    public abstract Dao_book getBookDao();
}
