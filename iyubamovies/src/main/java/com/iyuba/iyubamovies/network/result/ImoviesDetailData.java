package com.iyuba.iyubamovies.network.result;

import androidx.annotation.Keep;

/**
 * 作者：renzhy on 17/2/21 15:26
 * 邮箱：renzhongyigoo@gmail.com
 */
@Keep
public class ImoviesDetailData {

    /**
     * EndTiming : 293.7
     * ParaId : 20
     * IdIndex : 1
     * Sentence_cn : 我是Dan Friedell。
     * Timing : 291.8
     * Sentence : I’m Dan Friedell.
     */

    private String EndTiming;
    private String ParaId;
    private String IdIndex;
    private String Sentence_cn;
    private String Timing;
    private String Sentence;

    public String getEndTiming() {
        return EndTiming;
    }

    public void setEndTiming(String EndTiming) {
        this.EndTiming = EndTiming;
    }

    public String getParaId() {
        return ParaId;
    }

    public void setParaId(String ParaId) {
        this.ParaId = ParaId;
    }

    public String getIdIndex() {
        return IdIndex;
    }

    public void setIdIndex(String IdIndex) {
        this.IdIndex = IdIndex;
    }

    public String getSentence_cn() {
        return Sentence_cn;
    }

    public void setSentence_cn(String Sentence_cn) {
        this.Sentence_cn = Sentence_cn;
    }

    public String getTiming() {
        return Timing;
    }

    public void setTiming(String Timing) {
        this.Timing = Timing;
    }

    public String getSentence() {
        return Sentence;
    }

    public void setSentence(String Sentence) {
        this.Sentence = Sentence;
    }

    @Override
    public String toString() {
        return "ImoviesDetailData{" +
                "EndTiming='" + EndTiming + '\'' +
                ", ParaId='" + ParaId + '\'' +
                ", IdIndex='" + IdIndex + '\'' +
                ", Sentence_cn='" + Sentence_cn + '\'' +
                ", Timing='" + Timing + '\'' +
                ", Sentence='" + Sentence + '\'' +
                '}';
    }
}
