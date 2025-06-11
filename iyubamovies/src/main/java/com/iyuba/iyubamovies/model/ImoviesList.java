package com.iyuba.iyubamovies.model;

import com.iyuba.iyubamovies.network.result.ImoviesListData;

/**
 * Created by iyuba on 2017/12/22.
 */

public class ImoviesList {

    private String seriesid;
    private String DescCn;
    private String Category;
    private String SeriesName;
    private String CreateTime;
    private String UpdateTime;
    private String HotFlg;
    private String pic;
    private String KeyWords;
    private String Other;
    private String Other1;
    public ImoviesList(ImoviesListData data){
        if(data!=null){
            seriesid = data.getId();
            DescCn = data.getDescCn();
            Category = data.getCategory();
            SeriesName = data.getSeriesName();
            CreateTime = data.getCreateTime();
            UpdateTime = data.getUpdateTime();
            HotFlg = data.getHotFlg();
            pic = data.getPic();
            KeyWords = data.getKeyWords();
            Other = "";
            Other1 = "";
        }
    }
    public ImoviesList(){

    }
    public ImoviesList(String seriesid,
                       String descCn,
                       String category,
                       String seriesName,
                       String createTime,
                       String updateTime,
                       String hotFlg,
                       String pic,
                       String keyWords,
                       String other,
                       String other1) {
        this.seriesid = seriesid;
        DescCn = descCn;
        Category = category;
        SeriesName = seriesName;
        CreateTime = createTime;
        UpdateTime = updateTime;
        HotFlg = hotFlg;
        this.pic = pic;
        KeyWords = keyWords;
        Other = other;
        Other1 = other1;
    }

    public String getSeriesid() {
        return seriesid;
    }

    public void setSeriesid(String seriesid) {
        this.seriesid = seriesid;
    }

    public String getDescCn() {
        return DescCn;
    }

    public void setDescCn(String descCn) {
        DescCn = descCn;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSeriesName() {
        return SeriesName;
    }

    public void setSeriesName(String seriesName) {
        SeriesName = seriesName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public String getHotFlg() {
        return HotFlg;
    }

    public void setHotFlg(String hotFlg) {
        HotFlg = hotFlg;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getKeyWords() {
        return KeyWords;
    }

    public void setKeyWords(String keyWords) {
        KeyWords = keyWords;
    }

    public void setOther(String other) {
        Other = other;
    }

    public String getOther() {
        return Other;
    }

    public void setOther1(String other1) {
        Other1 = other1;
    }

    public String getOther1() {
        return Other1;
    }

    @Override
    public String toString() {
        return "ImoviesList{" +
                "seriesid='" + seriesid + '\'' +
                ", DescCn='" + DescCn + '\'' +
                ", Category='" + Category + '\'' +
                ", SeriesName='" + SeriesName + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", UpdateTime='" + UpdateTime + '\'' +
                ", HotFlg='" + HotFlg + '\'' +
                ", pic='" + pic + '\'' +
                ", KeyWords='" + KeyWords + '\'' +
                '}';
    }
}
