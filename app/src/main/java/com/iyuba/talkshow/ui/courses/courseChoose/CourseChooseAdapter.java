package com.iyuba.talkshow.ui.courses.courseChoose;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.databinding.ItemCourseChosseBinding;
import com.iyuba.talkshow.ui.courses.wordChoose.WordChoosePresenter;

import java.util.List;
import javax.inject.Inject;

public class CourseChooseAdapter extends RecyclerView.Adapter<CourseChooseAdapter.ViewHolder> {

    private Context context ;
    private List<SeriesData> dataBeans;

    @Inject
    WordChoosePresenter wordChoosePresenter;
    CourseCallback courseCallback ;
    private int targetFlag = 0;


    public CourseChooseAdapter(List<SeriesData> dataBeans, int flag) {
        this.dataBeans = dataBeans;
        this.targetFlag = flag ;
    }

    public List<SeriesData> getDataBeans() {
        return dataBeans;
    }

    public void setDataBeans(List<SeriesData> dataBeans) {
        this.dataBeans.clear();
        this.dataBeans.addAll(dataBeans);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemCourseChosseBinding binding = ItemCourseChosseBinding.inflate(LayoutInflater.from(viewGroup.getContext()),viewGroup,false);
        this.context = viewGroup.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setItem(dataBeans.get(i));
    }

    @Override
    public int getItemCount() {
        return dataBeans.size() ;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView title;
        private TextView views;
        private ImageView image;

        public ViewHolder(@NonNull ItemCourseChosseBinding binding) {
            super(binding.getRoot());
            title = binding.title ;
            views = binding.views;
            image = binding.image;
        }


        public void setItem(SeriesData dataBean) {
            title.setText(dataBean.getSeriesName());
            views.setText(dataBean.getDescCn());
             Glide.with(context).load(dataBean.getPic()).placeholder(R.drawable.default_pic_v).into(image);
             setClick();
        }

        public void setClick(){
            image.setOnClickListener(v -> onViewClicked());
            image.setOnLongClickListener(v->onViewLongClicked());
        }


//        @OnClick(R.id.image)
        public void onViewClicked() {
            courseCallback.onCourseClicked(Integer.parseInt(dataBeans.get(getAdapterPosition()).getId()),dataBeans.get(getAdapterPosition()).getSeriesName());
            ((Activity)context).finish();
        }

//        @OnLongClick(R.id.image)
        public boolean onViewLongClicked() {
            courseCallback.onCourseLongClicked(Integer.parseInt(dataBeans.get(getAdapterPosition()).getId()));
            return true ;
        }
    }

    interface CourseCallback {
        void onCourseClicked(int series,String title);
        void onCourseLongClicked(int series);
    }

    public void setVoaCallback(CourseCallback mVoaCallback) {
        this.courseCallback = mVoaCallback;
    }
}
