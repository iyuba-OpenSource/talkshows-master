package com.iyuba.iyubamovies.ui.view.download;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.iyubamovies.R;
import com.iyuba.iyubamovies.base.IMoviesBaseActivity;
import com.iyuba.iyubamovies.model.OneSerisesData;
import com.iyuba.iyubamovies.ui.adapter.ImoviesDownLoadAdapter;
import com.iyuba.iyubamovies.ui.presenter.IMoviesDownLoadPresenter;
import com.iyuba.iyubamovies.ui.view.OneMv.OneMvSerisesView;
import com.iyuba.iyubamovies.weight.IMoviesDividerItemDecoration;

import java.util.List;

public class DownLoadView extends IMoviesBaseActivity implements DownLoadViewInf{

    private RecyclerView imv_downloadlist;
    private ImoviesDownLoadAdapter downLoadAdapter;
    private IMoviesDownLoadPresenter presenter;
    private RecyclerView imv_finish_list;
    private ImageView imv_back;
    private ImageView imv_delete;
    private TextView imv_complete;
    private boolean ishowcheck = false;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_imovies_download_view;
    }

    @Override
    protected void initWeight() {
        imv_downloadlist = (RecyclerView) findViewById(R.id.imovies_download_list);
        downLoadAdapter = new ImoviesDownLoadAdapter();
        imv_downloadlist.setLayoutManager(new LinearLayoutManager(this));
        imv_downloadlist.addItemDecoration(new IMoviesDividerItemDecoration(
                this, R.drawable.imv_download_recyclerview_thick_divider,IMoviesDividerItemDecoration.VERTICAL_LIST));
        imv_downloadlist.setAdapter(downLoadAdapter);
        imv_finish_list = (RecyclerView) findViewById(R.id.imovies_finish_lish);
        imv_back = (ImageView) findViewById(R.id.imovies_nav_left);
        imv_delete = (ImageView) findViewById(R.id.imovies_nav_right_button);
        imv_complete = (TextView) findViewById(R.id.imovies_nav_right_text);
        imv_complete.setVisibility(View.GONE);
    }

    @Override
    protected void setListener() {
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imv_delete.setVisibility(View.GONE);
                downLoadAdapter.setIsshowcheck(true);
                imv_complete.setVisibility(View.VISIBLE);
                ishowcheck = true;
            }
        });
        imv_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imv_complete.setVisibility(View.GONE);
                downLoadAdapter.setIsshowcheck(false);
                imv_delete.setVisibility(View.VISIBLE);
                ishowcheck = false;
                presenter.deleteDatas();

            }
        });
        downLoadAdapter.setOnDownLoadItemClickListener(new ImoviesDownLoadAdapter.OnDownLoadItemClickListener() {
            @Override
            public void onDownloadingItemClick(View view, int position) {

            }

            @Override
            public void onDownloadedItemClick(View view, int position) {
                presenter.onDownLoadedItemClick(position);
            }

            @Override
            public void onCheckedItemListener(View view, int position, boolean ischeck) {
                if(ischeck)
                    presenter.addCheckLisener(position);
                else
                    presenter.removeCheck(position);
            }
        });
    }
    private void initData(){
        dialog = new ProgressDialog(this);
        presenter = new IMoviesDownLoadPresenter(this);
        presenter.getDownLoadData();

    }

    @Override
    public void setDownLoadListItems(List<Object> objects) {
        downLoadAdapter.setItemDatas(objects);
    }

    @Override
    public void showDialog(String data) {
        dialog.setMessage(data);
        dialog.show();
    }

    @Override
    public void dismissdiloag() {
        if(dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onDownLoadedItem(OneSerisesData data) {
        startActivity(new Intent(this, OneMvSerisesView.class).putExtra("serisesid",data.getSerisesid())
                .putExtra("voaid",data.getId()));
    }

}
