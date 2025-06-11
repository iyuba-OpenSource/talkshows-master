package com.iyuba.lib_common.model.local.manager;

import com.iyuba.lib_common.model.local.RoomDB;
import com.iyuba.lib_common.model.local.entity.DubbingHelpEntity;

public class CommonDataManager {

    //获取配音界面辅助数据
    public static DubbingHelpEntity getDubbingHelpData(long itemId, int userId){
        return RoomDB.getInstance().getDubbingHelpDao().getSingleData(itemId, userId);
    }

    //保存数据到配音界面辅助表
    public static void saveDataToDubbingHelp(DubbingHelpEntity entity){
        RoomDB.getInstance().getDubbingHelpDao().saveSingleData(entity);
    }
}
