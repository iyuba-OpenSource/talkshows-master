package com.iyuba.iyubamovies.ui.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.iyubamovies.R;
import com.iyuba.iyubamovies.base.IMoviesBaseActivity;
import com.iyuba.iyubamovies.model.DataBean;
import com.iyuba.iyubamovies.model.ImoviesList;
import com.iyuba.iyubamovies.ui.adapter.IMoviesAdapter;
import com.iyuba.iyubamovies.ui.presenter.IMoviesListPresenter;
import com.iyuba.iyubamovies.ui.view.OneMv.OneMvSerisesView;
import com.iyuba.iyubamovies.ui.view.download.DownLoadView;
import com.iyuba.iyubamovies.util.ImoviesGlideBannerUtil;
import com.iyuba.module.commonvar.CommonVars;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class IMoviesListView extends IMoviesBaseActivity implements IIMoviesListViewInf{

    private SmartRefreshLayout refreshLayout;
    private RecyclerView movieslist;
    private ImageView titleback;
    private TextView title_content;
    private ImageView title_right;
    private Banner banner;
    private IMoviesAdapter adapter;
    private List<Object> datas;
    private View header;
    private List<String> titles;
    private List<Integer> defaultpics;
    private List<String>pics;
    private ImoviesList bannerdata;

    private IMoviesListPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("IMoviesListView","onCreate");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_imovies_list_view;
    }

    @Override
    protected void initWeight() {
       initData();
       refreshLayout = (SmartRefreshLayout) findViewById(R.id.imovies_refresh_layout);
       movieslist = (RecyclerView) findViewById(R.id.imovies_list);
       titleback = (ImageView) findViewById(R.id.imovies_title_back);
       title_content = (TextView) findViewById(R.id.imovies_title_content);
       title_right = (ImageView) findViewById(R.id.imovies_title_right);
       title_right.setImageResource(R.drawable.imovies_downloadicon);
       header = LayoutInflater.from(this).inflate(R.layout.imovies_item_header,null);
       initBanner(header);
       movieslist.setLayoutManager(new GridLayoutManager(this,2));
       adapter = new IMoviesAdapter(this,datas);
       adapter.setHeaderView(header);
       movieslist.setAdapter(adapter);
       title_content.setText("美剧");
//       refreshLayout.setEnableLoadmore(false);
       //TextSomeDatas();
    }
    private void initData(){
        titles = new ArrayList<>();
        pics = new ArrayList<>();
        defaultpics = new ArrayList<>();
        datas = new ArrayList<>();
        titles.add("数据正在准备中！");
        presenter = new IMoviesListPresenter(this);
        defaultpics.add(R.drawable.imovies_loading);
    }
    private void initBanner(View view){
        banner = (Banner) view.findViewById(R.id.imovies_banner);
        List<DataBean> list = new ArrayList<>();
        try {
            if ((defaultpics != null) && (titles != null)) {
                for (int i = 0; i < defaultpics.size(); i++) {
                    list.add(new DataBean(defaultpics.get(i), titles.get(i), i));
                }
            }
        } catch (Exception var) {}
//        banner.setAdapter(new ImageTitleAdapter(list));
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        banner.setImages(defaultpics);
        banner.setBannerTitles(titles);
//        banner.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(Object data, int position) {
//                if(bannerdata!=null){
//                    OneMvSerisesView.curplayposition = 0;
//                    startActivity(new Intent(IMoviesListView.this, OneMvSerisesView.class).putExtra("serisesid",bannerdata.getSeriesid()));
//                }
//            }
//        });
        banner.setImageLoader(new ImoviesGlideBannerUtil());
    }
    private void TextSomeDatas(){
        ImoviesList list = new ImoviesList("9","The Big Bang Theory season 9","606","生活大爆炸第九季","2017-12-22 04:12:55.0",
                "2017-12-22 04:12:55.0","1","http://apps." + CommonVars.domain + "/iyuba/images/voaseries/9.jpg","喜剧,幽默,情景剧,美剧","","");
        ImoviesList list2 = new ImoviesList("8","The Big Bang Theory season 8","606","生活大爆炸第八季","2017-12-22 04:12:55.0",
                "2017-12-22 04:12:55.0","1","http://apps." + CommonVars.domain + "/iyuba/images/voaseries/8.jpg","喜剧,幽默,情景剧,美剧","","");
        ImoviesList list3 = new ImoviesList("10","The Big Bang Theory season 10","606","生活大爆炸第十季","2017-12-22 04:12:55.0",
                "2017-12-22 04:12:55.0","1","http://http://apps." + CommonVars.domain + "/iyuba/images/voaseries/10.jpg","喜剧,幽默,情景剧,美剧","","");
        List<Object> imoviesLists = new ArrayList<>();
        imoviesLists.add(list);
        imoviesLists.add(list3);
        imoviesLists.add(list2);
        titles.clear();
        titles.add("生活大爆炸第八季");
        pics.clear();
        pics.add("http://apps." + CommonVars.domain + "/iyuba/images/voaseries/10.jpg");
//        banner.setImages(pics);
//        banner.setBannerTitles(titles);
        addAdapterData(imoviesLists);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TextSomeDatas();
        Log.e("IMoviesListView","onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        banner.start();
        Log.e("IMoviesListView","onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        banner.stop();
        banner.releaseBanner();
        Log.e("IMoviesListView","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        presenter=null;
        if(datas!=null){
            datas.clear();
            datas=null;
        }
        if(titles!=null){
            titles.clear();
            titles=null;
        }
        if(pics!=null){
            pics.clear();
            pics=null;
        }
        if(defaultpics!=null){
            defaultpics.clear();
            defaultpics=null;
        }
        Log.e("IMoviesListView","onDestroy");
    }

    @Override
    protected void setListener() {
        title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        titleback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if(presenter!=null)
                presenter.getRefreshdata();
            }
        });
        adapter.setImoviesOnItemClickListener(new IMoviesAdapter.ImoviesOnItemClickListener() {
            @Override
            public void OnImvoiesItmeClick(View view, int position) {
                ImoviesList data = (ImoviesList) datas.get(position);
                OneMvSerisesView.curplayposition = 0;
                startActivity(new Intent(IMoviesListView.this, OneMvSerisesView.class).putExtra("serisesid",data.getSeriesid()));
            }
        });
        title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IMoviesListView.this, DownLoadView.class));
            }
        });
        if(presenter!=null)
            presenter.getData();
    }

    @Override
    public void DataOfIntenetError() {

    }

    @Override
    public void showToastMessage(String text) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void dismissDialog() {

    }

    @Override
    public void refreshlist() {

    }

    @Override
    public void setAdapterData(List<Object> datas) {
        if(adapter!=null&&datas!=null)
            adapter.resetData(datas);
    }

    @Override
    public void addAdapterData(List<Object> datas) {
        if(adapter!=null&&datas!=null)
            adapter.addData(datas);
    }

    @Override
    public void clearAdapterData() {
        if(adapter!=null&&datas!=null)
            adapter.clearData();
    }

    @Override
    public void setBannerData(Object obj) {
        if(obj==null)
            return;
        if(obj instanceof ImoviesList){
            Log.e("不为","null");
            ImoviesList data = (ImoviesList) obj;
            titles.clear();
            titles.add(data.getSeriesName());
            defaultpics.clear();
            pics.clear();
            pics.add(data.getPic());

            banner.setImages(pics);
            banner.setBannerTitles(titles);
//            List<DataBean> list = new ArrayList<>();
//            try {
//                if ((pics != null) && (titles != null)) {
//                    for (int i = 0; i < pics.size(); i++) {
//                        list.add(new DataBean(pics.get(i), titles.get(i), i));
//                    }
//                }
//            } catch (Exception var) {}
//            banner.setAdapter(new ImageTitleAdapter(list));
//            bannerdata = data;
        }
    }

    @Override
    public void onRefreshComplete() {
        if(refreshLayout!=null)
            refreshLayout.finishRefresh();
    }

    @Override
    public void onLoadMoreComplete() {
        if(refreshLayout!=null)
            refreshLayout.finishLoadMore();
    }
}
