package com.iyuba.talkshow.ui.widget.divider;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.lib_common.data.Constant;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.model.Header;
import com.iyuba.talkshow.ui.main.MainActivity;

/**
 * Created by Administrator on 2016/12/30/030.
 */

public class MainGridItemDivider extends GridItemDivider {
    public MainGridItemDivider(Context context) {
        super(context);
        mDivider = context.getResources().getDrawable(R.drawable.voa_activity_divider);
    }

    @Override
    boolean isLastColumn(RecyclerView parent, int pos, int spanCount, int childCount) {
        if (App.Apk.isChild()) {
            if (pos < 3 || pos == 7) {
                return true;
            }

        } else {
            if (Header.isHeaderOrFooter(pos)) {
                return true;
            }
        }

        if (pos <= Header.startIndex) {
            return pos % MainActivity.SPAN_COUNT == 0;
        }
        return (pos % MainActivity.SPAN_COUNT == (Header.startIndex % MainActivity.SPAN_COUNT));
    }

    @Override
    boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        return false;
    }
}
