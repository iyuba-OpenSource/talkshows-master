package com.iyuba.talkshow.ui.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_common.model.remote.bean.base.BaseBean_data;
import com.iyuba.lib_common.util.LibGlide3Util;
import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.sdk.other.NetworkUtil;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.databinding.ActivityWalletHistoryBinding;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
import com.iyuba.talkshow.ui.lil.bean.Reward_history;
import com.iyuba.talkshow.util.RxUtil;
import com.iyuba.talkshow.util.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @title: 钱包历史记录
 * @date: 2023/10/23 10:42
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class WalletHistoryActivity extends BaseViewBindingActivity<ActivityWalletHistoryBinding> {

    //数据
    @Inject
    DataManager mDataManager;

    //适配器
    private WalletHistoryAdapter historyAdapter;
    //页码
    private int pageIndex = 1;
    //每页数量
    private static final int PAGE_COUNT = 20;
    //加载数据
    private Subscription checkDataDis;

    public static void start(Context context){
        Intent intent = new Intent();
        intent.setClass(context,WalletHistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initToolbar();
        initUserInfo();
        initList();

        binding.refreshLayout.autoRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRefreshAndMore();
    }

    /***************************初始化************************/
    private void initToolbar(){
        binding.toolbar.tvTopCenter.setText("钱包历史记录");
        binding.toolbar.imgTopLeft.setVisibility(View.VISIBLE);
        binding.toolbar.imgTopLeft.setImageResource(R.mipmap.img_back);
        binding.toolbar.imgTopLeft.setOnClickListener(v->{
            finish();
        });
        binding.toolbar.imgTopRight.setVisibility(View.VISIBLE);
        binding.toolbar.imgTopRight.setBackgroundResource(0);
        binding.toolbar.imgTopRight.setImageResource(R.drawable.ic_tips);
        binding.toolbar.imgTopRight.setOnClickListener(v->{
            double money = UserInfoManager.getInstance().getMoney();
            String showMsg = "当前钱包金额:" + money + "元,满10元可在[爱语吧]微信公众号提现(关注绑定爱语吧账号)";

            new AlertDialog.Builder(this)
                    .setTitle("奖励说明")
                    .setMessage(showMsg)
                    .show();
        });

        binding.showTag.type.setText("类型");
        binding.showTag.type.setTextColor(LibResUtil.getInstance().getColor(R.color.colorPrimary));
        binding.showTag.reward.setText("金额(元)");
        binding.showTag.reward.setTextColor(LibResUtil.getInstance().getColor(R.color.colorPrimary));
        binding.showTag.time.setText("时间");
        binding.showTag.time.setTextColor(LibResUtil.getInstance().getColor(R.color.colorPrimary));
    }

    private void initUserInfo(){
        String userIconUrl = Constant.Url.getMiddleUserImageUrl(UserInfoManager.getInstance().getUserId(), String.valueOf(System.currentTimeMillis()));
        LibGlide3Util.loadCircleImg(mContext, userIconUrl, R.drawable.default_avatar, binding.userIcon);
        binding.userName.setText(UserInfoManager.getInstance().getUserName());
        String showTips = "(满1元可以抵扣会员购买，付费全站会员可以提现)";
        binding.userMoney.setText("金额："+UserInfoManager.getInstance().getMoney()+"元\n"+showTips);
    }

    private void initList(){
        binding.refreshLayout.setEnableRefresh(true);
        binding.refreshLayout.setEnableLoadMore(true);
        binding.refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        binding.refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (!NetworkUtil.isConnected(WalletHistoryActivity.this)){
                    stopRefreshAndMore();
                    ToastUtil.showToast(WalletHistoryActivity.this,"请链接网络后重试");
                    return;
                }

                checkData(true);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (!NetworkUtil.isConnected(WalletHistoryActivity.this)){
                    stopRefreshAndMore();
                    ToastUtil.showToast(WalletHistoryActivity.this,"请链接网络后重试");
                    return;
                }

                pageIndex = 1;
                checkData(false);
            }
        });

        historyAdapter = new WalletHistoryAdapter(this,new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(historyAdapter);

        binding.showTag.type.setText("类型");
        binding.showTag.type.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.showTag.reward.setText("金额(元)");
        binding.showTag.reward.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.showTag.time.setText("时间");
        binding.showTag.time.setTextColor(getResources().getColor(R.color.colorPrimary));
    }


    /******************************数据刷新和操作********************************/
    //加载数据
    private void checkData(boolean isAdd){
        RxUtil.unsubscribe(checkDataDis);
        checkDataDis = mDataManager.getWalletHistory(pageIndex,PAGE_COUNT, UserInfoManager.getInstance().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean_data<List<Reward_history>>>() {
                    @Override
                    public void onCompleted() {
                        stopRefreshAndMore();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToast(WalletHistoryActivity.this,"查询记录失败，请重试");
                    }

                    @Override
                    public void onNext(BaseBean_data<List<Reward_history>> bean) {
                        if (bean!=null&&bean.getResult().equals("200")){
                            if (bean.getData()==null||bean.getData().size()==0){
                                ToastUtil.showToast(WalletHistoryActivity.this,"暂无更多数据");
                                return;
                            }

                            stopRefreshAndMore();
                            historyAdapter.refreshData(bean.getData(),isAdd);
                            pageIndex++;
                        }else {
                            ToastUtil.showToast(WalletHistoryActivity.this,"暂无更多数据");
                        }
                    }
                });
    }

    //刷新下拉和上滑
    private void stopRefreshAndMore(){
        binding.refreshLayout.finishRefresh(true);
        binding.refreshLayout.finishLoadMore(true);
    }
}
