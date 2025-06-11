package com.iyuba.talkshow.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.module.toolbox.DBUtil;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.data.model.Category;
import com.iyuba.talkshow.data.model.Collect;
import com.iyuba.talkshow.data.model.Download;
import com.iyuba.talkshow.data.model.Level;
import com.iyuba.talkshow.data.model.Record;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.Thumb;
import com.iyuba.talkshow.data.model.University;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.data.model.VoaSoundNew;
import com.iyuba.talkshow.data.model.VoaText;
import com.iyuba.talkshow.ui.main.MainActivity;
import com.iyuba.talkshow.util.SqlUtil;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public class DatabaseHelper {

    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        SqlBrite.Builder briteBuilder = new SqlBrite.Builder();
        mDb = briteBuilder.build().wrapDatabaseHelper(dbOpenHelper, Schedulers.immediate());
    }


    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<Voa> setVoas(final Collection<Voa> newVoas) {
        return Observable.create(new Observable.OnSubscribe<Voa>() {
            @Override
            public void call(Subscriber<? super Voa> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    if (newVoas != null) {
                        for (Voa voa : newVoas) {
                            long result = mDb.insert(Db.VoaTable.TABLE_NAME,
                                    Db.VoaTable.toContentValues(voa),
                                    SQLiteDatabase.CONFLICT_REPLACE);
                            if (result >= 0) {
                                subscriber.onNext(voa);
                            }
                        }
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    //将数据插入到数据库中
    public Observable<Voa> insertVoaData(Collection<Voa> data) {
        return Observable.create(new Observable.OnSubscribe<Voa>() {
            @Override
            public void call(Subscriber<? super Voa> subscriber) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }

                BriteDatabase.Transaction transaction = mDb.newTransaction();

            }
        });
    }

    private Observable<Voa> getTypeNumVoas(final int type, final int size) {
        return Observable.create(new Observable.OnSubscribe<Voa>() {
            @Override
            public void call(Subscriber<? super Voa> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                                + " WHERE " + Db.VoaTable.COLUMN_CATEGORY + " =? "
                                + " order by " + Db.VoaTable.COLUMN_CREATE_TIME + " desc "
                                + "LIMIT 0, ?",
                        String.valueOf(type), String.valueOf(size));
                while (cursor.moveToNext()) {
                    subscriber.onNext(Db.VoaTable.parseCursor(cursor));
                }
                cursor.close();
                subscriber.onCompleted();
            }
        });
    }

    private Observable<Voa> getNineHotVideoVoas() {
        return Observable.create(new Observable.OnSubscribe<Voa>() {
            @Override
            public void call(Subscriber<? super Voa> subscriber) {
                String sql =
                        "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                                + " WHERE " + Db.VoaTable.COLUMN_CATEGORY + " NOT IN("
                                + Category.Value.DONG_MAN + ", " + Category.Value.TING_GE + ")"
                                + " GROUP BY " + Db.VoaTable.COLUMN_CATEGORY;
                Timber.e(sql);
                Cursor cursor = mDb.query(sql);
                while (cursor.moveToNext()) {
                    subscriber.onNext(Db.VoaTable.parseCursor(cursor));
                }
                cursor.close();
                subscriber.onCompleted();
            }
        });
    }

    private Observable<Voa> getLastHotVideoVoas() {
        return Observable.create(new Observable.OnSubscribe<Voa>() {
            @Override
            public void call(Subscriber<? super Voa> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                                + " WHERE " + Db.VoaTable.COLUMN_CATEGORY + " = ?"
                                + " LIMIT 1, 1", String.valueOf(Category.Value.FILM));
                while (cursor.moveToNext()) {
                    subscriber.onNext(Db.VoaTable.parseCursor(cursor));
                }
                cursor.close();
                subscriber.onCompleted();
            }
        });
    }

    public Observable<VoaText> getVoaTextByIndex(int voaId, String index) {
        return Observable.create(new Observable.OnSubscribe<VoaText>() {
            @Override
            public void call(Subscriber<? super VoaText> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT * FROM " + Db.VoaTextTable.TABLE_NAME
                                + " WHERE " + Db.VoaTextTable.COLUMN_VOA_ID + " = ? "
                                + " AND " + Db.VoaTextTable.COLUMN_PARA_ID + " = ?"
                                + " LIMIT 1, 1", String.valueOf(voaId), index);
                if (cursor.moveToNext()) {

//                    int maxId = 0;
                    subscriber.onNext(Db.VoaTextTable.parseCursor(cursor, voaId));
                } else {
                    subscriber.onNext(null);
                }
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Integer> getMinVoaId() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT MIN( " + Db.VoaTable.COLUMN_VOA_ID + ") FROM "
                                + Db.VoaTable.TABLE_NAME);
                if (cursor.moveToNext()) {
                    int maxId = cursor.getInt(0);
                    subscriber.onNext(maxId);
                } else {
                    subscriber.onNext(0);
                }
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Integer> getMaxVoaId() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT MAX( " + Db.VoaTable.COLUMN_VOA_ID + ") FROM "
                                + Db.VoaTable.TABLE_NAME);
                if (cursor.moveToNext()) {
                    int maxId = cursor.getInt(0);
                    subscriber.onNext(maxId);
                } else {
                    subscriber.onNext(0);
                }
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<Voa>> getVoas() {
        return Observable.merge(getNineHotVideoVoas(), getLastHotVideoVoas(),
//                getTypeNumVoas(Category.Value.DONG_MAN, MainActivity.DONG_MAN_SIZE),
//                getTypeNumVoas(Category.Value.TING_GE, MainActivity.TING_GE_SIZE)).toList();
                getTypeNumVoas(Category.Value.DONG_MAN, MainActivity.DONG_MAN_SIZE),
                getTypeNumVoas(Category.Value.TING_GE, MainActivity.TING_GE_SIZE)).toList();
    }

    public Observable<List<Voa>> getVoas(int pageNum, int pageCount,
                                         String type1, String type2,
                                         String type3, String type4,
                                         String type5, String type6, String type7) {
        return Observable.merge(getVoaByLevelOne(pageNum, pageCount),
                getVoasByCategoryNotWith(false, type1, pageCount, "0"),
                getVoasByCategoryNotWith(false, type2, pageCount, "0"),
                getVoasByCategoryNotWith(false, type3, pageCount, "0"),
                getVoasByCategoryNotWith(false, type4, pageCount, "0"),
                getVoasByCategoryNotWith(false, type5, pageCount, "0"),
                getVoasByCategoryNotWith(false, type6, pageCount, "0"),
                getVoasByCategoryNotWith(false, type7, pageCount, "0")
        ).toList();
    }

    public Observable<List<Voa>> getVivoVoas(int pageNum, int pageCount,
                                             String type1, String type2,
                                             String type3, String type4,
                                             String type6) {
        return Observable.merge(getVoaByLevelOne(pageNum, pageCount),
                getVoasByCategoryNotWith(false, type1, pageCount, "0"),
                getVoasByCategoryNotWith(false, type2, pageCount, "0"),
                getVoasByCategoryNotWith(false, type3, pageCount, "0"),
                getVoasByCategoryNotWith(false, type4, pageCount, "0"),
                getVoasByCategoryNotWith(false, type6, pageCount, "0")
        ).toList();
    }

    public Observable<List<Voa>> getChildHomeVoas(String type) {
        return Observable.merge(getVoasByCategoryNotWith(false, type, 4, "0"),
                getVoaByBothOne(313, Level.Value.ALL, 1, 10)
        ).toList();
    }

    public Observable<List<Voa>> getXiaoxueHomeVoas(String type) {
        return Observable.merge(null,
                getVoaByBothOne(313, Level.Value.ALL, 1, 10)
        ).toList();
    }

    public Observable<Voa> getVoaById(int voaId) {
        return mDb.createQuery(Db.VoaTable.TABLE_NAME,
                "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                        + " WHERE " + Db.VoaTable.COLUMN_VOA_ID + " = ?",
                String.valueOf(voaId)).mapToOne(new Func1<Cursor, Voa>() {
            @Override
            public Voa call(Cursor cursor) {
                if (cursor != null) {
                    return Db.VoaTable.parseCursor(cursor);
                } else {
                    return null;
                }
            }
        });
    }

    public Observable<List<Voa>> getVoa(int category, String level, int pageNum, int pageSize) {
        if (category == Category.Value.ALL) {
            return getVoaByLevel(level, pageNum, pageSize);
        } else if (category != 5000) {
            return getVoaByBoth(category, level, pageNum, pageSize);
        } else {
            return getVoaXiaoxue(category, level, pageNum, pageSize);
        }
    }

    public Observable<List<Voa>> getVoa(String category, String level, int pageNum, int pageSize) {

        return getVoaByBoth(category, level, pageNum, pageSize);
    }

    private Observable<List<Voa>> getVoaByBoth(String category, String level, int pageNum, int pageSize) {
        int offSize = (pageNum - 1) * pageSize;
        return mDb.createQuery(Db.VoaTable.TABLE_NAME,
                        "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                                + " WHERE (" + SqlUtil.handleIn(Db.VoaTable.COLUMN_HOT_FLG, level.split(Level.Value.SEP))
                                + ") AND " + Db.VoaTable.COLUMN_CATEGORY + " in  ( ? ) "
                                + " order by " + Db.VoaTable.COLUMN_CREATE_TIME + " desc "
                                + " LIMIT ?, ?"
                        , String.valueOf(category), String.valueOf(offSize), String.valueOf(pageSize))
                .mapToList(new Func1<Cursor, Voa>() {
                    @Override
                    public Voa call(Cursor cursor) {
                        return Db.VoaTable.parseCursor(cursor);
                    }
                });
    }

    private Observable<List<Voa>> getVoaByBoth(int category, String level, int pageNum, int pageSize) {
        int offSize = (pageNum - 1) * pageSize;
        return mDb.createQuery(Db.VoaTable.TABLE_NAME,
                        "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                                + " WHERE (" + SqlUtil.handleIn(Db.VoaTable.COLUMN_HOT_FLG, level.split(Level.Value.SEP))
                                + ") AND " + Db.VoaTable.COLUMN_CATEGORY + " = ? "
                                + " order by " + Db.VoaTable.COLUMN_VOA_ID + " desc "
                                + " LIMIT ?, ?"
                        , String.valueOf(category), String.valueOf(offSize), String.valueOf(pageSize))
                .mapToList(new Func1<Cursor, Voa>() {
                    @Override
                    public Voa call(Cursor cursor) {
                        return Db.VoaTable.parseCursor(cursor);
                    }
                });
    }


    private Observable<List<Voa>> getVoaXiaoxue(int category, String level, int pageNum, int pageSize) {
        int offSize = (pageNum - 1) * pageSize;
        return mDb.createQuery(Db.VoaTable.TABLE_NAME,
                        "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                                + " WHERE (" + SqlUtil.handleIn(Db.VoaTable.COLUMN_HOT_FLG, level.split(Level.Value.SEP))
                                + ") AND " + Db.VoaTable.COLUMN_CATEGORY + " > ? "
                                + " order by " + Db.VoaTable.COLUMN_CREATE_TIME + " desc "
                                + " LIMIT ?, ?"
                        , "311", String.valueOf(offSize), String.valueOf(pageSize))
                .mapToList(new Func1<Cursor, Voa>() {
                    @Override
                    public Voa call(Cursor cursor) {
                        return Db.VoaTable.parseCursor(cursor);
                    }
                });
    }

    public void insertToSeries(SeriesData bean) {
        mDb.insert(Db.SeriesTable.TABLE_NAME,
                Db.SeriesTable.toContentValues(bean),
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteVoa4Category(String bean) {
        mDb.delete(Db.VoaTable.TABLE_NAME, Db.VoaTable.COLUMN_CATEGORY + " = ?", bean);
    }

    public void deleteVoa4Series(String bean) {
        mDb.delete(Db.VoaTable.TABLE_NAME, Db.VoaTable.COLUMN_SERIES + " = ?", bean);
    }

    public void deleteSeries4Category(String bean) {
        mDb.delete(Db.SeriesTable.TABLE_NAME, Db.SeriesTable.COLUMN_SERIES_CATEGORY + " = ?", bean);
    }

    public List<SeriesData> getSeries4Category(String seriesId) {
        Log.e("DatabaseHelper", "getSeries4Category ---  " + seriesId);
        List<SeriesData> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.SeriesTable.TABLE_NAME
                + " WHERE " + Db.SeriesTable.COLUMN_SERIES_CATEGORY + " =  " + seriesId
                + " ORDER BY " + Db.SeriesTable.COLUMN_SERIES_ID + " ASC ";
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            list.add(Db.SeriesTable.parseCursor(cursor));
        }
        return list;
    }

    public Observable<List<SeriesData>> getSeriesList(String category) {
        return mDb.createQuery(Db.SeriesTable.TABLE_NAME,
                        "SELECT * FROM " + Db.SeriesTable.TABLE_NAME
                                + " WHERE  " + Db.SeriesTable.COLUMN_SERIES_CATEGORY + " = ?"
                                + " ORDER BY " + Db.SeriesTable.COLUMN_SERIES_ID + " ASC "
                        , String.valueOf(category))
                .mapToList(new Func1<Cursor, SeriesData>() {
                    @Override
                    public SeriesData call(Cursor cursor) {
                        return Db.SeriesTable.parseCursor(cursor);
                    }
                });
    }

    public long insertToVoa(Voa bean) {
        return mDb.insert(Db.VoaTable.TABLE_NAME, Db.VoaTable.toContentValues(bean), SQLiteDatabase.CONFLICT_REPLACE);
    }
//    public void insertToSeries(Voa bean){
//        mDb.insert(Db.SeriesTable.TABLE_NAME,
//                Db.SeriesTable.toContentValues(bean),
//                SQLiteDatabase.CONFLICT_REPLACE);
//    }

    private Observable<List<Voa>> getVoaByLevel(String level, int pageNum, int pageSize) {
        int offSize = (pageNum - 1) * pageSize;

        //这里处理下，如果需要屏蔽中小学英语的，则增加下列内容的屏蔽
        int[] juniorBookId = new int[]{
                Category.Value.XIN_QIDIAN,
                Category.Value.PEP,
                Category.Value.JINGTONG,
                Category.Value.CHUZHONG,
                Category.Value.BEISHI_SANQIDIAN,
                Category.Value.BEISHI_YIQIDIAN,
                Category.Value.SAN_QIDIAN,
                Category.Value.YI_QIDIAN,
        };

        //如果需要屏蔽小猪佩奇和加菲猫的，则增加下列内容的屏蔽
        int[] pigBookId = new int[]{

        };
        int[] garfieldBookId = new int[]{

        };

        String juniorFix = SqlUtil.norEqual(Db.VoaTable.COLUMN_CATEGORY,juniorBookId);

        String searchSql = "";
        //判断是否屏蔽操作
        if (ConfigData.isBlockJuniorEnglish(LibResUtil.getInstance().getContext())){
            searchSql = "select * from "+Db.VoaTable.TABLE_NAME
                    +" where ("+ SqlUtil.handleIn(Db.VoaTable.COLUMN_HOT_FLG, level.split(Level.Value.SEP))
                    +") and ("+juniorFix
                    + ") order by " + Db.VoaTable.COLUMN_CREATE_TIME + " desc limit ?, ?";
        }else {
            searchSql = "select * from " + Db.VoaTable.TABLE_NAME
                    + " where " + SqlUtil.handleIn(Db.VoaTable.COLUMN_HOT_FLG, level.split(Level.Value.SEP))
                    + " order by " + Db.VoaTable.COLUMN_CREATE_TIME + " desc " +
                    "limit ?, ?";
        }

        return mDb.createQuery(Db.VoaTable.TABLE_NAME, searchSql, String.valueOf(offSize), String.valueOf(pageSize))
                .mapToList(new Func1<Cursor, Voa>() {
                    @Override
                    public Voa call(Cursor cursor) {
                        return Db.VoaTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<Voa> getVoaByLevelOne(int pageNum, final int pageSize) {
        final int offSize = (pageNum - 1) * pageSize;
        Timber.e("*******offSize: " + offSize);
        return Observable.create(new Observable.OnSubscribe<Voa>() {
            @Override
            public void call(Subscriber<? super Voa> subscriber) {
                String sql = "SELECT * FROM 'VOA' "
                        + " ORDER BY " + Db.VoaTable.COLUMN_CREATE_TIME + " DESC "
                        + " LIMIT " + offSize + ", " + pageSize + " ;";
                Log.e("create", sql);
                Cursor cursor = mDb.query(sql);
                while (cursor.moveToNext()) {
//                    Timber.e("---");
                    subscriber.onNext(Db.VoaTable.parseCursor(cursor));
                }
                cursor.close();
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<Voa>> getRecommendList(int category, int pageNum, int pageSize) {
        int offSize = (pageNum - 1) * pageSize;
        return mDb.createQuery(Db.VoaTable.TABLE_NAME,
                        "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                                + " WHERE " + Db.VoaTable.COLUMN_CATEGORY + " = ? "
                                + " ORDER BY " + Db.VoaTable.COLUMN_READ_COUNT + " DESC "
                                + " LIMIT ?, ?"
                        , String.valueOf(category), String.valueOf(offSize), String.valueOf(pageSize))
                .mapToList(new Func1<Cursor, Voa>() {
                    @Override
                    public Voa call(Cursor cursor) {
                        return Db.VoaTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<List<Voa>> getSeriesList(int series, int pageNum, int pageSize) {
        int offSize = (pageNum - 1) * pageSize;
        return mDb.createQuery(Db.VoaTable.TABLE_NAME,
                        "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                                + " WHERE " + Db.VoaTable.COLUMN_SERIES + " = ? "
                                + " ORDER BY " + Db.VoaTable.COLUMN_VOA_ID + " ASC "
                                + " LIMIT ?, ?"
                        , String.valueOf(series), String.valueOf(offSize), String.valueOf(pageSize))
                .mapToList(new Func1<Cursor, Voa>() {
                    @Override
                    public Voa call(Cursor cursor) {
                        return Db.VoaTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<VoaText> setVoaTexts(final Collection<VoaText> voaTexts, final int voaId) {
        return Observable.create(new Observable.OnSubscribe<VoaText>() {
            @Override
            public void call(Subscriber<? super VoaText> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    if (voaTexts != null) {
                        mDb.delete(Db.VoaTextTable.TABLE_NAME,
                                Db.VoaTextTable.COLUMN_VOA_ID + " = ?", String.valueOf(voaId));
                        for (VoaText voaText : voaTexts) {
                            long result = mDb.insert(Db.VoaTextTable.TABLE_NAME,
                                    Db.VoaTextTable.toContentValues(voaText, String.valueOf(voaId)),
                                    SQLiteDatabase.CONFLICT_REPLACE);
                            if (result >= 0) {
                                voaText.setVoaId(voaId);
                                subscriber.onNext(voaText);
                            }
                        }
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public List<VoaText> getAllVoaText() {
        List<VoaText> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.VoaTextTable.TABLE_NAME
                + " ORDER BY " + Db.VoaTextTable.COLUMN_VOA_ID + " ASC";
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            VoaText record = Db.VoaTextTable.parseCursor(cursor);
            list.add(record);
        }
        cursor.close();
        Log.e("DatabaseHelper", "getAllVoaText size = " + list.size());
        return list;
    }

    public Observable<List<VoaText>> getVoaTexts(final int voaId) {
        return mDb.createQuery(Db.VoaTextTable.TABLE_NAME,
                        "SELECT * FROM " + Db.VoaTextTable.TABLE_NAME
                                + " WHERE " + Db.VoaTextTable.COLUMN_VOA_ID + " = ?"
                                + " ORDER BY " + Db.VoaTextTable.COLUMN_PARA_ID + " ASC",
                        String.valueOf(voaId))
                .mapToList(new Func1<Cursor, VoaText>() {
                    @Override
                    public VoaText call(Cursor cursor) {
                        return Db.VoaTextTable.parseCursor(cursor, voaId);
                    }
                });
    }

    public Observable<Boolean> deleteRecord(final long timestamp) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                long result = mDb.delete(Db.RecordTable.TABLE_NAME,
                        Db.RecordTable.COLUMN_TIMESTAMP + " = ?",
                        String.valueOf(timestamp));
                subscriber.onNext(result > 0);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> saveRecord(final Record record) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                ContentValues contentValues = Db.RecordTable.toContentValues(record);
                long result = mDb.insert(Db.RecordTable.TABLE_NAME, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                Log.e("insert record", "result :  " + result);
                subscriber.onNext(result > 0);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<Record>> getDraftRecord() {
        return mDb.createQuery(Db.RecordTable.TABLE_NAME,
                        "SELECT * FROM " + Db.RecordTable.TABLE_NAME
//                        + " WHERE " + Db.RecordTable.COLUMN_FINISH_NUM + " < "
//                        + Db.RecordTable.COLUMN_TOTAL_NUM
                                + " ORDER BY " + Db.RecordTable.COLUMN_DATE + " DESC")
                .mapToList(new Func1<Cursor, Record>() {
                    @Override
                    public Record call(Cursor cursor) {
                        Record record = Db.RecordTable.parseCursor(cursor);
                        Timber.e("call***" + record.titleCn());
                        return record;
                    }
                });
    }

    public List<Record> getRecordbyVoaId(int vaoid) {
        Log.e("DatabaseHelper", "getRecordbyVoaId ---id1");
        List<Record> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.RecordTable.TABLE_NAME
                + " WHERE " + Db.RecordTable.COLUMN_VOA_ID + " = " + vaoid
                + " ORDER BY " + Db.RecordTable.COLUMN_DATE + " ASC";
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            Record record = Db.RecordTable.parseCursor(cursor);
            list.add(record);
        }
        return list;
    }

    public Observable<Boolean> saveVoaSound(final VoaSoundNew record) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                long result = mDb.delete(Db.VoaSoundTable.TABLE_NAME, Db.VoaSoundTable.COLUMN_ITEMID + " = ?", String.valueOf(record.itemid()));
                Log.e("DatabaseHelper", "delete result :  " + result);
                ContentValues contentValues = Db.VoaSoundTable.toContentValues(record);
                result = mDb.insert(Db.VoaSoundTable.TABLE_NAME, contentValues);
                Log.e("DatabaseHelper", "saveVoaSound result :  " + result);
                subscriber.onNext(result > 0);
                subscriber.onCompleted();
            }
        });
    }

    public List<VoaSoundNew> getVoaSoundVoaId(int vaoid) {
        Log.e("DatabaseHelper", "getVoaSoundVoaId ---id1");
        List<VoaSoundNew> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.VoaSoundTable.TABLE_NAME
                + " WHERE " + Db.VoaSoundTable.COLUMN_VOA_ID + " = " + vaoid
                + " ORDER BY " + Db.VoaSoundTable.COLUMN_ITEMID + " ASC";
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            VoaSoundNew record = Db.VoaSoundTable.parseCursor(cursor);
            list.add(record);
        }
        cursor.close();
        return list;
    }

    public List<VoaSoundNew> getVoaSoundVoaTime(int vaoid, long timestamp) {
        Log.e("DatabaseHelper", "getVoaSoundVoaTime ---id1");
        List<VoaSoundNew> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.VoaSoundTable.TABLE_NAME
                + " WHERE " + Db.VoaSoundTable.COLUMN_VOA_ID + " = " + vaoid
                + " AND " + Db.VoaSoundTable.COLUMN_TIME + " = " + timestamp
                + " ORDER BY " + Db.VoaSoundTable.COLUMN_ITEMID + " ASC";
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            VoaSoundNew record = Db.VoaSoundTable.parseCursor(cursor);
            list.add(record);
        }
        return list;
    }

    public List<VoaSoundNew> getVoaSoundItemid(int itemid) {
        List<VoaSoundNew> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.VoaSoundTable.TABLE_NAME
                + " WHERE " + Db.VoaSoundTable.COLUMN_ITEMID + " = " + itemid
                + " ORDER BY " + Db.VoaSoundTable.COLUMN_ITEMID + " ASC";
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            VoaSoundNew record = Db.VoaSoundTable.parseCursor(cursor);
            list.add(record);
        }
        return list;
    }

    public List<VoaSoundNew> getVoaSoundItemUid(int uid, long itemid) {
        Log.e("DatabaseHelper", "getVoaSoundVoaTime ---id1");
        List<VoaSoundNew> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.VoaSoundTable.TABLE_NAME
                + " WHERE " + Db.VoaSoundTable.COLUMN_UID + " = " + uid
                + " AND " + Db.VoaSoundTable.COLUMN_ITEMID + " = " + itemid
                + " ORDER BY " + Db.VoaSoundTable.COLUMN_ITEMID + " ASC";
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            VoaSoundNew record = Db.VoaSoundTable.parseCursor(cursor);
            list.add(record);
        }
        return list;
    }

    public List<VoaSoundNew> getVoaSoundItemid(long itemid, int uid) {
        Log.e("DatabaseHelper", "getVoaSoundItemid ---id1");
        List<VoaSoundNew> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.VoaSoundTable.TABLE_NAME
                + " WHERE " + Db.VoaSoundTable.COLUMN_ITEMID + " = " + itemid
                + " AND " + Db.VoaSoundTable.COLUMN_UID + " = " + uid
                + " ORDER BY " + Db.VoaSoundTable.COLUMN_ITEMID + " ASC";
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            VoaSoundNew record = Db.VoaSoundTable.parseCursor(cursor);
            list.add(record);
        }
        return list;
    }

    public List<VoaSoundNew> getVoaSoundVoaId(int voaId, int uid) {
        Log.e("DatabaseHelper", "getVoaSoundItemid ---id1");
        List<VoaSoundNew> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.VoaSoundTable.TABLE_NAME
                + " WHERE " + Db.VoaSoundTable.COLUMN_VOA_ID + " = " + voaId
                + " AND " + Db.VoaSoundTable.COLUMN_UID + " = " + uid
                + " ORDER BY " + Db.VoaSoundTable.COLUMN_ITEMID + " ASC";
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            VoaSoundNew record = Db.VoaSoundTable.parseCursor(cursor);
            list.add(record);
        }
        return list;
    }

    public int checkDbUpgrade() {
        SQLiteDatabase db = mDb.getWritableDatabase();
        if (db == null) {
            return 0;
        }
        try {
            db.beginTransaction();
            if (!DBUtil.isColumnExist(db, Db.VoaSoundTable.TABLE_NAME, Db.VoaSoundTable.COLUMN_WORDS)) {
                db.execSQL("ALTER TABLE  '" + Db.VoaSoundTable.TABLE_NAME + "'  ADD 'words' TEXT ");
            }
            List<VoaText> voaTexts = getAllVoaText();
            if ((voaTexts != null) && (voaTexts.size() > 0)) {
                Log.e("DatabaseHelper", "checkDbUpgrade getAllVoaText size = " + voaTexts.size());
                db.execSQL("ALTER TABLE  '" + Db.VoaTextTable.TABLE_NAME + "'  RENAME TO 'voaBack' ");
                db.execSQL(Db.VoaTextTable.CREATE);
                int sum = 0;
                for (VoaText voaText : voaTexts) {
                    long result = mDb.insert(Db.VoaTextTable.TABLE_NAME,
                            Db.VoaTextTable.toContentValues(voaText, String.valueOf(voaText.getVoaId())),
                            SQLiteDatabase.CONFLICT_REPLACE);
                    if (result >= 0) {
                        ++sum;
                    }
                }
                Log.e("DatabaseHelper", "checkDbUpgrade insert sum = " + sum);
                db.execSQL("DROP TABLE IF EXISTS 'voaBack' ");
            } else {
                Log.e("DatabaseHelper", "checkDbUpgrade getAllVoaText is null. ");
                db.execSQL("DROP TABLE IF EXISTS '" + Db.VoaTextTable.TABLE_NAME + "'");
                db.execSQL(Db.VoaTextTable.CREATE);
            }
            db.setTransactionSuccessful();
        } catch (Exception var2) {
            return 0;
        } finally {
            db.endTransaction();
        }
        return 1;
    }

    public Observable<List<Record>> getDraftRecord(long mTimeStamp) {
        return mDb.createQuery(Db.RecordTable.TABLE_NAME,
                        "SELECT * FROM " + Db.RecordTable.TABLE_NAME
                                + " WHERE " + Db.RecordTable.COLUMN_TIMESTAMP + " = " + mTimeStamp
                                + " ORDER BY " + Db.RecordTable.COLUMN_DATE + " DESC")
                .mapToList(new Func1<Cursor, Record>() {
                    @Override
                    public Record call(Cursor cursor) {
                        Record record = Db.RecordTable.parseCursor(cursor);
                        Timber.e("call***" + record.title());
                        return record;
                    }
                });
    }

    public Observable<List<Record>> getFinishedRecord() {
//        Thread.sleep(1000);
//        new Object().wait();
        return mDb.createQuery(Db.RecordTable.TABLE_NAME,
                        "SELECT * FROM " + Db.RecordTable.TABLE_NAME
                                + " WHERE " + Db.RecordTable.COLUMN_FINISH_NUM
                                + " >= " + Db.RecordTable.COLUMN_TOTAL_NUM
                                + " ORDER BY " + Db.RecordTable.COLUMN_DATE + " DESC")
                .mapToList(new Func1<Cursor, Record>() {
                    @Override
                    public Record call(Cursor cursor) {
                        return Db.RecordTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<Boolean> deleteRecord(final List<String> timestamps) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    if (timestamps != null) {
                        for (String timestamp : timestamps) {
                            mDb.delete(Db.RecordTable.TABLE_NAME,
                                    Db.RecordTable.COLUMN_TIMESTAMP + " = ?",
                                    String.valueOf(timestamp));
                        }
                    }
                    transaction.markSuccessful();
                    subscriber.onNext(true);
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Boolean> saveCollect(final Collect collect) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Db.CollectTable.COLUMN_UID, collect.uid());
                contentValues.put(Db.CollectTable.COLUMN_VOA_ID, collect.voaId());
                contentValues.put(Db.CollectTable.COLUMN_DATE, collect.date());
                long result = mDb.insert(Db.CollectTable.TABLE_NAME, contentValues);
                subscriber.onNext(result > 0);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> deleteCollect(final int uid, final List<String> list) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    if (list != null) {
                        for (String voaId : list) {
                            mDb.delete(Db.CollectTable.TABLE_NAME,
                                    Db.CollectTable.COLUMN_VOA_ID + " = ? AND "
                                            + Db.CollectTable.COLUMN_UID + " = ?",
                                    voaId, String.valueOf(uid));
                        }
                    }
                    transaction.markSuccessful();
                    subscriber.onNext(true);
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Boolean> deleteCollect(final int uid, final int voaId) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                long result = mDb.delete(Db.CollectTable.TABLE_NAME,
                        Db.CollectTable.COLUMN_VOA_ID + " = ? AND "
                                + Db.CollectTable.COLUMN_UID + " = ?",
                        String.valueOf(voaId), String.valueOf(uid));
                subscriber.onNext(result > 0);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<Collect>> getCollect(final int uid) {
        return Observable.create(new Observable.OnSubscribe<Collect>() {
            @Override
            public void call(Subscriber<? super Collect> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT * FROM " + Db.CollectTable.TABLE_NAME
                                + " WHERE " + Db.CollectTable.COLUMN_UID + " = ?",
                        String.valueOf(uid));
                while (cursor.moveToNext()) {
                    subscriber.onNext(Db.CollectTable.parseCursor(cursor));
                }
                cursor.close();
                subscriber.onCompleted();
            }
        }).doOnNext(new Action1<Collect>() {
            @Override
            public void call(Collect collect) {
                Cursor voaCursor = mDb.query(
                        "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                                + " WHERE " + Db.VoaTable.COLUMN_VOA_ID + " = ?",
                        String.valueOf(collect.voaId()));
                if (voaCursor.moveToNext()) {
                    collect.setVoa(Db.VoaTable.parseCursor(voaCursor));
                }
            }
        }).toList();
    }

    public Observable<Integer> getCollectByVoaId(final int voaId) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT " + Db.CollectTable.COLUMN_VOA_ID
                                + " FROM " + Db.CollectTable.TABLE_NAME
                                + " WHERE " + Db.CollectTable.COLUMN_VOA_ID + " = ?",
                        String.valueOf(voaId));
                if (cursor.moveToNext()) {
                    subscriber.onNext(cursor.getInt(0));
                } else {
                    subscriber.onNext(-1);
                }
                cursor.close();
                subscriber.onCompleted();
            }
        });
    }

    //保存配音下载文件信息
    public Observable<Boolean> saveDownload(final Download download) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                ContentValues contentValues = Db.DownloadTable.toContentValues(download);
                long result = mDb.insert(Db.DownloadTable.TABLE_NAME, contentValues);
                subscriber.onNext(result > 0);
                subscriber.onCompleted();
            }
        });
    }

    //删除配音下载信息
    public Observable<Boolean> deleteDownload(final List<String> list) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    if (list != null) {
                        for (String id : list) {
                            mDb.delete(Db.DownloadTable.TABLE_NAME,
                                    Db.DownloadTable.COLUMN_ID + " =? ",
                                    String.valueOf(id));
                        }
                    }
                    transaction.markSuccessful();
                    subscriber.onNext(true);
                } finally {
                    transaction.end();
                }
            }
        });
    }

    //获取配音下载信息
    public Observable<List<Download>> getDownload(int uid) {
        return Observable.create(new Observable.OnSubscribe<Download>() {
            @Override
            public void call(Subscriber<? super Download> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT * FROM " + Db.DownloadTable.TABLE_NAME
                                + " WHERE " + Db.DownloadTable.COLUMN_UID + " = " + uid
                                + " GROUP BY " + Db.DownloadTable.COLUMN_VOA_ID);
                while (cursor.moveToNext()) {
                    subscriber.onNext(Db.DownloadTable.parseCursor(cursor));
                }
                cursor.close();
                subscriber.onCompleted();
            }
        }).doOnNext(new Action1<Download>() {
            @Override
            public void call(Download download) {
                Cursor voaCursor = mDb.query(
                        "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                                + " WHERE " + Db.VoaTable.COLUMN_VOA_ID + " = ?",
                        String.valueOf(download.voaId()));
                if (voaCursor.moveToNext()) {
                    download.setVoa(Db.VoaTable.parseCursor(voaCursor));
                }
            }
        }).toList();
    }

    //获取单个配音下载信息
    public Download getSingleDownload(int uid, int voaId) {
        Cursor cursor = mDb.query("SELECT * FROM " + Db.DownloadTable.TABLE_NAME + " WHERE " + Db.DownloadTable.COLUMN_UID + " =? AND " + Db.DownloadTable.COLUMN_VOA_ID + " =? ", String.valueOf(uid), String.valueOf(voaId));
        if (cursor.moveToNext()) {
            return Db.DownloadTable.parseCursor(cursor);
        }
        return null;
    }

    public Observable<List<University>> getUniversity(final String keyword, final int size) {
        return Observable.create(new Observable.OnSubscribe<University>() {
            @Override
            public void call(Subscriber<? super University> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT * FROM " + Db.UniversityTable.TABLE_NAME
                                + " WHERE " + Db.UniversityTable.COLUMN_UNI_NAME
                                + " LIKE ? ",
                        new String[]{"%" + keyword + "%"});
                while (cursor.moveToNext()) {
                    subscriber.onNext(Db.UniversityTable.parseCursor(cursor));
                }
                cursor.close();
                subscriber.onCompleted();
            }
        }).toList();
    }

    public Observable<List<University>> getAllUniversity() {
        return Observable.create(new Observable.OnSubscribe<University>() {
            @Override
            public void call(Subscriber<? super University> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT * FROM " + Db.UniversityTable.TABLE_NAME);
                while (cursor.moveToNext()) {
                    subscriber.onNext(Db.UniversityTable.parseCursor(cursor));
                }
                cursor.close();
                subscriber.onCompleted();
            }
        }).toList();
    }

    public List<Thumb> getCommentThumb(final int uid, final int commentId) {
        Log.e("DatabaseHelper", "getCommentThumb ---id1");
        Cursor cursor = mDb.query(
                "SELECT * FROM " + Db.ThumbTable.TABLE_NAME
                        + " WHERE " + Db.ThumbTable.COLUMN_UID + " = " + uid
                        + " AND " + Db.ThumbTable.COLUMN_COMMENT_ID + " = " + commentId);
        List<Thumb> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Thumb record = Db.ThumbTable.parseCursor(cursor);
            list.add(record);
        }
        return list;
    }

    public Observable<Thumb> getThumb(final int uid, final int commentId) {
        return Observable.create(new Observable.OnSubscribe<Thumb>() {
            @Override
            public void call(Subscriber<? super Thumb> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT * FROM " + Db.ThumbTable.TABLE_NAME
                                + " WHERE " + Db.ThumbTable.COLUMN_UID + " = ? "
                                + " AND " + Db.ThumbTable.COLUMN_COMMENT_ID + " = ?",
                        String.valueOf(uid), String.valueOf(commentId));
                if (cursor.moveToNext()) {
                    subscriber.onNext(Db.ThumbTable.parseCursor(cursor));
                } else {
                    subscriber.onNext(null);
                }
                cursor.close();
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> insertThumb(final Thumb thumb) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Db.ThumbTable.COLUMN_UID, thumb.uid());
                contentValues.put(Db.ThumbTable.COLUMN_COMMENT_ID, thumb.commentId());
                contentValues.put(Db.ThumbTable.COLUMN_ACTION, thumb.getAction());
                long result = mDb.insert(Db.ThumbTable.TABLE_NAME, contentValues);
                subscriber.onNext(result > 0);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> updateThumb(final Thumb thumb) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Cursor cursor = mDb.query(
                        "UPDATE " + Db.ThumbTable.TABLE_NAME
                                + " SET " + Db.ThumbTable.COLUMN_ACTION + " = ? "
                                + " WHERE " + Db.ThumbTable.COLUMN_UID + " = ? "
                                + " AND " + Db.ThumbTable.COLUMN_COMMENT_ID + " = ?",
                        String.valueOf(thumb.getAction()), String.valueOf(thumb.uid()),
                        String.valueOf(thumb.commentId()));
                if (cursor.moveToNext()) {
                    subscriber.onNext(cursor.getInt(0) > 0);
                }
                cursor.close();
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> deleteThumb(final int uid, final int id) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                long result = mDb.delete(Db.ThumbTable.TABLE_NAME,
                        Db.ThumbTable.COLUMN_UID + " = ? AND "
                                + Db.ThumbTable.COLUMN_COMMENT_ID + " = ?",
                        String.valueOf(uid), String.valueOf(id));
                subscriber.onNext(result > 0);
                subscriber.onCompleted();
            }
        });
    }

    public boolean isTrial(Voa voa) {
        int tempVoaId = voa.voaId() - 3;
        int series = 0;
        String sql = String.format("SELECT SERIES from VOA WHERE VOAID = (%s)", tempVoaId);
        Cursor cursor = mDb.query(sql);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            series = cursor.getInt(0);
        }
        return series != voa.series();
    }

    public Observable<Voa> getVoasByCategoryNotWith(Boolean refreshSimple, String category, int limit, String ids) {

        String selectSeries;
        if (ids.contains(",")) {
            selectSeries = String.format("SELECT SERIES from VOA WHERE VOAID IN (%s)", ids);
        } else {
            selectSeries = "0";
        }
        final String selectSeriesSql = String.format(
                "SELECT * FROM VOA WHERE VOAID IN ( " +
                        "  SELECT  VOAID  FROM VOA WHERE CATEGORY in (%s) AND VOAID NOT IN (%s)) AND SERIES NOT IN (%s)" +
                        "   " +
                        " ORDER BY RANDOM() LIMIT %s; ", category, ids, selectSeries, 1);
        Log.e("category sql", selectSeriesSql);


        final String sql = String.format("SELECT * FROM VOA WHERE VOAID IN ( " +
                "  SELECT  VOAID  FROM VOA WHERE CATEGORY in (%s) AND VOAID NOT IN (%s) " +
                ") " +
                " ORDER BY RANDOM() LIMIT %s; ", category, ids, limit);
        Log.e("category sql", sql);

        return Observable.create(new Action1<Emitter<Voa>>() {
            @Override
            public void call(Emitter<Voa> emitter) {
                Cursor cursor1 = mDb.query(selectSeriesSql);
                int count = 0;

                Cursor cursor = mDb.query(sql);
                while (cursor.moveToNext()) {
                    Voa showVoa = Db.VoaTable.parseCursor(cursor);
                    emitter.onNext(showVoa);
                    Log.d("数据显示--第四部分", showVoa.titleCn());

                    if (count++ == 2) {
                        break;
                    }
                }

                if ((cursor1 != null) && cursor1.getCount() > 0) {
                    cursor1.moveToNext();
                    Voa showVoa = Db.VoaTable.parseCursor(cursor1);
                    emitter.onNext(showVoa);
                    Log.d("数据显示--第二部分", showVoa.titleCn());
                } else {
                    if ((cursor != null) && cursor.moveToNext()) {
                        Voa showVoa = Db.VoaTable.parseCursor(cursor);
                        emitter.onNext(showVoa);
                        Log.d("数据显示--第三部分", showVoa.titleCn());
                    }
                }

                cursor.close();
                cursor1.close();
                emitter.onCompleted();
            }
        }, Emitter.BackpressureMode.NONE);
    }


    public Observable<Voa> getVoaByBothOne(int category, String level, int pageNum, int pageSize) {
        int offSize = (pageNum - 1) * pageSize;

        String sql = "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                + " WHERE (" + SqlUtil.handleIn(Db.VoaTable.COLUMN_HOT_FLG, level.split(Level.Value.SEP))
                + ") AND " + Db.VoaTable.COLUMN_CATEGORY + " = ? "
                + " order by " + Db.VoaTable.COLUMN_CREATE_TIME + " desc "
                + " LIMIT ?, ?";

        String realSql = sql;

        return Observable.create(emitter -> {
            Cursor cursor = mDb.query(realSql, String.valueOf(category), String.valueOf(offSize), String.valueOf(pageSize));
            while (cursor.moveToNext()) {
                Voa showVoa = Db.VoaTable.parseCursor(cursor);
                emitter.onNext(showVoa);

                Log.d("数据显示--第一部分", showVoa.titleCn());
            }
            cursor.close();
            emitter.onCompleted();
        }, Emitter.BackpressureMode.NONE);
    }

    public Observable<Voa> getVoasBySeries(int category) {

        return Observable.create(emitter -> {
            String sql = "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                    + " WHERE " + Db.VoaTable.COLUMN_SERIES + " = ? "
                    + " order by " + Db.VoaTable.COLUMN_VOA_ID + " asc ";
            Cursor cursor = mDb.query(sql, String.valueOf(category));
            while (cursor.moveToNext()) {
//                Timber.e("---");
                emitter.onNext(Db.VoaTable.parseCursor(cursor));
            }
            cursor.close();
            emitter.onCompleted();
        }, Emitter.BackpressureMode.NONE);
    }


    public Observable<SeriesData> getSeriesById(String seriesId) {
        Timber.e("---id1");
        return Observable.create(emitter -> {
            String sql = "SELECT * FROM " + Db.SeriesTable.TABLE_NAME
                    + " WHERE " + Db.SeriesTable.COLUMN_SERIES_ID + " = " + seriesId;
            Cursor cursor = mDb.query(sql);
            while (cursor.moveToNext()) {
                Timber.e("---id");
                emitter.onNext(Db.SeriesTable.parseCursor(cursor));
            }
            cursor.close();
            emitter.onCompleted();
        }, Emitter.BackpressureMode.NONE);
    }

    public Observable<List<Voa>> getVoasBySeriesId(String seriesId) {
        return mDb.createQuery(Db.VoaTable.TABLE_NAME,
                        "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                                + " WHERE " + Db.VoaTable.COLUMN_SERIES + " = ? "
                                + " ORDER BY " + Db.VoaTable.COLUMN_VOA_ID + " ASC "
                        , seriesId)
                .mapToList(new Func1<Cursor, Voa>() {
                    @Override
                    public Voa call(Cursor cursor) {
                        return Db.VoaTable.parseCursor(cursor);
                    }
                });
    }

    public List<Voa> getVoa4Series(String seriesId) {
        Log.e("DatabaseHelper", "getVoa4Series ---  " + seriesId);
        List<Voa> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                + " WHERE " + Db.VoaTable.COLUMN_SERIES + " =  " + seriesId
                + " ORDER BY " + Db.VoaTable.COLUMN_VOA_ID + " ASC ";
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            list.add(Db.VoaTable.parseCursor(cursor));
        }
        return list;
    }

    public List<Voa> getVoa4Category(String seriesId) {
        Log.e("DatabaseHelper", "getVoa4Series ---  " + seriesId);
        List<Voa> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                + " WHERE " + Db.VoaTable.COLUMN_CATEGORY + " =  " + seriesId
                + " ORDER BY " + Db.VoaTable.COLUMN_VOA_ID + " ASC ";
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            list.add(Db.VoaTable.parseCursor(cursor));
        }
        return list;
    }

    public Observable<List<Voa>> searchLocalVoa(String keyword) {
        Log.e("DatabaseHelper", "searchLocalVoa ---  " + keyword);
        List<Voa> list = new ArrayList<>();
        return Observable.create(emitter -> {
            Cursor cursor = mDb.query(
                    "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                            + " WHERE " + Db.VoaTable.COLUMN_DESC_CN
                            + " LIKE ? ", new String[]{"%" + keyword + "%"});
            while (cursor.moveToNext()) {
                list.add(Db.VoaTable.parseCursor(cursor));
            }
            emitter.onNext(list);
            cursor.close();
            emitter.onCompleted();
        }, Emitter.BackpressureMode.NONE);
    }

    public Observable<List<Voa>> getXiaoxueByBookId(int seriesId) {
        Timber.e("---id1");
        List<Voa> list = new ArrayList<>();
        return Observable.create(emitter -> {

            String sql = "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                    + " WHERE " + Db.VoaTable.COLUMN_SERIES + " =  " + seriesId
                    + " ORDER BY " + Db.VoaTable.COLUMN_VOA_ID + " ASC ";
            Cursor cursor = mDb.query(sql);
            while (cursor.moveToNext()) {
                Timber.e("---id");
                list.add(Db.VoaTable.parseCursor(cursor));
            }
            emitter.onNext(list);
            cursor.close();
            emitter.onCompleted();
        }, Emitter.BackpressureMode.NONE);
    }

    public List<Integer> getXiaoxueVoaIdsByBookId(int seriesId) {
        Timber.e("---id1");
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT DISTINCT voaId FROM " + Db.VoaTable.TABLE_NAME
                + " WHERE " + Db.VoaTable.COLUMN_SERIES + " =  " + seriesId
                + " ORDER BY " + Db.VoaTable.COLUMN_VOA_ID + " ASC ";
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            Timber.e("---id");
            list.add(cursor.getInt(0));
        }
        return list;
    }


    public Observable<List<Voa>> getXIaoxueByBookId(int bookId) {
        return mDb.createQuery(Db.VoaTable.TABLE_NAME,
                        "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                                + " WHERE " + Db.VoaTable.COLUMN_SERIES + " = ? "
                                + " ORDER BY " + Db.VoaTable.COLUMN_VOA_ID + " ASC "
                        , String.valueOf(bookId))
                .mapToList(new Func1<Cursor, Voa>() {
                    @Override
                    public Voa call(Cursor cursor) {
                        return Db.VoaTable.parseCursor(cursor);
                    }
                });
    }

    public List<Voa> getVoaByVoaId(int vaoId) {
        List<Voa> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                + " WHERE " + Db.VoaTable.COLUMN_VOA_ID + " =  " + vaoId;
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            list.add(Db.VoaTable.parseCursor(cursor));
        }
        return list;
    }

    public Observable<List<SeriesData>> getSeriesList4Ids(String idList) {
        StringBuffer queryString = new StringBuffer("(");
        queryString.append(idList);
        queryString.append(")");
        Log.d("TAG", "getSeriesList4Ids: " + queryString.toString());
        return Observable.create(new Observable.OnSubscribe<SeriesData>() {
            @Override
            public void call(Subscriber<? super SeriesData> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT * FROM " + Db.SeriesTable.TABLE_NAME
                                + " WHERE " + Db.SeriesTable.COLUMN_SERIES_CATEGORY + " in "
                                + String.valueOf(queryString)
                );
                if (cursor.getCount() == 0) {
                    subscriber.onNext(null);
                } else {
                    while (cursor.moveToNext()) {
                        subscriber.onNext(Db.SeriesTable.parseCursor(cursor));
                    }
                }

                cursor.close();
                subscriber.onCompleted();
            }
        }).toList();
    }

    public Observable<List<SeriesData>> getSeriesListFromIds(List<Integer> idList) {
        StringBuffer queryString = new StringBuffer("(");
        for (int i : idList) {
            queryString.append(i);
            queryString.append(",");
        }
        queryString.delete(queryString.length() - 1, queryString.length());
        queryString.append(")");
        Log.d("TAG", "getSeriesListFromIds: " + queryString.toString());
        return Observable.create(new Observable.OnSubscribe<SeriesData>() {
            @Override
            public void call(Subscriber<? super SeriesData> subscriber) {
                Cursor cursor = mDb.query(
                        "SELECT * FROM " + Db.SeriesTable.TABLE_NAME
                                + " WHERE " + Db.SeriesTable.COLUMN_SERIES_ID + " in "
                                + String.valueOf(queryString)
                );
                if (cursor.getCount() == 0) {
                    subscriber.onNext(null);
                } else {
                    while (cursor.moveToNext()) {
                        subscriber.onNext(Db.SeriesTable.parseCursor(cursor));
                    }
                }

                cursor.close();
                subscriber.onCompleted();
            }
        }).toList();
//        while (cursor.moveToNext()) {
//            subscriber.onNext(Db.VoaTable.parseCursor(cursor));
//        }
//        cursor.close();
//        subscriber.onCompleted();

//                .mapToList(new Func1<Cursor, SeriesData>() {
//                    @Override
//                    public SeriesData call(Cursor cursor) {
//                        return Db.SeriesTable.parseCursor(cursor);
//                    }
//                });
    }

