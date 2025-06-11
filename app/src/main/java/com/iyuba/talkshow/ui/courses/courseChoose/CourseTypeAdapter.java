package com.iyuba.talkshow.ui.courses.courseChoose;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.databinding.CourseChooseTitlenewBinding;
import com.iyuba.talkshow.databinding.CourseChooseTypenewBinding;
import com.iyuba.talkshow.ui.courses.common.TypeHolder;

import java.util.List;

public class CourseTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Callback callback;
    Context context;


    public CourseTypeAdapter(List<TypeHolder> types) {
        this.types = generateTypes(types);
    }

    private List<TypeHolder> generateTypes(List<TypeHolder> types) {
        for (int i = 0; i <types.size() ; i++) {
            if (types.get(i).getId()==0&& i!= 0){
                if (i%5==0) continue;
                for (int j = 0; j < 5-i%5 ; j++){
                    types.add(i,new TypeHolder(1,""));
                }
            }
        }
        return types ;
    }

    private int mActiviteHolder = 1;

    private List<TypeHolder> types ;

    private enum ITEM_TYPE {
        TITLE, COURSE_TYPE
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =parent.getContext();
        if (viewType == ITEM_TYPE.TITLE.ordinal()) {
            CourseChooseTitlenewBinding bindingTitle = CourseChooseTitlenewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new TitleViewHolder(bindingTitle);
        } else if (viewType == ITEM_TYPE.COURSE_TYPE.ordinal()) {
            CourseChooseTypenewBinding bindingType = CourseChooseTypenewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new TypeViewHolder(bindingType);
        }
        return null ;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof TypeViewHolder){
            TypeViewHolder holder = (TypeViewHolder) viewHolder;
            holder.setItem(types.get(i),i);
            holder.setClick();
        }else {
            TitleViewHolder holder = (TitleViewHolder) viewHolder;
            holder.setItem(types.get(i));
            holder.setClick();
        }
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (types.get(position).getId()== 0 ) {
            type = ITEM_TYPE.TITLE.ordinal();
        } else  {
            type = ITEM_TYPE.COURSE_TYPE.ordinal();
        }
        return type;
    }

    public interface Callback {
        void onTypeChoose(int id);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public class TypeViewHolder extends RecyclerView.ViewHolder {

        int pos ;
        private TextView type;
        private RelativeLayout item;

        public TypeViewHolder(@NonNull CourseChooseTypenewBinding itemView) {
            super(itemView.getRoot());
            type = itemView.type;
            item = itemView.item;
        }

        public void setItem(TypeHolder typeHolder, int pos) {
            type.setText(typeHolder.getValue());
            this.pos = pos ;
            if (mActiviteHolder == pos){
                type.setTextColor(context.getResources().getColor(R.color.WHITE));
                type.setBackgroundResource(R.drawable.green_corner);
            }else {
                type.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                type.setBackgroundResource(R.drawable.transparent_corner);
            }
        }

        public void clickType(){
            if (types.get(pos).getId()==1) return;
            mActiviteHolder = pos ;
            notifyDataSetChanged();
            callback.onTypeChoose(types.get(pos).getId());
        }

        public void setClick() {
            item.setOnClickListener(v -> clickType());
        }
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public TitleViewHolder(@NonNull CourseChooseTitlenewBinding itemView) {
            super(itemView.getRoot());
            title = itemView.title;

        }
        public void setItem(TypeHolder typeHolder) {
            title.setText(typeHolder.getValue());
        }

        public void setClick() {

        }

    }

}
