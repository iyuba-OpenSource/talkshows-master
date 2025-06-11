package com.iyuba.talkshow.ui.words;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;

import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.play.Player;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.manager.ConfigManager;
import com.iyuba.talkshow.databinding.ActivityWordNoteBinding;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.ToastUtil;
import com.iyuba.widget.recycler.EndlessListRecyclerView;
import com.iyuba.wordtest.db.WordOp;
import com.iyuba.wordtest.entity.WordEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * 生词本
 */
public class WordNoteActivity extends BaseViewBindingActivity<ActivityWordNoteBinding> implements WordNoteMvpView {
    private static final int PAGE_COUNT = 30;

    public static Intent buildIntent(Context context) {
        return new Intent(context, WordNoteActivity.class);
    }

    private Player mPlayer;

    private WordsAdapter mAdapter;

    @Inject
    WordNotePresenter mPresenter;

    @Inject
    DataManager dataManager ;

    @Inject
    ConfigManager configManager ;

    private boolean mLoadingFlag = false;
    private int mCurrentPage = 1;
    List<Word> selectedWords;
    private WordOp wordOp;
    Context context ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //将tactivity
        activityComponent().inject(this);
        mPresenter.attachView(this);

        context =this ;
        setSupportActionBar(binding.toolbar);
        mPlayer = new Player();
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        binding.toolbar.setTitle("我的生词");

        mAdapter = new WordsAdapter();
        binding.recycler.setAdapter(mAdapter);
        binding.recycler.setOnEndlessListener(mEndlessListener);

        wordOp = new WordOp(mContext);
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.clearSelection();
        mAdapter.setDeleteMode(false);

        mPresenter.detachView();
        mPlayer.stopAndRelease();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_word_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.wordmenu_sync:
                if (!NetStateUtil.isConnected(TalkShowApplication.getContext())) {
                    showMessage("请开启网络连接后同步生词本!");
                    break;
                }
                if (!mLoadingFlag) {
                    mPresenter.getLatestData(UserInfoManager.getInstance().getUserId(), PAGE_COUNT);
                }
                break;
            case R.id.wordmenu_edit:
                //先判断是否存在数据
                List<WordEntity> myWords = wordOp.findWordByUser(UserInfoManager.getInstance().getUserId());
                if (myWords==null||myWords.size()==0){
                    ToastUtil.showToast(WordNoteActivity.this,"暂无收藏的单词数据");
                    return true;
                }

                if (item.isChecked()){
                    //先判断当前是否存在选中的数据
                    selectedWords = mAdapter.getSelectedWords();
                    if ((selectedWords != null) && selectedWords.size() > 0) {
                        mPresenter.deleteWords(UserInfoManager.getInstance().getUserId(), buildStr(selectedWords),item);
                    }else {
                        item.setChecked(false);
                        item.setIcon(R.drawable.button_edit_white);
                        mAdapter.setDeleteMode(false);
                    }
                }else {
                    item.setChecked(true);
                    item.setIcon(R.drawable.button_delete_white);
                    mAdapter.setDeleteMode(true);
                }

                //旧版本删除单词功能
//                startActionMode(mEditActionModeCallback);
//                mAdapter.setDeleteMode(true);
                break;
//                mQueryDialog.show();
//                int category = mCategoryDataHelper.getDefaultCategoryCode();
//                Intent intent = SearchActivity.buildIntent(this, category);
//                startActivity(intent);
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        List<WordEntity> myWords = wordOp.findWordByUser(UserInfoManager.getInstance().getUserId());
        if ((myWords != null) && (myWords.size()>0)) {
            Log.e("WordNoteActivity", "initData myWords.size " + myWords.size());
            List<Word> wordList = new ArrayList<>();
//            for (WordEntity entity: myWords) {
//                if ((entity != null) && !TextUtils.isEmpty(entity.key)) {
//                    if (entity.voa > 0) {
//                        if (!(entity.key.contains(entity.voa + ""))) {
//                            wordList.add(Entity2Word(entity));
//                        }
//                    } else {
//                        wordList.add(Entity2Word(entity));
//                    }
//                }
//            }

            //重新处理这个数据，之前操作有点问题，这里如果检测到之前的数据有问题，则直接删除
            for (int i = 0; i < myWords.size(); i++) {
                WordEntity entity = myWords.get(i);
                //需要删除的数据
                if (entity.key.contains(String.valueOf(entity.voa))){
                    wordOp.deleteWord(entity.key,UserInfoManager.getInstance().getUserId());
                }else {
                    wordList.add(Entity2Word(entity));
                }
            }

            mCurrentPage = 1;
            mAdapter.setList(wordList, true);
            binding.textTinyHint.setText(wordList.size() + "个");
        } else {
            ToastUtil.show(this,"暂无本地数据，请开启网络连接后同步生词本!");
            mCurrentPage = 1;
            mAdapter.setList(new ArrayList<>(), true);
            binding.textTinyHint.setText("");
        }
    }

    private Word Entity2Word(WordEntity entity) {
        if (entity == null) {
            return null;
        }
        Word word = new Word();
        word.key = entity.key;
        word.audioUrl = entity.audio;
        word.def = entity.def;
        word.pron = entity.pron;
        word.lang = entity.lang;
        word.userid = String.valueOf(UserInfoManager.getInstance().getUserId());
        return word;
    }

    private WordEntity Word2Entity(Word word) {
        if (word == null) {
            return null;
        }
        WordEntity entity = new WordEntity();
        entity.key = word.key;
        entity.audio = word.audioUrl;
        entity.def = word.def;
        entity.pron = word.pron;
        entity.lang = word.lang;
        entity.voa = 0;
        entity.book = 0;
        entity.unit = 0;
        return entity;
    }
