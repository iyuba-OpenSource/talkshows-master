package com.iyuba.talkshow.ui.about;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.databinding.ActivitySendbookBinding;
import com.iyuba.talkshow.ui.base.BaseActivity;
import com.iyuba.talkshow.util.ToastUtil;

import java.util.List;


/**
 * @author yq QQ:1032006226
 * @name talkshow
 * @class name：com.iyuba.talkshow.ui.about
 * @class describe
 * @time 2019/1/6 14:10
 * @change
 * @chang time
 * @class describe
 */
public class SendBookActivity extends BaseActivity {

    ActivitySendbookBinding binding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendbookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setSupportActionBar(mToolbar);
//        SpannableString spanText = new SpannableString("送书啦！\n" +
//                "只要在手机应用商店中对本应用进行五星好评，并截图发送给QQ：3099007489，即可获得十本英文短篇名著+最适合学英语的20部经典英文电影。\n机会难得，不容错过，小伙伴们赶快行动吧!");
        SpannableString spanText = new SpannableString("\u3000\u3000送书啦！\n\u3000\u3000只要在应用商店中对本应用进行五星好评，并截图发给QQ：3099007489，即可获得3天的会员试用资格以及赠送一本由爱语吧名师团队编写的电子书哦。\n\u3000\u3000机会难得，不容错过，小伙伴们赶快行动吧!");

        spanText.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);       //设置文件颜色
                ds.setUnderlineText(true);      //设置下划线
            }

            @Override
            public void onClick(View view) {
                if (isQQClientAvailable(SendBookActivity.this)) {
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=";
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url + "3099007489")));
                } else {
                    ToastUtil.show(SendBookActivity.this, "未安装qq客户端");
                }
            }
        }, 36, 46, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.text.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
        binding.text.setText(spanText);
        binding.text.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件

        binding.commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } catch (Exception e) {
                    AlertDialog dialog = new AlertDialog.Builder(SendBookActivity.this).create();
                    dialog.setIcon(android.R.drawable.ic_dialog_alert);
                    dialog.setTitle(getResources().getString(R.string.alert_title));
                    dialog.setMessage("您当前未安装应用市场，无法使用该功能");
                    dialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.alert_btn_ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    dialog.show();
                }
            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
