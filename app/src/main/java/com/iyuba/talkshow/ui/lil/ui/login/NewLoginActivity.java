package com.iyuba.talkshow.ui.lil.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iyuba.lib_common.manager.NetHostManager;
import com.iyuba.lib_common.model.remote.bean.Mob_verify;
import com.iyuba.lib_common.model.remote.manager.UserRemoteManager;
import com.iyuba.lib_common.ui.BaseViewBindingActivity;
import com.iyuba.lib_common.util.LibRxUtil;
import com.iyuba.lib_common.util.LibToastUtil;
import com.iyuba.lib_user.data.NewLoginType;
import com.iyuba.lib_user.listener.UserinfoCallbackListener;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.module.headlinetalk.ui.widget.LoadingDialog;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.databinding.AtyLoginNewBinding;
import com.iyuba.talkshow.event.LoginEvent;
import com.iyuba.talkshow.ui.lil.manager.TempDataManager;
import com.iyuba.talkshow.ui.user.register.phone.RegisterByPhoneActivity;
import com.iyuba.talkshow.ui.user.register.submit.RegisterSubmitActivity;
import com.iyuba.talkshow.ui.web.WebActivity;
import com.mob.secverify.OAuthPageEventCallback;
import com.mob.secverify.SecVerify;
import com.mob.secverify.VerifyCallback;
import com.mob.secverify.common.exception.VerifyException;
import com.mob.secverify.datatype.VerifyResult;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @title:
 * @date: 2023/8/25 09:42
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class NewLoginActivity extends BaseViewBindingActivity<AtyLoginNewBinding> {
    //登录类型
    private static final String tag_loginType = "loginType";
    private String loginType = "loginType";
    //加载弹窗
    private LoadingDialog loadingDialog;
    //微信小程序的token
    private String wxMiniToken = null;

    public static void start(Context context,String loginType){
        Intent intent = new Intent();
        intent.setClass(context,NewLoginActivity.class);
        intent.putExtra(tag_loginType,loginType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initView();
        initData();
        initClick();

        switchType();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /************************初始化********************************/
    private void initView(){
        //设置账号和密码
        String userName = UserInfoManager.getInstance().getLoginAccount();
        if (!TextUtils.isEmpty(userName)){
            binding.etUserName.setText(userName);
        }
        String password = UserInfoManager.getInstance().getLoginPassword();
        if (!TextUtils.isEmpty(password)){
            binding.etPassword.setText(password);
        }

        //关闭账号登录界面的图片
        binding.imageView1.setVisibility(View.INVISIBLE);
    }

    private void initData(){
        binding.loginType.appIcon.setImageResource(R.drawable.ic_launcher);
        binding.loginType.appName.setText(getResources().getString(R.string.app_name));

        loginType = getIntent().getStringExtra(tag_loginType);
        if (TextUtils.isEmpty(loginType)){
            loginType = NewLoginType.loginByAccount;
        }

        //隐私政策显示
        binding.loginType.agreeTv.setText(setPrivacySpan());
        binding.loginType.agreeTv.setMovementMethod(new LinkMovementMethod());

        //账号登录界面的隐私政策显示
        binding.tvLoginPrivacy.setText(setPrivacySpan());
        binding.tvLoginPrivacy.setMovementMethod(new LinkMovementMethod());
    }

    private void initClick(){
        binding.btnBack.setOnClickListener(v->{
            finish();
        });
        binding.btnRegister.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setClass(this, RegisterByPhoneActivity.class);
            startActivity(intent);
            finish();
        });
        binding.btnLogin.setOnClickListener(v->{
            if (!binding.cbLoginPrivacy.isChecked()){
                LibToastUtil.showToast(this,"请先阅读并同意隐私政策和用户协议");
                hideKeyBoard();
                return;
            }

            if (verifyAccountAndPsd()){
                String userName = binding.etUserName.getText().toString().trim();
                String userPwd = binding.etPassword.getText().toString().trim();

                accountLogin(userName,userPwd);
            }
        });
        /*tvSmallLogin.setOnClickListener(v->{
            if (!cbPrivacy.isChecked()){
                ToastUtil.showToast(this,"请先阅读并同意隐私政策和用户协议");
                return;
            }

            toWxLogin();
        });*/
        binding.tvForgetPassword.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setClass(this, WebActivity.class);
            intent.putExtra("url","http://m." + NetHostManager.getInstance().getDomainShort() + "/m_login/inputPhonefp.jsp");
            intent.putExtra("title","重置密码");
            startActivity(intent);
        });
        /*binding.loginType.vxLogin.setOnClickListener(v->{
            if (!rbCheck.isChecked()){
                ToastUtil.showToast(this,"请先阅读并同意隐私政策和用户协议");
                return;
            }

            toWxLogin();
        });*/
        binding.loginType.accountLogin.setOnClickListener(v->{
            binding.loginType.getRoot().setVisibility(View.GONE);
            binding.accountLoginLayout.setVisibility(View.VISIBLE);
        });
    }

    private SpannableStringBuilder setPrivacySpan(){
        String childPrivacyStr = "《儿童隐私政策》";
        String privacyStr = "《隐私政策》";
        String termStr = "《用户协议》";
        String showMsg = "我已阅读并同意"+childPrivacyStr+"、"+privacyStr+"、"+termStr;

        SpannableStringBuilder spanStr = new SpannableStringBuilder();
        spanStr.append(showMsg);
        //儿童隐私政策
        int childPrivacyIndex = showMsg.indexOf(childPrivacyStr);
        spanStr.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(NewLoginActivity.this, WebActivity.class);
                String url = App.Url.CHILD_PROTOCOL_URL + App.APP_NAME_CH;
                intent.putExtra("url", url);
                intent.putExtra("title", childPrivacyStr);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.colorPrimary));
            }
        },childPrivacyIndex,childPrivacyIndex+childPrivacyStr.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //隐私政策
        int privacyIndex = showMsg.indexOf(privacyStr);
        spanStr.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(NewLoginActivity.this, WebActivity.class);
                String url = App.Url.PROTOCOL_URL + App.APP_NAME_CH;
                intent.putExtra("url", url);
                intent.putExtra("title", privacyStr);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.colorPrimary));
            }
        },privacyIndex,privacyIndex+privacyStr.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //用户协议
        int termIndex = showMsg.indexOf(termStr);
        spanStr.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(NewLoginActivity.this, WebActivity.class);
                String url = App.Url.USAGE_URL + App.APP_NAME_CH;
                intent.putExtra("url", url);
                intent.putExtra("title", termStr);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.colorPrimary));
            }
        },termIndex,termIndex+termStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    /**************************样式显示*****************************/
    //切换样式
    private void switchType(){
        if (loginType.equals(NewLoginType.loginByWXSmall)){
            //微信登录
            /*wxLoginView.setVisibility(View.VISIBLE);
            accountLoginView.setVisibility(View.GONE);

            getWXSmallToken();*/
        }else if (loginType.equals(NewLoginType.loginByVerify)){
            //秒验登录
            binding.loginType.getRoot().setVisibility(View.GONE);
            binding.tvSmallLogin.setVisibility(View.GONE);
            binding.accountLoginLayout.setVisibility(View.GONE);

            showVerify();
        }else {
            //账号登录
            binding.loginType.getRoot().setVisibility(View.GONE);
            binding.tvSmallLogin.setVisibility(View.GONE);
            binding.accountLoginLayout.setVisibility(View.VISIBLE);
        }
    }

    /******************************登录方式************************/
    /*************微信登录***********/
    //获取微信小程序的token
    /*private void getWXSmallToken(){
        startLoading("正在加载登录信息～");
        LoginPresenter.getWXSmallToken(new Observer<VXTokenResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(VXTokenResponse bean) {
                closeLoading();

                if (bean.getResult().equals("200")){
                    wxMiniToken = bean.getToken();

                    //这里判断微信是否已经安装
                    IWXAPI wxapi = WXAPIFactory.createWXAPI(NewLoginActivity.this, Constant.getWxKey());
                    if (!wxapi.isWXAppInstalled()){
                        wxMiniToken = null;

                        loginType = LoginType.loginByAccount;
                        switchType();
                    }
                }else {
                    wxMiniToken = null;

                    loginType = LoginType.loginByAccount;
                    switchType();
                }
            }

            @Override
            public void onError(Throwable e) {
                closeLoading();
                wxMiniToken = null;

                loginType = LoginType.loginByAccount;
                switchType();
            }

            @Override
            public void onComplete() {

            }
        });
    }*/

    //跳转到微信登录
    /*private void toWxLogin(){
        IWXAPI wxapi = WXAPIFactory.createWXAPI(NewLoginActivity.this,Constant.getWxKey());
        if (!wxapi.isWXAppInstalled()){
            ToastUtil.showToast(NewLoginActivity.this,"您还未安装微信客户端");
            return;
        }

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName="gh_a8c17ad593be";
        req.path="/subpackage/getphone/getphone?token="+wxMiniToken+"&appid="+Constant.APPID;
        req.miniprogramType=WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
        wxapi.sendReq(req);

        //放在临时框架中，后面要用
        WxLoginSession.getInstance().setWxSmallToken(wxMiniToken);

        finish();
    }*/

    /*************秒验登录***********/
    //展示秒验功能
    private void showVerify(){
        if (SecVerify.isVerifySupport()&& TempDataManager.getInstance().getMobVerify()){
            startLoading("正在获取登录信息~");
            SecVerify.OtherOAuthPageCallBack(new OAuthPageEventCallback() {
                @Override
                public void initCallback(OAuthPageEventResultCallback callback) {
                    callback.pageOpenCallback(new PageOpenedCallback() {
                        @Override
                        public void handle() {
                            closeLoading();
                        }
                    });
                }
            });
            SecVerify.verify(new VerifyCallback() {
                @Override
                public void onOtherLogin() {
                    //点击其他登录方式
                    closeLoading();
                    loginType = NewLoginType.loginByAccount;
                    switchType();
                }

                @Override
                public void onUserCanceled() {
                    //用户取消
                    closeLoading();
                    SecVerify.finishOAuthPage();
                    NewLoginActivity.this.finish();
                }

                @Override
                public void onComplete(VerifyResult result) {
                    //调用完成
                    if (result!=null){
                        //这里调用接口，从服务器获取数据展示
                        checkMobDataFromServer(result);
                    }else {
                        closeLoading();
                        loginType = NewLoginType.loginByAccount;
                        switchType();
                    }
                }

                @Override
                public void onFailure(VerifyException e) {
                    //调用失败
                    closeLoading();
                    loginType = NewLoginType.loginByAccount;
                    switchType();
                }
            });
        }else {
            closeLoading();
            loginType = NewLoginType.loginByAccount;
            switchType();
        }
    }

    //秒验和服务器查询
    private Disposable mobVerifyDis;
    private void checkMobDataFromServer(VerifyResult result){
        startLoading("正在获取用户信息～");
        LibRxUtil.unDisposable(mobVerifyDis);
        UserRemoteManager.mobVerifyFromServer(result.getToken(), result.getOpToken(), result.getOperator())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Mob_verify>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mobVerifyDis = d;
                    }

                    @Override
                    public void onNext(Mob_verify bean) {
                        //存在数据
                        if (bean!=null){
                            //存在账号数据
                            if (bean.getIsLogin().equals("1")&&bean.getUserinfo()!=null){
                                //根据20001重新获取数据
                                UserInfoManager.getInstance().getRemoteUserInfo(bean.getUserinfo().getUid(), new UserinfoCallbackListener() {
                                    @Override
                                    public void onSuccess() {
                                        closeLoading();
                                        SecVerify.finishOAuthPage();

                                        NewLoginActivity.this.finish();
                                        EventBus.getDefault().post(new LoginEvent());
                                    }

                                    @Override
                                    public void onFail(String errorMsg) {
                                        closeLoading();
                                        SecVerify.finishOAuthPage();

                                        hideKeyBoard();
                                        LibToastUtil.showToast(NewLoginActivity.this,errorMsg);
                                        loginType = NewLoginType.loginByAccount;
                                        switchType();
                                    }
                                });
                                return;
                            }

                            //不存在账号数据，但是存在电话号
                            if (bean.getRes()!=null&&!TextUtils.isEmpty(bean.getRes().getPhone())){
                                closeLoading();
                                SecVerify.finishOAuthPage();

                                Intent intent = new Intent();
                                intent.setClass(NewLoginActivity.this, RegisterSubmitActivity.class);
                                intent.putExtra(RegisterSubmitActivity.PhoneNum, bean.getRes().getPhone());
                                intent.putExtra(RegisterSubmitActivity.RegisterMob,true);
                                //合成随机数据显示
                                intent.putExtra(RegisterSubmitActivity.UserName,getRandomByPhone(bean.getRes().getPhone()));
                                intent.putExtra(RegisterSubmitActivity.PassWord,bean.getRes().getPhone().substring(bean.getRes().getPhone().length()-6));
                                startActivity(intent);

                                NewLoginActivity.this.finish();
                                return;
                            }
                        }

                        closeLoading();
                        SecVerify.finishOAuthPage();

                        hideKeyBoard();
                        LibToastUtil.showToast(NewLoginActivity.this,"获取登录信息失败，请手动登录");
                        loginType = NewLoginType.loginByAccount;
                        switchType();
                    }

                    @Override
                    public void onError(Throwable e) {
                        SecVerify.finishOAuthPage();
                        hideKeyBoard();
                        LibToastUtil.showToast(NewLoginActivity.this,"获取登录信息失败，请手动登录");
                        loginType = NewLoginType.loginByAccount;
                        switchType();
                    }

                    @Override
                    public void onComplete() {
                        LibRxUtil.unDisposable(mobVerifyDis);
                    }
                });
    }

    /*************账号登录************/
    //验证账号和密码
    private boolean verifyAccountAndPsd(){
        String userName = binding.etUserName.getText().toString().trim();
        String userPwd = binding.etPassword.getText().toString().trim();

        if (userName.length() < 3) {
            hideKeyBoard();
            LibToastUtil.showToast(this,getResources().getString(R.string.login_check_effective_user_data));
            return false;
        }

        if (userPwd.length() == 0) {
            hideKeyBoard();
            LibToastUtil.showToast(this,getResources().getString(R.string.login_check_user_pwd_null));
            return false;
        }

        if (userPwd.length() < 6 || userPwd.length() > 20) {
            hideKeyBoard();
            LibToastUtil.showToast(this,getResources().getString(R.string.login_check_user_pwd_constraint));
            return false;
        }

        return true;
    }

    //账号登录
    private Disposable accountLoginDis;

    private void accountLogin(String userName,String userPwd){
        startLoading("正在登录～");
        UserInfoManager.getInstance().postRemoteAccountLogin(userName, userPwd, new UserinfoCallbackListener() {
            @Override
            public void onSuccess() {
                closeLoading();
                EventBus.getDefault().post(new LoginEvent());
                isToFulfillInfo();
            }

            @Override
            public void onFail(String errorMsg) {
                closeLoading();
                hideKeyBoard();
                LibToastUtil.showToast(NewLoginActivity.this,errorMsg);
            }
        });
    }

    //判断是否跳转到用户信息完善界面
    private void isToFulfillInfo() {
        NewLoginActivity.this.finish();
    }

    /*******************************辅助功能***********************/
    //开启加载弹窗
    private void startLoading(String msg){
        if (loadingDialog==null){
            loadingDialog = new LoadingDialog(this);
        }

        if (loadingDialog.isShowing()){
            return;
        }

        loadingDialog.setMessage(msg);
        loadingDialog.show();
    }

    //关闭加载弹窗
    private void closeLoading(){
        if (loadingDialog!=null&&loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }


    //根据手机号随机生成用户名称
    private String getRandomByPhone(String phone){
        StringBuilder builder = new StringBuilder();
        builder.append("iyuba");

        //随机数
        for (int i = 0; i < 4; i++) {
            int randomInt = (int) (Math.random()*10);
            builder.append(randomInt);
        }

        String lastPhone = null;
        if (phone.length()>4){
            lastPhone = phone.substring(phone.length()-4);
        }else {
            String time = String.valueOf(System.currentTimeMillis());
            lastPhone = time.substring(time.length()-4);
        }
        builder.append(lastPhone);
        return builder.toString();
    }

    /*************************************设置键盘显示和隐藏********************************/
    //隐藏键盘(用于处理鸿蒙手机上的显示问题)
    private void hideKeyBoard(){
        if (isKeyBoardOpen()){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //判断键盘是否弹窗
    private boolean isKeyBoardOpen(){
        int height = getWindow().getDecorView().getHeight();
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return height*2/3 > rect.bottom;
    }
}
