package com.iyuba.iyubamovies.network.result;

import java.util.List;

/**
 * Created by iyuba on 2017/12/22.
 */

public class ImoviesListResult {

    /**
     * result : 1
     * total : 1
     * data : [{"DescCn":"The Big Bang Theory season 10",
     * "Category":"606",
     * "SeriesName":"生活大爆炸第十季",
     * "CreateTime":"2017-11-23 03:11:06.0",
     * "UpdateTime":"2017-11-23 03:11:27.0",
     * "HotFlg":"0",
     * "Id":"10",
     * "pic":"http://apps."+com.iyuba.talkshow.Constant.Web.WEB_SUFFIX+"iyuba/images/voaseries/10.jpg",
     * "KeyWords":"喜剧,幽默,情景剧,美剧"}
     * ]
     * message : success
     */

    private int result;
    private int total;
    private String message;
    private List<ImoviesListData> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ImoviesListData> getData() {
        return data;
    }

    public void setData(List<ImoviesListData> data) {
        this.data = data;
    }


}
