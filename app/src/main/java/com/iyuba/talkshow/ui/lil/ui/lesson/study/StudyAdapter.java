package com.iyuba.talkshow.ui.lil.ui.lesson.study;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * @title:
 * @date: 2024/1/3 15:12
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class StudyAdapter extends FragmentStateAdapter {

    private List<Fragment> list;

    public StudyAdapter(@NonNull FragmentActivity fragmentActivity,List<Fragment> list) {
        super(fragmentActivity);
        this.list = list;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    //刷新数据
    public void refreshData(List<Fragment> refreshList){
        this.list = refreshList;
        notifyDataSetChanged();
    }
}
