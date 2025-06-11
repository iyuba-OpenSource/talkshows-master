package com.iyuba.iyubamovies.network;

import android.util.Log;

import com.google.gson.Gson;
import com.iyuba.iyubamovies.database.ImoviesDatabaseManager;
import com.iyuba.iyubamovies.network.result.ImoviesCommentData;
import com.iyuba.iyubamovies.network.result.ImoviesCommentResult;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import rx.functions.Func1;

/**
 * Created by iyuba on 2017/8/29.
 */

public class CommentReslutToItemsMapper implements Func1<ResponseBody,List<ImoviesCommentData>> {
    private static CommentReslutToItemsMapper mapper = new CommentReslutToItemsMapper();
    public static CommentReslutToItemsMapper getIntence(String usid){
        uid = usid;
        manager = ImoviesDatabaseManager.getInstance();
        return mapper;
    }
    public static int TotalPage = 1;
    public static int Counts = 0;
    private static String uid;
    private static ImoviesDatabaseManager manager;
    @Override
    public List<ImoviesCommentData> call(ResponseBody responseBody) {
        try {
            String body = responseBody.string();
                Gson gson = new Gson();
                ImoviesCommentResult result = gson.fromJson(body,ImoviesCommentResult.class);
                Log.e("Tag-map",body);
                if(result.ResultCode.equals("511")||result.ResultCode.equals("501"))
                {
                    TotalPage = result.TotalPage;
                    Counts = result.Counts;
                    List<ImoviesCommentData> comments = result.data;
                    for(ImoviesCommentData comment:comments){
                        comment.isselectzan = manager.isClickZAN(uid,comment.id);
                        comment.setCommentcounts(result.Counts);
                    }
                    return comments;
                }else {
                    //Log.e("Tag",result.Message);
                    return null;
                }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Tag-excep",e.toString());
            return null;
        }

    }
//    @Override
//    public List<Comment> call(CommentResult commentResult) {
//        Log.e("TAG",commentResult.Message);
//       // List<Comment>comments = commentResult.data;
//        List<Comment>comments = new ArrayList<>();
//        Log.e("TAG--CommentMapper",comments.size()+"");
//        return comments;
//    }
}
