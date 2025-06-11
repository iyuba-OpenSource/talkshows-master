package com.iyuba.wordtest.db;


import android.annotation.SuppressLint;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;

import com.iyuba.wordtest.entity.BookLevels;
import com.iyuba.wordtest.entity.CategorySeries;
import com.iyuba.wordtest.entity.CetRootWord;
import com.iyuba.wordtest.entity.NewBookLevels;
import com.iyuba.wordtest.entity.TalkShowListen;
import com.iyuba.wordtest.entity.TalkShowTests;
import com.iyuba.wordtest.entity.TalkShowWords;
import com.iyuba.wordtest.entity.TalkshowTexts;
import com.iyuba.wordtest.manager.WordConfigManager;
import com.iyuba.wordtest.utils.FileUtils;
@SuppressLint("StaticFieldLeak")
@Database(entities = { CetRootWord.class , TalkShowWords.class, TalkShowTests.class, TalkshowTexts.class, BookLevels.class, NewBookLevels.class, CategorySeries.class, OfficialAccount.class, TalkShowListen.class}, version = 5,exportSchema = false)
public abstract class WordDataBase extends RoomDatabase {

    private static final String DB_NAME = "words.db";
    private static WordDataBase instance;
    private static Context mContext ;

    public static WordDataBase getInstance(Context context) {
        mContext = context ;
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), WordDataBase.class, DB_NAME)
                    .addCallback(sOnOpenCallback)
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4,MIGRATION_4_5)
                    .allowMainThreadQueries() // 允许主线程
                    .build();
        }
        return instance;
    }

//    private static WordDataBa/se create(final Context context) {
//        return
//    }

    private static RoomDatabase.Callback sOnOpenCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    initializeData(db);
                }};

    private static void initializeData(SupportSQLiteDatabase db) {
        FileUtils.executeAssetsSQL(mContext,db,"importdata.sql");
    }

    public static boolean loadDbData() {
        return FileUtils.executeAssetsSQL(mContext, instance.getOpenHelper().getWritableDatabase(),"importwords.sql");
    }

    public abstract TalkShowTextDao getTalkShowTextDao();
    public abstract TalkShowTestsDao getTalkShowTestsDao();
    public abstract NewBookLevelDao getNewBookLevelDao();
    public abstract TalkShowWordsDao getTalkShowWordsDao();
    public abstract BookLevelDao getBookLevelDao();
    public abstract CetRootWordDAO getCetRootWordDao();
    public abstract CategorySeriesDao getCategorySeriesDao();
    public abstract OfficialAccountDao getOfficialAccountDao();

    //单词听写
    public abstract TalkShowListenDao getTalkShowListenDao();

//    static final Migration MIGRATION_2_3 = new Migration(2,3) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            //此处对于数据库中的所有更新都需要写下面的代码
//
//        }
//    };
//
//    static final Migration MIGRATION_1_3 = new Migration(1,3) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            //此处对于数据库中的所有更新都需要写下面的代码
//
//        }
//    };

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `TalkShowTests` (`word` TEXT NOT NULL, `uid` TEXT, `version` INTEGER NOT NULL, `position` INTEGER NOT NULL, `updateTime` TEXT, `voa_id` INTEGER NOT NULL, `book_id` INTEGER NOT NULL, `unit_id` INTEGER NOT NULL, `idindex` INTEGER NOT NULL, `pic_url` TEXT, `audio` TEXT, `examples` TEXT, `answer` TEXT, `pron` TEXT, `def` TEXT, `flag` INTEGER NOT NULL, `Sentence` TEXT, `Sentence_cn` TEXT, `Sentence_audio` TEXT, `videoUrl` TEXT, `wrong` INTEGER NOT NULL, PRIMARY KEY(`unit_id`, `book_id`, `position`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `NewBookLevels` (`bookId` INTEGER NOT NULL, `level` INTEGER NOT NULL, `version` INTEGER NOT NULL, `download` INTEGER NOT NULL, `uid` TEXT NOT NULL, PRIMARY KEY(`bookId`, `uid`))");
            WordConfigManager.Instance(mContext).putInt(WordConfigManager.WORD_DB_NEW_LOADED, 1);
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `CategorySeries` (`Category` INTEGER NOT NULL, `SeriesName` TEXT, `lessonName` TEXT, `SourceType` TEXT, `lessonType` TEXT, `isVideo` TEXT, `uid` INTEGER NOT NULL, PRIMARY KEY(`Category`, `uid`))");
        }
    };
    static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `OfficialAccount` (`id` INTEGER NOT NULL, `newsfrom` TEXT, `createTime` TEXT, `image_url` TEXT, `title` TEXT, `url` TEXT, `count` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        }
    };

    //增加单词听写
    static final Migration MIGRATION_4_5 = new Migration(4,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `TalkShowListen`(`book_id` INTEGER NOT NULL,`unit_id` INTEGER NOT NULL,`position` INTEGER NOT NULL,`uid` TEXT NOT NULL,`word` TEXT,`porn` TEXT,`def` TEXT,`audio` TEXT,`spell` TEXT,`status` INTEGER NOT NULL,`error_count` INTEGER NOT NULL,`update_time` TEXT,PRIMARY KEY(`book_id`,`unit_id`,`position`,`uid`))");
        }
    };
}