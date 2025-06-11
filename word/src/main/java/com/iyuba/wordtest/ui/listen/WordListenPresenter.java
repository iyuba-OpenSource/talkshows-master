package com.iyuba.wordtest.ui.listen;

import android.app.Activity;
import android.content.Context;

import com.iyuba.module.mvp.BasePresenter;
import com.iyuba.wordtest.db.WordDataBase;
import com.iyuba.wordtest.entity.TalkShowListen;
import com.iyuba.wordtest.entity.TalkShowWords;
import com.iyuba.wordtest.entity.WordEntity;
import com.iyuba.wordtest.network.HttpManager;
import com.iyuba.wordtest.utils.MediaUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @desction:
 * @date: 2023/2/7 10:19
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public class WordListenPresenter extends BasePresenter<WordListenMvpView> {

    @Override
    public void detachView() {
        super.detachView();
    }

    //获取当前单元听写的单词信息
    public List<TalkShowListen> getUnitListenWordList(Context context,int bookId,int unitId,String uid){
        return WordDataBase.getInstance(context).getTalkShowListenDao().getSpellWordData(bookId, unitId, uid);
    }

    //获取当前单元正确的听写的单词信息
    public List<TalkShowListen> getUnitRightListenWordList(Context context,int bookId,int unitId,String uid){
        return WordDataBase.getInstance(context).getTalkShowListenDao().getRightSpellWordData(bookId, unitId, uid);
    }

    //插入当前单元听写的单词
    public long insertSingleUnitListenWord(Context context,TalkShowListen listen){
        return WordDataBase.getInstance(context).getTalkShowListenDao().insertSpellWord(listen);
    }

    //删除当前单元听写的单词
    public void  deleteUnitListenWord(Context context,int bookId,int unitId,String uid){
        WordDataBase.getInstance(context).getTalkShowListenDao().deleteSpellWord(bookId, unitId, uid);
    }

    //获取当前单元单词信息
    public List<TalkShowWords> getUnitWordList(Context context,int bookId, int unitId){
        return WordDataBase.getInstance(context).getTalkShowWordsDao().getUnitWords(bookId,unitId);
    }

    //更新当前单词数据（没有音频的话）
    public long updateSingleWord(Context context,TalkShowWords words){
        return WordDataBase.getInstance(context).getTalkShowWordsDao().updateSingleWord(words);
    }

    //查询单词信息
    public void searchWord(String wordTemp){
        HttpManager.getWordApi().getWordApi(wordTemp)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<WordEntity>() {
                    @Override
                    public void accept(WordEntity s) {
                        getMvpView().showSearchResult(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable)  {
                        if (getMvpView() != null) {
                            if (MediaUtils.isConnected((Activity) getMvpView())) {
                                getMvpView().showText("获取单词网络播放地址失败");
                            } else {
                                getMvpView().showText("暂时没有这个单词的音频，请打开数据网络播放。");
                            }
                        }
                        if (throwable != null) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }
}
