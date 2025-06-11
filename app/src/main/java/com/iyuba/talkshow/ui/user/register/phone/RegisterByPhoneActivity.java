package com.iyuba.talkshow.ui.user.register.phone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.databinding.ActivityRegisterByPhoneBinding;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.user.register.email.RegisterActivity;
import com.iyuba.talkshow.ui.user.register.submit.RegisterSubmitActivity;
import com.iyuba.talkshow.ui.web.WebActivity;
import com.iyuba.talkshow.ui.widget.LoadingDialog;
import com.iyuba.talkshow.util.TelNumMatch;
import com.iyuba.talkshow.util.ToastUtil;
import com.iyuba.talkshow.util.VerifyCodeSmsObserver;
import com.mob.MobSDK;
import com.tencent.vasdolly.helper.ChannelReaderUtil;
import com.umeng.analytics.MobclickAgent;

import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 手机注册界面
 */
public class RegisterByPhoneActivity extends BaseActivity implements RegisterByPhoneMvpView {


    ActivityRegisterByPhoneBinding binding ;
    @Inject
    RegisterByPhonePresenter mPresenter;
    @Inject
    ConfigManager configManager;
    private Timer timer;

    VerifyCodeSmsObserver.OnReceiveVerifyCodeSMSListener listener;
    private LoadingDialog mLoadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityRegisterByPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activityComponent().inject(this);
        setSupportActionBar(binding.registerToolbar.listToolbar);
        setupProtocol();
        mPresenter.attachView(this);
        mLoadingDialog = new LoadingDialog(this);
        setClick();

