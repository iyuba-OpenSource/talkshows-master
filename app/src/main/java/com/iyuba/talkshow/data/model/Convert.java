package com.iyuba.talkshow.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.iyuba.talkshow.data.manager.ConvertCallback;

@AutoValue
public abstract class Convert {
    public abstract int voaId();
    public abstract int paraId();
    public abstract long timestamp();
    public abstract String filename();
    public abstract ConvertCallback callback();

    public static Builder builder() {
        return new AutoValue_Convert.Builder();
    }

    public static TypeAdapter<Download> typeAdapter(Gson gson) {
        return new AutoValue_Download.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setVoaId(int voaId);
        public abstract Builder setParaId(int paraId);
        public abstract Builder setTimestamp(long timestamp);
        public abstract Builder setFilename(String filename);
        public abstract Builder setCallback(ConvertCallback callback);
        public abstract Convert build();
    }
}
