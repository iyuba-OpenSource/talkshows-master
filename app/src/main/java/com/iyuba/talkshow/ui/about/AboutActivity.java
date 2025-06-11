package com.iyuba.talkshow.ui.about;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.iyuba.talkshow.BuildConfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.local.PreferencesHelper;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.manager.VersionManager;
import com.iyuba.talkshow.data.model.QQResponse;
import com.iyuba.talkshow.databinding.ActivityAboutBinding;
import com.iyuba.talkshow.event.LoginOutEvent;
import com.iyuba.talkshow.http.Http;
import com.iyuba.talkshow.http.HttpCallback;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.web.WebActivity;
import com.iyuba.talkshow.util.BrandUtil;
import com.iyuba.talkshow.util.Util;
import com.tencent.vasdolly.helper.ChannelReaderUtil;
import com.umeng.analytics.MobclickAgent;
import com.youdao.sdk.common.OAIDHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import okhttp3.Call;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * 关于界面
 */
@RuntimePermissions
public class AboutActivity extends BaseActivity implements AboutMvpView {
    private static final String VERSION_CODE = "versionCode";
    private static final String APP_URL = "appUrl";
    public static final String TAG = "AboutActivity";
    @Inject
    VersionManager mVersionManager;
    @Inject
    AboutPresenter mPresenter;
    @Inject
    PreferencesHelper mPreferencesHelper;
    @Inject
    ConfigManager mConfigManager;

    private ClearUserFragment fragment;
    ActivityAboutBinding binding ;

    //测试-渠道号次数
    private int channelCount = 0;

