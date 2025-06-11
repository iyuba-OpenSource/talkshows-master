package com.iyuba.talkshow.util;

/**
 * Created by Administrator on 2016/11/21 0021.
 */

public class SqlUtil {
    public static String handleIn(String name, String[] values) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < values.length - 1; i++) {
            sb.append(name).append(" = ").append(values[i]).append(" or ");
        }
        sb.append(name).append(" = ").append(values[values.length - 1]);
        return sb.toString();
    }

    public static String norEqual(String name,int[] bookId){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bookId.length-1; i++) {
            builder.append(name).append(" != ").append(bookId[i]).append(" and ");
        }
        builder.append(name).append(" != ").append(bookId[bookId.length-1]);
        return builder.toString();
    }
}
