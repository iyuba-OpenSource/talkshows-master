package com.iyuba.iyubamovies.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.dlex.bizs.DLManager;
import com.iyuba.dlex.bizs.DLTaskInfo;
import com.iyuba.dlex.interfaces.IDListener;
import com.iyuba.iyubamovies.R;
import com.iyuba.iyubamovies.database.ImoviesDatabaseManager;
import com.iyuba.iyubamovies.manager.IMoviesApp;
import com.iyuba.iyubamovies.model.OneSerisesData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iyuba on 2018/1/24.
 */

public class ImoviesDownLoadAdapter extends RecyclerView.Adapter{

    private final static int DOWNLOADINGTYPE = 1;
    private final static int DOWNLOADTYPE = 2;
    private List<Object> datas;
    private DLManager dlManager;
    private boolean isshowcheck = false;
    private ImoviesDatabaseManager databaseManager;
    private OnDownLoadItemClickListener onDownLoadItemClickListener;
    public ImoviesDownLoadAdapter(){
        this.datas = new ArrayList<>();
        dlManager = IMoviesApp.getDlManager();
        databaseManager = ImoviesDatabaseManager.getInstance();
    }
    public ImoviesDownLoadAdapter(List<Object> datas){
        this.datas = datas;
        dlManager = IMoviesApp.getDlManager();
        databaseManager = ImoviesDatabaseManager.getInstance();
    }
    public void setItemDatas(List<Object>datas){
        this.datas = datas;
        notifyDataSetChanged();
    }
    public void addItemDatas(List<Object>datas){
        if(datas!=null){
            datas.addAll(datas);
            notifyDataSetChanged();
        }
    }
    public void clearItemDatas(){
        if(datas!=null){
            datas.clear();
            notifyDataSetChanged();
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == DOWNLOADINGTYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imovies_downloading_item,parent,false);
            return new DownLoadingViewHold(view);
        }else if(viewType == DOWNLOADTYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imovies_download_item,parent,false);
            return new DoWnloadViewHold(view);
        }else
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(datas!=null&&datas.size()>0){
            OneSerisesData data = (OneSerisesData) datas.get(position);
            if("1".equals(data.getIsDownload())&&"1".equals(data.getIsFinishDownLoad())){
                return DOWNLOADTYPE;
            }else if("1".equals(data.getIsDownload())&&!"1".equals(data.getIsFinishDownLoad())){
                return DOWNLOADINGTYPE;
            }else
                return super.getItemViewType(position);
        }
        else
            return super.getItemViewType(position);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(datas!=null&&position<datas.size()) {
            final OneSerisesData data = (OneSerisesData) datas.get(position);
            Log.e("data--",data.toString());
            if (holder instanceof DoWnloadViewHold) {
                final DoWnloadViewHold doWnloadViewHold = (DoWnloadViewHold) holder;
                doWnloadViewHold.imv_title_en.setText(data.getTitle());
                doWnloadViewHold.imv_date.setText(data.getCreateTime().substring(0,11));
                doWnloadViewHold.imv_basic_pic.setImageResource(R.drawable.imovies_loading);
                Glide.with(holder.itemView.getContext()).load(data.getPic())
                        .placeholder(R.drawable.imovies_loading)
                        .error(R.drawable.imovies_loading)
                        .into(doWnloadViewHold.imv_basic_pic);
                doWnloadViewHold.imv_category_name.setText(data.getCategoryName());
                doWnloadViewHold.imv_title_cn.setText(data.getTitle_cn());
                doWnloadViewHold.imv_cate_sign.setImageResource(R.drawable.imovies_cate_video);
                doWnloadViewHold.data = data;
                doWnloadViewHold.imv_ischoose.setOnCheckedChangeListener(null);
                if(isshowcheck) {
                    doWnloadViewHold.imv_ischoose.setChecked(data.ischeck);
                    doWnloadViewHold.imv_ischoose.setVisibility(View.VISIBLE);
                } else {
                    doWnloadViewHold.imv_ischoose.setChecked(data.ischeck);
                    doWnloadViewHold.imv_ischoose.setVisibility(View.GONE);
                }
                doWnloadViewHold.imv_ischoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isshowcheck){
                            if(onDownLoadItemClickListener!=null){
                                data.ischeck = isChecked;
                                onDownLoadItemClickListener.onCheckedItemListener(buttonView,position,isChecked);
                            }
                        }
                    }
                });
                doWnloadViewHold.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onDownLoadItemClickListener!=null) {
                            if(!isshowcheck)
                            onDownLoadItemClickListener.onDownloadedItemClick(v, position);
                        }
                        if(isshowcheck){
                            data.ischeck = !data.ischeck;
                            doWnloadViewHold.imv_ischoose.setChecked(data.ischeck);
                        }
                    }
                });
            }else if (holder instanceof DownLoadingViewHold) {
                final DownLoadingViewHold doWnloadViewHold = (DownLoadingViewHold) holder;
                doWnloadViewHold.imv_basic_pic.setImageResource(R.drawable.imovies_loading);
                doWnloadViewHold.setData(data);
                Glide.with(holder.itemView.getContext()).load(data.getPic())
                        .placeholder(R.drawable.imovies_loading)
                        .error(R.drawable.imovies_loading)
                        .into(doWnloadViewHold.imv_basic_pic);
                doWnloadViewHold.imv_progress.setProgress(0);
                doWnloadViewHold.imv_ischoose.setOnCheckedChangeListener(null);
                if(isshowcheck){
                    doWnloadViewHold.imv_ischoose.setVisibility(View.VISIBLE);
                    doWnloadViewHold.imv_ischoose.setChecked(data.ischeck);
                }
                else {
                    doWnloadViewHold.imv_ischoose.setChecked(data.ischeck);
                    doWnloadViewHold.imv_ischoose.setVisibility(View.GONE);
                }

                doWnloadViewHold.imv_ischoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isshowcheck){
                            if(onDownLoadItemClickListener!=null){
                                data.ischeck = isChecked;
                                onDownLoadItemClickListener.onCheckedItemListener(buttonView,position,isChecked);

                            }
                        }
                    }
                });
                doWnloadViewHold.imv_title_cn.setText(data.getTitle_cn());
                final DLTaskInfo info = dlManager.getDLTaskInfo(getDownTag(data));
                if(info!=null){
                    switch ( info.state ) {
                        case DLTaskInfo.TaskState.INIT:
                            doWnloadViewHold.imv_downstate_tv.setText("下载准备中");
                            doWnloadViewHold.imv_down_sate.setImageResource(R.drawable.imovies_download_pause);
                            break;
                        case DLTaskInfo.TaskState.WAITING:
                            doWnloadViewHold.imv_downstate_tv.setText("等待中");
                            doWnloadViewHold.imv_down_sate.setImageResource(R.drawable.imovies_download_pause);
                            break;
                        case DLTaskInfo.TaskState.PREPARING:
                        case DLTaskInfo.TaskState.DOWNLOADING:
                            doWnloadViewHold.imv_progress.setProgress(info.getCurrentPercentage());
                            doWnloadViewHold.imv_down_tv.setText(info.getCurrentPercentage()+"%");
                            doWnloadViewHold.imv_downstate_tv.setText("下载中");
                            doWnloadViewHold.imv_down_sate.setImageResource(R.drawable.imovies_download_start);
                            break;
                        case DLTaskInfo.TaskState.ERROR:
                        case DLTaskInfo.TaskState.PAUSING:
                            //dlManager.resumeTask(info);
                            doWnloadViewHold.imv_progress.setProgress(info.getCurrentPercentage());
                            doWnloadViewHold.imv_down_tv.setText(info.getCurrentPercentage()+"%");
                            doWnloadViewHold.imv_downstate_tv.setText("已暂停");
                            doWnloadViewHold.imv_down_sate.setImageResource(R.drawable.imovies_download_pause);
                            break;
                        case DLTaskInfo.TaskState.COMPLETED:
                            if(info.isFileExist()) {
                                if (!"1".equals(data.getIsFinishDownLoad())) {
                                    data.setIsFinishDownLoad("1");
                                    data.setIsDownload("1");
                                    databaseManager.saveSerise(data);
                                }
                            }else {
                                dlManager.cancelTask(info);
                                data.setIsFinishDownLoad("0");
                                data.setIsDownload("0");
                                databaseManager.saveSerise(data);
                            }
                            doWnloadViewHold.imv_down_sate.setImageResource(R.drawable.imovies_cate_video);
                            doWnloadViewHold.imv_downstate_tv.setText("已完成");
                            break;
                        default:
                            break;
                    }

                }
                if(info!=null)
                info.setDListener(new IDListener() {

                    @Override
                    public void onPrepare() {

                    }

                    @Override
                    public void onStart(String fileName, String realUrl, int fileLength) {
                        Log.e("fileLength",""+fileLength);
                        if(doWnloadViewHold.imv_downstate_tv!=null)
                        {
                            doWnloadViewHold.imv_downstate_tv.setText("正在下载");
                            doWnloadViewHold.imv_down_sate.setImageResource(R.drawable.imovies_download_start);
                        }

                    }

                    @Override
                    public void onProgress(int progress) {

                        if(doWnloadViewHold.imv_progress!=null&&doWnloadViewHold.data==data)
                            doWnloadViewHold.imv_progress.setProgress(info.getCurrentPercentage());
                        if(doWnloadViewHold.imv_down_tv!=null&&doWnloadViewHold.data==data)
                            doWnloadViewHold.imv_down_tv.setText(info.getCurrentPercentage()+"%");
                    }

                    @Override
                    public void onStop(int progress) {
                        Log.e("onstop","已停止");
                    }

                    @Override
                    public void onFinish(File file) {
                        data.setIsDownload("1");
                        data.setIsFinishDownLoad("1");
                        databaseManager.saveSerise(data);
                        Log.e("finish","储存了");
                        if(doWnloadViewHold.imv_downstate_tv!=null)
                            doWnloadViewHold.imv_downstate_tv.setText("下载完成");
                        doWnloadViewHold.imv_down_sate.setImageResource(R.drawable.imovies_cate_video);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(int status, String error) {
                        dlManager.stopTask(info);
                        if(doWnloadViewHold.imv_down_sate!=null)
                            doWnloadViewHold.imv_down_sate.setImageResource(R.drawable.imovies_download_pause);
                        if(doWnloadViewHold.imv_downstate_tv!=null)
                            doWnloadViewHold.imv_downstate_tv.setText("下载出错");
                    }
                });
                doWnloadViewHold.imv_down_sate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(info==null)
                            return;
                        if(info.state==DLTaskInfo.TaskState.PAUSING||info.isError()){
                            dlManager.resumeTask(info);
                            doWnloadViewHold.imv_downstate_tv.setText("正在下载");
                            doWnloadViewHold.imv_down_sate.setImageResource(R.drawable.imovies_download_start);
                        }else {
                            info.state = DLTaskInfo.TaskState.PAUSING;
                            dlManager.stopTask(info);
                            doWnloadViewHold.imv_downstate_tv.setText("已暂停");
                            doWnloadViewHold.imv_down_sate.setImageResource(R.drawable.imovies_download_pause);
                        }
                    }
                });
                doWnloadViewHold.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onDownLoadItemClickListener!=null)
                            onDownLoadItemClickListener.onDownloadingItemClick(v,position);
                        if(isshowcheck){
                             data.ischeck = !data.ischeck;
                             doWnloadViewHold.imv_ischoose.setChecked(data.ischeck);
                        }
                    }
                });

                //doWnloadViewHold.setData(data);
            }
        }
    }
    private String getDownTag(OneSerisesData data){
        return "imovies"+data.getId()+data.getSerisesid()+data.getType();
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setOnDownLoadItemClickListener(OnDownLoadItemClickListener onDownLoadItemClickListener) {
        this.onDownLoadItemClickListener = onDownLoadItemClickListener;
    }

    public void setIsshowcheck(boolean isshowcheck) {
        this.isshowcheck = isshowcheck;
        notifyDataSetChanged();
    }
    class DoWnloadViewHold extends RecyclerView.ViewHolder{
        TextView imv_title_cn;
        ImageView imv_basic_pic;
        TextView imv_category_name;
        TextView imv_date;
        ImageView imv_cate_sign;
        TextView imv_title_en;
        CheckBox imv_ischoose;
        OneSerisesData data;
        boolean ischeck = false;
        //View DoWnload_item;
        public DoWnloadViewHold(View itemView) {
            super(itemView);
            imv_title_cn = (TextView) itemView.findViewById(R.id.imovies_title_cn);
            imv_basic_pic = (ImageView) itemView.findViewById(R.id.imovies_basic_pic);
            imv_category_name = (TextView) itemView.findViewById(R.id.imovies_category_name);
            imv_date = (TextView) itemView.findViewById(R.id.imovies_date);
            imv_cate_sign = (ImageView) itemView.findViewById(R.id.imovies_cate_iv);
            imv_title_en = (TextView) itemView.findViewById(R.id.imovies_title_en);
            imv_ischoose = (CheckBox) itemView.findViewById(R.id.imovies_is_choose);
        }
    }
    class DownLoadingViewHold extends RecyclerView.ViewHolder{
        TextView imv_title_cn;
        ImageView imv_basic_pic;
        CheckBox imv_ischoose;
        ProgressBar imv_progress;
        TextView imv_down_tv;
        TextView imv_downstate_tv;
        ImageView imv_down_sate;
        boolean ischeck = false;
        OneSerisesData data;
        public DownLoadingViewHold(View itemView) {
            super(itemView);
            imv_title_cn = (TextView) itemView.findViewById(R.id.imovies_title_cn);
            imv_basic_pic = (ImageView) itemView.findViewById(R.id.imovies_basic_pic);
            imv_ischoose = (CheckBox) itemView.findViewById(R.id.imovies_is_choose);
            imv_progress = (ProgressBar) itemView.findViewById(R.id.imovies_progress);
            imv_down_tv = (TextView) itemView.findViewById(R.id.imovies_dl_text);
            imv_downstate_tv = (TextView) itemView.findViewById(R.id.imovies_dl_state);
            imv_down_sate = (ImageView) itemView.findViewById(R.id.imovies_state);
        }
        public void setData(OneSerisesData data) {
            this.data = data;
        }
    }

    public interface OnDownLoadItemClickListener{
        void onDownloadingItemClick(View view,int position);
        void onDownloadedItemClick(View view,int position);
        void onCheckedItemListener(View view,int position,boolean ischeck);
    }
}
