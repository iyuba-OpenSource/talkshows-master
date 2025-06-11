package com.iyuba.talkshow.util.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.iyuba.lib_common.data.Constant;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load(Constant.Url.AD_PIC+path)
                .into(imageView);
    }
}
