package com.iyuba.talkshow.ui.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.databinding.ItemCourseDetailBinding;
import com.iyuba.talkshow.ui.series.SeriesActivity;

import java.util.List;

public class VestBagAdapter extends RecyclerView.Adapter<VestBagAdapter.VestBagHolder> {

    private Context context;
    private List<SeriesData> list;

    public VestBagAdapter(Context context, List<SeriesData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VestBagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCourseDetailBinding binding = ItemCourseDetailBinding.inflate(LayoutInflater.from(context),parent,false);
        return new VestBagHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VestBagHolder holder, int position) {
        if (holder==null){
            return;
        }

        SeriesData data = list.get(position);
        //图片
        Glide.with(context).load(data.getPic()).into(holder.imageView);
        //标题
        holder.titleView.setText(data.getSeriesName());
        //说明
        holder.descView.setText(data.getDescCn());


        holder.itemView.setOnClickListener(v->{
            SeriesData series = (SeriesData) list.get(position);
            if ((series != null) && !TextUtils.isEmpty(series.getId())) {
                context.startActivity(SeriesActivity.getIntent(context, series.getId(), series.getCategory()));
            } else {
                context.startActivity(SeriesActivity.getIntent(context, "201", App.DEFAULT_SERIESID));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class VestBagHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView titleView;
        private TextView descView;
        private TextView playView;

        public VestBagHolder(ItemCourseDetailBinding binding){
            super(binding.getRoot());

            imageView = binding.image;
            titleView = binding.title;
            descView = binding.desc;
            playView = binding.views;
            playView.setVisibility(View.GONE);
        }
    }
}
