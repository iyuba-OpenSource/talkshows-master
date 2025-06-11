package com.iyuba.iyubamovies.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.iyuba.iyubamovies.R;
import com.iyuba.iyubamovies.ui.adapter.IMvserisesItemAdapter;

import java.util.List;

/**
 * Created by iyuba on 2018/1/16.
 */

public class IMvChooseSerisesPopWindow {
    private PopupWindow popupWindow;
    private View view;
    private Context context;
    private RecyclerView pop_list;
    private ImageView close;
    private IMvserisesItemAdapter adapter;
    private boolean isdownload = false;
    private OnPopWindowDismissListener onPopWindowDismissListener;
    public IMvChooseSerisesPopWindow(Context context){
        view = LayoutInflater.from(context).inflate(R.layout.imv_serises_popview,null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,false);
        initpop(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.imv_popwin_anim_style);
        this.context = context;
    }
    private void initpop(View view){
        pop_list = (RecyclerView) view.findViewById(R.id.imv_pop_list);
        close = (ImageView) view.findViewById(R.id.imv_pop_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if(onPopWindowDismissListener!=null){
                    onPopWindowDismissListener.onDismissClick();
                }
            }
        });
        pop_list.setLayoutManager(new GridLayoutManager(context,5));
        if(adapter!=null)
        pop_list.setAdapter(adapter);
    }
    public void setDatas(List<Object> items){
        adapter.setItems(items);
    }
    public boolean isShowing(){
       return popupWindow.isShowing();
    }
    public void setAdapter(IMvserisesItemAdapter adapter){
         this.adapter = adapter;
         pop_list.setAdapter(this.adapter);
    }
    public void setPopItemClickLisener(IMvserisesItemAdapter.OnCircleItemClickLisenter onCircleItemClickLisenter){
        adapter.setOnCircleItemClickLisenter(onCircleItemClickLisenter);
    }
    @SuppressLint("WrongConstant")
    public void showDownPosition(View view){
        if(Build.VERSION.SDK_INT<24) {
            popupWindow.showAsDropDown(view);

        } else {
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);
            int h = view.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            popupWindow.setHeight(h);
            popupWindow.showAsDropDown(view);
        }
    }
    public void dismiss(){
        popupWindow.dismiss();
    }
    public interface OnPopWindowDismissListener{
        void onDismissClick();
    }

    public void setOnPopWindowDismissListener(OnPopWindowDismissListener onPopWindowDismissListener) {
        this.onPopWindowDismissListener = onPopWindowDismissListener;
    }
}
