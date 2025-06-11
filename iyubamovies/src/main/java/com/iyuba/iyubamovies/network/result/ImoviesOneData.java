package com.iyuba.iyubamovies.network.result;

/**
 * Created by iyuba on 2017/12/23.
 */

public class ImoviesOneData {
    /**
     * Type : series
     * DescCn : 夫妻之间的猜疑：在莱纳德的父亲阿弗里德与谢尔顿的母亲玛丽共度一夜后，大家不得不第二天早上处理这一尴尬的局面。而佩妮的家人到达了典礼现场，包括她焦虑不已的妈妈苏珊和她从事毒品交易的哥哥兰德尔。
     * Category : 606
     * Title_cn : 夫妻之间的猜疑-生活大爆炸第十季第1集
     * CreateTime : 2017-11-23 18:55:08.0
     * Title : The Conjugal Conjecture-The Big Bang Theory (season 10) No.1
     * CategoryName : 喜剧
     * Sound : http://staticvip."+com.iyuba.talkshow.Constant.Web.WEB_SUFFIX+"sounds/voa/201711/606001.mp3
     * Id : 606001
     * Pic : http://static."+com.iyuba.talkshow.Constant.Web.WEB_SUFFIX+"images/voa/606001.jpg
     * Flag : 1
     * ReadCount : 8
     */

    private String Type;
    private String DescCn;
    private String Category;
    private String Title_cn;
    private String CreateTime;
    private String Title;
    private String CategoryName;
    private String Sound;
    private String Id;
    private String Pic;
    private String Flag;
    private String ReadCount;

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

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

    public String getTitle_cn() {
        return Title_cn;
    }

    public void setTitle_cn(String Title_cn) {
        this.Title_cn = Title_cn;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public String getSound() {
        return Sound;
    }

    public void setSound(String Sound) {
        this.Sound = Sound;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String Pic) {
        this.Pic = Pic;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String Flag) {
        this.Flag = Flag;
    }

    public String getReadCount() {
        return ReadCount;
    }

    public void setReadCount(String ReadCount) {
        this.ReadCount = ReadCount;
    }
}
