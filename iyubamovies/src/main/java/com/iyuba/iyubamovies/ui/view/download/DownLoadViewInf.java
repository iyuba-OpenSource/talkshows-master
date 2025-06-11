package com.iyuba.iyubamovies.ui.view.download;

import com.iyuba.iyubamovies.model.OneSerisesData;

import java.util.List;

/**
 * Created by iyuba on 2018/1/24.
 */

public interface DownLoadViewInf {
    void setDownLoadListItems(List<Object> objects);
    void showDialog(String data);
    void dismissdiloag();
    void onDownLoadedItem(OneSerisesData data);
}
