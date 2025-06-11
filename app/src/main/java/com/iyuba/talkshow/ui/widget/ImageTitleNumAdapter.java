//package com.iyuba.talkshow.ui.widget;
//
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.signature.StringSignature;
//import com.iyuba.iyubamovies.model.DataBean;
//import com.iyuba.lib_common.data.Constant;
//import com.iyuba.talkshow.R;
//import com.youth.banner.adapter.BannerAdapter;
//
//import java.util.List;
//
///**
// * Created by carl shen on 21/12/16.
// *
// */
//public class ImageTitleNumAdapter extends BannerAdapter<DataBean, ImageTitleNumAdapter.BannerViewHolder> {
//
//    public ImageTitleNumAdapter(List<DataBean> mDatas) {
//        super(mDatas);
//    }
//
//    @Override
//    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_image_title_num, parent, false);
//        return new BannerViewHolder(view);
//    }
//
//    @Override
//    public void onBindView(BannerViewHolder holder, DataBean data, int position, int size) {
//        if (TextUtils.isEmpty(data.imageUrl)) {
//            holder.imageView.setImageResource(data.imageRes);
//        } else {
//            Glide.with(holder.itemView.getContext())
//                    .load(Constant.Url.AD_PIC + data.imageUrl)
//                    .signature(new StringSignature(data.imageUrl))
//                    .placeholder(com.iyuba.iyubamovies.R.drawable.imovies_loading)
//                    .into(holder.imageView);
//        }
//        holder.title.setText(data.title);
//        holder.numIndicator.setText((position + 1) + "/" + size);
//    }
//
//
//    class BannerViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//        TextView title;
//        TextView numIndicator;
//
//        public BannerViewHolder(@NonNull View view) {
//            super(view);
//            imageView = view.findViewById(R.id.image);
//            title = view.findViewById(R.id.bannerTitle);
//            numIndicator = view.findViewById(R.id.numIndicator);
//        }
//    }
//
//}
