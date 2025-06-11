package com.iyuba.iyubamovies.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.iyuba.iyubamovies.R;

/**
 * Created by iyuba on 2018/1/18.
 */

public class IMoviesInputPopWindow {
    private Context context;
    private View view;
    private PopupWindow inputWindow;
    private EditText content;
    private TextView dismiss;
    private TextView send;
    private String in_content="";
    private LinearLayout root;
    private InputMethodManager imm;
    private int position = -1;
    private OnInputSendClickListener onInputSendClickListener;
//    private OnContentSizeOutLimitListener onContentSizeOutLimitListener;
    public IMoviesInputPopWindow(Context context){
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.imv_input_popview,null);
        initView(view);
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
    private void initView(View view){
        content = (EditText) view.findViewById(R.id.imv_input_content);
        dismiss = (TextView) view.findViewById(R.id.imv_input_dismiss);
        send = (TextView) view.findViewById(R.id.imv_input_send);
        content.setTextIsSelectable(true);
        content.setFocusable(true);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imm.isActive())
                imm.toggleSoftInput(1500, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        content.setFocusableInTouchMode(true);
        inputWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
        inputWindow.setFocusable(true);
        // 设置允许在外点击消失
        inputWindow.setOutsideTouchable(false);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        inputWindow.setBackgroundDrawable(new BitmapDrawable());
        //软键盘不会挡着popupwindow
        inputWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置菜单显示的位置
        //inputWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //监听菜单的关闭事件
        inputWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        //监听触屏事件
        inputWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                return false;
            }
        });
        inputWindow.setAnimationStyle(R.style.imv_popwin_anim_style);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText("");
                dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onInputSendClickListener!=null){
                    in_content = content.getText().toString().trim();
                    send.setText("发送中...");
                    send.setTextColor(Color.GRAY);
                    send.setClickable(false);
                    onInputSendClickListener.OnInputSendClick(in_content,position);

                }
            }
        });
    }
    public void sendSuccess(){
        in_content = "";
        content.setText("");
        dismiss();
    }
    public void sendUnsuccess(){
        send.setText("发送");
        send.setClickable(true);
    }
    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @SuppressLint("WrongConstant")
    public void showDownAs(View view){
        if(Build.VERSION.SDK_INT<24) {
            inputWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            inputWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            inputWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
//            Rect rect = new Rect();
//            view.getGlobalVisibleRect(rect);
//            int h = view.getResources().getDisplayMetrics().heightPixels - rect.bottom;
//            inputWindow.setHeight(h);
//            inputWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//            inputWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            inputWindow.showAsDropDown(view);
            inputWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
            inputWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            inputWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        imm.toggleSoftInput(2000, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public boolean isshowing(){
        return inputWindow.isShowing();
    }
    public void dismiss(){
        position = -1;
        send.setText("发送");
        send.setClickable(true);

        if(inputWindow.isShowing())
            inputWindow.dismiss();
    }

    public void setOnInputSendClickListener(OnInputSendClickListener onInputSendClickListener) {
        this.onInputSendClickListener = onInputSendClickListener;
    }

    public interface OnInputSendClickListener{
        void OnInputSendClick(String content,int position);
    }
//    public interface OnContentSizeOutLimitListener{
//        void OnContentSizeOutLimit();
//    }
//
//    public void setOnContentSizeOutLimitListener(OnContentSizeOutLimitListener onContentSizeOutLimitListener) {
//        this.onContentSizeOutLimitListener = onContentSizeOutLimitListener;
//    }
}
