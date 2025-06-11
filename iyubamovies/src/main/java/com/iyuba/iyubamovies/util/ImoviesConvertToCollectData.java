//package com.iyuba.iyubamovies.util;
//
////import com.iyuba.basichdsfavorlibrary.db.BasicHDsFavorPart;
//import com.iyuba.iyubamovies.model.OneSerisesData;
//
///**
// * Created by iyuba on 2018/1/23.
// */
//
//public class ImoviesConvertToCollectData {
//    public static BasicHDsFavorPart simplifySeriesData(OneSerisesData data, String userId,
//                                                       String Synflg, String insertFrom,
//                                                       String CollectFlg, String CollecTime){
//
//        BasicHDsFavorPart basicHDsFavorPart = new BasicHDsFavorPart();
//        basicHDsFavorPart.setId(data.getId());
//        basicHDsFavorPart.setUserId(userId);
//        basicHDsFavorPart.setType(data.getType());
//        basicHDsFavorPart.setCategory(data.getCategory());
//        basicHDsFavorPart.setCategoryName(data.getCategoryName());
//        basicHDsFavorPart.setCreateTime(data.getCreateTime());
//        basicHDsFavorPart.setPic(data.getPic());
//        basicHDsFavorPart.setTitle_cn(data.getTitle_cn());
//        basicHDsFavorPart.setTitle(data.getTitle());
//        basicHDsFavorPart.setSynflg(Synflg);
//        basicHDsFavorPart.setInsertFrom(insertFrom);
//        basicHDsFavorPart.setOther1("");
//        basicHDsFavorPart.setOther2("");
//        basicHDsFavorPart.setOther3("");
//        basicHDsFavorPart.setFlag(CollectFlg);
//        basicHDsFavorPart.setCollectTime(CollecTime);
//        basicHDsFavorPart.setSound(data.getSound());
//        basicHDsFavorPart.setSource("");
//        return basicHDsFavorPart;
//    }
//
//}
