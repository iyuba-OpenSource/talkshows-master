package com.iyuba.talkshow.ui.lil.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iyuba.talkshow.ui.lil.local.entity.Entity_book;

import java.util.List;

@Dao
public interface Dao_book {

    //插入数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveData(List<Entity_book> list);

    //获取当前类型下的数据
    @Query("select * from Entity_book where typeId=:typeId order by bookId asc")
    List<Entity_book> queryMultiData(String typeId);
}
