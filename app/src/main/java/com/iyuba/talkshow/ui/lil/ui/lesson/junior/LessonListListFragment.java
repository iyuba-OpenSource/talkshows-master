package com.iyuba.talkshow.ui.lil.ui.lesson.junior;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.iyuba.lib_common.bean.BookChapterBean;
import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.event.RefreshDataEvent;
import com.iyuba.lib_common.ui.BaseViewBindingFragment;
import com.iyuba.lib_common.util.LibStackUtil;
import com.iyuba.lib_common.util.LibToastUtil;
import com.iyuba.sdk.other.NetworkUtil;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.databinding.LayoutListRefreshTitlePlayBinding;
import com.iyuba.talkshow.ui.lil.manager.StudySettingManager;
import com.iyuba.talkshow.ui.lil.service.JuniorBgPlayEvent;
import com.iyuba.talkshow.ui.lil.service.JuniorBgPlayManager;
import com.iyuba.talkshow.ui.lil.service.JuniorBgPlaySession;
import com.iyuba.talkshow.ui.lil.ui.choose.LessonBookChooseActivity;
import com.iyuba.talkshow.ui.lil.ui.choose.junior.manager.JuniorBookChooseManager;
import com.iyuba.talkshow.ui.lil.ui.lesson.study.StudyActivity;
import com.iyuba.talkshow.util.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: 中小学英语课程界面
 * @date: 2024/1/2 14:07
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class LessonListListFragment extends BaseViewBindingFragment<LayoutListRefreshTitlePlayBinding> implements LessonListView {

    private LessonListPresenter presenter;
    private LessonListAdapter lessonListAdapter;

    public static LessonListListFragment getInstance(){
        LessonListListFragment fragment = new LessonListListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        presenter = new LessonListPresenter();
        presenter.attachView(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initToolbar();
        initList();
        initClick();

        //获取本地数据
        refreshData(JuniorBookChooseManager.getInstance().getBookType(),JuniorBookChooseManager.getInstance().getBookId());
    }

    @Override
    public void onResume() {
        super.onResume();
        isToOtherPage = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isToOtherPage = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

        presenter.detachView();
    }

    /*******************************初始化**************************/
    private void initToolbar(){
        binding.toolbar.rightImage.setImageResource(R.mipmap.ic_course);
        binding.toolbar.rightImage.setOnClickListener(v->{
            LessonBookChooseActivity.start(getActivity(), TypeLibrary.BookType.junior_primary,false);
        });
    }

    private void initList(){
        binding.refreshLayout.setEnableRefresh(true);
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (!NetworkUtil.isConnected(getActivity())){
                    binding.refreshLayout.finishRefresh(false);
                    LibToastUtil.showLongToast(getActivity(),"请链接网络后重试～");
                    return;
                }

                presenter.loadNetChapterData(JuniorBookChooseManager.getInstance().getBookType(),"", JuniorBookChooseManager.getInstance().getBookId());
            }
        });

        lessonListAdapter = new LessonListAdapter(getActivity(),new ArrayList<>());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(lessonListAdapter);
        lessonListAdapter.setOnItemClickListener(new LessonListAdapter.OnItemClickListener() {
            @Override
            public void onClick(Pair<Integer, BookChapterBean> pair) {
                StudyActivity.start(getActivity(),pair.second.getTypes(),pair.second.getBookId(),pair.second.getVoaId(),pair.first);
            }
        });

        //隐藏底部控制栏
        binding.playLayout.setVisibility(View.GONE);
    }

    private void initClick(){
        binding.playLayout.setOnClickListener(v->{
            //跳转
            BookChapterBean chapterBean = JuniorBgPlaySession.getInstance().getCurData();
            StudyActivity.start(getActivity(),chapterBean.getTypes(),chapterBean.getBookId(),chapterBean.getVoaId(),JuniorBgPlaySession.getInstance().getPlayPosition());
        });
        binding.playImg.setOnClickListener(v->{
            ExoPlayer exoPlayer = JuniorBgPlayManager.getInstance().getPlayService().getPlayer();
            if (exoPlayer!=null){
                if (exoPlayer.isPlaying()){
                    EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_control_pause));
                }else {
                    EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_control_play));
                }
            }else {
                ToastUtil.showToast(getActivity(),"未初始化音频");
            }
        });
    }

    /*********************************刷新数据***************************/
    //刷新数据
    private void refreshData(String types,String bookId){
        binding.toolbar.title.setText(JuniorBookChooseManager.getInstance().getBookName());
        presenter.getLessonData(types, "",bookId);
    }

    /*****************************回调数据*************************/
    @Override
    public void showData(List<BookChapterBean> list) {
        if (list!=null){
            binding.refreshLayout.finishRefresh(true);
            binding.recyclerView.scrollToPosition(0);
            lessonListAdapter.refreshData(list);

            //保存在后台数据中
            JuniorBgPlaySession.getInstance().setVoaList(list);
            if (list.size()==0){
                ToastUtil.showToast(getActivity(),"暂无该章节的数据~");
            }
        }else {
            binding.refreshLayout.finishRefresh(false);
            ToastUtil.showToast(getActivity(),"暂无当前课本内容");
        }
    }

    @Override
    public void loadNetData() {
        binding.refreshLayout.autoRefresh();
    }

    //刷新操作回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshDataEvent event){

        //书籍选择
        if (event.getType().equals(TypeLibrary.RefreshDataType.junior)){
            refreshData(JuniorBookChooseManager.getInstance().getBookType(), JuniorBookChooseManager.getInstance().getBookId());
        }

        //退出app
        if (event.getType().equals(TypeLibrary.RefreshDataType.existApp)){
            LibStackUtil.getInstance().existApp();
        }
    }

    /*******************************音频播放相关*************************/
    //是否去了其他界面
    private boolean isToOtherPage = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayEvent(JuniorBgPlayEvent event){
        if (!isToOtherPage){
            //加载完成
            if (event.getShowType().equals(JuniorBgPlayEvent.event_audio_prepareFinish)){
                EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_control_play));
            }

            //播放完成
            if (event.getShowType().equals(JuniorBgPlayEvent.event_audio_completeFinish)){
                EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_audio_switch));
            }
        }

        //播放
        if (event.getShowType().equals(JuniorBgPlayEvent.event_control_play)){
            ExoPlayer exoPlayer = JuniorBgPlayManager.getInstance().getPlayService().getPlayer();
            if (exoPlayer!=null&&!exoPlayer.isPlaying()){
                exoPlayer.play();
            }
            BookChapterBean chapterBean = JuniorBgPlaySession.getInstance().getCurData();
            binding.playLayout.setVisibility(View.VISIBLE);
            binding.playImg.setImageResource(R.drawable.pause);
            if (chapterBean!=null){
                binding.playTitle.setText(chapterBean.getTitleEn());
                binding.playTitleCn.setText(chapterBean.getTitleCn());
                //通知栏处理
                JuniorBgPlayManager.getInstance().getPlayService().showNotification(false,true,chapterBean.getTitleEn());
            }
        }

        //暂停
        if (event.getShowType().equals(JuniorBgPlayEvent.event_control_pause)){
            //暂停播放
            ExoPlayer exoPlayer = JuniorBgPlayManager.getInstance().getPlayService().getPlayer();
            if (exoPlayer!=null&&exoPlayer.isPlaying()){
                exoPlayer.pause();
            }
            //显示图标
            BookChapterBean chapterBean = JuniorBgPlaySession.getInstance().getCurData();
            if (binding.playLayout.getVisibility() == View.VISIBLE){
                binding.playLayout.setVisibility(View.VISIBLE);
            }
            binding.playImg.setImageResource(R.drawable.play);
            if (chapterBean!=null){
                binding.playTitle.setText(chapterBean.getTitleEn());
                binding.playTitleCn.setText(chapterBean.getTitleCn());
                //通知栏处理
                JuniorBgPlayManager.getInstance().getPlayService().showNotification(false,false,chapterBean.getTitleEn());
            }
        }

        //隐藏控制栏
        if (event.getShowType().equals(JuniorBgPlayEvent.event_control_hide)){
            //停止播放
            ExoPlayer exoPlayer = JuniorBgPlayManager.getInstance().getPlayService().getPlayer();
            exoPlayer.pause();
            //通知栏处理-将通知栏重置
            BookChapterBean chapterBean = JuniorBgPlaySession.getInstance().getCurData();
            JuniorBgPlayManager.getInstance().getPlayService().showNotification(true,false,chapterBean.getTitleEn());
            //隐藏控制栏
            binding.playLayout.setVisibility(View.GONE);
        }

        //切换播放
        if (event.getShowType().equals(JuniorBgPlayEvent.event_audio_switch)){
            //根据当前类型选择下一个音频的位置
            ExoPlayer exoPlayer = JuniorBgPlayManager.getInstance().getPlayService().getPlayer();

            String playMode = StudySettingManager.getInstance().getContentPlayMode();
            if (playMode.equals(TypeLibrary.PlayModeType.SINGLE_SYNC)){
                //单曲循环
                exoPlayer.seekTo(0);
                //这里不需要重新加载
                JuniorBgPlayManager.getInstance().getPlayService().setPrepare(false);

                EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_audio_play));
                EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_control_play));
            }else if (playMode.equals(TypeLibrary.PlayModeType.ORDER_PLAY)){
                //顺序播放
                EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_audio_pause));

                if (JuniorBgPlaySession.getInstance().getPlayPosition() <= JuniorBgPlaySession.getInstance().getVoaList().size()-1){
                    //刷新数据显示
                    int nextPosition = JuniorBgPlaySession.getInstance().getPlayPosition()+1;
                    Log.d("下一个位置", "位置--"+nextPosition+"--顺序");

                    if (isToOtherPage){
                        EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_data_refresh,nextPosition));
                    }else {
                        //获取音频并播放
                        JuniorBgPlaySession.getInstance().setPlayPosition(nextPosition);
                        initPlayerAndPlayAudio(JuniorBgPlaySession.getInstance().getCurData());
                    }
                }else {
                    LibToastUtil.showToast(getActivity(),"课程播放完成");
                }
            }else if (playMode.equals(TypeLibrary.PlayModeType.RANDOM_PLAY)){
                //随机播放
                EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_audio_pause));

                //获取随机数
                int randomIndex = (int) (JuniorBgPlaySession.getInstance().getVoaList().size()*Math.random());
                if (randomIndex == JuniorBgPlaySession.getInstance().getPlayPosition()){
                    if (randomIndex == JuniorBgPlaySession.getInstance().getVoaList().size()-1){
                        randomIndex--;
                    }
                    randomIndex++;
                }

                Log.d("下一个位置", "位置--"+randomIndex+"--随机");

                if (isToOtherPage){
                    EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_data_refresh,randomIndex));
                }else {
                    //获取音频并播放
                    JuniorBgPlaySession.getInstance().setPlayPosition(randomIndex);
                    initPlayerAndPlayAudio(JuniorBgPlaySession.getInstance().getCurData());
                }
            }
        }
    }

    //播放音频
    private void initPlayerAndPlayAudio(BookChapterBean chapterBean){
        MediaItem mediaItem = MediaItem.fromUri(chapterBean.getAudioUrl());
        JuniorBgPlayManager.getInstance().getPlayService().getPlayer().setMediaItem(mediaItem);
        JuniorBgPlayManager.getInstance().getPlayService().getPlayer().prepare();
    }
}
