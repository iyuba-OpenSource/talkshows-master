package com.iyuba.talkshow.ui.lil.ui.imooc;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.iyuba.imooclib.IMooc;
import com.iyuba.imooclib.ImoocManager;
import com.iyuba.imooclib.ui.mobclass.MobClassFragment;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_common.ui.BaseViewBindingFragment;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.AdTestKeyData;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.databinding.LayoutContainerBinding;
import com.iyuba.talkshow.ui.lil.ui.ad.util.show.AdShowUtil;
import com.iyuba.talkshow.ui.lil.util.ScreenUtil;

import java.util.ArrayList;

/**
 * 微课界面
 */
public class ImoocFragment extends BaseViewBindingFragment<LayoutContainerBinding> {

    public static ImoocFragment getInstance(boolean isShowBack){
        ImoocFragment fragment = new ImoocFragment();
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

        showImooc();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //显示微课
    public void showImooc(){
        //设置appId
        ImoocManager.appId = String.valueOf(App.APP_ID);
        //根据要求显示有道广告
        IMooc.setAdAppId(String.valueOf(AdShowUtil.NetParam.AppId));
        IMooc.setStreamAdPosition(AdShowUtil.NetParam.SteamAd_startIndex,AdShowUtil.NetParam.SteamAd_intervalIndex);
        IMooc.setYoudaoId(AdTestKeyData.KeyData.TemplateAdKey.template_youdao);
        IMooc.setYdsdkTemplateKey(AdTestKeyData.KeyData.TemplateAdKey.template_csj,AdTestKeyData.KeyData.TemplateAdKey.template_ylh,AdTestKeyData.KeyData.TemplateAdKey.template_ks,AdTestKeyData.KeyData.TemplateAdKey.template_baidu,AdTestKeyData.KeyData.TemplateAdKey.template_vlion);
        //设置广告自适应
        int adWidth = ScreenUtil.getScreenW(getActivity());
        IMooc.setYdsdkTemplateAdWidthHeight(adWidth,0);

        //是否回退
        boolean isShowBack = getArguments().getBoolean(StrLibrary.back,false);

        //内容显示
        ArrayList<Integer> typeIdFilter = new ArrayList<>();
        //类型显示
        int ownerId = 3;
        if (getActivity().getPackageName().equals(Constant.PackageName.PACKAGE_pappa)){
            typeIdFilter.add(25);//小学
            typeIdFilter.add(21);//新概念
            typeIdFilter.add(3);//voa

            ownerId = 25;
        }else {
            typeIdFilter.add(3);//voa
            typeIdFilter.add(27);

            ownerId = 3;
        }
        //设置数据显示(这里原来是3，但是应该是sdk限制了，设置成25就显示了)
        Bundle args = MobClassFragment.buildArguments(ownerId, isShowBack, typeIdFilter);
        MobClassFragment mMocFragment = MobClassFragment.newInstance(args);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.container,mMocFragment).show(mMocFragment).commitNowAllowingStateLoss();
    }
}
