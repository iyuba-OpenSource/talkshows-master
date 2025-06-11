package com.iyuba.iyubamovies.model;

import androidx.annotation.Keep;

/**
 * 作者：renzhy on 17/3/10 13:28
 * 邮箱：renzhongyigoo@gmail.com
 */
@Keep
public class ImoviesStudyRecord {
    public String lesson;
    public String voaid;
    public String starttime;
    public String endtime;
    public String testmode;
    public String flag;

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getVoaid() {
        return voaid;
    }

    public void setVoaid(String voaid) {
        this.voaid = voaid;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getTestmode() {
        return testmode;
    }

    public void setTestmode(String testmode) {
        this.testmode = testmode;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
