package com.iyuba.talkshow.ui.lil.ui.choose;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.ui.BaseViewBindingActivity;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.databinding.LayoutContainerBinding;
import com.iyuba.talkshow.ui.lil.ui.choose.junior.JuniorChooseFragment;

/**
 * @title: 选书界面
 * @date: 2023/5/19 11:37
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class LessonBookChooseActivity extends BaseViewBindingActivity<LayoutContainerBinding> {

    public static void start(Context context,String type,boolean isWord){
        Intent intent = new Intent();
        intent.setClass(context, LessonBookChooseActivity.class);
        intent.putExtra(StrLibrary.types,type);
        intent.putExtra(StrLibrary.word,isWord);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        initFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initFragment(){
        String types = getIntent().getStringExtra(StrLibrary.types);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (types.equals(TypeLibrary.BookType.junior_primary)
                ||types.equals(TypeLibrary.BookType.junior_middle)){
            boolean isJuniorWord = getIntent().getBooleanExtra(StrLibrary.word,false);

            //中小学
            JuniorChooseFragment fragment = JuniorChooseFragment.getInstance(isJuniorWord);
            transaction.add(R.id.container,fragment).show(fragment);
        }
        /*else if (types.equals(TypeLibrary.BookType.newCamstory)
                ||types.equals(TypeLibrary.BookType.newCamstoryColor)
                ||types.equals(TypeLibrary.BookType.bookworm)){
            //小说
            NovelChooseFragment fragment = NovelChooseFragment.getInstance();
            transaction.add(R.id.container,fragment).show(fragment);
        }*/
        transaction.commitAllowingStateLoss();
    }
}
