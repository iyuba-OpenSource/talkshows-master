package com.iyuba.wordtest.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.iyuba.lib_common.util.LibGlide3Util;
import com.iyuba.wordtest.BuildConfig;
import com.iyuba.wordtest.R;
import com.iyuba.wordtest.manager.WordManager;
import com.iyuba.wordtest.ui.TalkshowWordListActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class StepAdapter extends BaseAdapter {


    private int step;
    private int size;
    private int book_id ;


    Context context;

    public StepAdapter(int book_id , int step, int size) {
        this.book_id = book_id;
        this.step = step;
        this.size = size;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ResourceAsColor", "DefaultLocale"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.wordtest_step_item, parent, false);
        }
        ViewHolder holder = new ViewHolder(convertView);
        TextView tv = holder.tv;
        CircleImageView iv = holder.img;
        ImageView iv_avtor = holder.img_avator;
        View vView = holder.lineVt;
        iv.setVisibility(View.GONE);
        iv_avtor.setVisibility(View.GONE);
        tv.setText(String.format("Unit %d", position + 1));

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BuildConfig.DEBUG&&WordManager.getInstance().vip == 0 && position>=2){
                    ToastUtils.showShort("非VIP会员只能免费解锁前两关，如需解锁更多关卡，请开通VIP后重试");
                    return;
                }
                if (position < step || BuildConfig.DEBUG) {
                    TalkshowWordListActivity.start(context, book_id , position + 1 ,position, false);
                }else {
                    ToastUtils.showShort(R.string.alert_step_no_finish);
                }

            }
        });

        if (step <= position) {
            tv.setBackground(context.getResources().getDrawable(R.drawable.word_step_bg_lock));
//            tv.setText("未解锁");
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv.getBackground().setTint(context.getResources().getColor(R.color.colorPrimary));
            }
        }
        vView.setVisibility(View.INVISIBLE);

        if (step == position + 1) {
            iv.setVisibility(View.VISIBLE);
            loadUserIcon( holder.img);
            iv_avtor.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private void loadUserIcon( ImageView imageView) {

        String userIconUrl = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid="
                + WordManager.getInstance().userid + "&size=big";
        LibGlide3Util.loadImg(context,userIconUrl,R.drawable.noavatar_small,imageView);
    }

    static class ViewHolder {
        TextView tv;
        View lineVt;
        CircleImageView img;
        ImageView img_avator;


        ViewHolder(View view) {
            tv = view.findViewById(R.id.tv);
            lineVt = view.findViewById(R.id.line_vt);
            img = view.findViewById(R.id.img);
            img_avator = view.findViewById(R.id.img_indicator);
        }
    }
}
