package com.iyuba.iyubamovies.model;

import com.iyuba.iyubamovies.network.result.ImoviesOneData;

/**
 * Created by iyuba on 2018/1/15.
 */

public class OneSerisesData {
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

    private String Serisesid;
    private String Other;
    private String Other1;
    private String isDownload;
    private String isFinishDownLoad;
    public boolean ischeck = false;
    public OneSerisesData(){

    }
    public OneSerisesData(String serisesid, ImoviesOneData data){

        Type = data.getType();
        DescCn = data.getDescCn();
        Category = data.getCategory();
        Title_cn = data.getTitle_cn();
        CreateTime = data.getCreateTime();
        Title = data.getTitle();
        CategoryName = data.getCategoryName();
        Sound = data.getSound();
        Id = data.getId();
        Pic = data.getPic();
        Flag = data.getFlag();
        ReadCount = data.getReadCount();
        Serisesid = serisesid;
        Other = "";
        Other1 = "";
        isDownload = "0";
        isFinishDownLoad = "0";
    }
    public OneSerisesData(String type,
                          String descCn,
                          String category,
                          String title_cn,
                          String createTime,
                          String title,
                          String categoryName,
                          String sound,
                          String id,
                          String pic,
                          String flag,
                          String readCount,
                          String serisesid,
                          String other,
                          String other1,
                          String isDownload,
                          String isFinishDownLoad) {
        Type = type;
        DescCn = descCn;
        Category = category;
        Title_cn = title_cn;
        CreateTime = createTime;
        Title = title;
        CategoryName = categoryName;
        Sound = sound;
        Id = id;
        Pic = pic;
        Flag = flag;
        ReadCount = readCount;
        Serisesid = serisesid;
        Other = other;
        Other1 = other1;
        this.isDownload = isDownload;
        this.isFinishDownLoad = isFinishDownLoad;
    }

    public void setOther1(String other1) {
        Other1 = other1;
    }

    public String getOther1() {
        return Other1;
    }

    public void setOther(String other) {
        Other = other;
    }

    public String getOther() {
        return Other;
    }

    public void setSerisesid(String serisesid) {
        Serisesid = serisesid;
    }

    public String getSerisesid() {
        return Serisesid;
    }

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

    public void setIsDownload(String isDownload) {
        this.isDownload = isDownload;
    }

    public String getIsDownload() {
        return isDownload;
    }

    public void setIsFinishDownLoad(String isFinishDownLoad) {
        this.isFinishDownLoad = isFinishDownLoad;
    }

    public String getIsFinishDownLoad() {
        return isFinishDownLoad;
    }

    @Override
    public String toString() {
        return "OneSerisesData{" +
                "Type='" + Type + '\'' +
                ", DescCn='" + DescCn + '\'' +
                ", Category='" + Category + '\'' +
                ", Title_cn='" + Title_cn + '\'' +
                ", CreateTime='" + CreateTime + '\'' +
                ", Title='" + Title + '\'' +
                ", CategoryName='" + CategoryName + '\'' +
                ", Sound='" + Sound + '\'' +
                ", Id='" + Id + '\'' +
                ", Pic='" + Pic + '\'' +
                ", Flag='" + Flag + '\'' +
                ", ReadCount='" + ReadCount + '\'' +
                ", Serisesid='" + Serisesid + '\'' +
                ", Other='" + Other + '\'' +
                ", Other1='" + Other1 + '\'' +
                ", isFinishDownLoad='" + isFinishDownLoad + '\'' +
                ", isDownload='" + isDownload + '\'' +
                '}';
    }
}
