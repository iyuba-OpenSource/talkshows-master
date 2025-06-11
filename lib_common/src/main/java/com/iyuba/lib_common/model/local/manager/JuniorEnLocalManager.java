package com.iyuba.lib_common.model.local.manager;

import com.iyuba.lib_common.model.local.RoomDB;
import com.iyuba.lib_common.model.local.entity.BookEntity_junior;
import com.iyuba.lib_common.model.local.entity.ChapterDetailEntity_junior;
import com.iyuba.lib_common.model.local.entity.ChapterEntity_junior;

import java.util.List;

/**
 * @title: 青少年英语-本地数据管理
 * @date: 2023/12/28 17:58
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description: 小学英语和初中英语内容
 */
public class JuniorEnLocalManager {

    /***************************************出版社********************************/
    //出版社数据为网络接口数据，不存在与本地，如有需要可以设计下

    /******************************************书籍*********************************/
    //数据库-保存中小学的书籍数据
    public static void saveBookToDB(List<BookEntity_junior> list){
        RoomDB.getInstance().getBookEntityJuniorDao().saveData(list);
    }

    //数据库-获取类型下的书籍数据
    public static List<BookEntity_junior> getBookFromDB(String typeId){
        return RoomDB.getInstance().getBookEntityJuniorDao().getDataByTypeId(typeId);
    }

    /********************************章节************************************/
    //数据库-保存中小学的章节数据
    public static void saveChapterToDB(List<ChapterEntity_junior> list){
        RoomDB.getInstance().getChapterEntityJuniorDao().saveData(list);
    }

    //数据库-获取书籍下的章节数据
    public static List<ChapterEntity_junior> getMultiChapterFromDB(String bookId){
        return RoomDB.getInstance().getChapterEntityJuniorDao().getDataByBookId(bookId);
    }

    //数据库-获取单个章节的数据
    public static ChapterEntity_junior getSingleChapterFromDB(String voaId){
        return RoomDB.getInstance().getChapterEntityJuniorDao().getDataByVoaId(voaId);
    }

    /********************************章节详情*********************************/
    //数据库-保存章节详情数据
    public static void saveChapterDetailToDB(List<ChapterDetailEntity_junior> list){
        RoomDB.getInstance().getChapterDetailEntityJuniorDao().saveData(list);
    }

    //数据库-获取章节下的章节详情数据
    public static List<ChapterDetailEntity_junior> getMultiChapterDetailFromDB(String voaId){
        return RoomDB.getInstance().getChapterDetailEntityJuniorDao().getDataByVoaId(voaId);
    }
}
