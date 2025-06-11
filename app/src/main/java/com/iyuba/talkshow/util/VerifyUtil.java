package com.iyuba.talkshow.util;

import android.content.Context;
import android.content.Intent;

import com.iyuba.talkshow.ui.vip.buyvip.NewVipCenterActivity;

/**
 * mob的一键登录
 */
public class VerifyUtil {

    //vip弹窗显示
    /*public static void showVipDialog(Context context,String ability,int vipType){
        new AlertDialog.Builder(context)
                .setTitle("VIP授权")
                .setMessage(ability+"功能需要VIP会员权限，是否立即开通VIP会员？")
                .setCancelable(false)
                .setNegativeButton("取消",null)
                .setPositiveButton("开通", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startToVip(context, vipType);
                    }
                }).show();
    }*/

    //跳转vip界面
    public static void startToVip(Context context,int vipType){
        Intent intent = new Intent();
        intent.setClass(context, NewVipCenterActivity.class);
        intent.putExtra(NewVipCenterActivity.HUI_YUAN,vipType);
        context.startActivity(intent);
    }

    //跳转登录界面
    /*private static void startToLogin(Context context){
        NewLoginUtil.startToLogin(context);
    }*/

    //跳转手机号注册界面
    /*private static void startToRegister(Context context,String phone,String userName,String password){
        Intent intent = new Intent();
        intent.setClass(context, RegisterByPhoneActivity.class);
        intent.putExtra(RegisterSubmitActivity.PhoneNum, phone);
        intent.putExtra(RegisterSubmitActivity.UserName, userName);
        intent.putExtra(RegisterSubmitActivity.PassWord, password);
        intent.putExtra(RegisterSubmitActivity.RegisterMob, 1);
        context.startActivity(intent);
    }*/

    //跳转一键登录界面
    /*public static void verify(Context context,AccountManager accountManager,LoadingDialog loadingDialog){
        boolean isSupport = SecVerify.isVerifySupport();
        if (isSupport&& Constant.User.isPreVerifyDone){
            login(context, accountManager,loadingDialog);
        }else {
            startToLogin(context);
        }
    }*/

    //调用mob的方法
    /*private static void mobVerify(Context context,OnDataBack onDataBack){
        SecVerify.verify(new VerifyCallback() {
            @Override
            public void onOtherLogin() {
                startToLogin(context);
            }

            @Override
            public void onUserCanceled() {
                SecVerify.finishOAuthPage();
            }

            @Override
            public void onComplete(VerifyResult result) {
                //提交数据到服务器
                if (result == null){
                    //登录后没有信息
                    ToastUtil.showToast(context,"未获取到账号信息，请手动登录");
                    startToLogin(context);
                }else {
                    //登录成功
                    Map<String,String> map = new HashMap<>();
                    map.put(UserService.Register.Param.Key.PROTOCOL, "10010");
                    try {
                        String token = URLEncoder.encode(result.getToken(),"UTF-8");
                        map.put("token", token);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        map.put("token", result.getToken());
                    }
                    map.put("opToken", result.getOpToken());
                    map.put("operator", result.getOperator());
                    map.put(UserService.Login.Param.Key.APP_ID, String.valueOf(App.APP_ID));
                    map.put("appkey", ConfigData.mob_key);

                    registerByMob(map,onDataBack);
                }
            }

            @Override
            public void onFailure(VerifyException e) {
                //登录失败
                SecVerify.finishOAuthPage();
                ToastUtil.showToast(context,"快速登录失败，请手动登录");

                if (onDataBack!=null){
                    onDataBack.back(false,null);
                }
            }
        });
    }*/

    //调用本地注册方法
    /*private static void registerByMob(Map<String,String> map,OnDataBack onDataBack){
        UserService.Creator.newUserService().registerByMob(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegisterMobResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (onDataBack!=null){
                            onDataBack.back(false,null);
                        }
                    }

                    @Override
                    public void onNext(RegisterMobResponse response) {
                        if (onDataBack!=null){
                            if (response==null){
                                onDataBack.back(false,null);
                            }else {
                                onDataBack.back(true,response);
                            }
                        }
                    }
                });
    }*/

    //调用整套登录方法
    /*private static void login(Context context,AccountManager accountManager,LoadingDialog loadingDialog){
        if (loadingDialog!=null){
            loadingDialog.setMessage("正在进行登录");
            loadingDialog.show();
        }

        //操作回调
        SecVerify.OtherOAuthPageCallBack(new OAuthPageEventCallback() {
            @Override
            public void initCallback(OAuthPageEventResultCallback cb) {
                cb.pageOpenCallback(() -> {

                });
                cb.loginBtnClickedCallback(() -> {

                });
                cb.agreementPageClosedCallback(() -> {

                });
                cb.agreementPageOpenedCallback(() -> {

                });
                cb.cusAgreement1ClickedCallback(() -> {

                });
                cb.cusAgreement2ClickedCallback(() -> {

                });
                cb.checkboxStatusChangedCallback(b -> {

                });
                cb.pageCloseCallback(() -> {
                    if (loadingDialog!=null){
                        loadingDialog.dismiss();
                    }
                });
            }
        });
        //数据回调
        mobVerify(context, new OnDataBack() {
            @Override
            public void back(boolean isSuccess, Object data) {
                if (!isSuccess){
                    if (loadingDialog!=null){
                        loadingDialog.dismiss();
                    }
                    startToLogin(context);
                    return;
                }

                RegisterMobResponse response = (RegisterMobResponse) data;
                if (response.isLogin==1){
                    //已经登录
                    ToastUtil.show(context,"快速登录成功");
                    if (response.userinfo != null) {
                        accountManager.setUser(response.userinfo, "");
                        accountManager.saveUser();
                    }
                    EventBus.getDefault().post(new LoginEvent());
                }else {
                    if (response.res!=null){
                        //这里直接登录
                        ToastUtil.show(context,"用户信息不完善，请注册后使用");
                        String randNum = "" + System.currentTimeMillis();
                        String phone = response.res.phone;
                        String user = "iyuba" + randNum.substring(randNum.length() - 4) + phone.substring(phone.length() - 4);
                        String pass = phone.substring(phone.length() - 6);
                        startToRegister(context,phone,user,pass);
                    }else {
                        //islogin=0并且无其他数据
                        ToastUtil.show(context,"未获取到用户数据，请使用用户名密码登录");
                        startToLogin(context);
                    }
                }

                if (loadingDialog!=null){
                    loadingDialog.dismiss();
                }
                SecVerify.finishOAuthPage();
            }
        });
    }*/

    //接口
    private interface OnDataBack{
        void back(boolean isSuccess,Object data);
    }
}
