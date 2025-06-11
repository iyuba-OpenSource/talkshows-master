package com.iyuba.lib_common.manager;

import android.content.SharedPreferences;

import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.lib_common.util.LibSPUtil;

/**
 * @desction: 网络host管理
 * @date: 2023/4/10 22:37
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public class NetHostManager {
    private static final String TAG = "NetHostManager";

    public static final String domain_name = "domain_name";//主域名
    private static final String DEF_DOMAIN_NAME = "www.iyuba.cn";
    public static final String domain_short = "domain_short";//短域名
    private static final String DEF_DOMAIN_SHORT = "iyuba.cn";
    public static final String domain_long = "domain_long";//长域名
    private static final String DEF_DOMAIN_LONG = "iyuba.com.cn";


    private static NetHostManager instance;

    public static NetHostManager getInstance(){
        if (instance==null){
            synchronized (NetHostManager.class){
                if (instance==null){
                    instance = new NetHostManager();
                }
            }
        }
        return instance;
    }

    private SharedPreferences open(){
        return LibSPUtil.getPreferences(LibResUtil.getInstance().getContext(), TAG);
    }

    public void setDomainName(String value){
        LibSPUtil.putString(open(),domain_name,value);
    }

    public String getDomainName(){
        return LibSPUtil.loadString(open(),domain_name,DEF_DOMAIN_NAME);
    }

    public void setDomainShort(String value){
        LibSPUtil.putString(open(),domain_short,value);
    }

    public String getDomainShort(){
        return LibSPUtil.loadString(open(),domain_short,DEF_DOMAIN_SHORT);
    }

    public void setDomainLong(String value){
        LibSPUtil.putString(open(),domain_long,value);
    }

    public String getDomainLong(){
        return LibSPUtil.loadString(open(),domain_long,DEF_DOMAIN_LONG);
    }
}
