package com.iyuba.iyubamovies.network.result;

import com.iyuba.dlex.bizs.DLTaskInfo;
import com.iyuba.iyubamovies.manager.IMoviesApp;
import com.iyuba.iyubamovies.model.OneSerisesData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iyuba on 2017/12/23.
 */

public class ImoviesByOneResult {


    /**
     * result : 1
     * total : 2
     * data : [
     * {"Type":"series",
     * "DescCn":"夫妻之间的猜疑：在莱纳德的父亲阿弗里德与谢尔顿的母亲玛丽共度一夜后，
     * 大家不得不第二天早上处理这一尴尬的局面。而佩妮的家人到达了典礼现场，
     * 包括她焦虑不已的妈妈苏珊和她从事毒品交易的哥哥兰德尔。",
     * "Category":"606",
     * "Title_cn":"夫妻之间的猜疑-生活大爆炸第十季第1集",
     * "CreateTime":"2017-11-23 18:55:08.0",
     * "Title":"The Conjugal Conjecture-The Big Bang Theory (season 10) No.1",
     * "CategoryName":"喜剧",
     * "Sound":"http://staticvip."+com.iyuba.talkshow.Constant.Web.WEB_SUFFIX+"sounds/voa/201711/606001.mp3",
     * "Id":"606001","Pic":"http://static."+com.iyuba.talkshow.Constant.Web.WEB_SUFFIX+"images/voa/606001.jpg",
     * "Flag":"1","ReadCount":"8"},
     * {"Type":"series","DescCn":"财产分割冲突：谢尔顿和莱纳德必须划分他们的共同财产，然后把佩妮的东西从4B号房间搬过来。",
     * "Category":"606","Title_cn":"财产分割冲突-生活大爆炸第十季第10集",
     * "CreateTime":"2017-11-23 19:22:50.0",
     * "Title":"The Property Division Collision-The Big Bang Theory (season 10) No.10",
     * "CategoryName":"喜剧",
     * "Sound":"http://staticvip."+com.iyuba.talkshow.Constant.Web.WEB_SUFFIX+"sounds/voa/201711/606010.mp3",
     * "Id":"606010",
     * "Pic":"http://static."+com.iyuba.talkshow.Constant.Web.WEB_SUFFIX+"images/voa/606010.jpg",
     * "Flag":"1",
     * "ReadCount":"0"}]
     * message : success
     */

    private int result;
    private int total;
    private String message;
    private List<ImoviesOneData> data;

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

    public List<ImoviesOneData> getData() {
        return data;
    }

    public void setData(List<ImoviesOneData> data) {
        this.data = data;
    }

    public List<OneSerisesData> convertToDatabaseData(String serisesid){
        if(data!=null)
        {
            List<OneSerisesData> serisesDatas = new ArrayList<>();
            for(ImoviesOneData idata:data){
                DLTaskInfo info = IMoviesApp.getDlManager().getDLTaskInfo("imovies" + idata.getId() + serisesid + idata.getType());
                OneSerisesData datas = new OneSerisesData(serisesid,idata);
                if(info!=null){
                    datas.setIsDownload("1");
                    if(info.state== DLTaskInfo.TaskState.COMPLETED){
                        datas.setIsFinishDownLoad("1");
                    }
                }
                serisesDatas.add(datas);
            }
            return serisesDatas;
        }
        return null;
    }

    @Override
    public String toString() {
        return "ImoviesByOneResult{" +
                "result=" + result +
                ", total=" + total +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
