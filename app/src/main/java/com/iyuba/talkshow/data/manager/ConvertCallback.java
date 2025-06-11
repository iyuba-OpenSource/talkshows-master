package com.iyuba.talkshow.data.manager;

import java.io.File;

public interface ConvertCallback {
    void onSuccess(File file);

    void onFailure(Exception e);
}
