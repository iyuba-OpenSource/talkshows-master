package com.iyuba.talkshow.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.iyuba.talkshow.ui.vip.buyvip.NewVipCenterActivity;

public class DialogUtil {

    /*******************************新的弹窗显示**********************************/
    //跳转vip界面弹窗
    public static void showVipDialog(Context context, String showMsg, int vipType){
        new AlertDialog.Builder(context)
                .setTitle("温馨提示")
                .setMessage(showMsg)
                .setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewVipCenterActivity.start(context,vipType);
                    }
                }).setNegativeButton("暂不需要",null)
                .create().show();
    }
}
