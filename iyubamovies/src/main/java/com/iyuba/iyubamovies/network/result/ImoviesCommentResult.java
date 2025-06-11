package com.iyuba.iyubamovies.network.result;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by iyuba on 2017/8/29.
 */

public class ImoviesCommentResult {

    public String ResultCode;
    public int ShuoShuoId;
    public int AddScore;
    //public String FilePath;
    public String Message;
    public int PageNumber;
    public int TotalPage;
    public int FirstPage;
    public int PrevPage;
    public int NextPage;
    public int LastPage;
    public int starCounts;
    public int Counts;
//    @Keep
//    @SerializedName("Self")
//    public List<?> Self;
    @Keep
    @SerializedName("data")
    public List<ImoviesCommentData> data;


}
