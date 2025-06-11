package com.iyuba.iyubamovies.ui.presenter;

import com.iyuba.iyubamovies.base.BaseView;
import com.iyuba.iyubamovies.database.ImoviesDatabaseManager;
import com.iyuba.iyubamovies.model.ImoviesList;
import com.iyuba.iyubamovies.network.ImoviesNetwork;
import com.iyuba.iyubamovies.network.api.GetImoviesListApi;
import com.iyuba.iyubamovies.network.result.ImoviesListResult;
import com.iyuba.iyubamovies.ui.view.IIMoviesListViewInf;
import com.iyuba.iyubamovies.util.IMoviesSignUtil;
import com.iyuba.iyubamovies.util.ImoviesNetworkDataConvert;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by iyuba on 2017/12/22.
 */

public class IMoviesListPresenter {
    private IIMoviesListViewInf iiMoviesListViewInf;
    private List<ImoviesList> datas;
    public IMoviesListPresenter(BaseView view){
        this.iiMoviesListViewInf = (IIMoviesListViewInf) view;
    }

    public void getRefreshdata(){
        ImoviesNetwork.getImoviesListApi().getImoviesListData("0", IMoviesSignUtil.getSignOfImoviesListApi(), GetImoviesListApi.format).
                enqueue(new Callback<ImoviesListResult>() {
                    @Override
                    public void onResponse(Call<ImoviesListResult> call, Response<ImoviesListResult> response) {
                        if(response.isSuccessful()&&response.body()!=null){
                            ImoviesListResult result = response.body();
                            if(result.getMessage().equals("success")&&result.getResult()==1){
                                if(result.getData()!=null&&result.getData().size()>0){
                                    ImoviesDatabaseManager.getInstance().SavaImoviesLists(
                                            ImoviesNetworkDataConvert.ImoviesListconvert(result.getData()));
                                        if(iiMoviesListViewInf==null)
                                            return;
                                        iiMoviesListViewInf.setBannerData(ImoviesNetworkDataConvert.ImoviesListconvert(result.getData()).get(0));
                                    result.getData().remove(0);
                                    if(iiMoviesListViewInf==null)
                                        return;
                                    iiMoviesListViewInf.setAdapterData(ImoviesNetworkDataConvert.
                                            ImoviesListconvertObjs(result.getData()));
                                }else {
                                    if(iiMoviesListViewInf!=null)
                                    iiMoviesListViewInf.showToastMessage("暂无最新数据。");
                                }
                            }else {
                                if(iiMoviesListViewInf!=null)
                                iiMoviesListViewInf.showToastMessage("暂无最新数据。");
                            }
                        }else {
                            if(iiMoviesListViewInf!=null)
                            iiMoviesListViewInf.DataOfIntenetError();
                        }
                        if(iiMoviesListViewInf!=null)
                        iiMoviesListViewInf.onRefreshComplete();
                    }

                    @Override
                    public void onFailure(Call<ImoviesListResult> call, Throwable t) {
                        if(iiMoviesListViewInf!=null){
                        iiMoviesListViewInf.DataOfIntenetError();
                            if(iiMoviesListViewInf!=null)
                        iiMoviesListViewInf.onRefreshComplete();
                        }
                    }
                });
    }
    public void getData(){
        datas = ImoviesDatabaseManager.getInstance().SelectAllImoviesLists();
        if(datas!=null&&datas.size()>0&&iiMoviesListViewInf!=null) {
            if(iiMoviesListViewInf!=null)
            iiMoviesListViewInf.setBannerData(datas.get(0));
            datas.remove(0);
            if(iiMoviesListViewInf!=null)
            iiMoviesListViewInf.setAdapterData(ImoviesNetworkDataConvert.
                    ImoviesconvertObjs(datas));
        }else {
            getRefreshdata();
        }
    }

    public void getMoreData(){
    }
    public void onDestroy(){
        if(ImoviesNetwork.getImoviesListApi().getImoviesListData("0",
                IMoviesSignUtil.getSignOfImoviesListApi(),
                GetImoviesListApi.format).isExecuted()){
            ImoviesNetwork.getImoviesListApi().
                    getImoviesListData("0",
                            IMoviesSignUtil.getSignOfImoviesListApi(),
                            GetImoviesListApi.format).cancel();
        }
        this.iiMoviesListViewInf = null;
        datas=null;
    }


}
