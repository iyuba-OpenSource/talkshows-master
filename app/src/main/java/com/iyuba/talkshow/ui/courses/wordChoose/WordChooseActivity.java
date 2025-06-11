package com.iyuba.talkshow.ui.courses.wordChoose;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.listener.OnSimpleClickListener;
import com.iyuba.lib_common.util.LibStackUtil;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.manager.AbilityControlManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.data.model.AppCheckResponse;
import com.iyuba.talkshow.databinding.ActivityCourseChooseBinding;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
import com.iyuba.talkshow.ui.courses.common.OpenFlag;
import com.iyuba.talkshow.ui.lil.dialog.bookType.BookTypeDialog;
import com.iyuba.talkshow.ui.widget.LoadingDialog;
import com.iyuba.talkshow.util.RxUtil;
import com.tencent.vasdolly.helper.ChannelReaderUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class WordChooseActivity extends BaseViewBindingActivity<ActivityCourseChooseBinding> {

    public  static final String FLAG = "FLAG";
    public  static final String TYPE = "TYPE";

    @Inject
    DataManager mDataManager;
    @Inject
    ConfigManager configManager ;
    private WordChooseFragment fragment;
    private int type;
    private int flag;

    //书籍类型弹窗
    private BookTypeDialog bookTypeDialog;
    //选中的书籍类型
    private String selectBookTypeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        flag = getIntent().getIntExtra(FLAG,300);
        type = getIntent().getIntExtra(TYPE, OpenFlag.TO_DETAIL);
        initTopBar();
        initFragment();

        openDialog();
        //这里增加人教版处理的接口
        String channel = ChannelReaderUtil.getChannel(this);
        if (ConfigData.renVerifyCheck
                && AbilityControlManager.getInstance().isLimitPep()){
            verifyCheck(channel);
        }else {
            closeDialog();

            AbilityControlManager.getInstance().setLimitPep(false);
            initFragment();
        }
    }

    private void initTopBar() {
        /*setSupportActionBar(binding.toolbar);

        String lessonType = configManager.getWordShowType();
        if (lessonType.equals("primary")){
            getSupportActionBar().setTitle("小学课程");
        }else if (lessonType.equals("junior")){
            getSupportActionBar().setTitle("初中课程");
        }
        binding.lessonType.setOnClickListener(v->{
            String[] lessonArray = new String[]{"小学课程","初中课程"};

            new AlertDialog.Builder(this)
                    .setTitle("选择级别")
                    .setItems(lessonArray, (dialog, which) -> {

                        getSupportActionBar().setTitle(lessonArray[which]);

                        switch (which){
                            case 0:
                                //小学课程
                                configManager.setWordShowType("primary");
                                break;
                            case 1:
                                //初中课程
                                configManager.setWordShowType("junior");
                                break;
                        }

                        fragment.refreshLessonData(configManager.getWordShowType());
                        dialog.dismiss();
                    }).show();
        });*/

        //设置新的操作(设置样式2)
        binding.toolbarOne.setVisibility(View.GONE);
        String lessonType = configManager.getWordShowType();
        if (lessonType.equals("primary")){
            selectBookTypeData = TypeLibrary.BookType.junior_primary;
            binding.toolbarTwo.title.setText("小学课程");
        }else if (lessonType.equals("junior")){
            selectBookTypeData = TypeLibrary.BookType.junior_middle;
            binding.toolbarTwo.title.setText("初中课程");
        }
        binding.toolbarTwo.leftImage.setImageResource(R.drawable.back);
        binding.toolbarTwo.leftLayout.setOnClickListener(v->{
            LibStackUtil.getInstance().finishCur();
        });
        binding.toolbarTwo.rightImage.setImageResource(R.drawable.ic_menu_white_new);
        binding.toolbarTwo.rightLayout.setOnClickListener(v->{
            showBookTypeDialog();
        });
    }

    public static void  start(Context context , int flag, int type){
        Intent intent = new Intent( );
        intent.setClass(context, WordChooseActivity.class);
        intent.putExtra(FLAG,flag);
        intent.putExtra(TYPE,type);
        context.startActivity(intent);
    }

    private void initFragment() {
        if (type == OpenFlag.TO_WORD) {
            fragment = WordChooseFragment.build("" + configManager.getWordCategory(), flag, type);
        } else {
            fragment = WordChooseFragment.build("" + configManager.getCourseCategory(), flag, type);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }*/

    //加载弹窗
    private LoadingDialog loadingDialog;

    //审核检查是否需要限制
    private Subscription mVerifyCheckSub;
    public void verifyCheck(String channel){
        int verifyRenId = ConfigData.getRenLimitChannelId(channel);

        RxUtil.unsubscribe(mVerifyCheckSub);
        mVerifyCheckSub = mDataManager.getAppCheckStatus(verifyRenId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppCheckResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        closeDialog();
                        AbilityControlManager.getInstance().setLimitPep(false);

                        initFragment();
                    }

                    @Override
                    public void onNext(AppCheckResponse response) {
                        closeDialog();
                        if (response.getResult().equals("0")){
                            AbilityControlManager.getInstance().setLimitPep(false);
                        }else {
                            AbilityControlManager.getInstance().setLimitPep(true);
                        }

                        initFragment();
                    }
                });
    }

    //开启加载弹窗
    private void openDialog(){
        if (loadingDialog==null){
            loadingDialog = new LoadingDialog(this);
            loadingDialog.setMessage("正在查询书本信息");
        }
        loadingDialog.show();
    }

    //关闭加载弹窗
    private void closeDialog(){
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RxUtil.unsubscribe(mVerifyCheckSub);
    }

    //书籍类型弹窗
    private void showBookTypeDialog(){
        if (bookTypeDialog==null){
            bookTypeDialog = new BookTypeDialog(this);
            bookTypeDialog.create();

            List<Pair<String,String>> list = new ArrayList<>();
            list.add(new Pair<>(TypeLibrary.BookType.junior_primary,"小学"));
            list.add(new Pair<>(TypeLibrary.BookType.junior_middle,"初中"));

            bookTypeDialog.setTitle("选择书籍类型");
            bookTypeDialog.setData(selectBookTypeData,list);
            bookTypeDialog.setListener(new OnSimpleClickListener<Pair<String, String>>() {
                @Override
                public void onClick(Pair<String, String> pair) {
                    selectBookTypeData = pair.first;

                    switch (pair.first){
                        case TypeLibrary.BookType.junior_primary:
                            //小学课程
                            binding.toolbarTwo.title.setText("小学课程");
                            configManager.setWordShowType("primary");
                            break;
                        case TypeLibrary.BookType.junior_middle:
                            //初中课程
                            binding.toolbarTwo.title.setText("初中课程");
                            configManager.setWordShowType("junior");
                            break;
                    }

                    fragment.refreshLessonData(configManager.getWordShowType());
                }
            });
        }else {
            bookTypeDialog.setType(selectBookTypeData);
        }
        bookTypeDialog.show();
    }
}
