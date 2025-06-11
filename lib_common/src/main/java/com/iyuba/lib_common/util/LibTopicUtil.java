package com.iyuba.lib_common.util;

import android.text.TextUtils;

import com.iyuba.lib_common.data.TypeLibrary;

/**
 * @title: 类型参数工具类
 * @date: 2023/12/28 17:23
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class LibTopicUtil {

    //根据书籍类型返回书籍类型名称
    public static String getBookTypeStr(String bookType){
        switch (bookType){
            case TypeLibrary.BookType.bookworm:
                return "牛津书虫英语";
            case TypeLibrary.BookType.newCamstory:
                return "剑桥英语小说馆";
            case TypeLibrary.BookType.newCamstoryColor:
                return "剑桥英语小说馆彩绘";
            case TypeLibrary.BookType.conceptFourUS:
                return "新概念英语全四册(美音)";
            case TypeLibrary.BookType.conceptFourUK:
                return "新概念英语全四册(英音)";
            case TypeLibrary.BookType.conceptJunior:
                return "新概念英语青少版";
            case TypeLibrary.BookType.conceptFour:
                return "新概念英语全四册";
            case TypeLibrary.BookType.junior_primary:
                return "小学英语";
            case TypeLibrary.BookType.junior_middle:
                return "初中英语";
        }
        return "专业书籍";
    }

    //根据书籍类型显示topic
    public static String getTopic(String bookType){
        if (TextUtils.isEmpty(bookType)){
            return "";
        }

        switch (bookType){
            //新概念系列
            case TypeLibrary.BookType.conceptFour:
            case TypeLibrary.BookType.conceptFourUS:
            case TypeLibrary.BookType.conceptFourUK:
            case TypeLibrary.BookType.conceptJunior:
                return "concept";
            //小学英语
            case TypeLibrary.BookType.junior_primary:
                return "primaryenglish";
            //初中英语
            case TypeLibrary.BookType.junior_middle:
                return "juniorenglish";
            //书虫
            case TypeLibrary.BookType.bookworm:
                return "bookworm";
            //剑桥小说馆
            case TypeLibrary.BookType.newCamstory:
                return "newCamstory";
            //剑桥小说馆彩绘
            case TypeLibrary.BookType.newCamstoryColor:
                return "newCamstoryColor";
        }
        return bookType;
    }

    //根据书籍类型显示appId
    public static int getAppId(String bookType){
        if (TextUtils.isEmpty(bookType)){
            return 0;
        }

        switch (bookType){
            //新概念系列
            case TypeLibrary.BookType.conceptFour:
            case TypeLibrary.BookType.conceptFourUS:
            case TypeLibrary.BookType.conceptFourUK:
            case TypeLibrary.BookType.conceptJunior:
                return 222;
            //小学英语
            case TypeLibrary.BookType.junior_primary:
                return 260;
            //初中英语
            case TypeLibrary.BookType.junior_middle:
                return 259;
            //书虫
            case TypeLibrary.BookType.bookworm:
                return 285;
            case TypeLibrary.BookType.newCamstory://剑桥小说馆
            case TypeLibrary.BookType.newCamstoryColor://剑桥小说馆彩绘
                return 227;
        }
        return 0;
    }

    //根据书籍类型显示文章收藏的topic
    public static String getLessonCollectTopic(String bookType){
        if (TextUtils.isEmpty(bookType)){
            return "";
        }

        switch (bookType){
            //新概念系列
            case TypeLibrary.BookType.conceptFour:
            case TypeLibrary.BookType.conceptFourUS:
            case TypeLibrary.BookType.conceptFourUK:
            case TypeLibrary.BookType.conceptJunior:
                return "concept";
            //小学英语
            case TypeLibrary.BookType.junior_primary:
                return "primary";
            //初中英语
            case TypeLibrary.BookType.junior_middle:
                return "junior";
            //书虫
            case TypeLibrary.BookType.bookworm:
                return "bookworm";
            //剑桥小说馆
            case TypeLibrary.BookType.newCamstory:
                return "newCamstory";
            //剑桥小说馆彩绘
            case TypeLibrary.BookType.newCamstoryColor:
                return "newCamstoryColor";
        }
        return bookType;
    }
}
