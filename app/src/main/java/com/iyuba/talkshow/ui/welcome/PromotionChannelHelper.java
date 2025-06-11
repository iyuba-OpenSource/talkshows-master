//package com.iyuba.talkshow.ui.welcome;
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
//    TypedArray mIcons;
//    @BindArray(R.array.promotion_channels)
//    String[] mNames;
//    @BindString(R.string.app_promotion_channel)
//    String mPromotionChannelName;
//    private int mPromotionChannelImageId;
//
//    public PromotionChannelHelper(Activity activity) {
//        ButterKnife.bind(this, activity);
//        int len = mIcons.length();
//        int[] imageIds = new int[len];
//        for (int i = 0; i < len; i++)
//            imageIds[i] = mIcons.getResourceId(i, 0);
//        mIcons.recycle();
//        mPromotionChannelImageId = imageIds[DEFAULT_IMAGE_POSITION];
//        for (int i = 0; i < mNames.length; i += 1) {
//            if (mNames[i].equals(mPromotionChannelName)) {
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
