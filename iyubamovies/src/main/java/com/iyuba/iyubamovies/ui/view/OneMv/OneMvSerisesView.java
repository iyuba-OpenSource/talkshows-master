package com.iyuba.iyubamovies.ui.view.OneMv;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.iyubamovies.R;
import com.iyuba.iyubamovies.base.IMoviesBaseActivity;
import com.iyuba.iyubamovies.manager.IMoviesConstant;
import com.iyuba.iyubamovies.model.OneSerisesData;
import com.iyuba.iyubamovies.network.CommentReslutToItemsMapper;
import com.iyuba.iyubamovies.network.ImoviesNetwork;
import com.iyuba.iyubamovies.network.result.ImoviesCommentData;
import com.iyuba.iyubamovies.network.result.PointsForIyubaResult;
import com.iyuba.iyubamovies.service.DownLoadService;
import com.iyuba.iyubamovies.ui.adapter.IMvserisesItemAdapter;
import com.iyuba.iyubamovies.ui.adapter.ImoviesCommentsAdapter;
import com.iyuba.iyubamovies.ui.presenter.OneMvSerisesPresenter;
import com.iyuba.iyubamovies.util.IMoviesNetWorkCheck;
import com.iyuba.iyubamovies.util.IMoviesShareUtil;
import com.iyuba.iyubamovies.util.IMoviesSignUtil;
import com.iyuba.iyubamovies.util.ImoviesNetworkDataConvert;
import com.iyuba.iyubamovies.util.ImoviesStudyRecordUtil;
import com.iyuba.iyubamovies.weight.IMVideoView;
import com.iyuba.iyubamovies.weight.IMoviesInputPopWindow;
import com.iyuba.iyubamovies.weight.IMvChooseSerisesPopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import de.hdodenhof.circleimageview.CircleImageView;
import devcontrol.DevControlActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.iyuba.basichdsfavorlibrary.db.BasicHDsFavorDBManager;
//import com.iyuba.basichdsfavorlibrary.db.BasicHDsFavorPart;