    public static Intent buildIntent(Context context, String versionCode, String appUrl) {
        Intent intent = new Intent(context, AboutActivity.class);
        intent.putExtra(VERSION_CODE, versionCode);
        intent.putExtra(APP_URL, appUrl);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityComponent().inject(this);
        mPresenter.attachView(this);
        setSupportActionBar(binding.aboutToolbar.listToolbar);

        binding.logoIv.setText(App.APP_NAME_CH);

        if (UserInfoManager.getInstance().isLogin()){
            binding.customClearUser.setVisibility(View.VISIBLE);
        }else {
            binding.customClearUser.setVisibility(View.GONE);
        }

        //增加一个测试功能
        /*if (BuildConfig.DEBUG) {
            binding.logoIv.setOnClickListener(v->{
                //测试清除新概念的进程
                String packageName = "com.iyuba.learnNewEnglish";

                ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                for (ActivityManager.RunningAppProcessInfo info:manager.getRunningAppProcesses()){
                    int pid = info.pid;
                    String pName = info.processName;

                    Log.d("进程操作", pid+"----"+pName);
                }
                manager.killBackgroundProcesses(packageName);

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();

                ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                Method method = null;
                try {
                    method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
                    method.invoke(mActivityManager, packageName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        mPresenter.detachView();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Intent intent = getIntent();
        String versionCode = intent.getStringExtra(VERSION_CODE);
        String appUrl = intent.getStringExtra(APP_URL);
        if (versionCode != null && appUrl != null) {
            binding.aboutHasnewImageview.setVisibility(View.VISIBLE);
            binding.aboutAppUpdateRl.setEnabled(false);
            binding.aboutDownloadProgressbar.setVisibility(View.VISIBLE);
            mPresenter.downloadApk(versionCode, appUrl);
        } else {
            if (App.APP_CHECK_UPGRADE) {
                mVersionManager.checkVersion(callBack);
            }
        }

        if (App.APP_HUAWEI_PRIVACY) {
            binding.aboutMoreAppBtn.setVisibility(View.GONE);
        }
        binding.weixinButton.setText(String.format("%s用户反馈群: %s", BrandUtil.getBrandChinese(), BrandUtil.getQQGroupNumber(mPreferencesHelper)));
        binding.weixinButton.setOnClickListener(v -> {
            joinQQGroup(BrandUtil.getQQGroupKey(mPreferencesHelper));
        });
        binding.aboutMoreAppBtn.setOnClickListener(v -> clickMoreAppBtn());
        binding.aboutVersionTextview.setText(MessageFormat.format(getString(R.string.about_version), VersionManager.VERSION_NAME));

        binding.customClearUser.setOnClickListener(v -> clickClearUser());
        binding.customeServicePart.setOnClickListener(v -> {
//            AboutActivityPermissionsDispatcher.callPhoneWithPermissionCheck(AboutActivity.this);
            callCustomPhone();
        });
        //这里统一修改成在主界面进行数据获取
//        requestQQNumber();

        //显示备案号
        if (getPackageName().equals(Constant.PackageName.PACKAGE_talkshow)){
            //英语口语秀
            binding.recordNumber.setText("京ICP备14035507号-45A");
        }else if (getPackageName().equals(Constant.PackageName.PACKAGE_peiyin)){
            //英语配音秀
            binding.recordNumber.setText("京ICP备18027903号-15A");
        }else if (getPackageName().equals(Constant.PackageName.PACKAGE_child)){
            //少儿英语
            binding.recordNumber.setText("京ICP备14035507号-47A");
        }else if (getPackageName().equals(Constant.PackageName.PACKAGE_pappa)){
            //小猪英语
            binding.recordNumber.setText("鲁ICP备2022029024号-6A");
        }else if (getPackageName().equals(Constant.PackageName.PACKAGE_pig)){
            //小猪佩奇
            binding.recordNumber.setVisibility(View.INVISIBLE);
        }
        binding.recordNumber.setOnClickListener(v->{
            //显示渠道类型
            channelCount++;

            if (channelCount>=6){
                String channel = ChannelReaderUtil.getChannel(getApplicationContext());
                binding.channel.setVisibility(View.VISIBLE);
                binding.channel.setText(channel);
            }
        });
    }

    public void requestQQNumber() {
        String url = "http://iuserspeech." + Constant.Web.WEB_SUFFIX.replace("/","") + ":9001/japanapi/getJpQQ.jsp?appid=" + App.APP_ID;
        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                try {
                    Log.e(TAG, "requestQQNumber onSucceed result  " + response);
                    QQResponse bean = new Gson().fromJson(response, QQResponse.class);
                    if ((bean != null) && (bean.result == 200)) {
                        if ((bean.data != null) && (bean.data.size() > 0)) {
                            mConfigManager.setQQEditor(bean.data.get(0).editor);
                            mConfigManager.setQQTechnician(bean.data.get(0).technician);
                            mConfigManager.setQQManager(bean.data.get(0).manager);
                        } else {
                            Log.e(TAG, "result ok, data is null? ");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                if (e != null) {
                    Log.e(TAG, "onError  " + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about_qq, menu);
        menu.findItem(R.id.menu1).setTitle("内容QQ:" + BrandUtil.getQQEditor(mPreferencesHelper));
        menu.findItem(R.id.menu2).setTitle("技术QQ:" + BrandUtil.getQQTechnician(mPreferencesHelper));
        menu.findItem(R.id.menu3).setTitle("投诉QQ:" + BrandUtil.getQQManager(mPreferencesHelper));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu1:
                Util.startQQ(mContext, BrandUtil.getQQEditor(mPreferencesHelper));
                return true;
            case R.id.menu2:
                Util.startQQ(mContext, BrandUtil.getQQTechnician(mPreferencesHelper));
                return true;
            case R.id.menu3:
                Util.startQQ(mContext, BrandUtil.getQQManager(mPreferencesHelper));
                return true;
            default:
                return false;
        }
    }

    void clickMoreAppBtn() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = df.parse("2019-01-09");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long timestamp = cal.getTimeInMillis();
        if (System.currentTimeMillis() < timestamp) {
            return;
        }
        Intent intent = WebActivity.buildIntent(this, Constant.Url.MORE_APP);
        startActivity(intent);
    }

    void clickUpdateBtn() {
        mVersionManager.checkVersion(callBack);
    }

    private void showAlertDialog(String msg, DialogInterface.OnClickListener ocl) {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(R.string.alert_title);
        alert.setMessage(msg);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.alert_btn_ok), ocl);
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.alert_btn_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.aboutAppUpdateRl.setEnabled(true);
                        binding.aboutDownloadProgressbar.setVisibility(View.INVISIBLE);
                    }
                });
        alert.show();
    }

    VersionManager.AppUpdateCallBack callBack = new VersionManager.AppUpdateCallBack() {
        @Override
        public void appUpdateSave(final String versionCode, final String appUrl) {
            binding.aboutHasnewImageview.setVisibility(View.VISIBLE);
            binding.aboutAppUpdateRl.setEnabled(false);
            binding.aboutDownloadProgressbar.setVisibility(View.VISIBLE);
            showAlertDialog(
                    MessageFormat.format(getString(R.string.about_update_alert), versionCode),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenter.downloadApk(versionCode, appUrl);
                        }
                    });
        }

        @Override
        public void appUpdateFailed() {
            binding.aboutHasnewImageview.setVisibility(View.INVISIBLE);
            binding.aboutDownloadProgressbar.setVisibility(View.INVISIBLE);
            showToast(App.APP_NAME_CH + getString(R.string.about_update_isnew));
        }
    };

    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    @Override
    public void setDownloadProgress(int progress) {
        binding.aboutDownloadProgressbar.setProgress(progress);
    }

    @Override
    public void setDownloadMaxProgress(int maxProgress) {
        binding.aboutDownloadProgressbar.setMax(maxProgress);
    }

    @Override
    public void setProgressVisibility(int visible) {
        binding.aboutDownloadProgressbar.setVisibility(visible);
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    void clickClearUser() {
        if (!UserInfoManager.getInstance().isLogin()) {
            showToast("注册登录后，才能注销账号！");
            NewLoginUtil.startToLogin(this);
            return;
        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_logout, null);
        TextView remindText = view.findViewById(R.id.remindText);
        remindText.setText(R.string.clear_user_alert);
        AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle(getString(R.string.alert_title))
                .setView(view)
                .create();
        dialog.show();
        TextView agreeNo = view.findViewById(R.id.text_no_agree);
        agreeNo.setOnClickListener(v -> {
            dialog.dismiss();
        });
        TextView agree = view.findViewById(R.id.text_agree);
        agree.setText("继续注销");
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Log.e(TAG, "clickClearUser clicked.");
                fragment= new ClearUserFragment();
                fragment.show(getFragmentManager(), "ClearUser");
                fragment.setOnResult(new IDialogResultListener() {
                    @Override
                    public void onDataResult(Object result) {
                        if (result == null) {
                            Log.e(TAG, "clickClearUser onDataResult is null.");
                            showToast("密码输入为空，请输入有效的密码！");
                            return;
                        }
                        String userPassword = (String) result;
                        Log.e(TAG, "onDataResult userPassword " + userPassword);
                        if (userPassword.length() < 6 || userPassword.length() > 20) {
                            Log.e(TAG, "clickClearUser onDataResult is null.");
                            showToast("密码的格式(6-20位英文、数字、下划线)");
                            return;
                        }
                        // no need check, as user may change password by web page
                        mPresenter.clearUser(userPassword);
                    }
                });
            }
        });
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AboutActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void callPhone() {
        Intent intents = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + "4008881905");
        intents.setData(data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intents);
    }

    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
    public void callPhoneFail() {
        showToast(getString(R.string.call_permission) + getString(R.string.permission_fail));
    }


    //去除拨打电话权限，增加方法
    private void callCustomPhone(){
        Intent intents = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + "4008881905");
        intents.setData(data);
        startActivity(intents);
    }

    //注销账号
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginOutEvent event) {
        binding.customClearUser.setVisibility(View.GONE);
        finish();
    }
}
