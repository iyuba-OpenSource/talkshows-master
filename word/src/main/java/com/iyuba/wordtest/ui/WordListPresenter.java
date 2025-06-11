package com.iyuba.wordtest.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.iyuba.module.mvp.BasePresenter;
import com.iyuba.wordtest.db.WordDataBase;
import com.iyuba.wordtest.entity.TalkShowTests;
import com.iyuba.wordtest.entity.TalkShowWords;
import com.iyuba.wordtest.entity.UpdateWordResponse;
import com.iyuba.wordtest.event.ToDetailEvent;
import com.iyuba.wordtest.event.WordTestEvent;
import com.iyuba.wordtest.manager.WordManager;
import com.iyuba.wordtest.network.HttpManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WordListPresenter extends BasePresenter<WordListMvpView> {

    public void refreshWords(Context context, int bookId, int version) {
        HttpManager.getSearchApi().updateWords(bookId, version)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<UpdateWordResponse>() {
                    @Override
                    public void accept(UpdateWordResponse response) {
                        if (getMvpView() != null) {
                            getMvpView().showText("单词同步刷新成功");
//                            getMvpView().refreshWords();
                        }
                        if (response == null || response.getData() == null) {
                            Log.e("WordListPresenter", "refreshWords response is null. ");
                            return;
                        }
                        Log.e("WordListPresenter", "refreshWords response getData().size " + response.getData().size());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (WordManager.WordDataVersion == 2) {
                                    List<TalkShowTests> wordsList = new ArrayList<>();
                                    for (TalkShowWords words: response.getData()) {
                                        if ((words != null) && !TextUtils.isEmpty(words.answer)) {
                                            wordsList.add(WordManager.getInstance().Words2Tests(words));
                                        }
                                    }
                                    WordDataBase.getInstance(context).getTalkShowTestsDao().insertWord(wordsList);
                                    Log.e("WordListPresenter", "refreshWords TalkShowTests size " + wordsList.size());
                                }
                                WordDataBase.getInstance(context).getTalkShowWordsDao().insertWord(response.getData());
                                EventBus.getDefault().post(new ToDetailEvent(bookId));
                                EventBus.getDefault().post(new WordTestEvent(bookId));
                            }
                        }).start();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {
                        throwable.printStackTrace();
                        //这里是刷新失败
                        getMvpView().showText(null);
                    }
                });
    }

}
