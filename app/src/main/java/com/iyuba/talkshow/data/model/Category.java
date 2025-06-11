package com.iyuba.talkshow.data.model;

import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.talkshow.constant.ConfigData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/11/19 0019.
 */

public class Category extends BasicNameValue {

    private Category(String name, int value) {
        super(name, value);
    }

    private static List<Category> categories() {
        List<Category> showList = new ArrayList<>();
        showList.add(new Category(Name.QUAN_BU, Value.ALL));
        showList.add(new Category(Name.DIAN_YING, Value.FILM));
        showList.add(new Category(Name.MEI_JU, Value.MEI_JU));
        showList.add(new Category(Name.SHI_SHI, Value.SHI_SHI));
        showList.add(new Category(Name.TI_YU, Value.TI_YU));
        showList.add(new Category(Name.GAO_XIAO, Value.GAO_XIAO));
        showList.add(new Category(Name.JI_LUE, Value.JI_LUE));
        showList.add(new Category(Name.SHEN_HUO, Value.SHEN_HUO));
        showList.add(new Category(Name.KE_JIAO, Value.KE_JIAO));
        showList.add(new Category(Name.DONG_MAN, Value.DONG_MAN));
        showList.add(new Category(Name.TING_GE, Value.TING_GE));
        showList.add(new Category(Name.YAN_JIANG, Value.YAN_JIANG));
        if (!ConfigData.isBlockJuniorEnglish(LibResUtil.getInstance().getContext())){
            showList.add(new Category(Name.XIN_QIDIAN, Value.XIN_QIDIAN));
            showList.add(new Category(Name.PEP, Value.PEP));
            showList.add(new Category(Name.JINGTONG, Value.JINGTONG));
            showList.add(new Category(Name.CHUZHONG, Value.CHUZHONG));
            showList.add(new Category(Name.SAN_QIDIAN, Value.SAN_QIDIAN));
            showList.add(new Category(Name.YI_QIDIAN, Value.YI_QIDIAN));
        }
        return showList;
    }

    /*private final static List<Category> categories = Arrays.asList(
            new Category(Name.QUAN_BU, Value.ALL),
            new Category(Name.DIAN_YING, Value.FILM),
            new Category(Name.MEI_JU, Value.MEI_JU),
            new Category(Name.SHI_SHI, Value.SHI_SHI),
            new Category(Name.TI_YU, Value.TI_YU),
            new Category(Name.GAO_XIAO, Value.GAO_XIAO),
            new Category(Name.JI_LUE, Value.JI_LUE),
            new Category(Name.SHEN_HUO, Value.SHEN_HUO),
            new Category(Name.KE_JIAO, Value.KE_JIAO),
            new Category(Name.DONG_MAN, Value.DONG_MAN),
            new Category(Name.TING_GE, Value.TING_GE),
            new Category(Name.YAN_JIANG, Value.YAN_JIANG),
            new Category(Name.XIN_QIDIAN, Value.XIN_QIDIAN),
            new Category(Name.JINGTONG, Value.JINGTONG),
            new Category(Name.YI_QIDIAN, Value.YI_QIDIAN),
            new Category(Name.SAN_QIDIAN, Value.SAN_QIDIAN),
            new Category(Name.PEP, Value.PEP),
            new Category(Name.CHUZHONG, Value.CHUZHONG)
    );*/

    public static List<Category> getCategories() {
        return categories();
    }

    public static String getName(int key) {
        for (Category category : categories()) {
            if (((int) category.getValue()) == key) {
                return category.getName();
            }
        }
        return "";
    }

    interface Name {
        String QUAN_BU = "全部";
        String DIAN_YING = "电影";
        String MEI_JU = "美剧";
        String SHI_SHI = "时事";
        String TI_YU = "体育";
        String GAO_XIAO = "搞笑";
        String JI_LUE = "纪录片";
        String SHEN_HUO = "生活";
        String KE_JIAO = "科教";
        String DONG_MAN = "动漫";
        String TING_GE = "听歌";
        String YAN_JIANG = "演讲";
        String PEPPA_PIG = "小猪佩奇";
        String XIN_QIDIAN = "新起点";
        String YI_QIDIAN = "一起点";
        String SAN_QIDIAN = "三起点";
        String JINGTONG = "精通";
        String PEP = "PEP";
        String CHUZHONG = "初中";
    }

    public interface Value {
        int ALL = 0;
        int FILM = 301;
        int MEI_JU = 302;
        int SHI_SHI = 303;
        int TI_YU = 304;
        int GAO_XIAO = 305;
        int JI_LUE = 306;
        int SHEN_HUO = 307;
        int KE_JIAO = 308;
        int DONG_MAN = 309;
        int TING_GE = 310;
        int YAN_JIANG = 311;
        int PEPPA_PIG = 312;
        int XIN_QIDIAN = 313;
        int PEP = 314;
        int JINGTONG = 315;
        int CHUZHONG = 316;
        int BEISHI_SANQIDIAN = 317;
        int BEISHI_YIQIDIAN = 318;
        int SAN_QIDIAN = 319;
        int YI_QIDIAN = 320;
    }
}
