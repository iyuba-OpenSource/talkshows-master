package com.iyuba.talkshow.newce;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iyuba.lib_common.data.Constant;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.data.manager.AbilityControlManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.databinding.FragmentWordstepBinding;
import com.iyuba.talkshow.event.LoginEvent;
import com.iyuba.talkshow.event.LoginOutEvent;
import com.iyuba.talkshow.event.RefreshBookEvent;
import com.iyuba.talkshow.event.RefreshWordEvent;
import com.iyuba.talkshow.injection.PerFragment;
import com.iyuba.talkshow.ui.base.BaseViewBindingFragmet;
import com.iyuba.talkshow.ui.courses.common.OpenFlag;
import com.iyuba.talkshow.ui.courses.wordChoose.WordChooseActivity;
import com.iyuba.talkshow.ui.lil.dialog.LoadingDialog;
import com.iyuba.talkshow.ui.lil.ui.login.NewLoginUtil;
import com.iyuba.talkshow.ui.main.MainActivity;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.ToastUtil;
import com.iyuba.wordtest.db.BookLevelDao;
import com.iyuba.wordtest.db.WordDataBase;
import com.iyuba.wordtest.entity.NewBookLevels;
import com.iyuba.wordtest.entity.TalkShowWords;
import com.iyuba.wordtest.event.WordTestEvent;
import com.iyuba.wordtest.manager.WordConfigManager;
import com.iyuba.wordtest.manager.WordManager;
import com.iyuba.wordtest.ui.WordStepActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by carl shen on 2020/7/31
 * New Primary English, new study experience.
 */
@PerFragment
public class WordstepFragment extends BaseViewBindingFragmet<FragmentWordstepBinding> implements WordMvpView {
    public static final String TAG = "WordstepFragment";
    private static final String BOOKID = "BOOKID";
    private static final String BOOKNAME = "BOOKNAME";
    //替换样式
    WordstepNewAdapter adapter;
//    WordstepAdapter adapter;
    WordDataBase db;
    int step;
    private List<Integer> getUnits;
    private final Handler handler = new Handler();

    private int bookId ;
    private BookLevelDao bookLevelDao;
    private String bookName;
    FragmentWordstepBinding binding;
    @Inject
    ConfigManager configManager;
    @Inject
    WordStepPresenter mPresenter;
    private LoadingDialog mLoadingDialog;
    private final int defaultUi = 1;
    private long starttime;

    private MainActivity activity2;

    public static WordstepFragment build(int bookId , String bookName) {
        WordstepFragment fragment  = new WordstepFragment();
        Bundle bundle  = new Bundle( );
        bundle.putString(BOOKNAME, bookName);
        bundle.putInt(BOOKID, bookId);
        fragment.setArguments(bundle);
        return fragment ;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity2 = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWordstepBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        getIntents();
        initWord();
        initDb(true);
        initData();
        refreshData();
        initClick();
    }

