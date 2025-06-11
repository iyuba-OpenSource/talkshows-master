package com.iyuba.talkshow.ui.user.collect;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.model.Collect;
import com.iyuba.talkshow.databinding.ActivityCollectionBinding;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.detail.DetailActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import javax.inject.Inject;

/**
 * 文章收藏
 */
public class CollectionActivity extends BaseActivity implements CollectionMvpView {

    @Inject
    CollectionAdapter mAdapter;
    @Inject
    CollectionPresenter mPresenter;

    OnCollectionClickListener mListener = new OnCollectionClickListener() {
        @Override
        public void onCollectionClick(Collect collect) {
//            if (collect.getVoa().series()!=0){
//                Intent intent =  SeriesActivity.getIntent(CollectionActivity.this, String.valueOf(collect.getVoa().series()), String.valueOf(collect.getVoa().category()));
//                startActivity(intent);
//            }else {
            if ((collect == null) || (collect.getVoa() == null)) {
                showToastShort("请先同步课程原文，才能跳转到相应的页面。");
                return;
            }
                Intent intent = DetailActivity.buildIntentLimit(CollectionActivity.this, collect.getVoa(), false);
                startActivity(intent);
//            }

        }
    };
     ActivityCollectionBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activityComponent().inject(this);
        setSupportActionBar(binding.collectionToolbar);
        mPresenter.attachView(this);
        mAdapter.setListener(mListener);
        binding.collectionRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.collectionRecyclerView.setLayoutManager(layoutManager);
        showLoadingLayout();
        mPresenter.getCollection();
        binding.editTv.setOnClickListener(v -> onEditClick());
        binding.deleteBtn.setOnClickListener(v -> onDeleteClick());

        binding.emptyView.emptyText.setText("暂无收藏的文章");
    }

//    @OnClick(R.id.edit_tv)
    public void onEditClick() {
        if (mAdapter.getMode() == Mode.SHOW) {
            binding.editTv.setText(getString(R.string.cancel));
            mAdapter.setMode(Mode.EDIT);
            mAdapter.notifyDataSetChanged();
            binding.deleteBtn.setVisibility(View.VISIBLE);
        } else {
            binding.editTv.setText(getString(R.string.edit));
            mAdapter.setMode(Mode.SHOW);
            //这里需要清除选中的内容
            mAdapter.clearSelection();

            mAdapter.notifyDataSetChanged();
            binding.deleteBtn.setVisibility(View.GONE);
        }
    }

//    @OnClick(R.id.delete_btn)
    public void onDeleteClick() {
        List<String> selectedIds = mAdapter.getSelection();
        if (selectedIds.size() > 0) {
            showLoadingLayout();
            mPresenter.deleteCollection(selectedIds);
        } else {
            showToast(R.string.select_nothing);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void showLoadingLayout() {
        binding.loadingLayout.getRoot().setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingLayout() {
        binding.loadingLayout.getRoot().setVisibility(View.GONE);
    }

    @Override
    public void setAdapterEmpty() {
        binding.emptyView.getRoot().setVisibility(View.VISIBLE);
        binding.collectionRecyclerView.setVisibility(View.GONE);
        binding.editTv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setAdapterData(List<Collect> data) {
        if (data != null && data.size()>0) {
            binding.editTv.setVisibility(View.VISIBLE);
        }else {
            binding.editTv.setVisibility(View.INVISIBLE);
        }
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
        binding.emptyView.getRoot().setVisibility(View.GONE);
        binding.collectionRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

}