public class OneMvSerisesView extends IMoviesBaseActivity implements OneMvSerisesViewInf,View.OnTouchListener,
        GestureDetector.OnGestureListener{
    private static final int GESTURE_MODIFY_PROGRESS = 1;
    private static final int GESTURE_MODIFY_VOLUME = 2;
    private static final int GESTURE_MODIFY_BRIGHT = 3;
    private static final float STEP_VOLUME = 10.0f;
    private static final float STEP_PROGRESS = 5.0f;
    private static int GESTURE_FLAG = 0;
    private TextView imv_title;
    private TextView imv_readcounts;
    private TextView imv_comments;
    private TextView imv_serisecounts;
    private TextView imv_commentstv;
    private RecyclerView imv_serises_list;
    private RecyclerView imv_commentlist;
    private SmartRefreshLayout imv_refresh_layout;
    private IMVideoView imv_video;

    private CircleImageView imv_usericon;
    private TextView imv_inputcomment;
    private ImageView imv_download;
    private ImageView imv_share;
    private ImageView imv_collect;
    private ImageView imv_changelanguage;
    private OneMvSerisesPresenter oneMvSerisesPresenter;
    private String serisesid = "";
    private List<Object>datas;
    private IMvChooseSerisesPopWindow popWindow;
    private RelativeLayout video_rl;
    private IMvserisesItemAdapter serisesAdapter;
    private TextView imv_titlebar_tilte;
    private ImageView imv_back;
    private SeekBar imv_seekbar;
    private TextView imv_current_time;
    private TextView imv_total_time;
    private ImageView imv_plorpause;
    private ImageView imv_fullscreen;
    private TextView imv_detail_text;
    private ScrollView imv_scroll_view;
    private RelativeLayout imv_bright;
    private RelativeLayout imv_voice;
    private RelativeLayout imv_progress;
    private TextView imv_bright_tv;
    private TextView imv_voice_tv;
    private TextView imv_progress_tv;
    private ImageView imv_voice_iv;
    private ImageView imv_bright_iv;
    private ImageView imv_progress_iv;
    private ImageView imv_back_img;
    private GestureDetector gestureDetector;
    private Context context;
    private boolean isplay = true;
    private boolean isen = true;
    private IMoviesInputPopWindow inputPopWindow;
    public static int curplayposition = 0;
    private boolean isfullscreen = false;
    private boolean firstScroll = false;
    private LinearLayout imv_toolbar,imv_playstate;
    private int playerWidth = 0;
    private int playerHeight = 0;
    private float mBrightness = -1.0f;
    private int maxVolume;
    private int currentVolume;
    private int videototaltime = -1;
    private int currentpage = 1;
    private ImoviesCommentsAdapter commentsAdapter;
    private AudioManager audioManager;
    private ProgressDialog waittingdialog;
    private List<ImoviesCommentData> commentsData;
    private OneSerisesData currentData;
    private boolean isdownload = false;
    private String voaid = "";
    private ImageView circle_progress;
    private ImageButton to_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initdata();
        Log.e("Oncreate","---");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_one_mv_serises_view;
    }

    @Override
    protected void initWeight() {
        waittingdialog = new ProgressDialog(this);
        imv_collect = (ImageView) findViewById(R.id.imv_collect);
        imv_share = (ImageView) findViewById(R.id.imv_share);
        imv_download = (ImageView) findViewById(R.id.imv_download);
        imv_commentlist = (RecyclerView) findViewById(R.id.imv_commentlist);
        imv_serises_list = (RecyclerView) findViewById(R.id.imv_serises_list);
        imv_comments = (TextView) findViewById(R.id.imv_comments);
        imv_commentstv = (TextView) findViewById(R.id.imv_commentstv);
        imv_title = (TextView) findViewById(R.id.imv_title);
        imv_readcounts = (TextView) findViewById(R.id.imv_readcounts);
        imv_serisecounts = (TextView) findViewById(R.id.imv_serisecounts);
        imv_refresh_layout = (SmartRefreshLayout) findViewById(R.id.imv_refresh_layout);
        imv_video = (IMVideoView) findViewById(R.id.imv_video);
        imv_usericon = (CircleImageView) findViewById(R.id.imv_usericon);
        imv_inputcomment = (TextView) findViewById(R.id.imv_inputcomment);
        imv_changelanguage = (ImageView) findViewById(R.id.imv_changelanguage);
        to_tv =  findViewById(R.id.to_tv);
        serisesAdapter = new IMvserisesItemAdapter();
        imv_serises_list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        imv_serises_list.setAdapter(serisesAdapter);
        video_rl = (RelativeLayout) findViewById(R.id.imv_video_container);
        popWindow = new IMvChooseSerisesPopWindow(this);
        popWindow.setAdapter(serisesAdapter);
        imv_titlebar_tilte = (TextView) findViewById(R.id.imv_titlebar_title);
        imv_back = (ImageView) findViewById(R.id.imv_back);
        imv_seekbar = (SeekBar) findViewById(R.id.imv_seekbar_player);
        imv_current_time = (TextView) findViewById(R.id.imv_current_time);
        imv_total_time = (TextView) findViewById(R.id.imv_total_time);
        imv_plorpause = (ImageView) findViewById(R.id.imv_pauseorplay);
        imv_fullscreen = (ImageView) findViewById(R.id.imv_fullscreen);
        imv_toolbar = (LinearLayout) findViewById(R.id.imv_titlebar_layout);
        imv_playstate = (LinearLayout) findViewById(R.id.imv_play_state_info);
        imv_detail_text = (TextView) findViewById(R.id.imv_detail_text);
        imv_scroll_view = (ScrollView) findViewById(R.id.imv_scroll_view);
        inputPopWindow = new IMoviesInputPopWindow(this);
        imv_bright = (RelativeLayout) findViewById(R.id.imv_gesture_bright_layout);
        imv_bright_tv = (TextView) findViewById(R.id.imv_geture_tv_bright_percentage);
        imv_bright_iv = (ImageView) findViewById(R.id.imv_gesture_iv_player_bright);
        imv_voice = (RelativeLayout) findViewById(R.id.imv_gesture_volume_layout);
        imv_voice_tv = (TextView) findViewById(R.id.imv_geture_tv_volume_percentage);
        imv_voice_iv = (ImageView) findViewById(R.id.imv_gesture_iv_player_volume);
        imv_progress = (RelativeLayout) findViewById(R.id.imv_gesture_progress_layout);
        imv_progress_tv = (TextView) findViewById(R.id.imv_gesture_tv_progress_time);
        imv_progress_iv = (ImageView) findViewById(R.id.imv_gesture_iv_progress);
        imv_back_img = (ImageView) findViewById(R.id.imv_back_img);
        circle_progress = (ImageView) findViewById(R.id.imv_circle_progress);
        commentsAdapter = new ImoviesCommentsAdapter(this,IMoviesConstant.UserId);
        imv_commentlist.setLayoutManager(new LinearLayoutManager(this));
        commentsData = new ArrayList<>();
        commentsAdapter.setCommentItems(commentsData);
        imv_commentlist.setAdapter(commentsAdapter);
    }

    @Override
    protected void setListener() {
        video_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( imv_toolbar.getVisibility()==View.VISIBLE){
                    setControlViewVisibility(View.GONE);
                    viewchangehandler.removeCallbacksAndMessages(null);
                }else {
                    setControlViewVisibility(View.VISIBLE);
                    viewchangehandler.removeCallbacksAndMessages(null);
                    viewchangehandler.sendEmptyMessageDelayed(0,5000);
                }
            }
        });
        imv_changelanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isen = !isen;
                if(isen)
                    oneMvSerisesPresenter.getMixDetail(0);
                else
                    oneMvSerisesPresenter.getMixDetail(1);
            }
        });
        imv_serisecounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.showDownPosition(video_rl);
            }
        });
        imv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serisesAdapter.setISDOWNLOAD(true);
                popWindow.showDownPosition(video_rl);
            }
        });
        popWindow.setOnPopWindowDismissListener(new IMvChooseSerisesPopWindow.OnPopWindowDismissListener() {
            @Override
            public void onDismissClick() {
                serisesAdapter.setISDOWNLOAD(false);
            }
        });
