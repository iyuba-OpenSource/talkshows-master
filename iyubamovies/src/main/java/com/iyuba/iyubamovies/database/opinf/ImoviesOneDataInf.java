package com.iyuba.iyubamovies.database.opinf;

import com.iyuba.iyubamovies.model.OneSerisesData;

import java.util.List;

/**
 * Created by iyuba on 2018/1/15.
 */

public interface ImoviesOneDataInf {
    String TABLE_NAME = "imvonedatas";
    String SERIESID = "seriesid";
    String DESCCN = "desccn";
    String CATEGORY = "category";
    String CATEGORYNAME = "categoryname";
    String CREATETIME= "createtime";
    String PIC = "pic";
    String OTHER = "other";
    String OTHER1 = "other1";
    String TITLE = "title";
    String TITLECN = "titlecn";
    String ID = "id";
    String SOUND = "sound";
    String READCOUNT = "readcount";
    String FLAG = "flag";
    String TYPE = "type";
    String ISDOWNLOAD = "isdownload";
    String ISFINISHDL = "isfinshdl";
    void saveSerises(List<OneSerisesData> datas);
    List<OneSerisesData> getSerisesBySerisesId(String serisesid);
    void saveSerise(OneSerisesData data);
    int countofSeriseid(String serisesid);
    void updateSerisesDownloadState(String download,String isfinishdl,String id,String seriseid);
    List<OneSerisesData> getAllSerisesByDownLoadState();
}
