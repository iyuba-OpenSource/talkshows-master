package com.iyuba.iyubamovies.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.iyubamovies.R;
import com.iyuba.iyubamovies.database.ImoviesDatabaseManager;
import com.iyuba.iyubamovies.network.ImoviesNetwork;
import com.iyuba.iyubamovies.network.api.GetSendCommentApi;
import com.iyuba.iyubamovies.network.result.ImoviesAgreeCountResult;
import com.iyuba.iyubamovies.network.result.ImoviesAplyCommentData;
import com.iyuba.iyubamovies.network.result.ImoviesCommentData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by iyuba on 2017/8/29.
 */

public class ImoviesCommentsAdapter extends  RecyclerView.Adapter{

    private List<ImoviesCommentData> comments;
    private CommentItemListener listener;
    private LinearLayoutManager mLayoutManager;
    private boolean showmore = true;
    private CommentItemZanClickListener selectZanListener;
    private ImoviesDatabaseManager manager;
    private String uid;
    private GetSendCommentApi api;
    public ImoviesCommentsAdapter(Context context, String uid){
        api = ImoviesNetwork.getSendCommentApi();
        manager = ImoviesDatabaseManager.getInstance();
        this.uid = uid;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imv_item_comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof CommentHolder){
            final CommentHolder vholder = (CommentHolder) holder;
            final ImoviesCommentData comment = comments.get(position);
            if(comment!=null){
                if(comment.UserName!=null&&!"".equals(comment.UserName))
                    vholder.user_name.setText(comment.UserName);
                else
                    vholder.user_name.setText(comment.Userid);
                vholder.user_time.setText(comment.CreateDate);
                vholder.user_content.setText(comment.ShuoShuo);
                vholder.comment_more.setVisibility(View.GONE);
                vholder.comment_more_list.setVisibility(View.GONE);
                vholder.comment_like_count.setText(comment.agreeCount);
                vholder.comment_more.setText("点击显示更多评论");
                vholder.comment_one_tim.setText("");
                vholder.comment_one_usn.setText("");
                vholder.comment_one_pos.setText("");
                vholder.comment_one_con.setText("");
                vholder.comment_show_lin.setVisibility(View.GONE);
                vholder.comment_like_count.setText("0");
                vholder.comment_like_count.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.imv_comment_normal_zan,0);
                vholder.comment_like_count.setText(comment.agreeCount);
                if(comment.isselectzan){
                    vholder.comment_like_count.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.imv_comment_select_zan, 0);
                    vholder.comment_like_count.setText(comment.agreeCount);
                }
                vholder.comment_like_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        vholder.comment_like_count.setCompoundDrawables(R.drawable.comment_select_zan,null,null,null);
                        if(!comment.isselectzan) {
                            vholder.comment_like_count.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.imv_comment_select_zan, 0);
                        //点赞操作
                            comment.agreeCount=(Integer.parseInt(comment.agreeCount)+1)+"";
                            vholder.comment_like_count.setText(comment.agreeCount);
                            Log.e("Tag--zan","点赞的操作");
                            api.clickAgreeCount(GetSendCommentApi.ZAN_PROTOCAL,comment.id).enqueue(new Callback<ImoviesAgreeCountResult>() {
                                @Override
                                public void onResponse(Call<ImoviesAgreeCountResult> call, Response<ImoviesAgreeCountResult> response) {

                                    if(response.isSuccessful()){
                                        if(response.body().getResultCode().equals("001")){
                                            manager.savaclickZan(uid,comment.id);
                                            return;
                                        }else {
                                            vholder.comment_like_count.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.imv_comment_normal_zan,0);
                                            comment.agreeCount=(Integer.parseInt(comment.agreeCount)-1)+"";
                                            comment.isselectzan = false;
                                            vholder.comment_like_count.setText(comment.agreeCount);
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<ImoviesAgreeCountResult> call, Throwable t) {
                                    vholder.comment_like_count.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.imv_comment_normal_zan,0);
                                    comment.isselectzan = false;
                                    comment.agreeCount=(Integer.parseInt(comment.agreeCount)-1)+"";
                                    vholder.comment_like_count.setText(comment.agreeCount);
                                }
                            });

