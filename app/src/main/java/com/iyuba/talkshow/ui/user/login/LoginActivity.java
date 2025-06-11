//package com.iyuba.talkshow.ui.user.login;
//
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.text.Html;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.TextPaint;
//import android.text.TextUtils;
//import android.text.method.LinkMovementMethod;
//import android.text.style.ClickableSpan;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.animation.AccelerateInterpolator;
//import android.widget.CompoundButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.core.content.ContextCompat;
//
//import com.daimajia.androidanimations.library.Techniques;
//import com.daimajia.androidanimations.library.YoYo;
//import com.iyuba.lib_common.data.Constant;
//import com.iyuba.talkshow.R;
//import com.iyuba.talkshow.constant.App;
//import com.iyuba.talkshow.data.DataManager;
//import com.iyuba.talkshow.data.manager.ChildLockManager;
//import com.iyuba.talkshow.data.manager.ConfigManager;
//import com.iyuba.talkshow.databinding.ActivityLoginBinding;
//import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
//import com.iyuba.talkshow.ui.user.edit.ImproveUserActivity;
//import com.iyuba.talkshow.ui.user.register.phone.RegisterByPhoneActivity;
//import com.iyuba.talkshow.ui.web.WebActivity;
//import com.iyuba.talkshow.ui.widget.LoadingDialog;
//import com.iyuba.talkshow.util.ToastUtil;
//import com.umeng.analytics.MobclickAgent;
//
//import javax.inject.Inject;
//
//
//public class LoginActivity extends BaseViewBindingActivity<ActivityLoginBinding> implements LoginMvpView {
//
//    @Inject
//    LoginPresenter mPresenter;
//    @Inject
//    ConfigManager mConfigManager;
//    @Inject
//    DataManager mDataManager;
//    LoadingDialog mDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activityComponent().inject(this);
//        setSupportActionBar(binding.loginToolbar.listToolbar);
//        setupProtocol();
//        mPresenter.attachView(this);
//        mDialog = new LoadingDialog(this);
//        setClick();
//    }
//
//    private void setClick() {
//        binding.loginRegisterBtn.setOnClickListener(v -> clickRegister());
//        binding.loginLoginBtn.setOnClickListener(v -> clickLogin());
//        binding.loginResetPwdTv.setOnClickListener(v -> startWeb());
//        binding.loginAutoCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mConfigManager.setAutoLogin(isChecked);
//            }
//        });
//        binding.registerProtocolTv.setOnClickListener(v->{
//            if (binding.registerProtocolTv.isSelected()){
//                binding.registerProtocolTv.setSelected(false);
//                setTextDrawable(binding.registerProtocolTv,R.drawable.checkbox_unchecked,50,50);
//            }else {
//                binding.registerProtocolTv.setSelected(true);
//                setTextDrawable(binding.registerProtocolTv,R.drawable.checkbox_checked,50,50);
//            }
//        });
//    }
//
//    public void setTextDrawable(TextView textView, int resId, int width, int height) {
//        Drawable drawable = ContextCompat.getDrawable(this, resId);
//        drawable.setBounds(0, 0, width, height);
//        textView.setCompoundDrawables(drawable, null, null, null);
//    }
//
//    private void setupProtocol() {
//        setTextDrawable(binding.registerProtocolTv,R.drawable.checkbox_unchecked,50,50);
//        String remindString = getResources().getString(R.string.read_agree_policy);
//
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(remindString);
//        spannableStringBuilder.setSpan(privacySpan, remindString.indexOf("隐私政策"), remindString.indexOf("隐私政策")+4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.setSpan(termsSpan,remindString.indexOf("使用条款"),remindString.indexOf("使用条款")+4,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        binding.registerProtocolTv.setText(spannableStringBuilder);
//        binding.registerProtocolTv.setMovementMethod(LinkMovementMethod.getInstance());
//    }
//
//    ClickableSpan privacySpan = new ClickableSpan() {
//        @Override
//        public void onClick(View widget) {
//            Intent intent = WebActivity.buildIntent(mContext, App.Url.PROTOCOL_URL+ App.APP_NAME_CH, "隐私政策");
//            mContext.startActivity(intent);
//        }
//
//        @Override
//        public void updateDrawState(TextPaint ds) {
//            ds.setColor(getResources().getColor(R.color.colorPrimary));
//            ds.setUnderlineText(true);
//        }
//    };
//
//    ClickableSpan termsSpan = new ClickableSpan() {
//        @Override
//        public void onClick(@NonNull View widget) {
//            Intent intent = WebActivity.buildIntent(mContext, App.Url.USAGE_URL+ App.APP_NAME_CH, "用户服务协议");
//            mContext.startActivity(intent);
//        }
//
//        @Override
//        public void updateDrawState(TextPaint ds) {
//            ds.setColor(getResources().getColor(R.color.colorPrimary));
//            ds.setUnderlineText(true);
//        }
//    };
//
//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        binding.loginAutoCheckbox.setChecked(mConfigManager.isAutoLogin());
//        binding.loginResetPwdTv.setText(Html.fromHtml("<a href=\"http://m."+com.iyuba.lib_common.data.Constant.Web.WEB_SUFFIX+"m_login/inputPhonefp.jsp\">"
//                + getString(R.string.login_find_password) + "</a>"));
//        binding.loginResetPwdTv.setMovementMethod(LinkMovementMethod.getInstance());
//        if (App.APP_ID == 280) {
//            binding.logoIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
////        binding.loginAutoCheckbox.setChecked(true);
//        MobclickAgent.onResume(this);
//        if (TextUtils.isEmpty(binding.loginUsernameEdt.getText().toString().trim()) && !TextUtils.isEmpty(mConfigManager.getAdName())) {
//            binding.loginUsernameEdt.setText(mConfigManager.getAdName());
//        }
//        if (TextUtils.isEmpty(binding.loginPwdEdt.getText().toString().trim()) && !TextUtils.isEmpty(mConfigManager.getAdPass())) {
//            binding.loginPwdEdt.setText(mConfigManager.getAdPass());
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mPresenter.detachView();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//        }
//        return true;
//    }
//
////    @PermissionGrant(REQUEST_LOCATION_PERMISSION)
//    public void login() {
//        mConfigManager.setAutoLogin(true);
//
//        String userName = binding.loginUsernameEdt.getText().toString();
//        String password = binding.loginPwdEdt.getText().toString();
//        mPresenter.login(userName, password);
//    }
//
////    @PermissionDenied(REQUEST_LOCATION_PERMISSION)
//    public void requestLocationFail() {
//        Toast.makeText(this, "权限不足,无法登录", Toast.LENGTH_SHORT).show();
//    }
//
////    @OnCheckedChanged(R.id.login_auto_checkbox)
////    void checkAutoLogin(boolean checked) {
////        mConfigManager.setAutoLogin(checked);
////    }
//
////    @OnClick(R.id.login_register_btn)
//    void clickRegister() {
//        if (ChildLockManager.getInstance().isChildLock(this,true)){
//            return;
//        }
//
//        Intent intent = new Intent(this, RegisterByPhoneActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
////    @OnClick(R.id.login_login_btn)
//    void clickLogin() {
//        String userName = binding.loginUsernameEdt.getText().toString();
//        String userPwd = binding.loginPwdEdt.getText().toString();
//        if (verification(userName, userPwd)) {
//
//            if (!binding.registerProtocolTv.isSelected()){
//                ToastUtil.showToast(mContext, "请选中下方的阅读并同意~");
//                return;
//            }
//            login();
//        }
//    }
//
//    private boolean verification(String userName, String userPwd) {
//        if (userName.length() < 3) {
//            YoYo.with(Techniques.Shake).duration(200)
//                    .interpolate(new AccelerateInterpolator())
//                    .playOn(findViewById(R.id.login_username_phone_ll));
////            mEdtUserName.setError(getResources().getString(R.string.login_check_effective_user_id));
//            return false;
//        }
//        if (userPwd.length() == 0) {
//            YoYo.with(Techniques.Shake).duration(200)
//                    .interpolate(new AccelerateInterpolator())
//                    .playOn(findViewById(R.id.login_pwd_ll));
////            mEdtPwd.setError(getResources().getString(R.string.login_check_user_pwd_null));
//            return false;
//        }
//        if (!checkUserPwd(userPwd)) {
//            YoYo.with(Techniques.Shake).duration(200)
//                    .interpolate(new AccelerateInterpolator())
//                    .playOn(findViewById(R.id.login_pwd_ll));
//            binding.loginPwdEdt.setError(getResources().getString(R.string.login_check_user_pwd_constraint));
//            return false;
//        }
//        return true;
//    }
//
//    private boolean checkUserPwd(String userPwd) {
//        return userPwd.length() >= 6 && userPwd.length() <= 20;
//    }
//
//    @Override
//    public void showWaitingDialog() {
//        mDialog.show();
//    }
//
//    @Override
//    public void dismissWaitingDialog() {
//        mDialog.dismiss();
//    }
//
//    @Override
//    public void showToast(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void startImproveUser(int userId){
//        if ((userId > 0) && mDataManager.getPreferencesHelper().loadBoolean("userId_" + userId, true)) {
//            Intent userInfo = new Intent(mContext, ImproveUserActivity.class);
//            userInfo.putExtra("register", false);
//            startActivity(userInfo);
//        }
//        finish();
//    }
//
//    public void startWeb(){
//        Intent intent  = new Intent();
//        intent.setClass(mContext, WebActivity.class);
//        intent.putExtra("url", "http://m." + Constant.Web.WEB_SUFFIX + "m_login/inputPhonefp.jsp");
//        intent.putExtra("title", "密码找回");
//        startActivity(intent);
//    }
//}
