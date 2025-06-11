package com.iyuba.talkshow.ui.courses.wordChoose;

import com.iyuba.talkshow.ui.courses.common.TypeHolder;

import java.util.ArrayList;
import java.util.List;

public class WordTypeHelper {
    public static final boolean TYPE_OPPO_RENJIAO = true;
    public static final int DEFAULT_TYPE =  0 ;
    public static final int TYPE_PRIMARY_RENJIAO  = 0;
    public static final int TYPE_PRIMARY_WAIYAN  = 3;
    public static final int TYPE_PRIMARY_BEISHI  = 1;
    public static final int TYPE_PRIMARY_BEIJING  = 2;
    public static final int TYPE_PRIMARY_CONCEPT  = 4;
    public static final int TYPE_JUNIOR  = 2;
    private static List<TypeHolder> renTypeHolder;
    private static List<TypeHolder> waiTypeHolder;
    private static List<TypeHolder> shiTypeHolder;
    private static List<TypeHolder> beiTypeHolder;
    private static List<TypeHolder> titleHolder;
    private static List<TypeHolder> nceTypeHolder;
    public static List<TypeHolder> getXiaoxueTitle(int type ) {
        if (titleHolder == null) {
            titleHolder = new ArrayList<>();
        } else {
            titleHolder.clear();
        }
        titleHolder.add(new TypeHolder(TYPE_PRIMARY_RENJIAO,"人教版"));
//        list.add(new TypeHolder(TYPE_PRIMARY_WAIYAN,"外研版"));
        titleHolder.add(new TypeHolder(TYPE_PRIMARY_BEISHI,"北师版"));
        if (type == TYPE_PRIMARY_BEIJING) {
            titleHolder.add(new TypeHolder(TYPE_PRIMARY_BEIJING,"北京版"));
        }
        return  titleHolder;
    }
    public static List<TypeHolder> getXiaoxueType(int type ) {
        switch (type) {
            case TYPE_PRIMARY_RENJIAO:
            default:
                if ((renTypeHolder != null) && renTypeHolder.size() > 0) {
                    return renTypeHolder;
                }
                renTypeHolder = new ArrayList<>();
                renTypeHolder.add(new TypeHolder(313,"新起点"));
                renTypeHolder.add(new TypeHolder(314,"PEP"));
                renTypeHolder.add(new TypeHolder(315,"精通"));
                return renTypeHolder;
            case TYPE_PRIMARY_WAIYAN:
                if ((waiTypeHolder != null) && waiTypeHolder.size() > 0) {
                    return waiTypeHolder;
                }
                waiTypeHolder = new ArrayList<>();
                waiTypeHolder.add(new TypeHolder(318,"一起点"));
                waiTypeHolder.add(new TypeHolder(317,"三起点"));
                return waiTypeHolder;
            case TYPE_PRIMARY_BEISHI:
                if ((shiTypeHolder != null) && shiTypeHolder.size() > 0) {
                    return shiTypeHolder;
                }
                shiTypeHolder = new ArrayList<>();
                shiTypeHolder.add(new TypeHolder(320,"一起点"));
                shiTypeHolder.add(new TypeHolder(319,"三起点"));
                return shiTypeHolder;
            case TYPE_PRIMARY_BEIJING:
                if ((beiTypeHolder != null) && beiTypeHolder.size() > 0) {
                    return beiTypeHolder;
                }
                beiTypeHolder = new ArrayList<>();
                beiTypeHolder.add(new TypeHolder(336,"小学英语"));
                return beiTypeHolder;
            case TYPE_PRIMARY_CONCEPT:
                if ((nceTypeHolder != null) && nceTypeHolder.size() > 0) {
                    return nceTypeHolder;
                }
                nceTypeHolder = new ArrayList<>();
                nceTypeHolder.add(new TypeHolder(337,"新概念"));
                return nceTypeHolder;
        }
    }
    public static List<TypeHolder> geChuTitle(int type ) {
        if ((chuTitleHolder != null) && chuTitleHolder.size() > 0) {
            return chuTitleHolder;
        }
        chuTitleHolder = new ArrayList<>();
//        list.add(new TypeHolder(0,"选择课本"));
        if (TYPE_OPPO_RENJIAO) {
            chuTitleHolder.add(new TypeHolder(316,"人教版"));
        }
//        list.add(new TypeHolder(330,"外研版"));
        chuTitleHolder.add(new TypeHolder(331,"北师版"));
        chuTitleHolder.add(new TypeHolder(332,"仁爱版"));
        return  chuTitleHolder ;
    }
    public static List<TypeHolder> geChuType(int type ) {
        if ((chuTypeHolder != null) && chuTypeHolder.size() > 0) {
            return chuTypeHolder;
        }
        chuTypeHolder = new ArrayList<>();
        chuTypeHolder.add(new TypeHolder(333,"冀教版"));
        chuTypeHolder.add(new TypeHolder(334,"译林版"));
        chuTypeHolder.add(new TypeHolder(335,"鲁教版"));
        return  chuTypeHolder ;
    }
    private static List<TypeHolder> chuTitleHolder;
    private static List<TypeHolder> chuTypeHolder;
    private static List<TypeHolder> chuKouHolder;
    public static List<TypeHolder> getChuKouHolder() {
        if ((chuKouHolder != null) && chuKouHolder.size() > 0) {
            return chuKouHolder;
        }
        chuKouHolder = new ArrayList<>();
        chuKouHolder.add(new TypeHolder(316,"人教版"));
        return chuKouHolder;
    }
}
