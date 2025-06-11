package com.iyuba.talkshow.ui.dubbing.dialog;

import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.injection.PerDialog;
import com.iyuba.talkshow.ui.base.BasePresenter;

import javax.inject.Inject;

@PerDialog
public class DubbingDialogPresenter extends BasePresenter<DubbingDialogMvpView> {

    private Voa mVoa;
    private String mTimeStamp;

    @Inject
    public DubbingDialogPresenter() {}

    public void saveDraft() {

    }

    public void clearFiles() {

    }

    public void setVoa(Voa mVoa) {
        this.mVoa = mVoa;
    }

    public void setTimeStamp(String timeStamp) {
        this.mTimeStamp = timeStamp;
    }
}
