package com.iyuba.iyubamovies.ui.adapter;

import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.iyubamovies.R;
import com.iyuba.iyubamovies.model.OneSerisesData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iyuba on 2018/1/16.
 */

public class IMvserisesItemAdapter extends RecyclerView.Adapter {
    private List<Object> items;
    private OnCircleItemClickLisenter onCircleItemClickLisenter;
    private static int clickpos = 0;
    private static boolean ISDOWNLOAD = false;
    public IMvserisesItemAdapter(){
        List<Object> items = new ArrayList<>();
        clickpos = 0;
        ISDOWNLOAD = false;
    }
    public IMvserisesItemAdapter(List<Object> items){
        this.items = items;
        clickpos = 0;
        ISDOWNLOAD = false;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.serises_item_layout,parent,false);
        return new SerisesItemHolder(item);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof SerisesItemHolder){
            final SerisesItemHolder itemHolder = (SerisesItemHolder) holder;
            Object obj = items.get(position);
            if(obj instanceof OneSerisesData){
                OneSerisesData item = (OneSerisesData) obj;
                itemHolder.circleitem.setText(position+1+"");
                if(clickpos==position){
                    itemHolder.circleitem.setBackgroundResource(R.drawable.imovies_red_circle);
                    itemHolder.circleitem.setTextColor(Color.WHITE);
                }else {
                    itemHolder.circleitem.setBackgroundResource(R.drawable.imovies_white_circle);
                    itemHolder.circleitem.setTextColor(Color.BLACK);
                }
                itemHolder.download_bs.setVisibility(View.INVISIBLE);
                itemHolder.download_bs.setImageResource(R.drawable.imovies_download_bs);
                if(ISDOWNLOAD){
                    if(item.getIsDownload().equals("0")){
                        itemHolder.download_bs.setVisibility(View.INVISIBLE);
                    }else {
                        itemHolder.download_bs.setVisibility(View.VISIBLE);
                        if(item.getIsFinishDownLoad().equals("0"))
                            itemHolder.download_bs.setImageResource(R.drawable.imovies_download_bs);
                        else
                            itemHolder.download_bs.setImageResource(R.drawable.imovies_download_finish);
                    }
                }
                itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onCircleItemClickLisenter!=null){
                            if(!ISDOWNLOAD) {
                                onCircleItemClickLisenter.OnCircleItemOnClick(v, position);
                                clickpos = position;
                                notifyDataSetChanged();
                            }else {
                                onCircleItemClickLisenter.OnCircleDownLoadItemClick(v,position);
                                notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        }

    }

    public  void setISDOWNLOAD(boolean ISDOWNLOAD) {
        IMvserisesItemAdapter.ISDOWNLOAD = ISDOWNLOAD;
        notifyDataSetChanged();
    }

    public void setClickpos(int clickpos) {
        IMvserisesItemAdapter.clickpos = clickpos;
        notifyDataSetChanged();
    }

    public void setOnCircleItemClickLisenter(OnCircleItemClickLisenter onCircleItemClickLisenter) {
        this.onCircleItemClickLisenter = onCircleItemClickLisenter;
    }

    @Override
    public int getItemCount() {
        return items==null?0:items.size();
    }
    public void setItems(List<Object> items){
        this.items = items;
        notifyDataSetChanged();
    }
    public void addItems(List<Object> items){
        if(items!=null)
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void cleadItems(){
        this.items.clear();
        notifyDataSetChanged();
    }
    class SerisesItemHolder extends RecyclerView.ViewHolder{
        private TextView circleitem;
        private ImageView download_bs;
        public SerisesItemHolder(View itemView) {
            super(itemView);
            circleitem = (TextView) itemView.findViewById(R.id.imv_circle_item);
            download_bs = (ImageView) itemView.findViewById(R.id.imv_video_download_bs);
        }
    }
    public interface OnCircleItemClickLisenter{
        void OnCircleItemOnClick(View view,int position);
        void OnCircleDownLoadItemClick(View view,int position);
    }


}
