//package com.iyuba.talkshow.util.promotion;
//
//import android.app.Activity;
//import android.content.res.TypedArray;
//
//import com.iyuba.talkshow.R;
//
//
//public class PromotionChannelHelper {
//
//    private static final int DEFAULT_IMAGE_POSITION = 0;
//
//    @BindString(R.string.channel_360)
//    String mChannel360Name;
//
//    @BindArray(R.array.promotion_channel_images)
//    TypedArray icons;
//    @BindArray(R.array.promotion_channels)
//    String[] names;
//    @BindString(R.string.app_promotion_channel)
//    String mPromotionChannelName;
//    private int mPromotionChannelImageId;
//
//    public PromotionChannelHelper(Activity activity) {
//        ButterKnife.bind(this, activity);
//        int len = icons.length();
//        int[] imageIds = new int[len];
//        for (int i = 0; i < len; i += 1)
//            imageIds[i] = icons.getResourceId(i, 0);
//        icons.recycle();
//        mPromotionChannelImageId = imageIds[DEFAULT_IMAGE_POSITION];
//        for (int i = 0; i < names.length; i += 1) {
//            if (names[i].equals(mPromotionChannelName)) {
//                mPromotionChannelImageId = imageIds[i];
//                break;
//            }
//        }
//    }
//
//    public int getChannelResourceId() {
//        return mPromotionChannelImageId;
//    }
//
//    public String getChannelName() {
//        return mPromotionChannelName;
//    }
//
//    public boolean isShoufa360() {
//        return mPromotionChannelName.equals(mChannel360Name);
//    }
//}
