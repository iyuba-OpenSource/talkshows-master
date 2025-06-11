package com.iyuba.talkshow.ui.user.me.dubbing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.databinding.ActivityMyDubbingBinding;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
import com.iyuba.talkshow.ui.base.MvpView;
import com.iyuba.talkshow.ui.detail.FragmentAdapter;
import com.iyuba.talkshow.ui.main.MainActivity;
import com.iyuba.talkshow.ui.user.download.DownloadFragment;
import com.iyuba.talkshow.ui.user.me.dubbing.draft.DraftFragment;
import com.iyuba.talkshow.ui.user.me.dubbing.released.ReleasedFragment;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程配音的主界面
 */
public class MyDubbingActivity extends BaseViewBindingActivity<ActivityMyDubbingBinding> implements MvpView {
    private static final String IS_BACK_TO_HOME = "back_home";
    private static final String SHOW_ITEM = "show_item";

    private boolean isBackToHome;
    private int showItem;


    private List<Fragment> mFragmentList = new ArrayList<>();
    private ReleasedFragment releasedFragment;//已发布
    private DraftFragment draftFragment;//草稿箱
    private DownloadFragment downloadFragment;//已下载

    public static Intent buildIntent(Context context, int showItem, boolean isBackToHome) {
        Intent intent = new Intent(context, MyDubbingActivity.class);
        intent.putExtra(SHOW_ITEM, showItem);
        intent.putExtra(IS_BACK_TO_HOME, isBackToHome);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.myDubbingToolbar);
        Intent intent = getIntent();
        isBackToHome = intent.getBooleanExtra(IS_BACK_TO_HOME, false);
        showItem = intent.getIntExtra(SHOW_ITEM, 0);
        binding.selectAll.setVisibility(View.GONE);
        initFragment();
        setClick();
    }

    private void setClick() {
        binding.deleteBtn.setOnClickListener(v -> onDeleteClick());
        binding.selectAll.setOnClickListener(v -> onSelectAll());
        binding.editTv.setOnClickListener(v -> onEditClick());
    }

    private void initFragment() {
        releasedFragment = new ReleasedFragment();
        mFragmentList.add(releasedFragment);
        draftFragment = new DraftFragment();
        mFragmentList.add(draftFragment);
        downloadFragment = new DownloadFragment();
        mFragmentList.add(downloadFragment);

//        mFragmentList.add(new UnreleasedFragment());
        String[] titles = getResources().getStringArray(R.array.my_dubbing_page_title);
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(
                this.getSupportFragmentManager(), mFragmentList, titles);
        binding.viewpager.setAdapter(mFragmentAdapter);
        binding.viewpager.setCurrentItem(showItem);
        binding.viewpager.setOffscreenPageLimit(2);
        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //setEditView(position);
            }

            @Override
            public void onPageSelected(int position) {
                setEditView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.myDubbingTabs.setupWithViewPager(binding.viewpager);
    }

    public void setEditView(int position) {
        Log.e("setEditView", "执行了" + position);
        Editable curEditable = (Editable) mFragmentList.get(position);
        if (curEditable.getMode() == Mode.SHOW) {
            binding.editTv.setText(getString(R.string.edit));
            binding.selectAll.setVisibility(View.GONE);
            curEditable.setMode(Mode.SHOW);
            binding.deleteBtn.setVisibility(View.GONE);
        } else {
            binding.selectAll.setVisibility(View.VISIBLE);
            binding.editTv.setText(getString(R.string.cancel));
            curEditable.setMode(Mode.EDIT);

            if (curEditable.getDataSize() == 0) {
                binding.deleteBtn.setVisibility(View.GONE);
            } else {
                binding.deleteBtn.setVisibility(View.VISIBLE);
            }
        }
    }

//    @OnClick(R.id.select_all)
    public void onSelectAll() {
        Editable curEditable = (Editable) mFragmentList.get(binding.viewpager.getCurrentItem());
        curEditable.addAllSelection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }

    public void onEditClick() {
        int position = binding.viewpager.getCurrentItem();
        Editable curEditable = (Editable) mFragmentList.get(position);
        if (curEditable.getMode() == Mode.SHOW) {
            binding.editTv.setText(getString(R.string.cancel));
            curEditable.setMode(Mode.EDIT);
            binding.selectAll.setVisibility(View.VISIBLE);

            if (curEditable.getDataSize() == 0) {
                binding.deleteBtn.setVisibility(View.GONE);
            } else {
                binding.deleteBtn.setVisibility(View.VISIBLE);
            }
        } else {
            binding.editTv.setText(getString(R.string.edit));
            binding.selectAll.setVisibility(View.GONE);
            curEditable.setMode(Mode.SHOW);
            binding.deleteBtn.setVisibility(View.GONE);

            //取消当前界面的选中状态
            int pageIndex = binding.viewpager.getCurrentItem();
            switch (pageIndex){
                case 0:
                    //已发布
                    if (releasedFragment!=null){
                        releasedFragment.clearSelectData();
                    }
                    break;
                case 1:
                    //草稿箱
                    if (draftFragment!=null){
                        draftFragment.clearSelectData();
                    }
                    break;
                case 2:
                    //已下载
                    if (downloadFragment!=null){
                        downloadFragment.clearSelectData();
                    }
                    break;
            }
        }
    }

    public void onDeleteClick() {
        Editable curEditable = (Editable) mFragmentList.get(binding.viewpager.getCurrentItem());
        curEditable.deleteCollection();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isBackToHome) {
                    startMainActivity();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public interface Item {
        int DOWNLOAD = 0;
        int DRAFT = 1;
        int RELEASED = 2;
        int UNRELEASED = 3;
    }
}
