package com.iyuba.talkshow.ui.lil.ui.newChoose.bean;

public class BookShowBean {

    private String id;
    private String name;
    private String pic;

    private String hasVideo;

    public BookShowBean(String id, String name, String pic, String hasVideo) {
        this.id = id;
        this.name = name;
        this.pic = pic;
        this.hasVideo = hasVideo;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPic() {
        return pic;
    }

    public String getHasVideo() {
        return hasVideo;
    }
}
