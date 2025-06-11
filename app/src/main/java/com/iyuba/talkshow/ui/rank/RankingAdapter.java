package com.iyuba.talkshow.ui.rank;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.lib_common.util.LibGlide3Util;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.model.RankingListBean;
import com.iyuba.talkshow.databinding.ItemRankingListBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;


/**
 * RankingAdapter
 *
 * @author wayne
 * @date 2018/2/6
 */
public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.Holder> {
    Context mContext;
    List<RankingListBean.DataBean> mList;
    private int total = 0;

    @Inject
    public RankingAdapter() {
        mList = new ArrayList<>();
    }

    public void addData(List<RankingListBean.DataBean> data,boolean isAdd) {
        /*mList.addAll(data);
        if (total == 0) {
            switch (mList.size()) {
                case 0:
                    total = 0;
                    break;
                case 1:
                    total = 1;
                    try {
                        this.mList.remove(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    total = 2;
                    try {
                        this.mList.remove(0);
                        this.mList.remove(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                default:
                    total = 3;
                    try {
                        this.mList.remove(0);
                        this.mList.remove(0);
                        this.mList.remove(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else if (total == 1) {
            switch (mList.size()) {
                case 0:
                    total = 1;
                    break;
                case 1:
                    total = 2;
                    try {
                        this.mList.remove(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                default:
                    total = 3;
                    try {
                        this.mList.remove(0);
                        this.mList.remove(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else if (total == 2) {
            switch (mList.size()) {
                case 0:
                    total = 2;
                    break;
                case 1:
                    total = 3;
                    try {
                        this.mList.remove(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
        notifyDataSetChanged();*/

        if (isAdd){
            mList.addAll(data);
        }else {
            //这里将前三个数据抛出去
            mList.clear();
            for (int i = 0; i < data.size(); i++) {
                if (i>2){
                    mList.add(data.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemRankingListBinding binding = ItemRankingListBinding.inflate(inflater, parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final RankingListBean.DataBean bean = mList.get(position);
        setRanking(holder.rank, position);
        //从第四名开始
        /*Glide.with(mContext)
                .load(bean.getImgSrc())
                .centerCrop()
                .transform(new CircleTransform(mContext))
                .placeholder(R.drawable.default_avatar)
                .into(holder.photo);*/
        LibGlide3Util.loadCircleImg(mContext,bean.getImgSrc(),R.drawable.default_avatar,holder.photo);
        holder.usernameTv.setText(bean.getName());
        holder.count.setText("配音数: " + bean.getCount());
        holder.average.setText(getAverage(bean.getScores(), bean.getCount()));
        holder.score.setText(bean.getScores() + "分");
//        holder.itemView.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, DubbingListActivity.class).putExtra("uid", bean.getUid())));
    }

    private String getAverage(int scores, int count) {
        if (count == 0) {
            return "平均分: 0";
        }
        return String.format(Locale.CHINA, "平均分: %s", scores / count);
    }

    private void setRanking(TextView tRank, int position) {
        tRank.setVisibility(View.VISIBLE);
        tRank.setText(String.valueOf(position + 4));
        tRank.setBackgroundResource(R.color.transparent);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clear() {
        mList.clear();
    }

    static class Holder extends RecyclerView.ViewHolder {

        public TextView usernameTv;
        public TextView count;
        public TextView average;
        public TextView rank;
        public TextView score;
        public ImageView photo;

        public Holder(ItemRankingListBinding itemView) {
            super(itemView.getRoot());
            usernameTv = itemView.usernameTv;
            count = itemView.count;
            average = itemView.average;
            usernameTv = itemView.usernameTv;
            rank = itemView.rank;
            score = itemView.score;
            photo = itemView.photo;
        }
    }
}
