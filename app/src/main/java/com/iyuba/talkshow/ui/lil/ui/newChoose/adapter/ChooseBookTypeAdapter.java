//package com.iyuba.talkshow.ui.lil.ui.newChoose.adapter;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.iyuba.lib_common.util.LibGlide3Util;
//import com.iyuba.talkshow.databinding.ItemChooseBookBinding;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.bean.BookShowBean;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.listener.OnSelectListener;
//
//import java.util.List;
//
//public class ChooseBookTypeAdapter extends RecyclerView.Adapter<ChooseBookTypeAdapter.BookTypeHolder> {
//
//    private Context context;
//    private List<BookShowBean> list;
//
//    public ChooseBookTypeAdapter(Context context, List<BookShowBean> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @NonNull
//    @Override
//    public BookTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        ItemChooseBookBinding binding = ItemChooseBookBinding.inflate(LayoutInflater.from(context),parent,false);
//        return new BookTypeHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BookTypeHolder holder, int position) {
//        if (holder==null){
//            return;
//        }
//
//        BookShowBean book = list.get(position);
//        LibGlide3Util.loadImg(context,book.getPic(),0,holder.bookPic);
//        holder.bookName.setText(book.getName());
//
//        if (TextUtils.isEmpty(book.getPic())){
//            holder.bookPicLayout.setVisibility(View.GONE);
//            holder.bookNameLayout.setVisibility(View.VISIBLE);
//        }else {
//            holder.bookPicLayout.setVisibility(View.VISIBLE);
//            holder.bookNameLayout.setVisibility(View.GONE);
//        }
//
//        holder.bookTitle.setText(book.getName());
//
//        holder.itemView.setOnClickListener(v->{
//            if (onSelectListener!=null){
//                onSelectListener.onSelect(book);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return list==null?0:list.size();
//    }
//
//    class BookTypeHolder extends RecyclerView.ViewHolder{
//
//        private CardView bookNameLayout;
//        private TextView bookName;
//        private CardView bookPicLayout;
//        private ImageView bookPic;
//
//        private TextView bookTitle;
//
//        public BookTypeHolder(ItemChooseBookBinding binding){
//            super(binding.getRoot());
//
//            bookNameLayout = binding.bookNameLayout;
//            bookName = binding.bookName;
//            bookPicLayout = binding.bookPicLayout;
//            bookPic = binding.bookPic;
//
//            bookTitle = binding.bookTitle;
//        }
//    }
//
//    //刷新数据
//    public void refreshData(List<BookShowBean> refreshList){
//        this.list = refreshList;
//        notifyDataSetChanged();
//    }
//
//    //接口
//    private OnSelectListener<BookShowBean> onSelectListener;
//
//    public void setOnSelectListener(OnSelectListener<BookShowBean> onSelectListener) {
//        this.onSelectListener = onSelectListener;
//    }
//}
