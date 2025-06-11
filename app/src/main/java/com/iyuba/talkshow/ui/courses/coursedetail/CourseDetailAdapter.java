package com.iyuba.talkshow.ui.courses.coursedetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.databinding.ItemCourseDetailBinding;
import com.iyuba.talkshow.ui.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;


public class CourseDetailAdapter extends RecyclerView.Adapter<CourseDetailAdapter.ViewHolder> {

    public List<Voa> getVoas() {
        return voas;
    }

    public void setVoas(List<Voa> voas) {

        this.voas.clear();
        this.voas.addAll(voas);
        notifyDataSetChanged();
    }

    private List<Voa> voas = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemCourseDetailBinding binding = ItemCourseDetailBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup, false);
        this.context = viewGroup.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setItem(voas.get(i));
        viewHolder.setClick();
    }

    @Override
    public int getItemCount() {
        return voas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCourseDetailBinding binding;
        public ViewHolder(@NonNull ItemCourseDetailBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setItem(Voa dataBean) {
            binding.title.setText(dataBean.titleCn());
            binding.views.setText("浏览量"+ dataBean.readCount());
            binding.desc.setText(dataBean.title());
            Glide.with(context).load(dataBean.pic()).placeholder(R.drawable.default_pic).into(binding.image);
        }


        public void onViewClicked() {
//            context.startActivity(DetailActivity.buildIntent(context, voas.get(getAdapterPosition()),true));
            context.startActivity(DetailActivity.buildIntentLimit(context, voas.get(getAdapterPosition()),true));
        }

        public void setClick() {
            binding.voa.setOnClickListener(v -> onViewClicked());
        }
    }
}
