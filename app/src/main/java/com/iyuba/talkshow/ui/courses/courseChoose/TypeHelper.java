package com.iyuba.talkshow.ui.courses.courseChoose;

import com.iyuba.talkshow.data.manager.AbilityControlManager;
import com.iyuba.talkshow.ui.courses.common.TypeHolder;

import java.util.ArrayList;
import java.util.List;

public class TypeHelper {
    public static final int DEFAULT_TYPE =  0 ;
    public static final int TYPE_PRIMARY_RENJIAO  = 1;
    public static final int TYPE_JUNIOR  = 2;
    public static List<TypeHolder> generateTypeHolders(int type ) {
        List<TypeHolder> list = new ArrayList<>();

        //这里根据人教版限制进行判断
        if (!AbilityControlManager.getInstance().isLimitPep()){
            list.add(new TypeHolder(0,"人教版"));
//        list.add(new TypeHolder(0,""));
            list.add(new TypeHolder(313,"新起点"));
            list.add(new TypeHolder(314,"PEP"));
            list.add(new TypeHolder(315,"精通"));
        }

        if (type!=TYPE_PRIMARY_RENJIAO){
//            list.add(new TypeHolder(316,"初中"));
//            list.add(new TypeHolder(0,"外研社"));
//            list.add(new TypeHolder(318,"一起点"));
//            list.add(new TypeHolder(317,"三起点"));
            list.add(new TypeHolder(0,"北师版"));
            list.add(new TypeHolder(320,"一起点"));
            list.add(new TypeHolder(319,"三起点"));
        }

        //这里根据接口进行审核处理
        if (!AbilityControlManager.getInstance().isLimitPep()){
            list.add(new TypeHolder(0,"初中"));
            list.add(new TypeHolder(316,"人教版"));
            if (type == TYPE_JUNIOR){
                list.clear();
                list.add(new TypeHolder(0,"人教版"));
                list.add(new TypeHolder(316,"初中"));
            }
        }
        return  list ;
    }
}
