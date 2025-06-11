package com.iyuba.iyubamovies.util;

import com.iyuba.iyubamovies.model.ImoviesList;
import com.iyuba.iyubamovies.model.OneSerisesData;
import com.iyuba.iyubamovies.network.result.ImoviesListData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by iyuba on 2017/12/23.
 */

public class ImoviesNetworkDataConvert {
    public static List<ImoviesList> ImoviesListconvert(List<ImoviesListData> imoviesListDatas){
        List<ImoviesList> imoviesLists = new ArrayList<>();
        if(imoviesListDatas!=null){
            for(ImoviesListData data:imoviesListDatas){
                ImoviesList imoviesList = new ImoviesList(data);
                imoviesLists.add(imoviesList);
            }
        }
        return imoviesLists;
    }
    public static List<Object> ImoviesListconvertObjs(List<ImoviesListData> imoviesListDatas){
        List<Object> imoviesLists = new ArrayList<>();
        if(imoviesListDatas!=null){
            for(ImoviesListData data:imoviesListDatas){
                ImoviesList imoviesList = new ImoviesList(data);
                imoviesLists.add(imoviesList);
            }
        }
        return imoviesLists;
    }
    public static List<Object> ImoviesconvertObjs(List<ImoviesList> imoviesListDatas){
        List<Object> imoviesLists = new ArrayList<>();
        if(imoviesListDatas!=null){
            for(ImoviesList data:imoviesListDatas){
                imoviesLists.add(data);
            }
        }
        return imoviesLists;
    }
    public static List<Object> SerisesconvertObjs(List<OneSerisesData> oneSerisesDatas){
        List<Object> imoviesLists = new ArrayList<>();
        if(oneSerisesDatas!=null){
            Collections.reverse(oneSerisesDatas);
            for(OneSerisesData data:oneSerisesDatas){
                imoviesLists.add(data);
            }
        }
        return imoviesLists;
    }
}
