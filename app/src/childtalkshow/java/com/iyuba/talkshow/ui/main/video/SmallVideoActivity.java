//package com.iyuba.talkshow.ui.main.video;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.iyuba.headlinelibrary.HeadlineType;
//import com.iyuba.headlinelibrary.IHeadline;
//import com.iyuba.headlinelibrary.IHeadlineManager;
//import com.iyuba.headlinelibrary.ui.title.DropdownTitleFragmentNew;
//import com.iyuba.lib_common.data.Constant;
//import com.iyuba.talkshow.R;
//import com.iyuba.talkshow.constant.App;
//import com.iyuba.talkshow.databinding.ActivityVideoSmallBinding;
//import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
//
///**
// * 小视频界面
// */
//public class SmallVideoActivity extends BaseViewBindingActivity<ActivityVideoSmallBinding> {
//
//    public static void start(Context context){
//        context.startActivity(new Intent(context,SmallVideoActivity.class));
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//
//        //这里注意修改共通模块的url
//        IHeadline.resetMseUrl();
//        String extraUrl = "http://iuserspeech." + Constant.Web.WEB_SUFFIX.replace("/", "") + ":9001/test/ai/";
//        IHeadline.setExtraMseUrl(extraUrl);
//        String extraMergeUrl = "http://iuserspeech." + Constant.Web.WEB_SUFFIX.replace("/", "") + ":9001/test/merge/";
//        IHeadline.setExtraMergeAudioUrl(extraMergeUrl);
//        //替换为小视频
//        IHeadlineManager.appId = String.valueOf(App.APP_ID);
//        IHeadlineManager.appName = App.APP_NAME_EN;
//设置广告类型
//        IHeadline.setAdAppId(String.valueOf(AdShowUtil.NetParam.AppId));
//        IHeadline.setStreamAdPosition(AdShowUtil.NetParam.SteamAd_startIndex,AdShowUtil.NetParam.SteamAd_intervalIndex);
//        IHeadline.setYoudaoStreamId(AdTestKeyData.KeyData.TemplateAdKey.template_youdao);
//        IHeadline.setYdsdkTemplateKey(AdTestKeyData.KeyData.TemplateAdKey.template_csj,AdTestKeyData.KeyData.TemplateAdKey.template_ylh,AdTestKeyData.KeyData.TemplateAdKey.template_ks,AdTestKeyData.KeyData.TemplateAdKey.template_baidu,AdTestKeyData.KeyData.TemplateAdKey.template_vlion);
//
//        String[] types = new String[]{
//                HeadlineType.SMALLVIDEO
//        };
//
//        Bundle videoBundle = DropdownTitleFragmentNew.buildArguments(10,types,true);
//        DropdownTitleFragmentNew fragmentNew = DropdownTitleFragmentNew.newInstance(videoBundle);
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(binding.frameLayout.getId(),fragmentNew);
//        transaction.show(fragmentNew).commit();
//    }
//}
