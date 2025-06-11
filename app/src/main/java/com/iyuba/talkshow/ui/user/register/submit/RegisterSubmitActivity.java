package com.iyuba.talkshow.ui.user.register.submit;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.Toast;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.databinding.ActivityRegisterSubmitBinding;
import com.iyuba.talkshow.event.LoginEvent;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.lil.view.LoadingNewDialog;
import com.iyuba.talkshow.ui.user.edit.ImproveUserActivity;
import com.iyuba.talkshow.ui.widget.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;


public class RegisterSubmitActivity extends BaseActivity implements RegisterSubmitMvpView {
    public static String PhoneNum = "phoneNumb";
    public static String UserName = "UserName";
    public static String PassWord = "PassWord";
    public static String RegisterMob = "RegisterMob";

    private String phoneNum;
    private String userName;
    private String passWord;
    private boolean registerMob;

    @Inject
    RegisterSubmitPresenter mPresenter;

    ActivityRegisterSubmitBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterSubmitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activityComponent().inject(this);
        setSupportActionBar(binding.registsubmitToolbar.listToolbar);
        mPresenter.attachView(this);
        binding.registsubmitSubmitBtn.setOnClickListener(v -> clickSubmit());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        phoneNum = getIntent().getExtras().getString(PhoneNum);
        userName = getIntent().getExtras().getString(UserName);
        passWord = getIntent().getExtras().getString(PassWord);
        registerMob = getIntent().getExtras().getBoolean(RegisterMob);

        if (registerMob) {
            binding.registsubmitUsernameEdt.setText(userName);
            binding.registsubmitPwdEdt.setText(passWord);
            binding.registsubmitPwdEdt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            binding.registerTip.setText(R.string.login_name_tip);
        }
        if (App.APP_ID == 280) {
            binding.logoIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
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
        mPresenter.detachView();
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

    void clickSubmit() {
        String userName = binding.registsubmitUsernameEdt.getText().toString();
        String passWord = binding.registsubmitPwdEdt.getText().toString();
//        String phoneNum = getIntent().getExtras().getString("phoneNumb");
        if (verification(userName, passWord)) {
            startLoading("正在注册账号...");
            mPresenter.register(userName,  passWord, phoneNum);
        }
    }

    private boolean verification(String userName, String passWord) {
        if (!checkUsernameLength(userName)) {
            binding.registsubmitUsernameEdt.setError(getString(R.string.register_check_username_1));
            return false;
        }
        if (!checkPasswordLength(passWord)) {
            binding.registsubmitPwdEdt.setError(getString(R.string.register_check_pwd_1));
            return false;
        }
        return true;
    }

    private boolean checkUsernameLength(String username) {
        return (username.length() >= 3 && username.length() <= 15);
    }

    private boolean checkPasswordLength(String userPwd) {
        return (userPwd.length() >= 6 || userPwd.length() <= 20);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startUploadImageActivity() {
        Intent intent = new Intent(this, ImproveUserActivity.class);
        intent.putExtra("register", true);
        startActivity(intent);

        //这里刷新数据显示
        EventBus.getDefault().post(new LoginEvent());
    }
    @Override
    public void startLoginActivity() {
        NewLoginUtil.startToLogin(this);
    }

    @Override
    public void finishRegisterActivity() {
        finish();
    }

    @Override
    public void dismissWaitingDialog() {
        stopLoading();
    }

    //开启加载弹窗
    private LoadingNewDialog loadingNewDialog;

    private void startLoading(String showMsg){
        if (loadingNewDialog==null){
            loadingNewDialog = new LoadingNewDialog(this);
            loadingNewDialog.create();
        }
        loadingNewDialog.setMsg(showMsg);
        loadingNewDialog.show();
    }

    private void stopLoading(){
        if (loadingNewDialog!=null && loadingNewDialog.isShowing()){
            loadingNewDialog.cancel();
        }
    }
}
