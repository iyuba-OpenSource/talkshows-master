package com.iyuba.iyubamovies.network.result;

/**
 * Created by iyuba on 2017/12/22.
 */

public class ImoviesListData {
    /**
     * DescCn : The Big Bang Theory season 10
     * Category : 606
     * SeriesName : 生活大爆炸第十季
     * CreateTime : 2017-11-23 03:11:06.0
     * UpdateTime : 2017-11-23 03:11:27.0
     * HotFlg : 0
     * Id : 10
     * pic : http://apps."+com.iyuba.talkshow.Constant.Web.WEB_SUFFIX+"iyuba/images/voaseries/10.jpg
     * KeyWords : 喜剧,幽默,情景剧,美剧
     */

    private String DescCn;
    private String Category;
    private String SeriesName;
    private String CreateTime;
    private String UpdateTime;
    private String HotFlg;
    private String Id;
    private String pic;
    private String KeyWords;

    public String getDescCn() {
        return DescCn;
    }

    public void setDescCn(String DescCn) {
        this.DescCn = DescCn;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public String getSeriesName() {
        return SeriesName;
    }

    public void setSeriesName(String SeriesName) {
        this.SeriesName = SeriesName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public String getHotFlg() {
        return HotFlg;
    }

    public void setHotFlg(String HotFlg) {
        this.HotFlg = HotFlg;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
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

    public void setKeyWords(String KeyWords) {
        this.KeyWords = KeyWords;
    }
}