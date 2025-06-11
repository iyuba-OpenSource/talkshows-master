package com.iyuba.talkshow.ui.help;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import com.iyuba.talkshow.ui.main.MainActivity;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.databinding.ActivityHelpUseBinding;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


public class HelpUseActivity extends BaseViewBindingActivity<ActivityHelpUseBinding> {
    private static final String IS_FIRST = "is_first";
    private boolean flag = false;
    private HelpViewAdapter mAdapter;

    public static Intent buildIntent(Context context, boolean isFirst) {
        Intent intent = new Intent(context, HelpUseActivity.class);
        intent.putExtra(IS_FIRST, isFirst);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        flag = getIntent().getBooleanExtra(IS_FIRST, false);
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
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        final List<Integer> resIdlist = getResIdList();

        binding.viewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                binding.pageIndicator.setCurrIndicator(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (position==4 && positionOffset>0){
//                    if (isFirst) {
//                        Intent intent = new Intent();
//                        intent.setClass(HelpUseActivity.this, MainActivity.class);
//                        intent.putExtra("showDialog",true);
//                        startActivity(intent);
//                    }
//                    finish();
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) { // state chain when scrolling : 1-->2-->0
                    case 0: //什么都没做
                        break;
                    case 1: //正在滑动
                        break;
                    case 2: //滑动完毕
                        break;
                }

            }
        });
        mAdapter = new HelpViewAdapter(resIdlist);
        mAdapter.setFirst(flag);
        binding.viewPager.setAdapter(mAdapter);
        mAdapter.setOnStartListener(new HelpViewAdapter.OnStartListener() {
            @Override
            public void onStart() {
                Log.e("WelcomeActivity", "HelpUseActivity onStart click. ");
//                EventBus.getDefault().post(new HelpEvent(0));
                Intent intent = new Intent(HelpUseActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        binding.viewPager.setOffscreenPageLimit(3);
        binding.pageIndicator.setIndicator(resIdlist.size());
        binding.pageIndicator.setCurrIndicator(0);
    }

    public List<Integer> getResIdList() {
        List<Integer> idList = new ArrayList<>();
        idList.add(R.drawable.help1);
        idList.add(R.drawable.help2);
        idList.add(R.drawable.help3);
        idList.add(R.drawable.help4);
        idList.add(R.drawable.help5);
        return idList;
    }

}