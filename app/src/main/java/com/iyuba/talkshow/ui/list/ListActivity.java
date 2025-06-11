package com.iyuba.talkshow.ui.list;

import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.iyuba.ad.adblocker.AdBlocker;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.model.Category;
import com.iyuba.talkshow.data.model.Level;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.databinding.ActivityListBinding;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.detail.DetailActivity;
import com.iyuba.talkshow.ui.widget.LoadingDialog;
import com.iyuba.talkshow.ui.widget.divider.ListGridItemDivider;
import com.iyuba.talkshow.util.AdInfoFlowUtil;
import com.iyuba.talkshow.util.DialogFactory;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import javax.inject.Inject;


public class ListActivity extends BaseActivity implements ListMvpView {


    ActivityListBinding binding;
    public static final String VOA_CATEGORY = "VOA_CATEGORY";
    public static final int SPAN_COUNT = 2;
    public static final int PAGE_SIZE = 17;

    @Inject
    ListPresenter mListPresenter;
    @Inject
    ListAdapter mListAdapter;


    AdInfoFlowUtil adInfoFlowUtil;

    private LoadingDialog mLoadingDialog;
    private int pageNum = 1;

    private ListAdapter.SelectorCallBack selectorCallback = new ListAdapter.SelectorCallBack() {
        @Override
        public void onSelectorClicked(int category, String level) {
            pageNum = 1;
            mListPresenter.loadNewVoas(category, level, pageNum, PAGE_SIZE);
            if (adInfoFlowUtil != null) {
                adInfoFlowUtil.reset();
            }
        }
    };

    private ListAdapter.VoaCallback voaCallback = new ListAdapter.VoaCallback() {
        @Override
        public void onVoaClicked(Voa voa) {
            if (voa != null) {
                Log.e("listAdapter.voacallback", "执行了！");
                startActivity(DetailActivity.buildIntentLimit(ListActivity.this, voa, false));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarLayout.listToolbar);
        mListPresenter.attachView(this);

        mLoadingDialog = new LoadingDialog(this);

        int category = getIntent().getIntExtra(VOA_CATEGORY, Category.Value.ALL);
        mListAdapter.setCategoryValue(category);
        mListAdapter.setLevelValue(Level.Value.ALL);
        mListAdapter.setSelectorCallBack(selectorCallback);
        mListAdapter.setVoaCallback(voaCallback);

        binding.swipeLayout.setRefreshHeader(new ClassicsHeader(this).setSpinnerStyle(SpinnerStyle.Translate));
        binding.swipeLayout.setRefreshFooter(new ClassicsFooter(mContext));
        binding.swipeLayout.setOnRefreshListener(refreshLayout -> {
            mListPresenter.loadVoas();
            if (adInfoFlowUtil!=null){
                adInfoFlowUtil.reset();
            }
        });
        binding.swipeLayout.setOnLoadMoreListener(refreshlayout -> {
            pageNum++;
            int category1 = mListAdapter.getCategoryValue();
            String level = mListAdapter.getLevelValue();
            mListPresenter.loadMoreVoas(category1, level, pageNum, PAGE_SIZE);
        });
        mListPresenter.loadNewVoas(category, Level.Value.ALL, pageNum, PAGE_SIZE);

        binding.recyclerView.setAdapter(mListAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mListAdapter.getSpanSize(position);
            }
        });
        binding.recyclerView.addItemDecoration(new ListGridItemDivider(this));
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。

        try {
            //屏蔽广告
            if (!AdBlocker.getInstance().shouldBlockAd()){
                adInfoFlowUtil = new AdInfoFlowUtil(mContext, UserInfoManager.getInstance().isVip(), new AdInfoFlowUtil.Callback() {
                    @Override
                    public void onADLoad(List ads) {
                        AdInfoFlowUtil.insertAD4(mListAdapter.getData(), ads, adInfoFlowUtil);
                        mListAdapter.updateItemCount();
                        mListAdapter.notifyDataSetChanged();
                    }
                });
            }
        } catch (Exception var2) { }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListPresenter.detachView();
    }

    @Override
    public void showVoas(List<Voa> voas) {
        dismissRefreshingView();
        mListAdapter.setVoaList(voas);
        mListAdapter.notifyDataSetChanged();
        if (adInfoFlowUtil != null) {
            adInfoFlowUtil.refreshAd();
        }
    }

    @Override
    public void showMoreVoas(List<Voa> voas) {
        dismissRefreshingView();
        mListAdapter.addVoaList(voas);
        mListAdapter.notifyDataSetChanged();
        if (adInfoFlowUtil != null) {
            adInfoFlowUtil.refreshAd();
        }
    }

    @Override
    public void showVoasEmpty() {
        dismissRefreshingView();
        mListAdapter.setEmptyVoaList();
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading)).show();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingDialog() {
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void dismissRefreshingView() {
//        mSwipeRefreshLayout.setRefreshing(false);
        binding.swipeLayout.finishRefresh();
        binding.swipeLayout.finishLoadMore();
    }
}
