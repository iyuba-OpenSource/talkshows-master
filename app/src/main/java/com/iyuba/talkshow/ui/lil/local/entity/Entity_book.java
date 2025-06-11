package com.iyuba.talkshow.ui.lil.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(primaryKeys = {"typeId","bookId"})
public class Entity_book {
    @NonNull
    public String typeId;//类型id
    @NonNull
    public String bookId;//书籍id

    public String createTime;
    public String isVideo;
    public String pic;
    public String keyWords;
    public String version;
    public String descCn;
    public String seriesCount;
    public String seriesName;
    public String updateTime;
    public String hotFlg;
    public String haveMicro;

    public Entity_book() {
    }

    @Ignore
    public Entity_book(@NonNull String typeId, @NonNull String bookId, String createTime, String isVideo, String pic, String keyWords, String version, String descCn, String seriesCount, String seriesName, String updateTime, String hotFlg, String haveMicro) {
        this.typeId = typeId;
        this.bookId = bookId;
        this.createTime = createTime;
        this.isVideo = isVideo;
        this.pic = pic;
        this.keyWords = keyWords;
        this.version = version;
        this.descCn = descCn;
        this.seriesCount = seriesCount;
        this.seriesName = seriesName;
        this.updateTime = updateTime;
        this.hotFlg = hotFlg;
        this.haveMicro = haveMicro;
    }
}
