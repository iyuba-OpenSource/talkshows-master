package com.iyuba.lib_common.model.local;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.iyuba.lib_common.model.local.dao.BookEntityJuniorDao;
import com.iyuba.lib_common.model.local.dao.ChapterDetailEntityJuniorDao;
import com.iyuba.lib_common.model.local.dao.ChapterEntityJuniorDao;
import com.iyuba.lib_common.model.local.dao.DubbingDao;
import com.iyuba.lib_common.model.local.dao.DubbingHelpDao;
import com.iyuba.lib_common.model.local.entity.BookEntity_junior;
import com.iyuba.lib_common.model.local.entity.ChapterDetailEntity_junior;
import com.iyuba.lib_common.model.local.entity.ChapterEntity_junior;
import com.iyuba.lib_common.model.local.entity.DubbingEntity;
import com.iyuba.lib_common.model.local.entity.DubbingHelpEntity;
import com.iyuba.lib_common.util.LibResUtil;

/**
 * @title: 数据库操作
 * @date: 2024/1/2 15:42
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
//@Database(entities = {BookEntity_junior.class, ChapterEntity_junior.class, ChapterDetailEntity_junior.class, DubbingHelpEntity.class},exportSchema = false,version = 3)
@Database(entities = {BookEntity_junior.class, ChapterEntity_junior.class, ChapterDetailEntity_junior.class, DubbingHelpEntity.class, DubbingEntity.class},exportSchema = false,version = 3)
public abstract class RoomDB extends RoomDatabase{
    private static final String TAG = "RoomDB";

    private static RoomDB instance;

    public static RoomDB getInstance(){
        if (instance==null){
            synchronized (RoomDB.class){
                if (instance==null){
                    instance = Room.databaseBuilder(LibResUtil.getInstance().getApplication(),RoomDB.class,getDBName())
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

    //数据库名称
    private static String getDBName(){
        //这里设置为包名最后一个+db
        String packageName = LibResUtil.getInstance().getApplication().getPackageName();
        int index = packageName.lastIndexOf(".");
        String dbName = packageName.substring(index+1);
        return dbName+".db";
    }

    //回调信息
    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }

        @Override
        public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
            super.onDestructiveMigration(db);
        }
    };


    /****************dao操作类***************/
    /*******中小学******/
    //书籍表-中小学书籍操作
    public abstract BookEntityJuniorDao getBookEntityJuniorDao();
    //章节表-中小学章节操作
    public abstract ChapterEntityJuniorDao getChapterEntityJuniorDao();
    //章节详情表-中小学章节详情操作
    public abstract ChapterDetailEntityJuniorDao getChapterDetailEntityJuniorDao();

    /*********其他***********/
    //配音或评测内容的辅助结果
    public abstract DubbingHelpDao getDubbingHelpDao();
    //配音或评测的操作
    public abstract DubbingDao getDubbingDao();

    /************广告***********/
    //广告历史记录
//    public abstract AdOperationHistoryDao getAdOperationHistoryDao();
}