//        imv_collect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(currentData!=null)
//                oneMvSerisesPresenter.collect(currentData);
//            }
//        });

        imv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentData!=null)
                IMoviesShareUtil.showShare(context,currentData.getPic(),currentData.getTitle(),currentData.getTitle_cn(),
                        IMoviesConstant.BuildShareSeriseURL(currentData.getId()));
            }
        });

        to_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDevice(IMoviesConstant.BuildVideoPlayOrDownLoadURL(currentData.getSerisesid(),currentData.getId()),currentData.getTitle_cn());
            }
        });
        IMoviesShareUtil.setOnShareStateListener(new IMoviesShareUtil.OnShareStateListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                showToast("分享成功");
                String srid = "";
                if (platform.getName().equals("QQ")
                        || platform.getName().equals("Wechat")
                        || platform.getName().equals("WechatFavorite")) {
                    srid = "7";
                } else if (platform.getName().equals("QZone")
                        || platform.getName().equals("WechatMoments")
                        || platform.getName().equals("SinaWeibo")
                        || platform.getName().equals("TencentWeibo")) {
                    srid = "19";
                }
                ImoviesNetwork.getPointsForIyubaApi().PointsForIyuba(srid,"1","1234567890"
                        + IMoviesSignUtil.getTime(),IMoviesConstant.UserId,IMoviesConstant.App_id,currentData.getId())
                        .enqueue(new Callback<PointsForIyubaResult>() {
                            @Override
                            public void onResponse(Call<PointsForIyubaResult> call, Response<PointsForIyubaResult> response) {
                                PointsForIyubaResult result = response.body();
                                if(result!=null&&"200".equals(result.getResult())){
                                    showToast("分享成功"+result.getAddcredit()+"分，当前积分为："
                                                    +result.getTotalcredit()+"分");
                                }

                            }

                            @Override
                            public void onFailure(Call<PointsForIyubaResult> call, Throwable t) {

                            }
                        });

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("tag","分享失败"+throwable.getMessage()+"---"+throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                showToast("分享已取消");
            }
        });
        imv_refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                oneMvSerisesPresenter.loadMoreData();
            }
        });
        imv_refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                oneMvSerisesPresenter.refreshData();
            }
        });
        serisesAdapter.setOnCircleItemClickLisenter(new IMvserisesItemAdapter.OnCircleItemClickLisenter() {
            @Override
            public void OnCircleItemOnClick(View view, int position) {
                if(datas==null)
                    return;
                ImoviesStudyRecordUtil.getInstance(context).recordStop("美剧","0","0",0);
                OneSerisesData data = (OneSerisesData) datas.get(position);
                setClickSerisesViewData(data);
                curplayposition = position;
            }
            @Override
            public void OnCircleDownLoadItemClick(View view,final int position) {
                if(!IMoviesConstant.Vipstatus.equals("0")) {
                    if (datas == null &&  position > datas.size() ) {
                        isdownload = false;
                        Log.e("call","datas==null&&pos>size");
                        return;
                    }
                    if (!IMoviesNetWorkCheck.isConnectInternet(context)) {
                        showToast("暂无网络连接。");
                        Log.e("call","暂无网络");
                        return;
                    }
//                    new RxPermissions(OneMvSerisesView.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
//                        @Override
//                        public void call(Boolean aBoolean) {
//                            if (aBoolean) {
                                if (!IMoviesNetWorkCheck.isConnectWIFI(context)) {
                                    new AlertDialog.Builder(context).setMessage("当前网络状态是数据网络连接,是否继续下载？").
                                            setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    return;
                                                }
                                            }).setPositiveButton("继续", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            OneSerisesData data = (OneSerisesData) datas.get(position);
                                            oneMvSerisesPresenter.DlSeriseData(data);
                                            isdownload = true;
                                        }
                                    }).create().show();
                                } else {
                                    OneSerisesData data = (OneSerisesData) datas.get(position);
                                    oneMvSerisesPresenter.DlSeriseData(data);
                                    isdownload = true;
                                }
                                Log.e("call","true");
