package com.iyuba.talkshow.ui.deletlesson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.databinding.ActivityLessonDeleteBinding;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.inject.Inject;

public class LessonDeleteActivity extends BaseViewBindingActivity<ActivityLessonDeleteBinding> implements LessonDeleteMVPView{

    @Inject
    LessonDeletePresenter deletePresenter ;
    LessonDeleteAdapter adapter ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar.listToolbar);
        activityComponent().inject(this);
        deletePresenter .attachView(this);
        adapter = new LessonDeleteAdapter();
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);
        setAnimation();
        deletePresenter.getDownloadedClass();
    }

    public static void start(Context context ){
        Intent intent = new Intent();
        intent.setClass(context,LessonDeleteActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lesson_delete, menu);
        return true;
    }

    private void setAnimation(){
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(8000);
        binding.recycler.setItemAnimator(defaultItemAnimator);
    }

    @Override
    public void showDeleteMessage(String message) {
        ToastUtils.showShort(message);
    }

    @Override
    public void showBookList(List<SeriesData> seriesData) {
        adapter.setBookList(seriesData);
    }

    private void delete(List<String> checkList){

        for (String bookId : checkList){
            deletePresenter.deleteLessons(Integer.parseInt(bookId));
        }
        deletePresenter.getDownloadedClass();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }else if (item.getItemId() == R.id.delete){
            if (adapter.getCheckList().size()>0){
                delete(adapter.getCheckList());
            }
        }
        return true;
    }


}
