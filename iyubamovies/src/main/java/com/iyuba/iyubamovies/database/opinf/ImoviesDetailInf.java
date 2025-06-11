package com.iyuba.iyubamovies.database.opinf;

import com.iyuba.iyubamovies.network.result.ImoviesDetailData;

import java.util.List;

/**
 * Created by iyuba on 2018/1/18.
 */

public interface ImoviesDetailInf {
    String ID = "Id";
    String SERISEID = "Seriseid";
    String TYPE = "Type";
    String PARAID = "ParaId";
    String IDINDEX = "IdIndex";
    String SENTENCE = "Sentence";
    String SENTENCE_CN = "Sentence_cn";
    String TIMING = "Timing";
    String ENDTIMING = "EndTiming";
    String TABLE_NAME = "imovies_serise_detail";
    void insertImoviesDetails(List<ImoviesDetailData> datas,String id,String seriseid,String type);
    List<ImoviesDetailData> getImoviesDetailsData(String id,String seriseid,String type);
}
