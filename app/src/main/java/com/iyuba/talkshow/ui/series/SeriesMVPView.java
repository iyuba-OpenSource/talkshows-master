package com.iyuba.talkshow.ui.series;

import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.ui.base.MvpView;

import java.util.List;

public interface SeriesMVPView extends MvpView {

    void dismissLoading();

    void showLoading();

    void setVoas(List<Voa> voa ,String key );

    void setChoise(List<SeriesData> list);
}
