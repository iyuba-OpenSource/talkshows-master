package com.iyuba.iyubamovies.network;

import com.iyuba.iyubamovies.network.result.ImoviesDetailData;
import com.iyuba.iyubamovies.network.result.ImoviesDetailResult;

import java.util.List;

import rx.functions.Func1;

/**
 * Created by iyuba on 2018/1/18.
 */

public class ImoviesDetailResultToDataMapper implements Func1<ImoviesDetailResult ,List<ImoviesDetailData>> {
    private static ImoviesDetailResultToDataMapper instance = new ImoviesDetailResultToDataMapper();

    private ImoviesDetailResultToDataMapper(){}

    public static ImoviesDetailResultToDataMapper getInstance(){
        return instance;
    }

    @Override
    public List<ImoviesDetailData> call(ImoviesDetailResult DetailResult) {
        List<ImoviesDetailData>  Details = DetailResult.Details;
        return  Details;
    }
}
