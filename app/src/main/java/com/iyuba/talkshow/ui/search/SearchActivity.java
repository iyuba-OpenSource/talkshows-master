package com.iyuba.talkshow.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.databinding.ActivitySearchBinding;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.detail.DetailActivity;
import com.iyuba.talkshow.ui.widget.LoadingDialog;
import com.iyuba.talkshow.ui.widget.divider.SearchListGridItemDivider;
import com.iyuba.talkshow.util.AdInfoFlowUtil;
import com.iyuba.talkshow.util.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

import javax.inject.Inject;


/**
 * SearchActivity
 *
 * @author wayne
 * @date 2018/2/10
 */
public class SearchActivity extends BaseActivity implements SearchMvpView {
    LoadingDialog mLoadingDialog;

    @Inject
    SearchPresenter mPresenter;
    @Inject
    SearchListAdapter mListAdapter;

    AdInfoFlowUtil adInfoFlowUtil;

    //是否加载更多数据
    private boolean isLoadMore = false;

    public static void start(Context context,String keyWord){
        Intent intent = new Intent();
        intent.setClass(context,SearchActivity.class);
        intent.putExtra(StrLibrary.word,keyWord);
        context.startActivity(intent);
    }

    private SearchListAdapter.VoaCallback voaCallback = new SearchListAdapter.VoaCallback() {
        @Override
        public void onVoaClicked(Voa voa) {
            if (voa != null) {
                startActivity(DetailActivity.buildIntentLimit(mContext, voa, false));
            }
        }
    };
    ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.listToolbar);
        mPresenter.attachView(this);

        mLoadingDialog = new LoadingDialog(mContext);

        binding.listToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this).setSpinnerStyle(SpinnerStyle.Translate));
        binding.smartRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));

        binding.smartRefreshLayout.setEnableRefresh(false);
        if (App.APP_ID == 280) {
            binding.smartRefreshLayout.setEnableLoadMore(false);
        }
        binding.smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                isLoadMore = true;
                mPresenter.loadMore();
            }
        });

        binding.searchRecyclerView.addItemDecoration(new SearchListGridItemDivider(this));
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mListAdapter.getSpanSize(position);
            }
        });
        binding.searchRecyclerView.setLayoutManager(layoutManager);
        binding.searchRecyclerView.setHasFixedSize(true);
        binding.searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mListAdapter.setVoaCallback(voaCallback);
        binding.searchRecyclerView.setAdapter(mListAdapter);

        try {
            //屏蔽广告
            if (!AdBlocker.getInstance().shouldBlockAd()){
                adInfoFlowUtil = new AdInfoFlowUtil(mContext, UserInfoManager.getInstance().isVip()||App.Apk.isChild(), new AdInfoFlowUtil.Callback() {
                    @Override
                    public void onADLoad(List ads) {
                        AdInfoFlowUtil.insertAD4(mListAdapter.getData(), ads, adInfoFlowUtil);
                        mListAdapter.notifyDataSetChanged();
                    }
                });
            }
        } catch (Exception var3) { }

        binding.etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.e("SearchActivity", "onKey keyCode " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    if (!TextUtils.isEmpty(binding.etSearch.getText().toString().trim())) {
                        isLoadMore = false;
                        search();
                    }
                }
                return false;
            }
        });
        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.etSearch.getText().toString().trim())) {
                    showToastShort("请输入关键字才能搜索");
                    return;
                }
                //隐藏键盘
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                isLoadMore = false;
                search();
            }
        });


        //这里增加新的处理逻辑，处理外面传过来的数据
        String keyWord = getIntent().getStringExtra(StrLibrary.word);
        if (!TextUtils.isEmpty(keyWord)){
            binding.etSearch.setText(keyWord);
            isLoadMore = false;
            search();
        }
    }

//    @OnClick(R.id.iv_search)
    public void search() {
        mListAdapter.getData().clear();
        mListAdapter.notifyDataSetChanged();
        mPresenter.search(binding.etSearch.getText().toString().trim());
    }

    @Override
    public void showVoas(List<Voa> list) {
        mListAdapter.addVoaList(list);
        if (adInfoFlowUtil != null) {
            adInfoFlowUtil.refreshAd();
        }
    }


    @Override
    public void dismissLoading() {
        mLoadingDialog.dismiss();
        binding.smartRefreshLayout.finishLoadMore();
    }

    @Override
    public void showLoading() {
        mLoadingDialog.show();
        binding.smartRefreshLayout.finishLoadMore();
    }

    @Override
    public void showEmptyVoas() {
        if (isLoadMore){
            ToastUtil.showToast(this,"暂无更多数据");
        }else {
            mListAdapter.setEmptyVoaList();
        }
    }

    @Override
    public void resetAd() {
        if (adInfoFlowUtil != null) {
            adInfoFlowUtil.reset();
        }
    }
}
