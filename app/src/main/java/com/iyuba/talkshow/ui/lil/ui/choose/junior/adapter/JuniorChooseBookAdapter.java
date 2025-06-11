package com.iyuba.talkshow.ui.lil.ui.choose.junior.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.lib_common.listener.OnSimpleClickListener;
import com.iyuba.lib_common.model.local.entity.BookEntity_junior;
import com.iyuba.lib_common.util.LibGlide3Util;
import com.iyuba.talkshow.databinding.ItemChooseBookBinding;

import java.util.List;

/**
 * @title: 书籍的适配器
 * @date: 2023/5/22 09:44
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class JuniorChooseBookAdapter extends RecyclerView.Adapter<JuniorChooseBookAdapter.BookHolder> {

    private Context context;
    private List<BookEntity_junior> list;

    public JuniorChooseBookAdapter(Context context, List<BookEntity_junior> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChooseBookBinding binding = ItemChooseBookBinding.inflate(LayoutInflater.from(context),parent,false);
        return new BookHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        if (holder==null){
            return;
        }

        BookEntity_junior book = list.get(position);
        LibGlide3Util.loadImg(context,book.pic,0,holder.bookPic);
        holder.bookName.setText(book.seriesName);

        if (TextUtils.isEmpty(book.pic)){
            holder.bookPicLayout.setVisibility(View.GONE);
            holder.bookNameLayout.setVisibility(View.VISIBLE);
        }else {
            holder.bookPicLayout.setVisibility(View.VISIBLE);
            holder.bookNameLayout.setVisibility(View.GONE);
        }

        holder.bookTitle.setText(book.seriesName);

        holder.itemView.setOnClickListener(v->{
            if (listener!=null){
                listener.onClick(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class BookHolder extends RecyclerView.ViewHolder{

        private CardView bookNameLayout;
        private TextView bookName;
        private CardView bookPicLayout;
        private ImageView bookPic;

        private TextView bookTitle;

        public BookHolder(ItemChooseBookBinding binding){
            super(binding.getRoot());

            bookNameLayout = binding.bookNameLayout;
            bookName = binding.bookName;
            bookPicLayout = binding.bookPicLayout;
            bookPic = binding.bookPic;

            bookTitle = binding.bookTitle;
        }
    }

    //刷新数据
    public void refreshData(List<BookEntity_junior> refreshList){
        this.list = refreshList;
        notifyDataSetChanged();
    }

    //回调
    private OnSimpleClickListener<BookEntity_junior> listener;

    public void setListener(OnSimpleClickListener<BookEntity_junior> listener) {
        this.listener = listener;
    }
}