//                            } else {
//                                Log.e("call","false");
//                                new AlertDialog.Builder(context).setMessage("没有读写权限，请在设置中，赋予权限，方可下载!").
//                                        setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                return;
//                                            }
//                                        }).create().show();
//                                //showToast("没有读写权限，请在设置中，赋予权限，方可下载!");
//                            }
//                        }
//                    });
                }else {
//                    Log.e("download_item","下载点击,弹出对话框");
//                    Toast.makeText(context,"vip用户专享下载，请到会员中心开通vip。",Toast.LENGTH_LONG).show();
                    new AlertDialog.Builder(context).setMessage("您还不是vip用户，vip用户才可以下载。").
                            setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).setPositiveButton("去开通vip", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            OneSerisesData data = (OneSerisesData) datas.get(position);
//                            oneMvSerisesPresenter.DlSeriseData(data);
//                            isdownload = true;
                                    Intent intent = new Intent(IMoviesConstant.VIPCENTER_ACTION);
                                    intent.putExtra("userId",IMoviesConstant.UserId);
                                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                                    startActivity(intent);
                        }
                    }).create().show();
                }
            }
        });
        commentsAdapter.setItemClickListener(new ImoviesCommentsAdapter.CommentItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                inputPopWindow.setPosition(position);
                inputPopWindow.showDownAs(video_rl);

            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        });
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //变成竖屏
                    isfullscreen = false;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                else {
                    finish();
                }
            }
        });
        imv_plorpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isplay = !isplay;
                onpauseclick(isplay);
            }
        });
        imv_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] position = new int[2];
                imv_usericon.getLocationOnScreen(position);
                System.out.println("getLocationOnScreen:" + position[0] + "," + position[1]);
                imv_scroll_view.scrollTo(0,position[1]);
            }
        });
        imv_inputcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPopWindow.setPosition(-1);
                inputPopWindow.showDownAs(video_rl);
            }
        });
        inputPopWindow.setOnInputSendClickListener(new IMoviesInputPopWindow.OnInputSendClickListener() {
            @Override
            public void OnInputSendClick(String content,int position) {
                if("".equals(content)){
                    showToast(getString(R.string.imv_input_nodata));
                    sendUnSuccess();
                }else {
                    oneMvSerisesPresenter.writeComment(content,position);
                }
            }
        });
        imv_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    //变成竖屏
                    isfullscreen = false;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    //变成横屏了
                    isfullscreen = true;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });
        video_rl.setOnTouchListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            ViewGroup.LayoutParams rl_paramters = video_rl.getLayoutParams();
//            rl_paramters.height = LinearLayout.LayoutParams.MATCH_PARENT;
//            rl_paramters.width = LinearLayout.LayoutParams.MATCH_PARENT;
//            imv_fullscreen.setImageResource(R.drawable.imovies_closescreen);
//            video_rl.setLayoutParams(rl_paramters);
           setVideoParams(imv_video.getmMediaPlayer(),true);

        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            ViewGroup.LayoutParams rl_paramters = video_rl.getLayoutParams();
