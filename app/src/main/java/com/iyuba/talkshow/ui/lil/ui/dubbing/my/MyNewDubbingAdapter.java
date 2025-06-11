package com.iyuba.talkshow.ui.lil.ui.dubbing.my;

import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class MyNewDubbingAdapter extends FragmentStateAdapter {

    private List<Pair<String,Fragment>> list;

    public MyNewDubbingAdapter(@NonNull FragmentActivity fragmentActivity, List<Pair<String,Fragment>> list) {
        super(fragmentActivity);
        this.list = list;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return list.get(position).second;
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }
}
