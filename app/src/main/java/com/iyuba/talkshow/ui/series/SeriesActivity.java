package com.iyuba.talkshow.ui.series;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.iyuba.lib_common.util.LibGlide3Util;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.databinding.ActivitySeriesBinding;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.detail.DetailActivity;
import com.iyuba.talkshow.ui.widget.LoadingDialog;
import com.iyuba.talkshow.ui.widget.divider.SeriesGridItemDivider;
import com.iyuba.talkshow.util.RxUtil;
import com.iyuba.talkshow.util.ToastUtil;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SeriesActivity extends BaseActivity implements SeriesMVPView{

    ActivitySeriesBinding binding ;
    LoadingDialog mLoadingDialog;

    public static Intent getIntent(Context contenxt , String series , String cat){
        Intent intent = new Intent(contenxt , SeriesActivity.class);
        intent.putExtra("series", series);
        intent.putExtra("cat", cat);
        return intent;
    }

    SeriesSimpleAdapter mAdapter;

    @Inject
    SeriesPresenter mPresenter;

    @Inject
    DataManager dataManager;
    private Subscription mSeries;

    private String seriesId ;
    private String catId ;
    public SeriesActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        binding = ActivitySeriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mLoadingDialog = new LoadingDialog(this);

//        setSupportActionBar(mToolBar);

        seriesId = getIntent().getStringExtra("series");
        catId = getIntent().getStringExtra("cat");
        mPresenter.attachView(this);

    }

    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        binding.back.setOnClickListener(cl -> back());
        binding.changeBook.setOnClickListener(cl -> onChangeBookClick());
        binding.recycler.addItemDecoration(new SeriesGridItemDivider(this));
        binding.recycler.setLayoutManager(layoutManager);
        mAdapter = new SeriesSimpleAdapter();
        binding.recycler.setAdapter(mAdapter);
        mAdapter.setVoaCallback(mVoaCallBack);
        binding.containerLayout.setRefreshHeader((new ClassicsHeader(this)).setSpinnerStyle(SpinnerStyle.Translate));
        binding.containerLayout.setEnableLoadMore(false);
        binding.containerLayout.setEnableRefresh(false);
//        mRefreshContainer.setOnRefreshListener(new OnRefreshListener() {
//            public void onRefresh(RefreshLayout refreshlayout) {
////                mPresenter.getData(MovieActivity.checkNeedReplaceData());
//            }
//        });
        getSeries4Cover(seriesId);
        mPresenter.getSeries(seriesId, catId);
        if (App.APP_ID < 280) {
            binding.changeBook.setVisibility(View.GONE);
        }
    }

    private SeriesSimpleAdapter.VoaCallback mVoaCallBack = new SeriesSimpleAdapter.VoaCallback() {
        @Override
        public void onVoaClick(Voa voa) {
            //这里要求进入下一个界面后，可以连续播放
            //目前采用的方案为将所有的数据传递过去，然后进行数据的处理
            startActivity(DetailActivity.buildIntentAutoPlay(mContext, mAdapter.getVoas(), voa,true));
        }
    };

    private void getSeries4Cover(String seriesId) {
        Log.e("SeriesActivity", "getSeries4Cover seriesId " + seriesId);
        RxUtil.unsubscribe(mSeries);
        mSeries = dataManager.getSeriesById(seriesId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SeriesData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(SeriesData bean) {
                        if ((bean == null) || TextUtils.isEmpty(bean.getId())) {
                            return;
                        }
                        LibGlide3Util.loadImg(mContext,bean.getPic(),R.drawable.pig_default,binding.seriesImg);
                        binding.title.setText(bean.getSeriesName());
                    }
                });
    }

    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        RxUtil.unsubscribe(mSeries);
    }

    public void showMessage(String message) {
//        Toast.makeText(this, message, 0).show();
    }

    public void onDataLoaded(List<Voa> data) {
//        mAdapter.setData(data);
    }

    public void onRefreshComplete() {
        binding.containerLayout.finishRefresh();
    }

    @Override
    public void showToastShort(int resId) {
        ToastUtil.show(mContext, getResources().getString(resId));
    }

    @Override
    public void showToastShort(String message) {
        ToastUtil.show(mContext, message);

    }

    @Override
    public void showToastLong(int resId) {
        ToastUtil.show(mContext, getResources().getString(resId));
    }

    @Override
    public void showToastLong(String message) {
        ToastUtil.show(mContext, message);

    }

    public void startDetailActivity(Voa voa) {
        if (voa != null) {
                startActivity(DetailActivity.buildIntentLimit(this, voa, true));
        }
    }

    @Override
    public void dismissLoading() {
        mLoadingDialog.dismiss();
//        smartRefreshLayout.finishLoadmore();
    }

    @Override
    public void showLoading() {
        mLoadingDialog.show();
    }

    @Override
    public void setVoas(List<Voa> voa,String key ) {
        if ((voa == null) || voa.isEmpty()) {
            showToastShort("暂时没有相应的数据");
            mAdapter.setVoas(new ArrayList<>());
            mAdapter.notifyDataSetChanged();
            return;
        }
        if (App.APP_ID < 280) {
            LibGlide3Util.loadImg(mContext,voa.get(0).pic(),R.drawable.pig_default,binding.seriesImg);
        }
//        voa = removeDuplicateUser(voa);
        mAdapter.setVoas(voa);
//        tv.setText(voa.get(0).pageTitle());
//        binding.tvDesc.setMaxLines(2);
//        binding.tvDesc.initWidth(binding.tvDesc.getWidth());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setChoise(List<SeriesData> list) {
        showChangeBookDialog(list);
    }

//    @OnClick(R.id.change_book)
    public void onChangeBookClick() {
        mPresenter.getSeriesList(catId);
    }

    private void showChangeBookDialog(List<SeriesData> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        List<String> choiceList = new ArrayList<>();
        for (SeriesData series:list) {
            choiceList.add(series.getSeriesName());
        }
        String[] choices = new String [list.size()] ;
        choiceList.toArray(choices);
        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                refreshContent(list.get(which).getId());
            }
        }).show();
    }

    private void refreshContent(String series) {
        seriesId = series;
        getSeries4Cover(seriesId);
        mPresenter.getSeries(series, catId);
    }

    private static ArrayList<Voa> removeDuplicateUser(List<Voa> users) {
        Set<Voa> set = new TreeSet<Voa>(new Comparator<Voa>() {
            @Override
            public int compare(Voa o1, Voa o2) {
                //字符串,则按照asicc码升序排列
                return (o1.voaId()+"").compareTo(o2.voaId()+"");
            }
        });
        set.addAll(users);
        return new ArrayList<Voa>(set);
    }


//    @OnClick(R.id.back)
    public void back(){
        finish();
    }

}