    @Override
    public void init() {
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private void initClick() {
        /*binding.refreshWord.setOnClickListener(v -> syncExamWord());
        binding.selectBook.setOnClickListener(v -> syncWordChoose());*/
        binding.toolbar.leftText.setText("同步闯关数据");
        binding.toolbar.leftLayout.setOnClickListener(v->{
            syncExamWord();
        });
        binding.toolbar.leftLayout.setVisibility(View.INVISIBLE);
        binding.toolbar.rightImage.setImageResource(R.mipmap.ic_course);
        binding.toolbar.rightLayout.setOnClickListener(v->{
            syncWordChoose();
        });

        //根据接口设置侧边栏
        if (AbilityControlManager.getInstance().isLimitLesson()){
            binding.toolbar.leftLayout.setVisibility(View.VISIBLE);
            binding.toolbar.leftText.setVisibility(View.GONE);
            binding.toolbar.leftImage.setImageResource(R.drawable.ic_menu_white);
            binding.toolbar.leftLayout.setOnClickListener(v->{
                if (activity2.isSlideOpen()) {
                    activity2.closeSlide();
                } else {
                    activity2.openSlide();
                }
            });
        }
    }

    private void syncWordChoose() {
        WordChooseActivity.start(getActivity(), OpenFlag.FINISH, OpenFlag.TO_WORD);
//        BookChooseActivity.start(getActivity(),BookChooseActivity.tag_word);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        initWord();
        initDb(false);
        Log.e("WordstepFragment", "LoginEvent init word and db... ");
//        mPresenter.syncExamWord(bookId, false);
        refreshData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginOutEvent event) {
        Log.e("WordstepFragment", "LoginOutEvent init word and db... ");
        bookId = ConfigData.default_word_book_id;
        bookName = ConfigData.default_word_book_title;
        if (configManager != null) {
            configManager.putWordId(ConfigData.default_word_book_id);
            configManager.putWordTitle(ConfigData.default_word_book_title);
        }

        initWord();
        refreshData();
    }
    private void initWord() {
        WordManager.getInstance().init(UserInfoManager.getInstance().getUserName(),
                String.valueOf(UserInfoManager.getInstance().getUserId()),
                App.APP_ID, Constant.EVAL_TYPE, UserInfoManager.getInstance().isVip() ? 1 : 0, App.APP_NAME_EN);
    }

    private void initDb(Boolean flag) {
        db = WordDataBase.getInstance(TalkShowApplication.getInstance());
        bookLevelDao  = db.getBookLevelDao();
        int wordLoad = WordConfigManager.Instance(mContext).loadInt(WordConfigManager.WORD_DB_NEW_LOADED, 0);
        if (wordLoad == 1) {
            int uidLoad = WordConfigManager.Instance(mContext).loadInt(WordConfigManager.WORD_DB_NEW_LOADED + UserInfoManager.getInstance().getUserId(), 0);
            Log.e("WordstepFragment", "initWord uidLoad " + uidLoad);
            if (uidLoad == 0) {
                TalkShowApplication.getSubHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        WordManager.getInstance().migrateData(TalkShowApplication.getContext());
                        Log.e("WordManager", "migrateData bookId " + bookId);
                        EventBus.getDefault().post(new RefreshWordEvent(bookId, 0));
                    }
                });
            }
        }
        List<String> words = db.getTalkShowWordsDao().getWords4Book(bookId);
        if ((words == null) || (words.size() < Constant.PRODUCT_WORDS)) {
            if (NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                if (flag) {
                    showLoadingDialog();
                }
                mPresenter.getWordsById(bookId);
            } else {
                if (flag) {
                    showToastShort("首次加载需要连接数据网络获取单词数据。");
                }
            }
        }
    }

    private void getIntents() {
        bookId = configManager.getWordId();
        bookName = configManager.getWordTitle();
        if (TextUtils.isEmpty(bookName)) {
            bookName = ConfigData.default_word_book_title;
        }

        if (bookId < 1){
            WordChooseActivity.start(getActivity(), OpenFlag.FINISH, OpenFlag.TO_WORD);
//            BookChooseActivity.start(getActivity(),BookChooseActivity.tag_word);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshBookWords(RefreshBookEvent event) {
        bookId = event.bookId;
        bookName = configManager.getWordTitle();
        List<String> words = db.getTalkShowWordsDao().getWords4Book(bookId);
        if ((words == null) || (words.size() < Constant.PRODUCT_WORDS)) {
            if (NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                showLoadingDialog();
                //设置默认数据
//                configManager.putCourseId(bookId);
//                configManager.putCourseTitle(bookName);
                configManager.putWordId(bookId);
                configManager.putWordTitle(bookName);

                mPresenter.getWordsById(bookId);
            } else {
                showToastShort("您选择的课程需要连接数据网络获取单词数据。");
            }
        } else {
            refreshData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SetBookWords(RefreshWordEvent event) {
        bookId = event.bookId;
        bookName = configManager.getWordTitle();
        Log.e("WordstepFragment", "refreshBookWords bookName " + bookName);
        refreshData();
        List<TalkShowWords> talkShowWordsList = db.getTalkShowWordsDao().getBookWords(bookId);
        if ((talkShowWordsList == null) || talkShowWordsList.size() < 1) {
            showToastShort("暂时没有相应的单词资源");
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WordTestEvent event) {
        refreshData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            finish();
        }
        return true;
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog==null){
            mLoadingDialog = new LoadingDialog(getActivity());
            mLoadingDialog.create();
        }
        mLoadingDialog.setMsg("正在加载中～");
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        /*if (!binding.refreshWord.isClickable()) {
            binding.refreshWord.setClickable(true);
        }*/
        if (!binding.toolbar.leftLayout.isClickable()){
            binding.toolbar.leftLayout.setClickable(true);
        }
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void syncExamWord() {
        if (!NetStateUtil.isConnected(TalkShowApplication.getInstance())) {
            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.please_check_network));
            return;
        }
        if (!UserInfoManager.getInstance().isLogin()) {
            startLogin();
            ToastUtil.showToast(mContext, "登录后才能同步闯关数据");
            return;
        }
        /*if (binding.refreshWord.isClickable()) {
            binding.refreshWord.setClickable(false);
        }*/
        if (binding.toolbar.leftLayout.isClickable()){
            binding.toolbar.leftLayout.setClickable(false);
        }else {
            ToastUtil.showToast(mContext, "不要重复点击，正在同步中。");
            return;
        }
        showLoadingDialog();
        mPresenter.syncExamWord(bookId, true);
    }

    public static Intent buildIntent(Context context , int bookId , String bookName){
        Intent intent = new Intent();
        intent.setClass(context, WordStepActivity.class);
        intent.putExtra(BOOKID , bookId);
        intent.putExtra(BOOKNAME , bookName);
        return intent;
    }

    public void initData(){
//        step = loadStep() ;
        binding.allWords.setText(String.format("%s 总单词数: %s",  bookName,db.getTalkShowWordsDao().getBookWords(bookId).size()));
        adapter = new WordstepNewAdapter(bookId, db.getTalkShowWordsDao(),configManager.getWordShowType());
        binding.gridview.setAdapter(adapter);
        adapter.setStepFragment(this);
        adapter.setMobCallback(() -> {
//            doLogin();
            startLogin();
        });
    }

    public void refreshData() {
//        binding.title.setText(bookName);

        bookName = configManager.getWordTitle();
        binding.toolbar.title.setText(bookName);

        TalkShowApplication.getSubHandler().post(new Runnable() {
            @Override
            public void run() {
                step = loadStep();
                getUnits = db.getTalkShowWordsDao().getUnitsByBook(bookId);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.allWords.setText(String.format("%s 总单词数: %s",  bookName,db.getTalkShowWordsDao().getBookWords(bookId).size()));
                        adapter = new WordstepNewAdapter(bookId, step, getUnits, db.getTalkShowWordsDao(),configManager.getWordShowType());
                        binding.gridview.setAdapter(adapter);
                        adapter.setStepFragment(WordstepFragment.this);
                        adapter.setMobCallback(() -> {
//                            doLogin();
                            startLogin();
                        });
                    }
                });
            }
        });
    }

    private int loadStep() {
        if (WordManager.WordDataVersion == 2) {
            NewBookLevels newLevels = db.getNewBookLevelDao().getBookLevel(bookId, String.valueOf(UserInfoManager.getInstance().getUserId()));
            if (newLevels != null) {
                return newLevels.level;
            }
        } else
        if (bookLevelDao != null && bookLevelDao.getBookLevel(bookId) != null) {
            return bookLevelDao.getBookLevel(bookId).level;
        }
        return 0;
    }

    /*@Override
    public void goResultActivity(LoginResult data) {
        if (data == null) {
            Log.e(TAG, "goResultActivity LoginResult is null. ");
            //去除提示信息
//            Toast.makeText(mContext, "请使用用户名密码登录。", Toast.LENGTH_SHORT).show();
            NewLoginUtil.startToLogin(getActivity());
        } else if (!TextUtils.isEmpty(data.getPhone())) {
            String randNum = "" + System.currentTimeMillis();
            String user = "iyuba" + randNum.substring(randNum.length() - 4) + data.getPhone().substring(data.getPhone().length() - 4);
            String pass = data.getPhone().substring(data.getPhone().length() - 6);
            Log.e(TAG, "goResultActivity.user  " + user);
            Log.e(TAG, "goResultActivity.pass  " + pass);
            Intent intent = new Intent(mContext, RegisterSubmitActivity.class);
            intent.putExtra(RegisterSubmitActivity.PhoneNum, data.getPhone());
            intent.putExtra(RegisterSubmitActivity.UserName, user);
            intent.putExtra(RegisterSubmitActivity.PassWord, pass);
            intent.putExtra(RegisterSubmitActivity.RegisterMob, 1);
            startActivity(intent);
        } else {
            Log.e(TAG, "goResultActivity LoginResult is ok. ");
        }
        SecVerify.finishOAuthPage();
        CommonProgressDialog.dismissProgressDialog();
    }*/


    //跳转到登陆界面
    private void startLogin(){
        NewLoginUtil.startToLogin(getActivity());
    }
}
