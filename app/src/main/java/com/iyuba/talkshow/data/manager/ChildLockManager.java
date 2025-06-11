package com.iyuba.talkshow.data.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.efs.sdk.base.newsharedpreferences.SharedPreferencesNewImpl;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.util.ToastUtil;

/**
 * @desction: 儿童锁操作
 * @date: 2023/3/7 14:40
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public class ChildLockManager {
    private static final String TAG = "ChildLockManager";

    private static ChildLockManager instance;

    public static ChildLockManager getInstance(){
        if (instance==null){
            synchronized (ChildLockManager.class){
                if (instance==null){
                    instance = new ChildLockManager();
                }
            }
        }
        return instance;
    }

    //保存数据
    private SharedPreferences preferences;
    private static final String VERSION = "version";
    private static final String PASSWORD = "password";
    private static final String CHECK = "check";

    //开启数据
    private void open(Context context){
        if (preferences==null){
            preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        }
    }

    //获取版本号
    public int getVersion(Context context){
        open(context);

        return preferences.getInt(VERSION,0);
    }

    public void setVersion(Context context,int version){
        open(context);

        preferences.edit().putInt(VERSION, version).apply();
    }

    //是否开启监护模式
    public boolean isOpen(Context context){
        open(context);

        return preferences.getBoolean(CHECK,false);
    }

    public void setCheck(Context context,boolean check){
        open(context);

        preferences.edit().putBoolean(CHECK,check).apply();
    }

    //获取密码
    public String getPassword(Context context){
        open(context);

        return preferences.getString(PASSWORD,"");
    }

    public void setPassword(Context context,String password){
        open(context);

        preferences.edit().putString(PASSWORD,password).apply();
    }

    //是否处于监护中
    public boolean isChildLock(Context context,boolean showMsg){
        open(context);

        boolean isLock = preferences.getBoolean(CHECK,false);

        if (isLock&&showMsg){
            ToastUtil.showToast(context,"当前功能暂不对未成年人开放");
        }

        return isLock;
    }
}
