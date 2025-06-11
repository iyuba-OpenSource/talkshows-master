//package com.iyuba.iyubamovies.ui.view;
//
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.signature.StringSignature;
//import com.iyuba.iyubamovies.R;
//import com.iyuba.iyubamovies.model.DataBean;
//import com.youth.banner.adapter.BannerAdapter;
//
//import java.util.List;
//
///**
// * Created by carl shen on 21/12/16.
// *
// */
//public class ImageTitleAdapter extends BannerAdapter<DataBean, ImageTitleHolder> {
//
//    public ImageTitleAdapter(List<DataBean> mDatas) {
//        super(mDatas);
//    }
//
//    @Override
//    public ImageTitleHolder onCreateHolder(ViewGroup parent, int viewType) {
//        return new ImageTitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_image_title, parent, false));
//    }
//
//    @Override
//    public void onBindView(ImageTitleHolder holder, DataBean data, int position, int size) {
//        if (TextUtils.isEmpty(data.imageUrl)) {
//            holder.imageView.setImageResource(data.imageRes);
//        } else {
//            Glide.with(holder.itemView.getContext())
//                    .load(data.imageUrl)
//                    .signature(new StringSignature(data.imageUrl))
//                    .placeholder(R.drawable.imovies_loading)
//                    .into(holder.imageView);
//        }
//        holder.title.setText(data.title);
//    }
//
//}
