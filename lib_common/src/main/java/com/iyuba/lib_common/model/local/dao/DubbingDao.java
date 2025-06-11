package com.iyuba.lib_common.model.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iyuba.lib_common.model.local.entity.DubbingEntity;

import java.util.List;

@Dao
public interface DubbingDao {

    //保存单个评测
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveSingleData(DubbingEntity entity);

    //获取单个评测
    @Query("select * from DubbingEntity where userId=:userId and voaId=:voaId and paraId=:paraId and indexId=:indexId")
    DubbingEntity getSingleData(int userId,int voaId,int paraId,int indexId);

    //获取多个评测
    @Query("select * from DubbingEntity where userId=:userId and voaId=:voaId order by paraId,indexId asc")
    List<DubbingEntity> getMultiData(int userId, int voaId);
}
