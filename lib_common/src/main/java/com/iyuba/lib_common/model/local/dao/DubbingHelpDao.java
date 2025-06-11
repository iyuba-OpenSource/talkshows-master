package com.iyuba.lib_common.model.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iyuba.lib_common.model.local.entity.DubbingHelpEntity;


@Dao
public interface DubbingHelpDao {

    //插入单个数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveSingleData(DubbingHelpEntity entity);

    //获取当前item的数据
    @Query("select * from DubbingHelpEntity where itemId=:itemId and userId=:userId")
    DubbingHelpEntity getSingleData(long itemId,int userId);
}
