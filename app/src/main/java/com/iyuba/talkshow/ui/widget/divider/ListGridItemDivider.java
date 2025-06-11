package com.iyuba.talkshow.ui.widget.divider;

import android.content.Context;
import android.graphics.Canvas;
import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.ui.list.ListActivity;
import com.iyuba.talkshow.ui.list.ListAdapter;

/**
 * Created by Administrator on 2016/12/30/030.
 */

public class ListGridItemDivider extends GridItemDivider {
    public ListGridItemDivider(Context context) {
        super(context);
        mDivider = context.getResources().getDrawable(R.drawable.voa_activity_divider);
    }

    @Override
    boolean isLastColumn(RecyclerView parent, int pos, int spanCount, int childCount) {
        if(pos == ListAdapter.SELECTOR_POSITION) {
            return true;
        } else if(pos % ListActivity.SPAN_COUNT == 0) {
            return true;
        }
        return false;
    }

    @Override
    boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        return false;
    }

    @Override
    void drawHorizontal(Canvas c, RecyclerView parent) {
        //don't draw
    }
}
