package com.iyuba.talkshow.ui.lil.ui.video;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.IHeadline;
import com.iyuba.headlinelibrary.IHeadlineManager;
import com.iyuba.headlinelibrary.ui.title.DropdownTitleFragmentNew;
import com.iyuba.imooclib.IMooc;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_common.ui.BaseViewBindingFragment;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.AdTestKeyData;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.databinding.LayoutContainerBinding;
import com.iyuba.talkshow.ui.lil.ui.ad.util.show.AdShowUtil;
import com.iyuba.talkshow.ui.lil.util.ScreenUtil;

/**
 * 视频界面
 */
public class SmallVideoFragment extends BaseViewBindingFragment<LayoutContainerBinding> {

    public static SmallVideoFragment getInstance(boolean isShowBack){
        SmallVideoFragment fragment = new SmallVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(StrLibrary.back,isShowBack);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showVideoView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //显示视频界面
    private void showVideoView(){
        //这里注意修改共通模块的url
        IHeadline.resetMseUrl();
        String extraUrl = "http://iuserspeech." + Constant.Web.WEB_SUFFIX.replace("/", "") + ":9001/test/ai/";
        IHeadline.setExtraMseUrl(extraUrl);
        String extraMergeUrl = "http://iuserspeech." + Constant.Web.WEB_SUFFIX.replace("/", "") + ":9001/test/merge/";
        IHeadline.setExtraMergeAudioUrl(extraMergeUrl);
        //替换为小视频
        IHeadlineManager.appId = String.valueOf(App.APP_ID);
        IHeadlineManager.appName = App.APP_NAME_EN;
        //设置广告类型
        IHeadline.setAdAppId(String.valueOf(AdShowUtil.NetParam.AppId));
        IHeadline.setStreamAdPosition(AdShowUtil.NetParam.SteamAd_startIndex,AdShowUtil.NetParam.SteamAd_intervalIndex);
        IHeadline.setYoudaoStreamId(AdTestKeyData.KeyData.TemplateAdKey.template_youdao);
        IHeadline.setYdsdkTemplateKey(AdTestKeyData.KeyData.TemplateAdKey.template_csj,AdTestKeyData.KeyData.TemplateAdKey.template_ylh,AdTestKeyData.KeyData.TemplateAdKey.template_ks,AdTestKeyData.KeyData.TemplateAdKey.template_baidu,AdTestKeyData.KeyData.TemplateAdKey.template_vlion);
        //设置广告自适应
        int adWidth = ScreenUtil.getScreenW(getActivity());
        IMooc.setYdsdkTemplateAdWidthHeight(adWidth,0);

        String[] types = new String[]{
                HeadlineType.SMALLVIDEO
        };

        //是否显示返回按钮
        boolean isShowBack = getArguments().getBoolean(StrLibrary.back,false);

        //设置视频样式
        Bundle videoBundle = DropdownTitleFragmentNew.buildArguments(10,types,isShowBack);
        DropdownTitleFragmentNew videoFragment = DropdownTitleFragmentNew.newInstance(videoBundle);
        //展示视频界面
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.container,videoFragment).show(videoFragment).commitNowAllowingStateLoss();
    }
}
