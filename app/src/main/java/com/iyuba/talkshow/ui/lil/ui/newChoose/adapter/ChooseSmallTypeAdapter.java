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
//import com.iyuba.talkshow.databinding.ItemChooseJuniorTypeBinding;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.bean.TypeShowBean;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.listener.OnSelectListener;
//
//import java.util.List;
//
//public class ChooseSmallTypeAdapter extends RecyclerView.Adapter<ChooseSmallTypeAdapter.SmallTypeHolder> {
//
//    private Context context;
//    private List<TypeShowBean.SmallTypeBean> list;
//
//    //选中的数据
//    private int selectIndex = 0;
//
//    public ChooseSmallTypeAdapter(Context context, List<TypeShowBean.SmallTypeBean> list) {
//        this.context = context;
//        this.list = list;
//    }
//
//    @NonNull
//    @Override
//    public SmallTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        ItemChooseJuniorTypeBinding binding = ItemChooseJuniorTypeBinding.inflate(LayoutInflater.from(context),parent,false);
//        return new SmallTypeHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SmallTypeHolder holder, int position) {
//        if (holder==null){
//            return;
//        }
//
//        String text = list.get(position).getName();
//        holder.textView.setText(text);
//
//        if (selectIndex == position){
//            holder.textView.setBackgroundResource(R.drawable.shape_corner_theme_border_10dp);
//            holder.textView.setTextColor(LibResUtil.getInstance().getColor(R.color.colorPrimary));
//        }else {
//            holder.textView.setBackgroundResource(0);
//            holder.textView.setTextColor(LibResUtil.getInstance().getColor(R.color.black));
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
//    class SmallTypeHolder extends RecyclerView.ViewHolder{
//
//        private TextView textView;
//
//        public SmallTypeHolder(ItemChooseJuniorTypeBinding binding){
//            super(binding.getRoot());
//
//            textView  = binding.textView;
//        }
//    }
//
//    //刷新数据
//    public void refreshData(List<TypeShowBean.SmallTypeBean> refreshList){
//        this.list = refreshList;
//        notifyDataSetChanged();
//    }
//
//    //刷新数据和位置
//    public void refreshDataAndIndex(List<TypeShowBean.SmallTypeBean> refreshList,int index){
//        this.list = refreshList;
//        this.selectIndex = index;
//        notifyDataSetChanged();
//    }
//
//    //接口
//    private OnSelectListener<TypeShowBean.SmallTypeBean> onSelectListener;
//
//    public void setOnSelectListener(OnSelectListener<TypeShowBean.SmallTypeBean> onSelectListener) {
//        this.onSelectListener = onSelectListener;
//    }
//}