//    //这里保存网络上查询出的课程数据，用于进行收藏保存
//    //INSERT INTO "voa"("voaId", "introDesc", "createTime", "category", "keyword", "title", "sound", "pic", "pageTitle", "url", "descCn", "titleCn", "publishTime", "hotFlg", "readCount", "series") VALUES (301166, '', '2019-04-23 11:23:22.0', 301, '', 'But you can control it.', '/201904/301166.mp3', 'http://staticvip.iyuba.cn/images/voa/301166.jpg', '', '', '这个小光环是我身体一部分了，不止是护甲。', '你可以控制它', '', 4, 160, 0);
//    public void saveNewtVoa(final Voa voa) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(Db.VoaTable.COLUMN_VOA_ID, voa.voaId());
//        contentValues.put(Db.VoaTable.COLUMN_INTRO_DESC, voa.introDesc());
//        contentValues.put(Db.VoaTable.COLUMN_CREATE_TIME, voa.createTime());
//        contentValues.put(Db.VoaTable.COLUMN_CATEGORY, voa.category());
//        contentValues.put(Db.VoaTable.COLUMN_KEYWORD, voa.keyword());
//        contentValues.put(Db.VoaTable.COLUMN_TITLE, voa.title());
//        contentValues.put(Db.VoaTable.COLUMN_SOUND, voa.sound());
//        contentValues.put(Db.VoaTable.COLUMN_PIC, voa.pic());
//        contentValues.put(Db.VoaTable.COLUMN_PAGE_TITLE, voa.pageTitle());
//        contentValues.put(Db.VoaTable.COLUMN_URL, voa.url());
//        contentValues.put(Db.VoaTable.COLUMN_DESC_CN, voa.descCn());
//        contentValues.put(Db.VoaTable.COLUMN_TITLE_CN, voa.titleCn());
//        contentValues.put(Db.VoaTable.COLUMN_PUBLISH_TIME, voa.publishTime());
//        contentValues.put(Db.VoaTable.COLUMN_HOT_FLG, voa.hotFlag());
//        contentValues.put(Db.VoaTable.COLUMN_READ_COUNT, voa.readCount());
//        contentValues.put(Db.VoaTable.COLUMN_SERIES, voa.series());
//        //增加video的参数
//        contentValues.put(Db.VoaTable.COLUMN_VIDEO,voa.video());
//
//        getVoaById(voa.voaId())
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(new Subscriber<Voa>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Voa voa) {
//                        if (voa==null){
//                            mDb.insert(Db.VoaTable.TABLE_NAME, contentValues);
//                        }else {
//                            mDb.update(Db.VoaTable.TABLE_NAME, contentValues,Db.VoaTable.COLUMN_VOA_ID+" = ?",new String[]{String.valueOf(voa.voaId())});
//                        }
//                    }
//                });
//    }


    //获取voa表中的总的数量和video字段数据的比对
    public long getVoaVideoNullCount() {
        String querySql = "SELECT COUNT(*) FROM 'VOA' WHERE video IS NULL";
        Cursor queryCursor = mDb.query(querySql);
        if (queryCursor.moveToNext()) {
            long count = queryCursor.getLong(0);
            return count;
        }
        return 0;
    }

    public List<Voa> getVoaXiaoxueByBookId(int bookId) {
        Log.e("DatabaseHelper", "getVoaXiaoxueByBookId ---  ");
        List<Voa> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.VoaTable.TABLE_NAME
                + " WHERE " + Db.VoaTable.COLUMN_SERIES + " =  " + bookId
                + " ORDER BY " + Db.VoaTable.COLUMN_VOA_ID + " ASC ";
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            list.add(Db.VoaTable.parseCursor(cursor));
        }
        return list;
    }

    public List<SeriesData> getSeriesCategory(int category) {
        Log.e("DatabaseHelper", "getSeriesId category = " + category);
        List<SeriesData> list = new ArrayList<>();
        String sql = "SELECT * FROM " + Db.SeriesTable.TABLE_NAME
                + " WHERE " + Db.SeriesTable.COLUMN_SERIES_CATEGORY + " = " + category;
        Cursor cursor = mDb.query(sql);
        while (cursor.moveToNext()) {
            SeriesData record = Db.SeriesTable.parseCursor(cursor);
            list.add(record);
        }
        cursor.close();
        return list;
    }
}
