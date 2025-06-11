package com.iyuba.talkshow.ui.vip.buyvip;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_common.event.RefreshDataEvent;
import com.iyuba.lib_common.util.LibDateUtil;
import com.iyuba.lib_common.util.LibGlide3Util;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.QQResponse;
import com.iyuba.talkshow.databinding.VipCenterBinding;
import com.iyuba.talkshow.event.LoginEvent;
import com.iyuba.talkshow.event.VIpChangeEvent;
import com.iyuba.talkshow.http.Http;
import com.iyuba.talkshow.http.HttpCallback;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.vip.buyiyubi.BuyIyubiActivity;
import com.iyuba.talkshow.ui.vip.payorder.PayOrderActivity;
import com.iyuba.talkshow.ui.web.WebActivity;
import com.iyuba.talkshow.ui.widget.MyGridAdapter;
import com.iyuba.talkshow.util.BrandUtil;
import com.iyuba.talkshow.util.ToastUtil;
import com.iyuba.talkshow.util.Util;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import okhttp3.Call;
import personal.iyuba.personalhomelibrary.ui.home.PersonalHomeActivity;

public class  NewVipCenterActivity extends BaseViewBindingActivity<VipCenterBinding> {
    public static final String HUI_YUAN = "HUI_YUAN";
    public static final int QUANZHAN = 0;
    public static final int BENYINGYONG = 1;
    public static final int HUANGJIN = 2;
    private int huiYuan = 0 ;

    @Inject
    DataManager mDataManager;

    private Intent intent;
    CheckBox[] cbList;
    private double price;

    @Inject
    ConfigManager mConfigManager;

