package com.iyuba.talkshow.ui.lil.ui.lesson.study.content;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.lib_common.bean.ChapterDetailBean;
import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.util.LibHelpUtil;
import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.databinding.ItemContentBinding;
import com.iyuba.talkshow.ui.lil.manager.StudySettingManager;
import com.iyuba.talkshow.ui.lil.view.SelectWordTextView;

import java.util.List;

/**
 * @title:
 * @date: 2024/1/3 15:03
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> {

    private Context context;
    private List<ChapterDetailBean> list;

    //选中的位置
    private int selectIndex = 0;
    //显示文本的类型
    private String showTextType = null;

    public ContentAdapter(Context context, List<ChapterDetailBean> list) {
        this.context = context;
        this.list = list;

        showTextType = StudySettingManager.getInstance().getContentLanguage();
    }

    @NonNull
    @Override
    public ContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContentBinding binding = ItemContentBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ContentHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentHolder holder, int position) {
        if (holder==null){
            return;
        }

        ChapterDetailBean bean = list.get(position);
        holder.sentenceView.setText(LibHelpUtil.transTitleStyle(bean.getSentence()));
        holder.sentenceCnView.setText(bean.getSentenceCn());

        if (selectIndex == position){
            holder.sentenceView.setTextColor(Color.parseColor("#2983c1"));
            holder.sentenceCnView.setTextColor(Color.parseColor("#2983c1"));
        }else {
            holder.sentenceView.setTextColor(LibResUtil.getInstance().getColor(R.color.black));
            holder.sentenceCnView.setTextColor(LibResUtil.getInstance().getColor(R.color.black));
        }

        if (showTextType.equals(TypeLibrary.TextShowType.ALL)){
            holder.sentenceView.setVisibility(View.VISIBLE);
            holder.sentenceCnView.setVisibility(View.VISIBLE);
        }else if (showTextType.equals(TypeLibrary.TextShowType.EN)){
            holder.sentenceView.setVisibility(View.VISIBLE);
            holder.sentenceCnView.setVisibility(View.INVISIBLE);
        }else if (showTextType.equals(TypeLibrary.TextShowType.CN)){
            holder.sentenceView.setVisibility(View.INVISIBLE);
            holder.sentenceCnView.setVisibility(View.VISIBLE);
        }

        holder.sentenceView.setOnClickWordListener(new SelectWordTextView.OnClickWordListener() {
            @Override
            public void onClickWord(String selectText) {
                //查询单词并且收藏操作
                if (onWordSearchListener!=null){
                    onWordSearchListener.onWordSearch(selectText);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class ContentHolder extends RecyclerView.ViewHolder{

        private SelectWordTextView sentenceView;
        private TextView sentenceCnView;

        public ContentHolder(ItemContentBinding binding){
            super(binding.getRoot());

            sentenceView = binding.sentence;
            sentenceCnView = binding.sentenceCn;
        }
    }

    //刷新数据
    public void refreshData(List<ChapterDetailBean> refreshList){
        this.list = refreshList;
        notifyDataSetChanged();
    }

    //刷新选中的位置
    public void refreshIndex(int index){
        this.selectIndex = index;
        notifyDataSetChanged();
    }

    //刷新显示文本的类型
    public void refreshShowTextType(String showType){
        this.showTextType = showType;
        notifyDataSetChanged();
    }

    //获取当前选中的位置
    public int getSelectIndex(){
        return selectIndex;
    }

    //回调单词查询
    private onWordSearchListener onWordSearchListener;

    public interface onWordSearchListener{
        //查询操作
        void onWordSearch(String word);
    }

    public void setOnWordSearchListener(onWordSearchListener onWordSearchListener) {
        this.onWordSearchListener = onWordSearchListener;
    }
}
