package com.iyuba.lib_common.model.remote.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Publish_marge {

    /**
     * appName : peppatalkshow
     * category : 309
     * flag : 1
     * format : json
     * idIndex : 0
     * paraId : 0
     * platform : android
     * score : 68
     * shuoshuotype : 3
     * sound : /202111/309680.mp3
     * topic : talkshow
     * username : iyuba95232050
     * voaid : 309680
     * wavList : [{"URL":"wav8/202402/talkshow/20240218/17082255237782772.mp3","beginTime":4.9,"duration":4.3,"endTime":11.9,"index":1},{"URL":"wav8/202402/talkshow/20240218/17082208867487798.mp3","beginTime":13.2,"duration":1.4,"endTime":17.9,"index":3},{"URL":"wav8/202402/talkshow/20240218/17082208280839484.mp3","beginTime":17.9,"duration":0,"endTime":20.6,"index":4}]
     */

    private String appName;
    private int category;
    private int flag;
    private String format;
    private int idIndex;
    private int paraId;
    private String platform;
    private int score;
    private int shuoshuotype;
    private String sound;
    private String topic;
    private String username;
    private int voaid;
    private List<WavListDTO> wavList;

    public Publish_marge(String appName, int category, int flag, String format, int idIndex, int paraId, String platform, int score, int shuoshuotype, String sound, String topic, String username, int voaid, List<WavListDTO> wavList) {
        this.appName = appName;
        this.category = category;
        this.flag = flag;
        this.format = format;
        this.idIndex = idIndex;
        this.paraId = paraId;
        this.platform = platform;
        this.score = score;
        this.shuoshuotype = shuoshuotype;
        this.sound = sound;
        this.topic = topic;
        this.username = username;
        this.voaid = voaid;
        this.wavList = wavList;
    }

    public String getAppName() {
        return appName;
    }

    public int getCategory() {
        return category;
    }

    public int getFlag() {
        return flag;
    }

    public String getFormat() {
        return format;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public int getParaId() {
        return paraId;
    }

    public String getPlatform() {
        return platform;
    }

    public int getScore() {
        return score;
    }

    public int getShuoshuotype() {
        return shuoshuotype;
    }

    public String getSound() {
        return sound;
    }

    public String getTopic() {
        return topic;
    }

    public String getUsername() {
        return username;
    }

    public int getVoaid() {
        return voaid;
    }

    public List<WavListDTO> getWavList() {
        return wavList;
    }

    public static class WavListDTO {
        /**
         * URL : wav8/202402/talkshow/20240218/17082255237782772.mp3
         * beginTime : 4.9
         * duration : 4.3
         * endTime : 11.9
         * index : 1
         */

        @SerializedName("URL")
        private String url;
        private double beginTime;
        private double duration;
        private double endTime;
        private int index;

        public WavListDTO(String url, Double beginTime, Double duration, Double endTime, Integer index) {
            this.url = url;
            this.beginTime = beginTime;
            this.duration = duration;
            this.endTime = endTime;
            this.index = index;
        }

        public String getUrl() {
            return url;
        }

        public double getBeginTime() {
            return beginTime;
        }

        public double getDuration() {
            return duration;
        }

        public double getEndTime() {
            return endTime;
        }

        public int getIndex() {
            return index;
        }
    }
}
