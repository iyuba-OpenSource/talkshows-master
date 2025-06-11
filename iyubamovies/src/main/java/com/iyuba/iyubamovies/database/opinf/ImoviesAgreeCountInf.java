package com.iyuba.iyubamovies.database.opinf;

/**
 * Created by iyuba on 2018/1/22.
 */

public interface ImoviesAgreeCountInf {
    String TABLE_NAME = "imoviesagreecount";
    String USERID = "userid";
    String CID = "cid";
    boolean isClickZAN(String uid, String id);
    void savaclickZan(String uid,String id);
}
