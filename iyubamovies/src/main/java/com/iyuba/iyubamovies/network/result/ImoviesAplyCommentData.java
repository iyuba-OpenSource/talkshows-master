package com.iyuba.iyubamovies.network.result;

/**
 * Created by iyuba on 2017/8/29.
 */

public class ImoviesAplyCommentData {

    private String ImgSrc;
    private String UserName;
    private int backId;
    private String againstCount;
    private int star;
    private int Userid;
    private String ShuoShuoType;
    private String ShuoShuo;
    private String id;
    private String vip;
    private String CreateDate;

    public ImoviesAplyCommentData(String imgSrc,
                                  int backId,
                                  String userName,
                                  int star,
                                  int userid,
                                  String againstCount,
                                  String shuoShuoType,
                                  String shuoShuo,
                                  String id,
                                  String vip,
                                  String createDate) {
        ImgSrc = imgSrc;
        UserName = userName;
        this.backId = backId;
        this.againstCount = againstCount;
        this.star = star;
        Userid = userid;
        ShuoShuoType = shuoShuoType;
        ShuoShuo = shuoShuo;
        this.id = id;
        this.vip = vip;
        CreateDate = createDate;
    }

    public void setImgSrc(String imgSrc) {
        ImgSrc = imgSrc;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setBackId(int backId) {
        this.backId = backId;
    }

    public void setAgainstCount(String againstCount) {
        this.againstCount = againstCount;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public void setUserid(int userid) {
        Userid = userid;
    }

    public void setShuoShuoType(String shuoShuoType) {
        ShuoShuoType = shuoShuoType;
    }

    public void setShuoShuo(String shuoShuo) {
        ShuoShuo = shuoShuo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getImgSrc() {
        return ImgSrc;
    }

    public String getUserName() {
        return UserName;
    }

    public int getBackId() {
        return backId;
    }

    public String getAgainstCount() {
        return againstCount;
    }

    public int getStar() {
        return star;
    }

    public int getUserid() {
        return Userid;
    }

    public String getShuoShuoType() {
        return ShuoShuoType;
    }

    public String getShuoShuo() {
        return ShuoShuo;
    }

    public String getId() {
        return id;
    }

    public String getVip() {
        return vip;
    }

    public String getCreateDate() {
        return CreateDate;
    }
}
