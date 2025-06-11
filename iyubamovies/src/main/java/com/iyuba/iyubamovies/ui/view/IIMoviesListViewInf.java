package com.iyuba.iyubamovies.ui.view;

import com.iyuba.iyubamovies.base.BaseView;

import java.util.List;

/**
 * Created by iyuba on 2017/12/22.
 */

public interface IIMoviesListViewInf extends BaseView{
    void DataOfIntenetError();
    void showToastMessage(String text);
    void showDialog();
    void dismissDialog();
    void refreshlist();
    void setAdapterData(List<Object> datas);
    void addAdapterData(List<Object> datas);
    void clearAdapterData();
    void setBannerData(Object obj);
    void onRefreshComplete();
    void onLoadMoreComplete();

}
