package com.iyuba.talkshow.ui.user.me.dubbing.draft;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.model.Record;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.databinding.FragmentDraftBinding;
import com.iyuba.talkshow.ui.base.BaseViewBindingFragmet;
import com.iyuba.talkshow.ui.lil.ui.dubbing.DubbingNewActivity;
import com.iyuba.talkshow.ui.user.me.dubbing.Editable;
import com.iyuba.talkshow.ui.user.me.dubbing.Mode;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import javax.inject.Inject;


public class DraftFragment extends BaseViewBindingFragmet<FragmentDraftBinding> implements DraftMvpView, Editable{
    private static final String TAG = DraftFragment.class.getSimpleName();


    @Inject
    DraftPresenter mPresenter;
    @Inject
    DraftAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
        mPresenter.attachView(this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    OnDraftClickListener mListener = new OnDraftClickListener() {
        @Override
        public void onDraftClick(Record record) {
            mPresenter.getVoa(record.voaId(), record.timestamp());
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.setMode(Mode.SHOW);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();

    }

    @Override
    public void setDraftData(List<Record> mData) {
        mAdapter.setData(mData);
        mAdapter.notifyDataSetChanged();
        getBinding().draftRecyclerView.setVisibility(View.VISIBLE);
        getBinding().emptyView.getRoot().setVisibility(View.GONE);
    }

    @Override
    public void setEmptyData() {
        getBinding().draftRecyclerView.setVisibility(View.GONE);
        getBinding().emptyView.getRoot().setVisibility(View.VISIBLE);
    }

    @Override
    public void startDubbingActivity(Voa voa, long timestamp) {
//        Intent intent = DubbingActivity.buildIntent(getContext(), voa, timestamp);
//        startActivity(intent);

        DubbingNewActivity.start(getActivity(),voa,voa.sound(),voa.video());
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingLayout() {
        getBinding().loadingLayout.getRoot().setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingLayout() {
        getBinding().loadingLayout.getRoot().setVisibility(View.GONE);
    }

    @Override
    public int getMode() {
        return mAdapter.getMode();
    }

    @Override
    public void setMode(int mode) {
        mAdapter.setMode(mode);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteCollection() {
        List<String> selection = mAdapter.getSelectedData();
        if(selection.size() > 0) {
            mPresenter.deleteDraftData(selection);
        } else {
            showToast(R.string.select_nothing);
        }
    }

    @Override
    public int getDataSize() {
        return mAdapter.getItemCount();
    }

    @Override
    public void addAllSelection() {
        mAdapter.addAll();
    }

    @Override
    public void init() {
        mPresenter.attachView(this);
        getBinding().emptyView.emptyText.setText(getString(R.string.has_no_dubbing1));
        getBinding().draftRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnDraftClickListener(mListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        getBinding().draftRecyclerView.setLayoutManager(layoutManager);
        getBinding().draftRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        getBinding().draftRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        showLoadingLayout();
        mPresenter.getDraftData();
    }

    //取消选中
    public void clearSelectData(){
        mAdapter.clearSelectedData();
    }
}