//            rl_paramters.height = ImoviesViewSizeUtil.dip2px(context,210.0f);
//            rl_paramters.width = LinearLayout.LayoutParams.MATCH_PARENT;
//            video_rl.setLayoutParams(rl_paramters);
//            imv_fullscreen.setImageResource(R.drawable.imovies_fullscreen);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setVideoParams(imv_video.getmMediaPlayer(),false);
        }
    }
    public void setVideoParams(MediaPlayer mediaPlayer, boolean isLand)  {
        //获取surfaceView父布局的参数
        ViewGroup.LayoutParams rl_paramters = video_rl.getLayoutParams();
        //获取SurfaceView的参数
        ViewGroup.LayoutParams sv_paramters = imv_video.getLayoutParams();
        //设置宽高比为16/9
        float screen_widthPixels = getResources().getDisplayMetrics().widthPixels;
        float screen_heightPixels = getResources().getDisplayMetrics().widthPixels * 9f / 16f;
        //取消全屏
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (isLand) {
            screen_heightPixels = getResources().getDisplayMetrics().heightPixels;
            //设置全屏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        rl_paramters.width = (int) screen_widthPixels;
        rl_paramters.height = (int) screen_heightPixels;
        //获取MediaPlayer的宽高
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        float video_por;
        try {
            video_por = videoWidth / videoHeight;
        }catch (Exception e){
            video_por = 0;
        }
        float screen_por;
        try {
            screen_por = screen_widthPixels / screen_heightPixels;
        }catch (Exception e){
            screen_por =0;
        }

        //16:9    16:12
        if (screen_por > video_por) {
            sv_paramters.height = (int) screen_heightPixels;
            sv_paramters.width = (int) (screen_heightPixels * screen_por);
        } else {
            //16:9  19:9
            sv_paramters.width = (int) screen_widthPixels;
            sv_paramters.height = (int) (screen_widthPixels / screen_por);
        }
        video_rl.setLayoutParams(rl_paramters);
        imv_video.setLayoutParams(sv_paramters);
    }
    private void setControlViewVisibility(int visibility){
        imv_toolbar.setVisibility(visibility);
        imv_playstate.setVisibility(visibility);
    }
    private void initdata(){
        serisesid = getIntent().getStringExtra("serisesid");
        voaid = getIntent().getStringExtra("voaid");
        oneMvSerisesPresenter = new OneMvSerisesPresenter(this);
        if(voaid!=null&&!"".equals(voaid)){
            oneMvSerisesPresenter.CollectToPlay(serisesid,voaid);
        }else {
            oneMvSerisesPresenter.getSerisesDataCounts(serisesid);
        }
        gestureDetector = new GestureDetector(this,this);
        gestureDetector.setIsLongpressEnabled(true);
        gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                //setPauseImage(true);
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        ViewTreeObserver viewObserver = video_rl.getViewTreeObserver();
        viewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                video_rl.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                playerWidth = video_rl.getWidth();
                playerHeight = video_rl.getHeight();
            }
        });
    }
    private void playVideo(OneSerisesData data){
        imv_video.reset();
        imv_video.stopPlayback();
//        imv_video.clearDraw();
        imv_back_img.setVisibility(View.VISIBLE);
        imv_total_time.setText("00:00");
        File localFile = new File(IMoviesConstant.FILE_PATH +"/"+ data.getSerisesid() +
                "_"+ data.getId() + "_" + data.getType() +".mp4");
        if(localFile.exists()){
            Log.e("tag","文件存在");
            imv_video.setVideoPath(IMoviesConstant.FILE_PATH +"/"+ data.getSerisesid() +
                    "_"+ data.getId() + "_" + data.getType() +".mp4");
        }else {
            imv_video.setVideoPath(IMoviesConstant.BuildVideoPlayOrDownLoadURL(data.getSerisesid(),data.getId()));
        }

        imv_seekbar.setSecondaryProgress(0);
        controlVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("tag","onResume");
        if(!isplay){
            onpauseclick(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onpauseclick(true);
        Log.e("tag","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("tag","onStop");
        if(isdownload)
        startDLService();
    }

    private void controlVideo(){
        imv_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int time = imv_video.getDuration();
                videototaltime = imv_video.getDuration();
                imv_seekbar.setMax(time);
                imv_total_time.setText(formatTime(time/1000));
                imv_video.start();
                clearRomateAnim(circle_progress);
                imv_back_img.setVisibility(View.GONE);
                controlhandler.sendEmptyMessage(0);
                viewchangehandler.removeCallbacksAndMessages(null);
                viewchangehandler.sendEmptyMessageDelayed(0,5000);
            }
        });
        imv_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ImoviesStudyRecordUtil.getInstance(context).recordStop("美剧","0","0",0);
                curplayposition++;
                if(datas==null)
                    return;
                if(curplayposition<datas.size()){

                    OneSerisesData data = (OneSerisesData) datas.get(curplayposition);
                    setClickSerisesViewData(data);
                    serisesAdapter.setClickpos(curplayposition);
                    videototaltime=0;
                }else {
                    curplayposition = 0;
                    showToast("当前剧集已播完");
                    videototaltime=0;
                    CommentReslutToItemsMapper.TotalPage = 1;
                }
            }
        });
        imv_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                return false;
            }
        });
        imv_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    imv_current_time.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    imv_video.seekTo(progress);
                } else {
                    imv_current_time.setTextColor(context.getResources().getColor(R.color.white));
                }
                imv_current_time.setText(formatTime(progress / 1000));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void onpauseclick(boolean isplay){
        viewchangehandler.removeCallbacksAndMessages(null);
        if(isplay){
            if(imv_video.isPlaying()){
                imv_video.pause();
                imv_plorpause.setImageResource(R.drawable.imovies_video_play);
                ImoviesStudyRecordUtil.getInstance(context).recordStop("美剧","0","0",0);
            }
        }
        else {
            imv_plorpause.setImageResource(R.drawable.imovies_video_pause);
            imv_video.start();
            if(currentData!=null)
                ImoviesStudyRecordUtil.getInstance(context).recordStart(currentData.getId());
        }
        viewchangehandler.sendEmptyMessageDelayed(0,5000);
    }
    private Handler controlhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (imv_video.getCurrentPosition() >= imv_video.getDuration()) {
                        imv_current_time.setText(formatTime(imv_video.getDuration() / 1000));
                    } else {
                        imv_current_time.setText(formatTime(imv_video.getCurrentPosition() / 1000));
                    }
                    imv_seekbar.setProgress(imv_video.getCurrentPosition());
                    controlhandler.sendEmptyMessageDelayed(0,1000);
                    break;
            }
        }
    };
    private Handler viewchangehandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:setControlViewVisibility(View.GONE);
                    break;
            }
        }
    };
    private String formatTime(int time) {
        int second = time % 60;
        int minute = time / 60;
        return String.format("%02d:%02d", minute, second);
    }
    @Override
    public void showToast(String content) {
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setSerisesList(List<OneSerisesData> datas) {
        this.datas = ImoviesNetworkDataConvert.SerisesconvertObjs(datas);
        serisesAdapter.setItems(this.datas);
    }

    @Override
    public void setClickSerisesViewData(final OneSerisesData data) {
        romateAnim(circle_progress);
        ImoviesStudyRecordUtil.getInstance(context).recordStart(data.getId());
        if(!IMoviesNetWorkCheck.isConnectInternet(context)){
            showToast("暂无网络连接。");
            doClickSerisesViewData(data);
            return;
        }
        if(!IMoviesNetWorkCheck.isConnectWIFI(context)){
           new AlertDialog.Builder(this).setMessage("当前网络状态数据网络连接,是否继续播放？").
                   setNegativeButton("取消", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                                return;
                       }
                   }).setPositiveButton("继续", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                    doClickSerisesViewData(data);
               }
           }).create().show();
        }else {
            doClickSerisesViewData(data);
        }

    }

    private void doClickSerisesViewData(OneSerisesData data){
        if(data!=null){
            currentData = data;
            videototaltime = 0;
            imv_title.setText(data.getTitle_cn());
            imv_titlebar_tilte.setText(data.getTitle_cn());
            imv_readcounts.setText(data.getReadCount()+"次播放");
            setControlViewVisibility(View.VISIBLE);
            oneMvSerisesPresenter.getDetailsData(data);
            playVideo(data);
            setCommentSize("0");
            CommentReslutToItemsMapper.TotalPage = 1;
            commentsAdapter.clearComments();
            /*delete by diao
            BasicHDsFavorPart basicHDsFavorPart =  BasicHDsFavorDBManager.getInstance(context)
                    .queryBasicHDsFavorPart(data.getId(),IMoviesConstant.UserId,data.getType());
            if(basicHDsFavorPart != null && basicHDsFavorPart.getFlag().equals("1")){
                imv_collect.setImageResource(R.drawable.imovies_collect_select);
            }else {
                imv_collect.setImageResource(R.drawable.imovies_collect);
            }*/
            serisesAdapter.setClickpos(curplayposition);
            oneMvSerisesPresenter.getCommentsData(data.getId(),"1",true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewchangehandler.removeCallbacksAndMessages(null);
        controlhandler.removeCallbacksAndMessages(null);
        imv_video.stopPlayback();
        curplayposition = 0;
        oneMvSerisesPresenter.onDestroy();
        oneMvSerisesPresenter=null;
        if(datas!=null)
            datas.clear();
        datas=null;
        if(commentsData!=null)
            commentsData.clear();
        commentsData=null;
        if(circle_progress!=null)
            clearRomateAnim(circle_progress);
        Log.e("OnDestroy","---");
    }

    @Override
    public void setTotalSerises(String totalSerises) {
        imv_serisecounts.setText(totalSerises+"集全");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(popWindow.isShowing()||inputPopWindow.isshowing()){
                popWindow.dismiss();
                serisesAdapter.setISDOWNLOAD(false);
                inputPopWindow.dismiss();
            }else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //变成竖屏
                isfullscreen = false;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return true;
            }
            else{
                finish();
                ImoviesStudyRecordUtil.getInstance(context).recordStop("美剧","0","0",0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onrefreshfinish() {
        imv_refresh_layout.finishRefresh();
    }

    @Override
    public void onrefreshloadmorefinish() {
        imv_refresh_layout.finishLoadMore();
    }

    @Override
    public void setImoviesDetail(String detail) {
        if(detail!=null||!"".equals(detail))
            imv_detail_text.setText(detail);
        else
            imv_detail_text.setText("1:No brief introduction");
    }

    @Override
    public void setCommentslistData(List<ImoviesCommentData> datas) {
        commentsAdapter.setCommentItems(datas);
    }

    @Override
    public void addCommentslistData(List<ImoviesCommentData> datas) {
        commentsAdapter.addCommentItems(datas);
    }

    @Override
    public void setCommentSize(String size) {
        imv_comments.setText(size+"条评论");
        imv_commentstv.setText(size+"条评论");
    }

    @Override
    public void showDialog(String data) {
        waittingdialog.setMessage(data);
        waittingdialog.show();
    }

    @Override
    public void dismissDialog() {
        if(waittingdialog.isShowing())
            waittingdialog.dismiss();
    }

    @Override
    public void sendSuccess() {
        inputPopWindow.sendSuccess();
    }

    @Override
    public void sendUnSuccess() {
        inputPopWindow.sendUnsuccess();
    }

    @Override
    public void changeCollectSrc(int id) {
        imv_collect.setImageResource(id);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void startDLService() {
        startService(new Intent(this,DownLoadService.class).putExtra("cmd","start"));
    }

    @Override
    public void showAlertDialog(String data) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            GESTURE_FLAG = 0;// 手指离开屏幕后，重置调节音量或进度的标志
            imv_voice.setVisibility(View.GONE);
            imv_bright.setVisibility(View.GONE);
            imv_progress.setVisibility(View.GONE);
        }
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        firstScroll = true;
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(isfullscreen) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int playingTime = imv_video.getCurrentPosition();
            int y = (int) e2.getRawY();
            if (firstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
                // 横向的距离变化大则调整进度，纵向的变化大则调整-音量
                if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                    imv_progress.setVisibility(View.VISIBLE);
                    imv_voice.setVisibility(View.GONE);
                    imv_bright.setVisibility(View.GONE);
                    GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
                } else {
                    if (mOldX > playerWidth * 3.0 / 5) {// 音量
                        imv_voice.setVisibility(View.VISIBLE);
                        imv_bright.setVisibility(View.GONE);
                        imv_progress.setVisibility(View.GONE);
                        GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
                    } else if (mOldX < playerWidth * 2.0 / 5) {//亮度
                        imv_voice.setVisibility(View.GONE);
                        imv_bright.setVisibility(View.VISIBLE);
                        imv_progress.setVisibility(View.GONE);
                        GESTURE_FLAG = GESTURE_MODIFY_BRIGHT;
                    }
                }
            }
            // 如果每次触摸屏幕后第一次scroll是调节进度，那之后的scroll事件都处理音量进度，直到离开屏幕执行下一次操作
            if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS&&videototaltime>0) {
                Log.e("tag--kuaijin", distanceX + "");
            if (Math.abs(distanceX) > Math.abs(distanceY)) {// 横向移动大于纵向移动
                if (distanceX >= STEP_PROGRESS) {// 快退，用步长控制改变速度，可微调
                    imv_progress_iv.setImageResource(R.drawable.imovies_move_rewind);
                    if (playingTime > 600) {//避免为负
                        playingTime -= 500; //scroll方法执行一次快退200毫秒
                    } else {
                        playingTime = 0;
                    }
                } else if (distanceX <= - STEP_PROGRESS) {//快进
                    Log.e("total-time",videototaltime+"---"+playingTime);
                    imv_progress_iv.setImageResource(R.drawable.imovies_move_fast_forward);
                    if (playingTime< videototaltime - 600) {// 避免超过总时长
                        playingTime += 500;// scroll执行一次快进200毫秒
                    } else {
                        playingTime = videototaltime - 600;
                    }
                }
                if (playingTime < 0) {
                    playingTime = 0;
                }
                imv_video.seekTo(playingTime);
                imv_progress_tv.setText(formatTime(playingTime/1000) + "/" +
                        formatTime(videototaltime/1000));
                }
            }
            // 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
            else if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {
                currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
                if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动大于横向移动
                    if (distanceY >= STEP_VOLUME) {// 音量调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
                        if (currentVolume < maxVolume) {// 为避免调节过快，distanceY应大于一个设定值
                            currentVolume++;
                        }
                        imv_voice_iv.setImageResource(R.drawable.imovies_voice);
                    } else if (distanceY <= -STEP_VOLUME) {//音量调小
                        if (currentVolume > 0) {
                            currentVolume--;
                            if (currentVolume == 0) {
                                imv_voice_iv.setImageResource(R.drawable.imovies_no_voice);
                            }
                        }
                    }
                    int percentage = (currentVolume * 100) / maxVolume;
                    imv_voice_tv.setText(percentage + "%");
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
                }
            }
            // 如果每次触摸屏幕后第一次scroll是调节亮度，那之后的scroll事件都处理亮度调节，直到离开屏幕执行下一次操作
            else if (GESTURE_FLAG == GESTURE_MODIFY_BRIGHT) {
                if (mBrightness < 0) {
                    mBrightness = getWindow().getAttributes().screenBrightness;
                    if (mBrightness <= 0.00f)
                        mBrightness = 0.50f;
                    if (mBrightness < 0.01f)
                        mBrightness = 0.01f;
                }
                WindowManager.LayoutParams lpa = getWindow().getAttributes();
                lpa.screenBrightness = mBrightness + (mOldY - y) / playerHeight;
                if (lpa.screenBrightness > 1.0f)
                    lpa.screenBrightness = 1.0f;
                else if (lpa.screenBrightness < 0.01f)
                    lpa.screenBrightness = 0.01f;
                getWindow().setAttributes(lpa);
                imv_bright_tv.setText((int) (lpa.screenBrightness * 100) + "%");
            }
            firstScroll = false;// 第一次scroll执行完成，修改标志
        }
        return false;
    }
    private void romateAnim(View view){
        view.setVisibility(View.VISIBLE);
        Animation circle_anim = AnimationUtils.loadAnimation(context, R.anim.imv_circle_round);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            view.startAnimation(circle_anim);  //开始动画
        }
    }
    private void clearRomateAnim(View view){
        view.clearAnimation();
        view.setVisibility(View.GONE);
    }
    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private void chooseDevice(String url,String title) {
        Intent intent = new Intent(this, DevControlActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        startActivity(intent);
    }


}
