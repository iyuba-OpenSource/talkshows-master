package com.iyuba.talkshow.ui.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.databinding.ActivityChangeNameBinding;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.ui.web.WebActivity;
import com.iyuba.talkshow.ui.widget.LoadingDialog;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

/**
 * Created by carl shen on 2021/2/25
 * New Primary English, new study experience.
 */
public class ChangeNameActivity extends BaseActivity implements ChangeNameMvpView {
    public static String RegisterMob = "RegisterMob";
    private String userName;
    private String passWord;
    private int changeFlag = 0;

    @Inject
    ChangeNamePresenter mPresenter;

    ActivityChangeNameBinding binding ;
    LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activityComponent().inject(this);
        setSupportActionBar(binding.changeNameToolbar.listToolbar);
        mPresenter.attachView(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mLoadingDialog = new LoadingDialog(this);
        binding.passwordSubmit.setOnClickListener(v -> clickSubmitPassword());
        binding.nameSubmit.setOnClickListener(v -> clickSubmit());
        binding.passwordForget.setOnClickListener(v -> startWeb());

        userName = UserInfoManager.getInstance().getUserName();
        if (!TextUtils.isEmpty(userName)) {
            binding.changeName.setText(getText(R.string.user_name) + "  " + userName);
        }
        changeFlag = getIntent().getExtras().getInt(RegisterMob, 0);
        if (changeFlag > 0) {
            binding.passwordConfirm.setVisibility(View.GONE);
            binding.nameChange.setVisibility(View.VISIBLE);
        } else {
            binding.passwordConfirm.setVisibility(View.VISIBLE);
            binding.nameChange.setVisibility(View.GONE);
        }
        if (App.APP_ID == 280) {
            binding.logoIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
        }
    }

    void clickSubmitPassword() {
        passWord = binding.passwordInput.getText().toString().trim();
        Log.e("ChangeNameActivity", "clickSubmitPassword passWord " + passWord);
        if (!checkPasswordLength(passWord)) {
            showToast(R.string.register_check_pwd_1);
            return;
        }
        if (passWord.equals(UserInfoManager.getInstance().getLoginPassword())) {
            showToast("用户名密码验证成功，请输入新用户名。");
            binding.passwordConfirm.setVisibility(View.GONE);
            binding.nameChange.setVisibility(View.VISIBLE);
            changeFlag = 1;
        } else {
            showToast("用户名密码验证失败！请重新输入，或者点击忘记密码找回密码。");
        }
    }
    void clickSubmit() {
        String userNewName = binding.nameInput.getText().toString();
        if (!checkUsernameLength(userNewName)) {
            showToast(R.string.register_check_username_1);
            return;
        }
        if (userNewName.equals(userName)) {
            showToast("新用户名和旧用户名相同。");
            return;
        }
        changeFlag = 2;
        mLoadingDialog.show();
        mPresenter.ChangeUserName(String.valueOf(UserInfoManager.getInstance().getUserId()), userNewName, userName);
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
                if (changeFlag > 0) {
                    changeFlag = 0;
                    binding.passwordConfirm.setVisibility(View.VISIBLE);
                    binding.nameChange.setVisibility(View.GONE);
                } else {
                    finish();
                }
                break;
        }
        return true;
    }

    private boolean checkUsernameLength(String username) {
        return ((username != null) && username.length() >= 3 && username.length() <= 15);
    }

    private boolean checkPasswordLength(String userPwd) {
        return ((userPwd != null) && userPwd.length() >= 6 && userPwd.length() <= 20);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    public void startWeb() {
        Intent intent  = new Intent();
        intent.setClass(mContext, WebActivity.class);
        intent.putExtra("url", "http://m." + Constant.Web.WEB_SUFFIX + "m_login/inputPhonefp.jsp");
        intent.putExtra("title", "密码找回");
        startActivity(intent);
    }

    @Override
    public void finishChangeActivity() {
        finish();
    }

    @Override
    public void dismissWaitingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