        //隐藏app的logo显示
        binding.logoIv.setVisibility(View.INVISIBLE);
    }

    private void setClick() {
        binding.registerByEmailTv.setVisibility(View.GONE);
        binding.registerByEmailTv.setOnClickListener(v -> clickToEmail());
        binding.registerNextBtn.setOnClickListener(v -> clickNext());
        binding.registerGetCodeBtn.setOnClickListener(v -> clickGetVCode());
    }

    public void setTextDrawable(TextView textView, int resId, int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(this, resId);
        drawable.setBounds(0, 0, width, height);
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    private void setupProtocol() {
        SpannableStringBuilder showSpan = new SpannableStringBuilder();
        String showText = getResources().getString(R.string.read_agree_policy);
        showSpan.append(showText);
        //儿童隐私政策
        String childPrivacyText = "《儿童隐私政策》";
        int childPrivacyIndex = showText.indexOf(childPrivacyText);
        ClickableSpan childPrivacySpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = WebActivity.buildIntent(mContext, App.Url.CHILD_PROTOCOL_URL + App.APP_NAME_CH, childPrivacyText);
                mContext.startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.colorPrimaryDark));
                ds.setUnderlineText(false);
            }
        };
        //隐私政策
        String privacyText = "《隐私政策》";
        int privacyIndex = showText.indexOf(privacyText);
        ClickableSpan privacySpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = WebActivity.buildIntent(mContext, App.Url.PROTOCOL_URL + App.APP_NAME_CH, privacyText);
                mContext.startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.colorPrimaryDark));
                ds.setUnderlineText(false);
            }
        };
        //使用条款
        String termText = "《使用条款》";
        int termIndex = showText.indexOf(termText);
        ClickableSpan termSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = WebActivity.buildIntent(mContext, App.Url.USAGE_URL + App.APP_NAME_CH, termText);
                mContext.startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.colorPrimaryDark));
                ds.setUnderlineText(false);
            }
        };
        //显示
        showSpan.setSpan(childPrivacySpan,childPrivacyIndex,childPrivacyIndex+childPrivacyText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        showSpan.setSpan(privacySpan,privacyIndex,privacyIndex+privacyText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        showSpan.setSpan(termSpan,termIndex,termIndex+termText.length(),Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        binding.tvPrivacy.setText(showSpan);
        binding.tvPrivacy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        MobSDK.submitPolicyGrantResult(true);
        MobSDK.init(getApplicationContext(), ConfigData.mob_key, ConfigData.mob_secret);
        SMSSDK.registerEventHandler(mEventHandler);
        listener = new VerifyCodeSmsObserver.OnReceiveVerifyCodeSMSListener() {
            @Override
            public void onReceive(String vcodeContent) {
                binding.registerCodeEdt.setText(vcodeContent);
            }
        };

        String channel = ChannelReaderUtil.getChannel(this);
        if (channel.equals("oppo")) {
            binding.logoIv.setImageResource(R.drawable.ic_launcher);
        }else {
            binding.logoIv.setImageResource(R.drawable.ic_launcher_old);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(mEventHandler);
        mPresenter.detachView();
        if (timer != null) {
            timer.cancel();
        }
        listener = null ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    void clickToEmail() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    void clickNext() {
        if (!binding.cbPrivacy.isChecked()){
            showToast("请选中下方的阅读并同意~");
            return;
        }

        configManager.setCheckAgree(true);
        String phoneNum = binding.registerPhoneNumEdt.getText().toString();
        String vcode = binding.registerCodeEdt.getText().toString();
        if (verification(phoneNum, vcode)) {
            requestSubmitCodePermissionSuccess();
        } else {
            showToast(R.string.verify_code_not_null);
        }
    }

    public void showToast(int resId) {
        hideKeyBoard();
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    void clickGetVCode() {
        String phoneNum = binding.registerPhoneNumEdt.getText().toString();
        if (verifyPhoneNumber(phoneNum)) {
            requestGetCodePermissionSuccess();
        } else {
            showToast(getString(R.string.phone_num_error));
        }
    }


    public void requestGetCodePermissionSuccess() {
        mLoadingDialog.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        String phoneNum = binding.registerPhoneNumEdt.getText().toString();
        mPresenter.getVerifyCode(phoneNum);
    }

    public void requestSubmitCodePermissionSuccess() {
        String phoneNumString = binding.registerPhoneNumEdt.getText().toString().trim();
        SMSSDK.submitVerificationCode("86", phoneNumString, binding.registerCodeEdt.getText().toString().trim());
    }


    private boolean verification(String phoneNum, String vcode) {
        if (!verifyPhoneNumber(phoneNum)) {
            showToast(getString(R.string.phone_num_error));
            return false;
        }
        if (TextUtils.isEmpty(vcode)) {
            showToast(getString(R.string.verify_code_not_null));
            return false;
        }
        return true;
    }

    private boolean verifyPhoneNumber(String phoneNum) {
        return TelNumMatch.isPhonenumberLegal(phoneNum);
    }

    @Override
    public void updateGetCodeBtn() {
        SMSSDK.getVerificationCode("86", binding.registerPhoneNumEdt.getText().toString());
        Log.d("RegisterByPhoneActivity", "updateGetCodeBtn: "+binding.registerPhoneNumEdt.getText().toString());
        binding.registerGetCodeBtn.setText(getString(R.string.register_input_code));
        binding.registerGetCodeBtn.setTextColor(Color.WHITE);
        setGetCodeBtn(false);
        timer = new Timer();
        TimerTask task = new TimerTask() {
            int countdown = 60;

            @Override
            public void run() {
                if (countdown > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.registerGetCodeBtn.setText(MessageFormat.format(getString(R.string.seconds_for_get_code), countdown));
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timer.cancel();
                            setGetCodeBtn(true);
                            binding.registerGetCodeBtn.setText(getString(R.string.get_verify_code));
                        }
                    });
                }
                countdown -= 1;
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    @Override
    public void dismissWaitingDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void registerSmsObserver() {
//        getContentResolver().registerContentObserver(Uri.parse("mContentEdt://sms/"), true, smsObserver);
    }

    private EventHandler mEventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                switch (event) {
                    case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(R.string.register_code_sent);
                                binding.registerNextBtn.setEnabled(true);
                            }
                        });
                        break;
                    case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(R.string.register_verify_success);
                            }
                        });
                        String phoneNumString = binding.registerPhoneNumEdt.getText().toString();
                        Intent intent = new Intent();

                        //跳转到注册界面
                        intent.setClass(RegisterByPhoneActivity.this, RegisterSubmitActivity.class);
                        intent.putExtra(RegisterSubmitActivity.PhoneNum, phoneNumString);
                        intent.putExtra(RegisterSubmitActivity.RegisterMob,true);
                        //合成随机数据显示
                        intent.putExtra(RegisterSubmitActivity.UserName,getRandomByPhone(phoneNumString));
                        intent.putExtra(RegisterSubmitActivity.PassWord,phoneNumString.substring(phoneNumString.length()-6));
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;
                }
            } else {
                if (data != null) {
                    Log.e("RegisterByPhoneActivity", "mEventHandler " + ((Throwable)data).getMessage());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            showToast(R.string.input_correct_code);
                            binding.registerGetCodeBtn.setText(getString(R.string.get_verify_code));
                            setGetCodeBtn(true);
                        }
                    }
                });
            }
        }
    };


    /*********************************键盘设置***************************/
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

    /**********************************切换获取验证码按钮*********************************/
    private void setGetCodeBtn(boolean isViewEnable){
        if (isViewEnable){
            binding.registerGetCodeBtn.setEnabled(true);
            binding.registerGetCodeBtn.setBackgroundResource(R.drawable.shape_green_button);
        }else {
            binding.registerGetCodeBtn.setEnabled(false);
            binding.registerGetCodeBtn.setBackgroundResource(R.drawable.shape_gray_button);
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
}
