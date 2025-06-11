package com.iyuba.talkshow.data.model;

import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.constant.ConfigData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/11/21 .
 */

public class Header extends BasicNameValue implements RecyclerItem {
    private int pic;
    private int position;
    private String desc;

    public Header(String name, Object value, int pic, int position, String desc) {
        super(name, value);
        this.pic = pic;
        this.position = position;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getPic() {
        return pic;
    }

    public int getPosition() {
        return position;
    }

    public static Header getNewsHeader() {
//        if (App.APP_CHECK_VIVO) {
//            return new Header("今日推荐", getCategory(1), getPic(0), 2, "精彩推荐不要错过");
//        }
//        return new Header("今日推荐", getCategory(6), getPic(0), 2, "精彩推荐不要错过");

        if (ConfigData.isBlockJuniorEnglish(LibResUtil.getInstance().getContext())) {
            return new Header("精彩视频", getCategory(1), getPic(0), 2, "精彩视频不要错过");
        }
        return new Header("精彩视频", getCategory(6), getPic(0), 2, "精彩视频不要错过");
    }

    public static Header getSeriesHeader() {
        return new Header("小猪佩奇", getCategory(1), getPic(0), 2, "更多");
    }

    public static CategoryFooter getNewsFooter() {
//      modified diao
        return new CategoryFooter(7, 0, getCategory(0));
    }

    public static CategoryFooter getChildNewsFooter() {
        return new CategoryFooter(7, 0, getCategory(6));
    }

    public static Header getChildHeader() {
        return new Header("小学英语", getCategory(6), getPic(2), getHeaderPosition(1), "开启你的二次元世界");
    }

    public static List<Header> getAllHeaders() {
        if (ConfigData.isBlockJuniorEnglish(LibResUtil.getInstance().getContext())) {
            return Arrays.asList(
                    new Header("影视娱乐", getCategory(1), getPic(1), getHeaderPosition(1), "火热经典的电影美剧"),
                    new Header("动漫配音", getCategory(2), getPic(2), getHeaderPosition(2), "开启你的二次元世界"),
                    new Header("生活百科", getCategory(3), getPic(3), getHeaderPosition(3), "点亮智慧生活"),
                    new Header("听歌学习", getCategory(4), getPic(4), getHeaderPosition(4), "欧美流行音乐专区"),
                    new Header("演讲纪录", getCategory(5), getPic(5), getHeaderPosition(5), "精彩的演讲与纪录片")
            );
        }
        return Arrays.asList(
                new Header("影视娱乐", getCategory(1), getPic(1), getHeaderPosition(1), "火热经典的电影美剧"),
                new Header("动漫配音", getCategory(2), getPic(2), getHeaderPosition(2), "开启你的二次元世界"),
                new Header("生活百科", getCategory(3), getPic(3), getHeaderPosition(3), "点亮智慧生活"),
                new Header("听歌学习", getCategory(4), getPic(4), getHeaderPosition(4), "欧美流行音乐专区"),
                new Header("演讲纪录", getCategory(5), getPic(5), getHeaderPosition(5), "精彩的演讲与纪录片"),
                new Header("小学教材", getCategory(6), getPic(6), getHeaderPosition(6), "小学英语"),
                new Header("初中教材", getCategory(7), getPic(7), getHeaderPosition(7), "初中英语")
//                new Header("小猪佩奇专栏", getCategory(6), getPic(6), getHeaderPosition(6), "小猪佩奇的故事")
        );
    }

    public static List<CategoryFooter> getALlFooters() {
        return Arrays.asList(
                new CategoryFooter(getFooterPosition(1), 1, getCategory(1)),
                new CategoryFooter(getFooterPosition(2), 1, getCategory(2)),
                new CategoryFooter(getFooterPosition(3), 1, getCategory(3)),
                new CategoryFooter(getFooterPosition(4), 1, getCategory(4)),
                new CategoryFooter(getFooterPosition(5), 1, getCategory(5)),
                new CategoryFooter(getFooterPosition(6), 1, getCategory(6)),
                new CategoryFooter(getFooterPosition(7), 1, getCategory(7))
        );
    }

    public static String getCategory(int type) {
        switch (type) {
            case 1:
                return "301,302,304,305";
            case 2:
                //这里根据要求，限制加菲猫数据的处理
                if (ConfigData.isOppoCopyrightLimit(LibResUtil.getInstance().getContext())) {
                    return "301";
                } else {
                    return "309";
                }
            case 3:
                return "303,307,308";
            case 4:
                return "310";
            case 5:
                return "306,311";
            case 6:
                return "313,314,315,319,320";
            case 7:
                return "316";
            default:
                //这里根据要求，限制加菲猫数据的处理
                StringBuffer buffer = new StringBuffer("301,302,303,304,305,306,307,308,310,311,312");
                if (!ConfigData.isOppoCopyrightLimit(LibResUtil.getInstance().getContext())) {
                    buffer.append(",309");
                }
                return buffer.toString();
        }
    }

    private static int getPic(int category) {
        switch (category) {
            case 1:
                return R.drawable.ic_category_video;
            case 2:
                return R.drawable.ic_category_cartoon;
            case 3:
                return R.drawable.ic_categoty_life;
            case 4:
                return R.drawable.ic_categoty_music;
            case 5:
                return R.drawable.ic_category_speech;
            case 6:
                return R.drawable.icon_primary;
            default:
                return R.drawable.ic_category_new;
        }
    }

    public static int startIndex = 8;

    public static int getHeaderPosition(int category) {
        return startIndex + (category - 1) * 6;
    }

    public static int getFooterPosition(int category) {
        return getHeaderPosition(category) + 5;
    }


    static List<Integer> posList = new ArrayList<>();

    static {
        posList = new ArrayList<>();
        setPosList();
    }

    private static void setPosList() {
        posList.add(2);
        posList.add(7);
        for (int i = 0; i < 5; i++) {
            posList.add(getHeaderPosition(i + 1));
            posList.add(getFooterPosition(i + 1));
        }
    }

    public static boolean isHeaderOrFooter(int pos) {
        if (pos < 3) {
            return true;
        }
        return posList.contains(pos);
    }

    public static void setStartIndex(int pos) {
        startIndex = pos;
        posList.clear();
        setPosList();
    }


    public static void resetStartIndex(Boolean isVip) {
        if (!isVip) {
            setStartIndex(8);
        } else {
            setStartIndex(8);
        }
    }
}
