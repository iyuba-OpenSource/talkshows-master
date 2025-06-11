package com.iyuba.talkshow.ui.lil.ui.lesson.junior;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.lib_common.bean.BookChapterBean;
import com.iyuba.lib_common.util.LibGlide3Util;
import com.iyuba.lib_common.util.LibHelpUtil;
import com.iyuba.talkshow.databinding.ItemChapterJunior1Binding;

import java.util.List;

/**
 * @title:
 * @date: 2024/1/2 14:36
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class LessonListAdapter extends RecyclerView.Adapter<LessonListAdapter.LessonHolder> {

    private Context context;
    private List<BookChapterBean> list;

    public LessonListAdapter(Context context, List<BookChapterBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LessonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChapterJunior1Binding binding = ItemChapterJunior1Binding.inflate(LayoutInflater.from(context),parent,false);
        return new LessonHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonHolder holder, int position) {
        if (holder==null){
            return;
        }

        BookChapterBean bean = list.get(position);
        LibGlide3Util.loadRoundImg(context,bean.getPicUrl(),0,10,holder.pic);
        holder.title.setText(LibHelpUtil.transTitleStyle(bean.getTitleEn()));
        holder.desc.setText(bean.getTitleCn());

        holder.itemView.setOnClickListener(v->{
            if (onItemClickListener!=null){
                onItemClickListener.onClick(new Pair<>(position,bean));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class LessonHolder extends RecyclerView.ViewHolder{

        private ImageView editView;

        private ConstraintLayout contentLayout;
        private ImageView pic;
        private TextView title;
        private TextView desc;
        private ImageView download;

        private ConstraintLayout bottomLayout;
        private LinearLayout readLayout;
        private ImageView readPic;
        private TextView readText;

        private LinearLayout evalLayout;
        private ImageView evalPic;
        private TextView evalText;

        private LinearLayout wordLayout;
        private ImageView wordPic;
        private TextView wordText;

        private LinearLayout mocLayout;
        private ImageView mocPic;
        private TextView mocText;

        private LinearLayout exerciseLayout;
        private ImageView exercisePic;
        private TextView exerciseText;

        public LessonHolder(ItemChapterJunior1Binding binding){
            super(binding.getRoot());

            editView = binding.editView;

            contentLayout = binding.contentLayout;
            pic = binding.pic;
            title = binding.title;
            desc = binding.desc;
            download = binding.download;

            bottomLayout = binding.bottomLayout;
            readLayout = binding.readLayout;
            readPic = binding.readPic;
            readText = binding.readText;

            evalLayout = binding.evalLayout;
            evalPic = binding.evalPic;
            evalText = binding.evalText;

            wordLayout = binding.wordLayout;
            wordPic = binding.wordPic;
            wordText = binding.wordText;

            mocLayout = binding.mocLayout;
            mocPic = binding.mocPic;
            mocText = binding.mocText;

            exerciseLayout = binding.exerciseLayout;
            exercisePic = binding.exercisePic;
            exerciseText = binding.exerciseText;
        }
    }

    /**************************刷新数据***********************/
    //刷新数据
    public void refreshData(List<BookChapterBean> refreshList){
        this.list = refreshList;
        notifyDataSetChanged();
    }

    /***************************回调接口***********************/
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        //点击item
        void onClick(Pair<Integer,BookChapterBean> pair);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
