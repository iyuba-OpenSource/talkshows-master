package com.iyuba.iyubamovies.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iyuba.iyubamovies.R;
import com.iyuba.iyubamovies.network.result.ImoviesAplyCommentData;

import java.util.List;

/**
 * Created by iyuba on 2017/8/30.
 */

public class ImoviesReplyCommentAdapter extends RecyclerView.Adapter{

    private List<ImoviesAplyCommentData> comments;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imovies_item_more_comment,parent,false);

        return new RViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof RViewHolder){
            RViewHolder viewHolder = (RViewHolder) holder;
            ImoviesAplyCommentData  comment = comments.get(position);
            if(comment!=null){
                viewHolder.sendtime.setText(comment.getCreateDate());
                viewHolder.username.setText(comment.getUserName()!=null&&
                        !"".equals(comment.getUserName())?comment.getUserName()+":":comment.getUserid()+":");
                viewHolder.position.setText(""+(position+1));
                viewHolder.content.setText(comment.getShuoShuo());
            }
        }
    }
    public void setCommentItems(List<ImoviesAplyCommentData>comments){
        this.comments = comments;
        notifyDataSetChanged();
    }
    public void addCommentItems(List<ImoviesAplyCommentData>comments){
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return comments.size();
    }
    public void setComments(List<ImoviesAplyCommentData>comments){

        this.comments = comments;
    }
    class RViewHolder extends RecyclerView.ViewHolder{

        public TextView position;
        public TextView username;
        public TextView content;
        public TextView sendtime;
        public RViewHolder(View itemView) {
            super(itemView);
            position = (TextView) itemView.findViewById(R.id.imv_comment_position);
            username = (TextView) itemView.findViewById(R.id.imv_comment_name);
            content = (TextView) itemView.findViewById(R.id.imv_comment_content);
            sendtime = (TextView) itemView.findViewById(R.id.imv_comment_item_sendtime);

        }
    }
}
