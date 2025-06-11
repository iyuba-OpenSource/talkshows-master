package com.iyuba.talkshow.ui.rank;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.lib_common.util.LibGlide3Util;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.model.RankingListBean;
import com.iyuba.talkshow.databinding.ActivityRankBinding;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.rank.dubbing.DubbingListActivity;
import com.iyuba.talkshow.ui.widget.LoadingDialog;
import com.iyuba.talkshow.ui.widget.divider.LinearItemDivider;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import javax.inject.Inject;


/**
 * RankActivity
 *
 * @author wayne
 * @date 2018/2/6
 */
public class RankActivity extends BaseActivity implements RankMvpView {

    @Inject
    RankPresenter mPresenter;

    LoadingDialog mLoadingDialog;

    @Inject
    RankingAdapter mRankingAdapter;

    ActivityRankBinding binding ;

    int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        binding = ActivityRankBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mPresenter.attachView(this);
        binding.listToolbar.listToolbar.setTitle("今日排行");
        setSupportActionBar(binding.listToolbar.listToolbar);
        mLoadingDialog = new LoadingDialog(mContext);

        binding.rankRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        LinearItemDivider divider = new LinearItemDivider(mContext, LinearItemDivider.VERTICAL_LIST);
        divider.setDivider(ResourcesCompat.getDrawable(getResources(), R.drawable.voa_ranking_divider, getTheme()));
        binding.rankRecyclerView.addItemDecoration(divider);
        // 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        binding.rankRecyclerView.setHasFixedSize(true);
        // 设置Item默认动画，加也行，不加也行。
        binding.rankRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.rankRecyclerView.setAdapter(mRankingAdapter);

        binding.rankRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 这里在加入判断，判断是否滑动到底部
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    int last = ((LinearLayoutManager) binding.rankRecyclerView.getLayoutManager()).findLastVisibleItemPosition();
//                    if (last == mRankingAdapter.getItemCount() - 1) {
//                        mPresenter.loadMoreListData();
//                    }
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mPresenter.loadDayRank();
        binding.myRank.setVisibility(UserInfoManager.getInstance().isLogin() ? View.VISIBLE : View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.day:
                binding.listToolbar.listToolbar.setTitle("今日排行");
                mPresenter.loadDayRank();
                return true;
            case R.id.week:
                binding.listToolbar.listToolbar.setTitle("本周排行");
                mPresenter.loadWeekRank();
                return true;
//            case R.id.month:
//                binding.listToolbar.listToolbar.setTitle("本月排行");
//                mPresenter.loadMonthRank();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showRankingList(List<RankingListBean.DataBean> data) {
        mRankingAdapter.clear();
        mRankingAdapter.addData(data,false);
    }

    @Override
    public void showMoreRankList(List<RankingListBean.DataBean> data) {
        mRankingAdapter.addData(data,true);
    }

    @Override
    public void showUserInfo(RankingListBean bean) {
        /*Glide.with(this)
                .load(bean.getMyimgSrc())
                .transform(new CircleTransform(mContext))
                .placeholder(R.drawable.default_avatar)
                .into(binding.ivAvatar);*/
        LibGlide3Util.loadCircleImg(this,bean.getMyimgSrc(),R.drawable.default_avatar,binding.ivAvatar);
        uid = bean.getMyid();
        binding.tvUsername.setText(bean.getMyname());
        binding.tvRank.setText(bean.getMyranking() + "");
        binding.count.setText("配音数：" + bean.getMycount());
        binding.score.setText(bean.getMyscores() + "分");
        binding.average.setText(getAverage(bean.getMyscores(), bean.getMycount()));
//        binding.myRank.setOnClickListener((view) -> click());

        //这里判断排行是否存在，然后进行处理
        if (bean.getData()!=null&&bean.getData().size()>0){
            binding.rankTop.setVisibility(View.VISIBLE);
            try {
                if (bean.getData().size()>0){
                    binding.rank1.setVisibility(View.VISIBLE);
                    RankingListBean.DataBean champion = bean.getData().get(0);
                    setViews(champion, binding.ivChampion,  binding.tvChampionName,  binding.tvChampionCount,  binding.tvChampioscore,  binding.tvChampioAverage);
                }else {
                    binding.rank1.setVisibility(View.INVISIBLE);
                }
                if (bean.getData().size()>1){
                    binding.rank2.setVisibility(View.VISIBLE);
                    RankingListBean.DataBean second = bean.getData().get(1);
                    setViews(second,  binding.ivSecondAvatar,  binding.tvSecondName,  binding.tvSecondCount,  binding.tvSecondScore,  binding.tvSecondAverage);
                }else {
                    binding.rank2.setVisibility(View.INVISIBLE);
                }
                if (bean.getData().size()>2){
                    binding.rank3.setVisibility(View.VISIBLE);
                    RankingListBean.DataBean third = bean.getData().get(2);
                    setViews(third,  binding.ivThirdAvatar,  binding.tvThirdName,  binding.tvThirdCount,  binding.tvThirdScore,  binding.tvThirdAverage);
                }else {
                    binding.rank3.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            binding.rankTop.setVisibility(View.GONE);
        }
    }

    private void setViews(final RankingListBean.DataBean bean, ImageView avatar, TextView name, TextView count, TextView score, TextView average) {
        /*Glide.with(this)
                .load(bean.getImgSrc())
                .transform(new CircleTransform(mContext))
                .placeholder(R.drawable.default_avatar)
                .into(avatar);*/
        LibGlide3Util.loadCircleImg(this,bean.getImgSrc(),R.drawable.default_avatar,avatar);
        name.setText(bean.getName());
        count.setText(bean.getCount() + "篇");
        average.setText(getAverage(bean.getScores(), bean.getCount()));
        score.setText(bean.getScores() + "分");
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mContext.startActivity(new Intent(mContext, DubbingListActivity.class).putExtra("uid", bean.getUid()));
            }
        });
    }

    private String getAverage(int score, int count) {
        if (count == 0) {
            return "0分";
        }
        return (score / count) + "分";
    }

    @Override
    public void showLoadingDialog() {
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        mLoadingDialog.dismiss();
    }


//    @OnClick(R.id.iv_avatar)
//    public void click() {
//        mContext.startActivity(new Intent(mContext, DubbingListActivity.class).putExtra("uid", uid));
//    }

//    @OnClick(R.id.my_rank)
    public void click() {
        mContext.startActivity(new Intent(mContext, DubbingListActivity.class).putExtra("uid", uid));
    }


}
