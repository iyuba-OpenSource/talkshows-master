package com.iyuba.iyubamovies.ui.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.iyubamovies.R;
import com.iyuba.iyubamovies.model.ImoviesList;

import java.util.List;


/**
 * Created by iyuba on 2017/12/22.
 */

public class IMoviesAdapter extends RecyclerView.Adapter {

    private static final int HEADER = 0;
    private static final int NORMAL = 1;
    private Context context;
    private View mHeaderView;
    private ImoviesOnItemClickListener imoviesOnItemClickListener;
    private List<Object> datas;
    public IMoviesAdapter(Context context,List<Object> datas){
        this.datas = datas;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView!=null&&viewType==HEADER)
        return new IMoviesItem(mHeaderView);
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.imovies_item_layout,parent,false);
        return new IMoviesItem(item);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) ==  HEADER) return;
        final int pos = getRealPosition(holder);
        if(pos<datas.size()){
            Object obj = datas.get(pos);
            if(obj instanceof ImoviesList&&holder instanceof IMoviesItem){
                Log.e("Imoviesitem","shide");
                IMoviesItem item = (IMoviesItem) holder;
                ImoviesList itemdata = (ImoviesList) obj;
                if(context!=null&&!((Activity)context).isFinishing())
                Glide.with(context).load(itemdata.getPic()).
                        placeholder(R.drawable.imovies_loading).
                        error(R.drawable.imovies_loading).
                        into(item.imovies_pic);
                item.imovies_title.setText(itemdata.getSeriesName());
                if(imoviesOnItemClickListener!=null){
                    item.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imoviesOnItemClickListener.OnImvoiesItmeClick(v,pos);
                        }
                    });
                }
            }
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView==null?datas.size():datas.size()+1;
    }
    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return NORMAL;
        if(position == 0) return HEADER;
        return NORMAL;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) ==  HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getHeaderView() {
        return mHeaderView;
    }

    public void addData(List<Object> datas){
        if(datas!=null&&datas.size()>0){
        this.datas.addAll(datas);
        notifyDataSetChanged();
        }
    }
    public void clearData()
    {
        if(datas!=null){
            datas.clear();
        notifyDataSetChanged();
        }
    }
    public void resetData(List<Object> datas){
        if(datas!=null){
            this.datas.clear();
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void setImoviesOnItemClickListener(ImoviesOnItemClickListener imoviesOnItemClickListener) {
        this.imoviesOnItemClickListener = imoviesOnItemClickListener;
    }

    class IMoviesItem extends RecyclerView.ViewHolder{
        ImageView imovies_pic;
        TextView imovies_title;
        public IMoviesItem(View itemView) {
            super(itemView);
            if(itemView == mHeaderView)
                return;
            imovies_pic =(ImageView) itemView.findViewById(R.id.imovies_pic);
            imovies_title = (TextView) itemView.findViewById(R.id.imovies_title);
        }
    }
    public interface ImoviesOnItemClickListener{
        void OnImvoiesItmeClick(View view,int position);
    }
}