    public static void start(Context context,int vipType){
        Intent intent = new Intent();
        intent.setClass(context,NewVipCenterActivity.class);
        intent.putExtra(HUI_YUAN,vipType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        StatusBarUtil.setColor(this, Color.parseColor("#FDDA94"), 0);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        BrandUtil.requestQQGroupNumber(mDataManager.getPreferencesHelper(), manager.getUid(), App.APP_ID);
        //这里统一修改成在主界面进行请求
//        requestQQNumber();
        cbList = new CheckBox[]{binding.benyingyongLl.cbBenyingyong1,
                binding.benyingyongLl.cbBenyingyong2,
                binding.quanzhanLl.cbQuanzhan1,binding.quanzhanLl. cbQuanzhan2,
                binding.quanzhanLl.cbQuanzhan3, binding.quanzhanLl.cbQuanzhan4, binding.quanzhanLl.cbQuanzhan5,
                binding.benyingyongLl.cbBenyingyong3,binding.benyingyongLl.cbBenyingyong4,
                binding.vipgoldLl.goldAppCb1,binding.vipgoldLl.goldAppCb2,
                binding.vipgoldLl.goldAppCb3,binding.vipgoldLl.goldAppCb4};
//        setDes();
        setClick();
        EventBus.getDefault().register(this);
        binding.tvVipBenyingyong.performClick();
        MyGridAdapter adapter = new MyGridAdapter(this);
        adapter.setCallback(new MyGridAdapter.ClickCallback() {
            @Override
            public void onClick(String hint, int position) {
                if (position == 7 ){
                    showDialogClickable(hint);
                }else {
                    ToastUtil.showToast(mContext, hint);
                }
            }
        });
        binding.quanzhanLl.gridview.setAdapter(adapter);
        if (App.APP_ID > 280) {
            binding.tvVipInfo.setText("VIP权限说明 (不含视频和微课)");
            binding.tvVipHuangjin.setVisibility(View.GONE);
            binding.vipgoldLl.getRoot().setVisibility(View.GONE);
        }

        binding.quanzhanLl.getRoot().setVisibility(View.GONE);
        huiYuan = getIntent().getIntExtra(HUI_YUAN, BENYINGYONG);
        setGroupVisble(huiYuan);
    }
    //    @OnClick({R.id.tv_vip_quanzhan, R.id.tv_vip_benyingyong,
//            R.id.cb_quanzhan1, R.id.rl_quanzhan1, R.id.cb_quanzhan2, R.id.rl_quanzhan2,
//            R.id.cb_quanzhan3, R.id.rl_quanzhan3, R.id.cb_quanzhan4, R.id.rl_quanzhan4, R.id.cb_quanzhan5, R.id.rl_quanzhan5,
//            R.id.cb_benyingyong1, R.id.rl_benyingyong1, R.id.cb_benyingyong2, R.id.rl_benyingyong2,
//            R.id.go_buy, R.id.back, R.id.user_img,R.id.cb_benyingyong3,R.id.rl_benyingyong3,
//            R.id.cb_benyingyong4,R.id.rl_benyingyong4})
    private void setClick() {
        binding.btnServe.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(mContext, binding.btnServe);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.toolbarmenu, popup.getMenu());
            Menu menu = popup.getMenu();
            menu.getItem(0).setTitle(BrandUtil.getBrandChinese() + "用户群:" + BrandUtil.getQQGroupNumber(mDataManager.getPreferencesHelper()));
            menu.getItem(1).setTitle("内容QQ:" + BrandUtil.getQQEditor(mDataManager.getPreferencesHelper()));
            menu.getItem(2).setTitle("技术QQ:" + BrandUtil.getQQTechnician(mDataManager.getPreferencesHelper()));
            menu.getItem(3).setTitle("投诉QQ:" + BrandUtil.getQQManager(mDataManager.getPreferencesHelper()));
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=";
                    if (item.getItemId() == R.id.test_qq) {
                        Util.startQQGroup(mContext, BrandUtil.getQQGroupKey(mDataManager.getPreferencesHelper()));
                    } else if (item.getItemId() == R.id.content_qq) {
                        Util.startQQ(mContext, BrandUtil.getQQEditor(mDataManager.getPreferencesHelper()));
                    } else if (item.getItemId() == R.id.tycnolge_qq) {
                        Util.startQQ(mContext, BrandUtil.getQQTechnician(mDataManager.getPreferencesHelper()));
                    } else if (item.getItemId() == R.id.tousu_qq) {
                        Util.startQQ(mContext, BrandUtil.getQQManager(mDataManager.getPreferencesHelper()));
                    }
                    return true;
                }
            });
            popup.show();
        });
        binding.tvVipQuanzhan.setOnClickListener(v -> onViewClicked(v));
        binding.tvVipBenyingyong.setOnClickListener(v -> onViewClicked(v));
        binding.tvVipHuangjin.setOnClickListener(v -> onViewClicked(v));
        binding.quanzhanLl.cbQuanzhan1.setOnClickListener(v -> onViewClicked(v));
        binding.quanzhanLl.cbQuanzhan2.setOnClickListener(v -> onViewClicked(v));
        binding.quanzhanLl.cbQuanzhan3.setOnClickListener(v -> onViewClicked(v));
        binding.quanzhanLl.cbQuanzhan4.setOnClickListener(v -> onViewClicked(v));
        binding.quanzhanLl.cbQuanzhan5.setOnClickListener(v -> onViewClicked(v));
        binding.quanzhanLl.rlQuanzhan1.setOnClickListener(v -> onViewClicked(v));
        binding.quanzhanLl.rlQuanzhan2.setOnClickListener(v -> onViewClicked(v));
        binding.quanzhanLl.rlQuanzhan3.setOnClickListener(v -> onViewClicked(v));
        binding.quanzhanLl.rlQuanzhan4.setOnClickListener(v -> onViewClicked(v));
        binding.quanzhanLl.rlQuanzhan5.setOnClickListener(v -> onViewClicked(v));
        binding.benyingyongLl.cbBenyingyong1.setOnClickListener(v -> onViewClicked(v));
        binding.benyingyongLl.cbBenyingyong2.setOnClickListener(v -> onViewClicked(v));
        binding.benyingyongLl.cbBenyingyong3.setOnClickListener(v -> onViewClicked(v));
        binding.benyingyongLl.cbBenyingyong4.setOnClickListener(v -> onViewClicked(v));
        binding.benyingyongLl.rlBenyingyong1.setOnClickListener(v -> onViewClicked(v));
        binding.benyingyongLl.rlBenyingyong2.setOnClickListener(v -> onViewClicked(v));
        binding.benyingyongLl.rlBenyingyong3.setOnClickListener(v -> onViewClicked(v));
        binding.benyingyongLl.rlBenyingyong4.setOnClickListener(v -> onViewClicked(v));
        binding.vipgoldLl.goldAppCb1.setOnClickListener(v -> onViewClicked(v));
        binding.vipgoldLl.goldAppCb2.setOnClickListener(v -> onViewClicked(v));
        binding.vipgoldLl.goldAppCb3.setOnClickListener(v -> onViewClicked(v));
        binding.vipgoldLl.goldAppCb4.setOnClickListener(v -> onViewClicked(v));
        binding.vipgoldLl.goldAppRe1.setOnClickListener(v -> onViewClicked(v));
        binding.vipgoldLl.goldAppRe2.setOnClickListener(v -> onViewClicked(v));
        binding.vipgoldLl.goldAppRe3.setOnClickListener(v -> onViewClicked(v));
        binding.vipgoldLl.goldAppRe4.setOnClickListener(v -> onViewClicked(v));
        binding.btnBuyiyuba.setOnClickListener(v -> onViewClicked(v));
        binding.userImg.setOnClickListener(v -> onViewClicked(v));
        binding.goBuy.setOnClickListener(v -> onViewClicked(v));
        binding.back.setOnClickListener(v -> onViewClicked(v));
    }

    private void showDialogClickable(String message) {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = WebActivity.buildIntent(mContext, Constant.Url.MORE_APP);
                startActivity(intent);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
            }
        } ;
        View view = LayoutInflater.from(mContext).inflate(R.layout.alert_text,null);
        TextView remindText = view.findViewById(R.id.remindText);
        String remindString = message ;

        SpannableStringBuilder spannableStringBuilder  = new SpannableStringBuilder(remindString);

        spannableStringBuilder.setSpan(clickableSpan, remindString.indexOf("app."+Constant.Web.WEB_SUFFIX.replace("/","")), remindString.indexOf("app."+Constant.Web.WEB_SUFFIX.replace("/",""))+12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        remindText.setText(spannableStringBuilder);
        remindText.setMovementMethod(LinkMovementMethod.getInstance());
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder .setView(view)
                .setPositiveButton("好的",null)
                .show();
    }

    public void requestQQNumber() {
        String url = "http://iuserspeech." + Constant.Web.WEB_SUFFIX.replace("/","") + ":9001/japanapi/getJpQQ.jsp?appid=" + App.APP_ID;
        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                try {
                    Log.e("NewVipCenterActivity", "requestQQNumber onSucceed result  " + response);
                    QQResponse bean = new Gson().fromJson(response, QQResponse.class);
                    if ((bean != null) && (bean.result == 200)) {
                        if ((bean.data != null) && (bean.data.size() > 0)) {
                            mConfigManager.setQQEditor(bean.data.get(0).editor);
                            mConfigManager.setQQTechnician(bean.data.get(0).technician);
                            mConfigManager.setQQManager(bean.data.get(0).manager);
                        } else {
                            Log.e("NewVipCenterActivity", "result ok, data is null? ");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                if (e != null) {
                    Log.e("NewVipCenterActivity", "onError  " + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDes();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @SuppressLint("SetTextI18n")
    private void setDes() {
        binding.expireTime.setVisibility(View.INVISIBLE);
        if (UserInfoManager.getInstance().isLogin()) {
            /*Glide.with(this)
                    .load(Constant.Url.getBigUserImageUrl(UserInfoManager.getInstance().getUserId(), mConfigManager.getPhotoTimestamp()))
                    .asBitmap()
                    .signature(new StringSignature(System.currentTimeMillis() + ""))
                    .placeholder(R.drawable.noavatar_small)
                    .into(binding.userImg);*/
            LibGlide3Util.loadImg(this,Constant.Url.getBigUserImageUrl(UserInfoManager.getInstance().getUserId(), mConfigManager.getPhotoTimestamp()),R.drawable.noavatar_small,binding.userImg);
            binding.expireTime.setVisibility(View.VISIBLE);
            binding.tvIyubi.setText("爱语币余额：" + UserInfoManager.getInstance().getIyuIcon());
            if (UserInfoManager.getInstance().isVip()) {
                binding.expireTime.setText(LibDateUtil.toDateStr(UserInfoManager.getInstance().getVipTime(), LibDateUtil.YMD));
            } else {
                binding.expireTime.setText("普通用户");
            }
            binding.userName.setText(UserInfoManager.getInstance().getUserName());
        } else {
            binding.userName.setText("未登录");
        }
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_vip_quanzhan:
                setGroupVisble(QUANZHAN);
                break;
            case R.id.tv_vip_benyingyong:
                setGroupVisble(BENYINGYONG);
                break;
            case R.id.tv_vip_huangjin:
                setGroupVisble(HUANGJIN);
                break;
            case R.id.cb_quanzhan1:
            case R.id.rl_quanzhan1:
                clearCheckState();
                binding.quanzhanLl.cbQuanzhan1.setChecked(true);
                buyQuanzhanVip(1);
                break;
            case R.id.cb_quanzhan2:
            case R.id.rl_quanzhan2:
                clearCheckState();
                binding.quanzhanLl.cbQuanzhan2.setChecked(true);
                buyQuanzhanVip(3);
                break;
            case R.id.cb_quanzhan3:
            case R.id.rl_quanzhan3:
                clearCheckState();
                binding.quanzhanLl.cbQuanzhan3.setChecked(true);
                buyQuanzhanVip(6);
                break;
            case R.id.cb_quanzhan4:
            case R.id.rl_quanzhan4:
                clearCheckState();
                binding.quanzhanLl.cbQuanzhan4.setChecked(true);
                buyQuanzhanVip(12);
                break;
            case R.id.cb_quanzhan5:
            case R.id.rl_quanzhan5:
                clearCheckState();
                binding.quanzhanLl.cbQuanzhan5.setChecked(true);
                buyQuanzhanVip(36);
                break;
            case R.id.cb_benyingyong1:
            case R.id.rl_benyingyong1:
                clearCheckState();
                binding.benyingyongLl. cbBenyingyong1.setChecked(true);
                buyBenyingyongVip(1);
                break;
            case R.id.cb_benyingyong2:
            case R.id.rl_benyingyong2:
                clearCheckState();
                binding.benyingyongLl.cbBenyingyong2.setChecked(true);
                buyBenyingyongVip(6);
                break;
            case R.id.cb_benyingyong3:
            case R.id.rl_benyingyong3:
                clearCheckState();
                binding.benyingyongLl.cbBenyingyong3.setChecked(true);
                buyBenyingyongVip(12);
                break;
            case R.id.cb_benyingyong4:
            case R.id.rl_benyingyong4:
                clearCheckState();
                binding.benyingyongLl.cbBenyingyong4.setChecked(true);
                buyBenyingyongVip(36);
                break;
            case R.id.gold_app_cb_1:
            case R.id.gold_app_re_1:
                clearCheckState();
                binding.vipgoldLl.goldAppCb1.setChecked(true);
                buyGoldVip(1);
                break;
            case R.id.gold_app_cb_2:
            case R.id.gold_app_re_2:
                clearCheckState();
                binding.vipgoldLl.goldAppCb2.setChecked(true);
                buyGoldVip(3);
                break;
            case R.id.gold_app_cb_3:
            case R.id.gold_app_re_3:
                clearCheckState();
                binding.vipgoldLl.goldAppCb3.setChecked(true);
                buyGoldVip(6);
                break;
            case R.id.gold_app_cb_4:
            case R.id.gold_app_re_4:
                clearCheckState();
                binding.vipgoldLl.goldAppCb4.setChecked(true);
                buyGoldVip(12);
                break;
            case R.id.btn_buyiyuba:
                if (UserInfoManager.getInstance().isLogin()) {
                    startActivity(new Intent(mContext, BuyIyubiActivity.class));
                } else {
                    startToLogin();
                }
                break;
            case R.id.go_buy:
                if (UserInfoManager.getInstance().isLogin()) {
                    startActivity(intent);
                } else {
                    startToLogin();
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.user_img:
                if (UserInfoManager.getInstance().isLogin()) {
                    UserInfoManager.getInstance().initUserInfo();
                    startActivity(PersonalHomeActivity.buildIntent (this,
                            UserInfoManager.getInstance().getUserId(),
                            UserInfoManager.getInstance().getUserName(), 0));
                } else {
                    startToLogin();
                }
                break;
            default:
                break;
        }
    }

    private void clearCheckState() {
        for (int i = 0; i < cbList.length; i++) {
            cbList[i].setChecked(false);
        }
    }

    private void setGroupVisble(int flag) {
        binding.quanzhanLl.getRoot().setVisibility(View.GONE);
        binding.benyingyongLl.getRoot().setVisibility(View.GONE);
        binding.vipgoldLl.getRoot().setVisibility(View.GONE);
        binding.tvVipQuanzhan.setBackgroundColor(0x00FFFFFF);
        binding.tvVipBenyingyong.setBackgroundColor(0x00FFFFFF);
        binding.tvVipHuangjin.setBackgroundColor(0x00FFFFFF);
        switch (flag) {
            case QUANZHAN:
                binding.quanzhanLl.quanzhanLl.setVisibility(View.VISIBLE);
                binding.tvVipQuanzhan.setBackgroundColor(0xFFFDDA94);
                binding.quanzhanLl.cbQuanzhan1.setChecked(true);
                binding.quanzhanLl.cbQuanzhan2.setChecked(false);
                binding.quanzhanLl.cbQuanzhan3.setChecked(false);
                binding.quanzhanLl.cbQuanzhan4.setChecked(false);
                binding.quanzhanLl.cbQuanzhan5.setChecked(false);
                buyQuanzhanVip(1);
                break;
            case BENYINGYONG:
                binding.benyingyongLl.benyingyongLl.setVisibility(View.VISIBLE);
                binding.tvVipBenyingyong.setBackgroundColor(0xFFFDDA94);
                binding.benyingyongLl. cbBenyingyong1.setChecked(true);
                binding.benyingyongLl.cbBenyingyong2.setChecked(false);
                binding.benyingyongLl.cbBenyingyong3.setChecked(false);
                binding.benyingyongLl.cbBenyingyong4.setChecked(false);
                buyBenyingyongVip(1);
                break;
            case HUANGJIN:
                binding.vipgoldLl.getRoot().setVisibility(View.VISIBLE);
                binding.tvVipHuangjin.setBackgroundColor(0xFFFDDA94);
                binding.vipgoldLl.goldAppCb1.setChecked(true);
                binding.vipgoldLl.goldAppCb2.setChecked(false);
                binding.vipgoldLl.goldAppCb3.setChecked(false);
                binding.vipgoldLl.goldAppCb4.setChecked(false);
                buyGoldVip(1);
                break;
        }
    }

    private void buyQuanzhanVip(int month) {
        price = PriceUtils.getSpend(month);

        /*intent = new Intent(this, PayOrderActivity.class);
        intent.putExtra("amount", month);
        {
            intent.putExtra("body", "花费" + price + "元购买全站vip");
            intent.putExtra("subject", "全站vip");
            intent.putExtra("description", "全站vip"+month+"个月");
            intent.putExtra(PayOrderActivity.PRODUCT_ID, 0);
        }
        intent.putExtra("price", price + "");  //价格*/

        String vipBody = "花费" + price + "元购买全站vip"+month+"个月";
        String vipSubject = "全站vip";
        String vipDesc = "全站vip"+month+"个月";
        String vipPrice = String.valueOf(price);
        intent = PayOrderActivity.buildIntent(this,vipDesc,vipPrice,vipSubject,vipBody,month,0,PayOrderActivity.Order_vip);
    }

    public void buyBenyingyongVip(int month){
        price = PriceUtils.getSpendBenyingyong(month);

        /*intent = new Intent(this, PayOrderActivity.class);
        intent.putExtra("price", price + "");  //价格

        if (month == 0) {
            intent.putExtra("body", "花费" + price + "元购买永久vip");
            intent.putExtra("subject", "永久vip");
            intent.putExtra("description", "永久vip");
            intent.putExtra(PayOrderActivity.PRODUCT_ID, 10);
        } else {
            intent.putExtra("body", "花费" + price + "元购买本应用vip"+month+"个月");
            intent.putExtra("subject", "本应用vip");
            intent.putExtra("description", "本应用vip"+month+"个月");
            intent.putExtra("amount", month);
            intent.putExtra(PayOrderActivity.PRODUCT_ID, 10);
        }*/

        String vipBody = "花费" + price + "元购买本应用vip"+month+"个月";
        String vipSubject = "本应用vip";
        String vipDesc = "本应用vip"+month+"个月";
        String vipPrice = String.valueOf(price);

        intent = PayOrderActivity.buildIntent(this,vipDesc,vipPrice,vipSubject,vipBody,month,10,PayOrderActivity.Order_vip);
    }

    public void buyGoldVip(int month){
        price = PriceUtils.getSpendVip(month);

        /*intent = new Intent(this, PayOrderActivity.class);
        intent.putExtra("price", price + "");  //价格

        if (month == 0) {
            intent.putExtra("body", "花费" + price + "元购买黄金vip");
            intent.putExtra("subject", "黄金vip");
            intent.putExtra("description", "黄金vip"+month+"个月");
            intent.putExtra(PayOrderActivity.PRODUCT_ID, Constant.PRODUCT_ID);
        } else {
            intent.putExtra("body", "花费" + price + "元购买黄金vip"+month+"个月");
            intent.putExtra("subject", "黄金vip");
            intent.putExtra("description", "黄金vip"+month+"个月");
            intent.putExtra("amount", month);
            intent.putExtra(PayOrderActivity.PRODUCT_ID, Constant.PRODUCT_ID);
        }*/

        String vipBody = "花费" + price + "元购买黄金vip"+month+"个月";
        String vipSubject = "黄金vip";
        String vipDesc = "黄金vip"+month+"个月";
        String vipPrice = String.valueOf(price);
        intent = PayOrderActivity.buildIntent(this,vipDesc,vipPrice,vipSubject,vipBody,month,26,PayOrderActivity.Order_vip);
    }

    //刷新会员信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VIpChangeEvent vipEvent) {
        UserInfoManager.getInstance().getRemoteUserInfo(UserInfoManager.getInstance().getUserId(), null);
    }

    //刷新登录信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        setDes();
    }

    //刷新用户信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshDataEvent event) {
        setDes();
    }

    private void startToLogin(){
        NewLoginUtil.startToLogin(this);
    }
}