package com.iyuba.talkshow.data.model;

import android.os.Parcelable;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/11/10 0010.
 */
@AutoValue
public abstract class Voa implements Parcelable, RecyclerItem {
    @SerializedName("IntroDesc")
    @Nullable public abstract String introDesc();
    @SerializedName("CreatTime")
    public abstract String createTime();
    @SerializedName("Category")
    public abstract int category();
    @SerializedName("Keyword")
    @Nullable public abstract String keyword();
    @SerializedName("Title")
    public abstract String title();
    @SerializedName("Sound")
    public abstract String sound();
    @SerializedName("Pic")
    public abstract String pic();
    @SerializedName("VoaId")
    public abstract int voaId();
    @SerializedName("Pagetitle")
    @Nullable public abstract String pageTitle();
    @SerializedName("Url")
    public abstract String url();
    @SerializedName("DescCn")
    public abstract String descCn();
    @SerializedName("Title_cn")
    public abstract String titleCn();
    @SerializedName("PublishTime")
    public abstract String publishTime();
    @SerializedName("HotFlg")
    public abstract int hotFlag();
    @SerializedName("ReadCount")
    public abstract int readCount();
    @SerializedName("Series")
    public abstract int series();

    // TODO: 2022/7/14 增加video参数，视频链接变为前缀+video
    @SerializedName("Video")
    @Nullable public abstract String video();

    public static Builder builder() {
        return new AutoValue_Voa.Builder();
    }

    public static TypeAdapter<Voa> typeAdapter(Gson gson) {
        return new AutoValue_Voa.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setIntroDesc(String introDesc);
        public abstract Builder setCreateTime(String createTime);
        public abstract Builder setCategory(int category);
        public abstract Builder setKeyword(String keyword);
        public abstract Builder setTitle(String title);
        public abstract Builder setSound(String sound);
        public abstract Builder setPic(String title);
        public abstract Builder setVoaId(int voaId);
        public abstract Builder setPageTitle(String pageTitle);
        public abstract Builder setUrl(String url);
        public abstract Builder setDescCn(String descCn);
        public abstract Builder setTitleCn(String titleCn);
        public abstract Builder setPublishTime(String publishTime);
        public abstract Builder setHotFlag(int hotFlag);
        public abstract Builder setReadCount(int readCount);
        public abstract Builder setSeries(int series);
        //增加video参数
        public abstract Builder setVideo(String video);
        public abstract Voa build();

    }
}