//    @Override
//    public void setLoading(boolean isLoading) {
//        mLoadingFlag = isLoading;
//        if (isLoading) {
//            .setMessage("正在加载数据");
//            dialog.show();
//        } else {
//            dialog.dismiss();
//        }
//    }

    @Override
    public void setRecyclerEndless(boolean isEndless) {
        binding.recycler.setEndless(isEndless);
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.show(this, message);
    }

    @Override
    public void showMessage(int resId) {
        ToastUtil.show(this, getResources().getString(resId));
    }

    @Override
    public void onLatestDataLoaded(List<Word> words, int total, boolean instantRefresh) {
//        mCurrentPage = 1;
//        mAdapter.setList(words, instantRefresh);
//        binding.textTinyHint.setText(total + "个");
        if ((words != null) && (words.size()>0)) {
            Log.e("WordNoteActivity", "onLatestDataLoaded need sync word " + words.size());
            showMessage("同步数据成功!");
            TalkShowApplication.getSubHandler().post(new Runnable() {
                @Override
                public void run() {
                    for (Word word: words) {
                        if ((word != null) && !TextUtils.isEmpty(word.key) && !wordOp.isExsitsWord(word.key, UserInfoManager.getInstance().getUserId())) {
                            long result = wordOp.insertWord(Word2Entity(word), UserInfoManager.getInstance().getUserId());
                            Log.e("WordNoteActivity", "onLatestDataLoaded insertWord result " + result);                       }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onMoreDataLoaded(List<Word> words, int page) {
        mCurrentPage = page;
        mAdapter.addList(words);
    }

    //旧版本删除单词功能
    /*@Override
    public void onDeleteAccomplish(int userId, ActionMode mode) {
//        mPresenter.getLatestInActionMode(userId, PAGE_COUNT, binding.recycler.getEndless(), mode);
        if ((selectedWords != null) && selectedWords.size() > 0) {
            showMessage("删除数据成功!");
            TalkShowApplication.getSubHandler().post(new Runnable() {
                @Override
                public void run() {
                    for (Word word: selectedWords) {
                        if ((word != null) && !TextUtils.isEmpty(word.key) && wordOp.isExsitsWord(word.key, manager.getUid())) {
                            wordOp.deleteWord(word.key, manager.getUid());
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mode.finish();
                            setRecyclerEndless(true);
                            initData();
                        }
                    });
                }
            });
        }
    }*/

    @Override
    public void onDeleteAccomplish(int userId,MenuItem menuItem) {
        if ((selectedWords != null) && selectedWords.size() > 0) {
            showMessage("删除数据成功!");

            TalkShowApplication.getSubHandler().post(new Runnable() {
                @Override
                public void run() {
                    for (Word word: selectedWords) {
                        if ((word != null) && !TextUtils.isEmpty(word.key) && wordOp.isExsitsWord(word.key, UserInfoManager.getInstance().getUserId())) {
                            wordOp.deleteWord(word.key, UserInfoManager.getInstance().getUserId());
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //重新设置下
                            menuItem.setChecked(false);
                            menuItem.setIcon(R.drawable.button_edit_white);

                            mAdapter.clearSelection();
                            mAdapter.setDeleteMode(false);

                            setRecyclerEndless(true);
                            initData();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onDeleteFail() {

    }

    private EndlessListRecyclerView.OnEndlessListener mEndlessListener = new EndlessListRecyclerView.OnEndlessListener() {
        @Override
        public void onEndless() {
            if (!mLoadingFlag) {
                mPresenter.loadMore(UserInfoManager.getInstance().getUserId(), mCurrentPage + 1, PAGE_COUNT);
            }
        }
    };

    //旧版本删除单词功能
    /*private ActionMode.Callback mEditActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.add("删除")
                    .setIcon(android.R.drawable.ic_menu_delete)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            selectedWords = mAdapter.getSelectedWords();
            if ((selectedWords != null) && selectedWords.size() > 0) {
                final int uid = manager.getUid();
                mPresenter.deleteWords(uid, buildStr(selectedWords), mode);
            } else {
                mode.finish();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelection();
            mAdapter.setDeleteMode(false);
        }

        private List<String> buildStr(List<Word> words) {
            List<String> strs = new ArrayList<>();
            for (Word word : words) {
                strs.add(word.key);
            }
            return strs;
        }

    };*/

    private List<String> buildStr(List<Word> words) {
        List<String> strs = new ArrayList<>();
        for (Word word : words) {
            strs.add(word.key);
        }
        return strs;
    }

    @Override
    public void showToastShort(int resId) {

    }

    @Override
    public void showToastShort(String message) {

    }

    @Override
    public void showToastLong(int resId) {

    }

    @Override
    public void showToastLong(String message) {

    }
}
