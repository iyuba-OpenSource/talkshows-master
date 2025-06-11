package com.iyuba.talkshow.ui.lil.local.manager;

import com.iyuba.lib_common.model.remote.bean.Junior_book;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.ui.lil.local.TempRoomDb;
import com.iyuba.talkshow.ui.lil.local.entity.Entity_book;
import com.iyuba.talkshow.ui.lil.ui.newChoose.bean.BookShowBean;

import java.util.ArrayList;
import java.util.List;

public class TempDbManager {
    private static TempDbManager instance;

    public static TempDbManager getInstance(){
        if (instance==null){
            synchronized (TempDbManager.class){
                if (instance==null){
                    instance = new TempDbManager();
                }
            }
        }
        return instance;
    }

    //插入数据
    public void saveData(List<SeriesData> list){
        //转换成需要的数据
        List<Entity_book> showList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SeriesData seriesData = list.get(i);

            showList.add(new Entity_book(
                    seriesData.getCategory(),
                    seriesData.getId(),
                    seriesData.getCreateTime(),
                    seriesData.getIsVideo(),
                    seriesData.getPic(),
                    seriesData.getKeyWords(),
                    seriesData.getVersion(),
                    seriesData.getDescCn(),
                    String.valueOf(seriesData.getSeriesCount()),
                    seriesData.getSeriesName(),
                    seriesData.getUpdateTime(),
                    seriesData.getHotFlg(),
                    String.valueOf(seriesData.getHaveMicro())
            ));
        }

        //插入数据
        TempRoomDb.getInstance().getBookDao().saveData(showList);
    }

    //获取当前类型数据
    public List<SeriesData> getMultiData(String typeId){
        List<SeriesData> seriesDataList = new ArrayList<>();
        //查询数据
        List<Entity_book> dbList = TempRoomDb.getInstance().getBookDao().queryMultiData(typeId);
        if (dbList!=null && dbList.size()>0){
            for (int i = 0; i < dbList.size(); i++) {
                Entity_book bookData = dbList.get(i);

                SeriesData seriesData = new SeriesData();
                seriesData.setCategory(bookData.typeId);
                seriesData.setId(bookData.bookId);

                seriesData.setPic(bookData.pic);
                seriesData.setVersion(bookData.version);
                seriesData.setIsVideo(bookData.isVideo);
                seriesData.setSeriesCount(Integer.parseInt(bookData.seriesCount));
                seriesData.setCreateTime(bookData.createTime);
                seriesData.setUpdateTime(bookData.updateTime);
                seriesData.setKeyWords(bookData.keyWords);
                seriesData.setDescCn(bookData.descCn);
                seriesData.setSeriesName(bookData.seriesName);

                seriesDataList.add(seriesData);
            }
        }

        return  seriesDataList;
    }

    //插入数据
    public void saveDataNew(List<Junior_book> list){
        //转换成需要的数据
        List<Entity_book> showList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Junior_book bookData = list.get(i);

            showList.add(new Entity_book(
                    bookData.getCategory(),
                    bookData.getId(),
                    bookData.getCreateTime(),
                    bookData.getIsVideo(),
                    bookData.getPic(),
                    bookData.getKeyWords(),
                    bookData.getVersion(),
                    bookData.getDescCn(),
                    String.valueOf(bookData.getSeriesCount()),
                    bookData.getSeriesName(),
                    bookData.getUpdateTime(),
                    bookData.getHotFlg(),
                    String.valueOf(bookData.getHaveMicro())
            ));
        }

        //插入数据
        TempRoomDb.getInstance().getBookDao().saveData(showList);
    }

    //获取当前类型数据
    public List<BookShowBean> getMultiDataNew(String typeId){
        List<BookShowBean> showList = new ArrayList<>();
        //查询数据
        List<Entity_book> dbList = TempRoomDb.getInstance().getBookDao().queryMultiData(typeId);
        if (dbList!=null && dbList.size()>0){
            for (int i = 0; i < dbList.size(); i++) {
                Entity_book bookData = dbList.get(i);

                showList.add(new BookShowBean(
                        bookData.bookId,
                        bookData.seriesName,
                        bookData.pic,
                        bookData.isVideo
                ));
            }
        }

        return  showList;
    }
}
