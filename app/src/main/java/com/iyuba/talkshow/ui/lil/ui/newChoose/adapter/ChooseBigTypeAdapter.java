//package com.iyuba.talkshow.ui.lil.ui.newChoose.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.iyuba.lib_common.util.LibResUtil;
//import com.iyuba.talkshow.R;
//import com.iyuba.talkshow.databinding.ItemChooseJuniorPublishBinding;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.bean.TypeShowBean;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.listener.OnSelectListener;
//
//import java.util.List;
//
//public class ChooseBigTypeAdapter extends RecyclerView.Adapter<ChooseBigTypeAdapter.BigTypeHolder> {
//
//    private Context context;
//    private List<TypeShowBean> list;
//
//    //选中的数据
//    private int  selectIndex = 0;
//
//    public ChooseBigTypeAdapter(Context context, List<TypeShowBean> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @NonNull
//    @Override
//    public BigTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        ItemChooseJuniorPublishBinding binding = ItemChooseJuniorPublishBinding.inflate(LayoutInflater.from(context),parent,false);
//        return new BigTypeHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BigTypeHolder holder, int position) {
//        if (holder==null){
//            return;
//        }
//
//        String text = list.get(position).getType();
//        holder.textView.setText(text);
//
//        if (selectIndex == position){
//            holder.textView.setBackgroundResource(R.drawable.shape_corner_theme_10dp);
//            holder.textView.setTextColor(LibResUtil.getInstance().getColor(R.color.white));
//        }else {
//            holder.textView.setBackgroundResource(0);
//            holder.textView.setTextColor(LibResUtil.getInstance().getColor(R.color.colorPrimary));
//        }
//
//        holder.itemView.setOnClickListener(v->{
//            if (selectIndex==position){
//                return;
//            }
//
//            //刷新显示
//            selectIndex = position;
//            notifyDataSetChanged();
//
//            if (onSelectListener!=null){
//                onSelectListener.onSelect(list.get(position));
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return list==null?0:list.size();
//    }
//
//    class BigTypeHolder extends RecyclerView.ViewHolder{
//
//        private TextView textView;
//
//        public BigTypeHolder(ItemChooseJuniorPublishBinding binding){
//            super(binding.getRoot());
//
//            textView = binding.textView;
//        }
//    }
//
//    //刷新数据和位置
//    public void refreshData(List<TypeShowBean> refreshList){
//        this.list = refreshList;
//        notifyDataSetChanged();
//    }
//
//    //接口
//    private OnSelectListener<TypeShowBean> onSelectListener;
//
//    public void setOnSelectListener(OnSelectListener<TypeShowBean> onSelectListener) {
//        this.onSelectListener = onSelectListener;
//    }
//}
