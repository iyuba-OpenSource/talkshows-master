package com.iyuba.iyubamovies.network.result;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by iyuba on 2017/8/29.
 */

public class ImoviesCommentData {
    public String ImgSrc;
    public int backId;
    public String UserName;
    public String stat;
    public String Title;
    public String ShuoShuoType;
    public String ShuoShuo;
    public String CreateDate;
    public String againstCount;
    public String Userid;
    public String agreeCount;
    public int TopicId;
    public String id;
    public String vip;
    public boolean isshowmore=false;
    public boolean isselectzan = false;
    public JsonElement backList;
    private int commentcounts = 0;
    private List<ImoviesAplyCommentData> jxblist;


    public void setCommentcounts(int commentcounts) {
        this.commentcounts = commentcounts;
    }

    public int getCommentcounts() {
        return commentcounts;
    }

    public List<ImoviesAplyCommentData> getJxblist() {
        String bstr = backList.toString();
        if(bstr!=null&&!"\"\"".equals(bstr))
        {   jxblist = new ArrayList<>();
            Gson gson = new Gson();
            Type type = new TypeToken<List<ImoviesAplyCommentData>>() {}.getType();
            Log.e("tag--getjxblist","获取gson");
            jxblist = gson.fromJson(bstr,type);
            Log.e("tag--getjxblist",jxblist.size()+"");
            Collections.reverse(jxblist);
            return jxblist;
        }
        return null;
    }
}
