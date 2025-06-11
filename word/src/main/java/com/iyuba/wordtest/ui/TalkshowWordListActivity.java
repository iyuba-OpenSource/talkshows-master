package com.iyuba.wordtest.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.wordtest.R;
import com.iyuba.wordtest.adapter.SimpleTalkshowAdapter;
import com.iyuba.wordtest.data.WordAppData;
import com.iyuba.wordtest.databinding.ActivityWordListBinding;
import com.iyuba.wordtest.db.WordDataBase;
import com.iyuba.wordtest.entity.BookLevels;
import com.iyuba.wordtest.entity.NewBookLevels;
import com.iyuba.wordtest.entity.TalkShowWords;
import com.iyuba.wordtest.event.ToDetailEvent;
import com.iyuba.wordtest.lil.fix.wordTrain.WordTrainActivity;
import com.iyuba.wordtest.manager.WordManager;
import com.iyuba.wordtest.ui.detail.WordDetailActivity;
import com.iyuba.wordtest.ui.test.WordTestActivity;
import com.iyuba.wordtest.utils.MediaUtils;
import com.iyuba.wordtest.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class TalkshowWordListActivity extends AppCompatActivity implements WordListMvpView {

    //参数
    public static String BOOKID ="BOOKID";
    public static String UNIT ="UNIT";
    public static String STEP ="STEP";
    public static final String IS_UNLOCK = "isUnLock";

    //训练类型
    private static final String train_enToCn = "英汉训练";
    private static final String train_cnToEn = "汉英训练";
    private static final String train_wordSpell = "单词拼写";
    private static final String train_listen = "听力训练";

    //单词的类型
    private String chooseWordType = TypeLibrary.BookType.junior_primary;

    private Context context;
    private WordDataBase db ;
    int bookId ;
    int unit ;
    int step ;
    boolean isUnLock = false;

    SimpleTalkshowAdapter adapter ;
    WordListPresenter presenter;

    //添加加载框
    private Dialog loadingDialog;

    private List<TalkShowWords> list = new ArrayList<>();
    ActivityWordListBinding binding;

    public static void start(Context context, int book_id, int unit, int pos, boolean isUnLock) {
        Intent intent = new Intent( );
        intent.setClass(context,TalkshowWordListActivity.class);
        intent.putExtra(BOOKID, book_id);
        intent.putExtra(UNIT, unit);
        intent.putExtra(STEP, pos);
        intent.putExtra(IS_UNLOCK,isUnLock);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWordListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary) , 0);
        context = this;
        presenter = new WordListPresenter();
        presenter.attachView(this);
        EventBus.getDefault().register(this);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initDialog();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        db = WordDataBase.getInstance(this);
        initWords();
        adapter = new SimpleTalkshowAdapter(list);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        binding.sidebar.setVisibility(View.GONE);
        setClick();
    }

    private void setClick() {
        binding.syncWord.setOnClickListener(v -> onViewClicked(v));
        binding.study.setOnClickListener(v -> onViewClicked(v));
        binding.test.setOnClickListener(v -> onViewClicked(v));
        binding.train.setOnClickListener(v->onViewClicked(v));
    }

    private void initWords() {
        bookId = getIntent().getIntExtra(BOOKID, 217);
        unit = getIntent().getIntExtra(UNIT,1);
        step = getIntent().getIntExtra(STEP,0);
        isUnLock = getIntent().getBooleanExtra(IS_UNLOCK,true);

        ColorStateList stateList = ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary));
        if (!isUnLock){
            stateList = ColorStateList.valueOf(getResources().getColor(android.R.color.darker_gray));
        }
        binding.test.setBackgroundTintList(stateList);

        //根据bookId显示名称
        String title = "Unit " + unit + " 单词";
        if ((450 <= bookId) && (bookId <= 457)) {
            title = "Lesson "+unit+" 单词";
        }
        binding.textTopTitle.setText(title);

        list.clear();
        list.addAll(db.getTalkShowWordsDao().getUnitWords(bookId, unit));
    }

    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.study) {
            WordDetailActivity.start(context, list, 0, bookId, unit);
        } else if (id == R.id.sync_word) {
            if (!MediaUtils.isConnected(getApplicationContext())) {
                ToastUtil.showToast(context, "请检查数据网络是否可用！");
                return;
            }

            //显示弹框
            if (loadingDialog!=null&&!loadingDialog.isShowing()){
                loadingDialog.show();
            }

            if (WordManager.WordDataVersion == 2) {
                NewBookLevels newLevels = db.getNewBookLevelDao().getBookLevel(bookId, WordManager.getInstance().userid);
                if (newLevels == null) {
                    presenter.refreshWords(getApplicationContext(), bookId, 0);
                } else {
                    presenter.refreshWords(getApplicationContext(), bookId, newLevels.version);
                }
            } else {
                BookLevels bookLevel = db.getBookLevelDao().getBookLevel(bookId);
                presenter.refreshWords(getApplicationContext(), bookId, bookLevel.version);
            }
        } else if (id == R.id.test) {
            //闯关功能
            if (!isUnLock){
                ToastUtil.showToast(this,"通关前面的单元后解锁此单元的闯关内容");
                return;
            }

            WordTestActivity.start(context, bookId, unit, step);
            finish();
        }else if (id == R.id.train){
            //将听写功能替换为训练的四个功能
//            WordListenActivity.start(context,bookId,unit);
            showTrainDialog();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initWords();
//        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ToDetailEvent event) {
        Log.e("TalkshowWordActivity", "ToDetailEvent bookId " + event.bookid);
        refreshWords();
    }

    @Override
    public void showText(String text) {
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }

        if (!TextUtils.isEmpty(text)){
            ToastUtil.showToast(context, text);
        }
    }

    @Override
    public void refreshWords() {
        list.clear();
        list.addAll(db.getTalkShowWordsDao().getUnitWords(bookId, unit));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        EventBus.getDefault().unregister(this);
    }

    //初始化加载框
    private void initDialog(){
        loadingDialog = new Dialog(this, R.style.DialogTheme);
        View loadingView = LayoutInflater.from(context).inflate(R.layout.dialog_loading,null);
        TextView tvText = loadingView.findViewById(R.id.loading_tv);
        tvText.setText("正在加载~");
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(loadingView);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    //显示训练的弹窗
    private void showTrainDialog(){
        String lessonType = WordAppData.getInstance(this).getWordType();
        if (lessonType.equals("primary")){
            chooseWordType = TypeLibrary.BookType.junior_primary;
        }else if (lessonType.equals("junior")){
            chooseWordType = TypeLibrary.BookType.junior_middle;
        }

        String[] trainArray = new String[]{train_enToCn,train_cnToEn,train_wordSpell,train_listen};

        new AlertDialog.Builder(this)
                .setTitle("选择训练类型")
                .setItems(trainArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        String chooseType = trainArray[which];
                        switch (chooseType){
                            case train_enToCn:
                                //英汉训练
                                WordTrainActivity.start(TalkshowWordListActivity.this,TypeLibrary.WordTrainType.Train_enToCn,chooseWordType,String.valueOf(bookId),unit,0);
                                break;
                            case train_cnToEn:
                                //汉英训练
                                WordTrainActivity.start(TalkshowWordListActivity.this,TypeLibrary.WordTrainType.Train_cnToEn,chooseWordType,String.valueOf(bookId),unit,0);
                                break;
                            case train_wordSpell:
                                //单词拼写
                                WordTrainActivity.start(TalkshowWordListActivity.this,TypeLibrary.WordTrainType.Word_spell,chooseWordType,String.valueOf(bookId),unit,0);
                                break;
                            case train_listen:
                                //听力训练
                                WordTrainActivity.start(TalkshowWordListActivity.this,TypeLibrary.WordTrainType.Train_listen,chooseWordType,String.valueOf(bookId),unit,0);
                                break;
                        }
                    }
                }).create().show();
    }
}
