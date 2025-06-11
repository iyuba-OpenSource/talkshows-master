package com.iyuba.iyubamovies.database.opinf;

import com.iyuba.iyubamovies.model.ImoviesList;

import java.util.List;

/**
 * Created by iyuba on 2017/12/23.
 */

public interface ImoviesListInf {
    String TABLE_NAME = "imovieslist";
    String SERIESID = "seriesid";
    String DESCCN = "desccn";
    String CATEGORY = "category";
    String SERIESNAME = "seriesname";
    String CREATETIME= "createtime";
    String UPDATETIME = "updatetime";
    String HOTFLG = "hotflg";
    String PIC = "pic";
    String KEYWORDS ="keywords";
    String OTHER = "other";
    String OTHER1 = "other1";
    void SavaImoviesList(ImoviesList data);
    void SavaImoviesLists(List<ImoviesList> datas);
    List<ImoviesList> SelectAllImoviesLists();
    ImoviesList SelectImoviesList(String seriesid);
    //List<ImoviesList> findImoviesListByPage(int count,int offset);
}