                            comment.isselectzan = true;
                        }
                    }
                });
                if(comment.getJxblist()!=null&&comment.getJxblist().size()>0)
                {
                    ImoviesAplyCommentData aplyComment = comment.getJxblist().get(0);
                    vholder.comment_show_lin.setVisibility(View.VISIBLE);
                    vholder.comment_one_tim.setText(aplyComment.getCreateDate());
                    vholder.comment_one_usn.setText(aplyComment.getUserName()!=null&&
                            !"".equals(aplyComment.getUserName())?aplyComment.getUserName()+":":aplyComment.getUserid()+":");
                    vholder.comment_one_pos.setText("1");
                    vholder.comment_one_con.setText(aplyComment.getShuoShuo());
                    if(comment.getJxblist().size()>1){
                        vholder.comment_more.setVisibility(View.VISIBLE);
                        vholder.comment_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(showmore) {
                                    showmore = false;
                                    ImoviesReplyCommentAdapter  madapter = new ImoviesReplyCommentAdapter();
                                    madapter.setCommentItems(comment.getJxblist());
                                    vholder.comment_more_list.setVisibility(View.VISIBLE);
                                    vholder.comment_more.setText("点击收起");
                                    vholder.comment_show_lin.setVisibility(View.GONE);
                                    vholder.comment_more_list.setLayoutManager(new LinearLayoutManager(vholder.itemView.getContext()));
//                                  vholder.comment_more_list.addItemDecoration(new DividerItemDecoration(
//                                        vholder.itemView.getContext(), R.drawable.basic_headlines_recyclerview_thick_divider
//                                        , DividerItemDecoration.VERTICAL_LIST));
                                    vholder.comment_more_list.setAdapter(madapter);
                                    comments.get(position).isshowmore = true;
                                }else {
                                    showmore = true;
                                    vholder.comment_show_lin.setVisibility(View.VISIBLE);
                                    vholder.comment_more_list.setVisibility(View.GONE);
                                    vholder.comment_more.setText("点击显示更多评论");
                                    comments.get(position).isshowmore = false;
                                }
                            }
                        });
                    }

                }else {
                    vholder.comment_more.setVisibility(View.GONE);
                }

                if(comment.isshowmore)
                {
                    vholder.comment_show_lin.setVisibility(View.GONE);
                    vholder.comment_more_list.setVisibility(View.VISIBLE);
                    showmore = false;
                    ImoviesReplyCommentAdapter  madapter = new ImoviesReplyCommentAdapter();
                    madapter.setCommentItems(comment.getJxblist());
                    vholder.comment_more_list.setVisibility(View.VISIBLE);
                    vholder.comment_more.setText("点击收起");
                    vholder.comment_more_list.setLayoutManager(new LinearLayoutManager(vholder.itemView.getContext()));
                    vholder.comment_more_list.setAdapter(madapter);
                }



                if(comment.vip.equals("0")) {
                    vholder.user_vip.setVisibility(View.GONE);
                    vholder.user_name.setTextColor(Color.parseColor("#505050"));

                }
                else {
                    vholder.user_vip.setVisibility(View.VISIBLE);
                    vholder.user_name.setTextColor(Color.parseColor("#E2AF48"));
                }
                Glide.with(holder.itemView.getContext()).load(comment.ImgSrc).placeholder(R.drawable.imovies_default_tx)
                        .error(R.drawable.imovies_default_tx).into(vholder.user_pic);

            }


            if(listener!=null)
            {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(holder.itemView,position);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        listener.onLongItemClick(holder.itemView,position);
                        return true;
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setCommentItems(List<ImoviesCommentData>comments){
        this.comments = comments;
        notifyDataSetChanged();
    }
    public void clearComments(){
        if(comments!=null)
            comments.clear();
        notifyDataSetChanged();
    }
    public void addCommentItems(List<ImoviesCommentData>comments){
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }
    class CommentHolder extends RecyclerView.ViewHolder{
        public CircleImageView user_pic;
        public TextView user_name;
        public ImageView user_vip;
        public TextView user_content;
        public TextView user_time;
        public TextView comment_more;
        public RecyclerView comment_more_list;
        public TextView comment_like_count;
        public LinearLayout comment_show_lin;
        public TextView comment_one_pos;
        public TextView comment_one_usn;
        public TextView comment_one_con;
        public TextView comment_one_tim;
        public CommentHolder(View itemView) {
            super(itemView);
            user_pic = (CircleImageView) itemView.findViewById(R.id.imv_user_pics);
            user_name = (TextView) itemView.findViewById(R.id.imv_comment_item_usernames);
            user_vip = (ImageView) itemView.findViewById(R.id.imv_comment_user_vips);
            user_content = (TextView) itemView.findViewById(R.id.imv_comment_item_contents);
            user_time = (TextView) itemView.findViewById(R.id.imv_comment_item_sendtimes);
            comment_more = (TextView) itemView.findViewById(R.id.imv_get_mores);
            comment_more_list = (RecyclerView) itemView.findViewById(R.id.imv_more_comments);
            comment_like_count = (TextView) itemView.findViewById(R.id.imv_comment_like_counts);
            comment_show_lin = (LinearLayout) itemView.findViewById(R.id.comment_show_lin);
            comment_one_con = (TextView) itemView.findViewById(R.id.imv_comment_contents);
            comment_one_pos = (TextView) itemView.findViewById(R.id.imv_comment_positions);
            comment_one_usn = (TextView) itemView.findViewById(R.id.imv_comment_names);
            comment_one_tim = (TextView) itemView.findViewById(R.id.imv_comment_citem_sendtime);

        }
    }
    public void setItemClickListener(CommentItemListener itemListener){
        this.listener = itemListener;
    }
    public void setItemOnZanClickListener(CommentItemZanClickListener selectZanListener){
        this.selectZanListener = selectZanListener;
    }
    public interface CommentItemListener{
        public void onItemClick(View view, int position);
        public void onLongItemClick(View view, int position);
    }
    public interface CommentItemZanClickListener{
         void selectZanListener(View view, int position);
    }
}
