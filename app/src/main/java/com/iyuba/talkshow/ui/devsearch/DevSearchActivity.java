package com.iyuba.talkshow.ui.devsearch;

import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.dlna.manager.DLNADeviceManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.widget.divider.LinearItemDivider;

import org.cybergarage.android.databinding.ActivityDevSearchBinding;
import org.cybergarage.upnp.Device;

import java.util.ArrayList;
import java.util.List;


import static com.android.dlna.manager.DLNADeviceManager.getInstance;

public class DevSearchActivity extends BaseActivity implements DevSearchMvpView {


    ActivityDevSearchBinding bining ;
    private DevSearchAdapter mAdapter;
    private List<Device> mDeviceList = new ArrayList<>();

    @Override
    public void showToastShort(int resId) {

    }

    @Override
    public void showToastShort(String message) {

    }

    @Override
    public void showToastLong(int resId) {

    }

    @Override
    public void showToastLong(String message) {

    }

    private void startRotate() {
        RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(5000);
        animation.setRepeatMode(Animation.RESTART);
        animation.setRepeatCount(Animation.INFINITE);
        bining.imgSearching.startAnimation(animation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bining = ActivityDevSearchBinding.inflate(getLayoutInflater());
        setContentView(bining.getRoot());
        setAndroidNativeLightStatusBar(this,true);
//        setSupportActionBar(toolbar);
        startDiscovery();
        startRotate();
        LinearItemDivider itemDecoration = new LinearItemDivider(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDivider(getResources().getDrawable(R.drawable.list_divider));
        bining.recyclerView.addItemDecoration(itemDecoration);
        bining.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DevSearchAdapter();
        bining.recyclerView.setAdapter(mAdapter);
//        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                startDiscovery(false);
//            }
//        });
        mAdapter.setOnItemClickListener(new DevSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getInstance().setCurrentDevice(mDeviceList.get(position));
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onRetryClick() {
                startDiscovery();
            }
        });
    }

    private void startDiscovery() {
        getInstance().startDiscovery(mListener);
        bining.searchingView.setVisibility(View.VISIBLE);
    }

    private final DLNADeviceManager.MediaRenderDeviceChangeListener mListener = new DLNADeviceManager.MediaRenderDeviceChangeListener() {
        @Override
        public void onStarted() {
            updateEmptyView(false);
        }

        @Override
        public void onDeviceListChanged(List<Device> list) {
            mDeviceList = list;
//            mRefreshLayout.setRefreshing(false);
            updateEmptyView(mDeviceList.isEmpty());
            mAdapter.setmDeviceList(mDeviceList);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFinished(List<Device> list) {
            bining.searchingView.setVisibility(View.GONE);
            mDeviceList = list;
//            mRefreshLayout.setRefreshing(false);
            updateEmptyView(mDeviceList.isEmpty());
            mAdapter.setmDeviceList(mDeviceList);
            mAdapter.notifyDataSetChanged();
        }
    };

    private void updateEmptyView(boolean visible) {
//        if (visible && mEmptyView.getVisibility() != View.VISIBLE) {
//            mEmptyView.setVisibility(View.VISIBLE);
//        } else if (!visible && mEmptyView.getVisibility() == View.VISIBLE) {
//            mEmptyView.setVisibility(View.GONE);
//        }
    }


//    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }



//    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
//        View decor = activity.getWindow().getDecorView();
//        if (dark) {
//            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        } else {
//            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }
//    }
}
