package com.iyuba.iyubamovies.network.result;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者：renzhy on 17/2/21 15:26
 * 邮箱：renzhongyigoo@gmail.com
 */
@Keep
public class ImoviesDetailResult {
    public String result;

    @Keep
    @SerializedName("data")
    public List<ImoviesDetailData> Details;
}
