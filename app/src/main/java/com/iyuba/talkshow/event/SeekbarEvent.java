package com.iyuba.talkshow.event;

public class SeekbarEvent {

    private long progress;
    private long duration;

    public SeekbarEvent(long progress, long duration) {
        this.progress = progress;
        this.duration = duration;
    }

    public long getProgress() {
        return progress;
    }

    public long getDuration() {
        return duration;
    }
}
