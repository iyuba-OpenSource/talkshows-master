package com.iyuba.talkshow.ui.list.show;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.model.Category;
import com.iyuba.talkshow.databinding.ItemListMenuBinding;

import java.util.List;

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.CategoryHolder> {

    private Context context;
    private List<Category> list;

    //选中的位置
    private int selectIndex = 0;

    public ListCategoryAdapter(Context context, List<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListMenuBinding binding = ItemListMenuBinding.inflate(LayoutInflater.from(context),parent,false);
        return new CategoryHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        if (holder==null){
            return;
        }

        Category category = list.get(position);
        holder.textView.setText(category.getName());
        if (selectIndex==position){
            holder.textView.setTextColor(context.getResources().getColor(R.color.white));
            holder.textView.setBackgroundResource(R.drawable.shape_btn_theme);
        }else {
            holder.textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.textView.setBackgroundResource(0);
        }
        holder.itemView.setOnClickListener(v->{
            if (selectIndex==position){
                return;
            }

            selectIndex = position;
            notifyDataSetChanged();

            if (onItemMenuClickListener!=null){
                onItemMenuClickListener.onClick(position,category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public CategoryHolder(ItemListMenuBinding binding){
            super(binding.getRoot());

            textView = binding.listText;
        }
    }

    //设置选中位置
    public void setSelect(int index){
        if (index<0 || index>list.size()-1){
            index = 0;
        }

        if (list!=null && list.size()>0){
            selectIndex = index;
            notifyDataSetChanged();
        }
    }

    //接口
    public OnItemMenuClickListener onItemMenuClickListener;

    public interface OnItemMenuClickListener{
        void onClick(int position,Category category);
    }

    public void setOnItemMenuClickListener(OnItemMenuClickListener onItemMenuClickListener) {
        this.onItemMenuClickListener = onItemMenuClickListener;
    }
}