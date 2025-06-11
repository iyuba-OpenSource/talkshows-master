package com.iyuba.talkshow.ui.child;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.iyuba.talkshow.data.manager.ChildLockManager;
import com.iyuba.talkshow.databinding.AtyChildLockBinding;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
import com.iyuba.talkshow.util.ToastUtil;

/**
 * @desction: 青少年模式
 * @date: 2023/3/7 16:48
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public class ChildLockActivity extends BaseViewBindingActivity<AtyChildLockBinding> {

    public static void start(Context context){
        Intent intent = new Intent();
        intent.setClass(context,ChildLockActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initData();
        initClick();
    }

    private void initData(){
        String password = ChildLockManager.getInstance().getPassword(this);
        boolean isCheck = ChildLockManager.getInstance().isOpen(this);
        if (TextUtils.isEmpty(password)){
            binding.switchMsg.setText("监护状态：未开启");
            binding.open.setText("设置监护");
        }else {
            if (isCheck){
                binding.switchMsg.setText("监护状态：已开启");
                binding.open.setText("关闭监护");
            }else {
                binding.switchMsg.setText("监护模式：未开启");
                binding.open.setText("开启监护");
            }
        }
    }

    private void initClick(){
        binding.back.setOnClickListener(v->{
            finish();
        });

        binding.open.setOnClickListener(v->{
            String showText = binding.open.getText().toString();

            String password = binding.password.getText().toString();
            String password2 = binding.password2.getText().toString();

            if (TextUtils.isEmpty(password)
                    ||TextUtils.isEmpty(password2)){
                ToastUtil.showToast(this,"请输入密码");
                return;
            }

            if (password.length()!=4
                    ||password2.length()!=4
                    ||!password.equals(password2)){
                ToastUtil.showToast(this,"两次密码不一致");
                return;
            }

            if (showText.equals("设置监护")){
                ChildLockManager.getInstance().setCheck(this,true);
                ChildLockManager.getInstance().setPassword(this,password);

                binding.switchMsg.setText("监护状态：已开启");
                binding.open.setText("关闭监护");
            }else if (showText.equals("关闭监护")){
                ChildLockManager.getInstance().setCheck(this,false);

                binding.switchMsg.setText("监护模式：未开启");
                binding.open.setText("开启监护");
            }else if (showText.equals("开启监护")){
                ChildLockManager.getInstance().setCheck(this,true);

                binding.switchMsg.setText("监护状态：已开启");
                binding.open.setText("关闭监护");
            }

            binding.password.setText("");
            binding.password2.setText("");
        });
    }
}
