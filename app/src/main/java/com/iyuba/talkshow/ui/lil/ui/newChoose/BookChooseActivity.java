//package com.iyuba.talkshow.ui.lil.ui.newChoose;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.iyuba.lib_common.data.StrLibrary;
//import com.iyuba.lib_common.ui.BaseViewBindingActivity;
//import com.iyuba.talkshow.R;
//import com.iyuba.talkshow.databinding.LayoutContainerBinding;
//
///**
// * 另一种类型的选书操作（课程和单词，暂时不用了，用现有的吧）
// */
//public class BookChooseActivity extends BaseViewBindingActivity<LayoutContainerBinding> {
//    //类型
//    public static final String tag_lesson = "tag_lesson";
//    public static final String tag_word = "tag_word";
//
//    public static void start(Context context,String showType){
//        Intent intent = new Intent();
//        intent.setClass(context, BookChooseActivity.class);
//        intent.putExtra(StrLibrary.type,showType);
//        context.startActivity(intent);
//    }
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//
//        showFragment();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    //显示数据
//    private void showFragment(){
//        String showType = getIntent().getStringExtra(StrLibrary.type);
//
//        BookChooseFragment chooseFragment = BookChooseFragment.getInstance(showType);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.container,chooseFragment).show(chooseFragment).commitNowAllowingStateLoss();
//    }
//
//    //获取新概念判断类型
//    private void getConceptVerifyData(){
//
//    }
//}
