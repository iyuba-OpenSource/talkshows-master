package com.iyuba.lib_common.util;

import android.text.TextUtils;

import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.data.UrlLibrary;
import com.iyuba.lib_common.manager.NetHostManager;

/**
 * @title: 链接拼接工具类
 * @date: 2023/12/28 17:22
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class LibUrlSpellUtil {

    /***********图片拼接***************/
    //用户信息接口的头像的拼接(头像以20001接口为准，其他接口可能不准)
    //http://static1.iyuba.cn/uc_server
    public static String fixUserPicUrl(int userId){
//        return "http://static1."+ NetHostManager.getInstance().getDomainShort() +"/uc_server/"+picSuffix;
        return "http://api."+NetHostManager.getInstance().getDomainLong()+"/v2/api.iyuba?protocol=10005&uid="+userId+"&size=middle&timestamp="+System.currentTimeMillis();
    }

    //新概念单词的图片拼接
    //http://static2.iyuba.cn/images/words/287/25/8.jpg
    public static String fixConceptWordPicUrl(String suffix){
        String preFix = "http://static2."+NetHostManager.getInstance().getDomainShort()+"/images/words/";
        return preFix+suffix;
    }

    //中小学英语的点读的图片拼接
    //http://staticvip.iyuba.cn/images/voa//202110/313001_1.jpg
    public static String fixJuniorImagePicUrl(String suffix){
        String prefix = "http://staticvip"+NetHostManager.getInstance().getDomainShort()+"/images/voa";
        return prefix+suffix;
    }

    //小说的音频和图片的拼接--和音频的一样
    //会员：http://staticvip2.iyuba.cn/+字段返回的内容（例如：http://static2.iyuba.cn/bookworm/images/1_1.jpg）
    //非会员：http://static2.iyuba.cn/+字段返回的内容（例如：http://staticvip2.iyuba.cn/bookworm/images/1_1.jpg）
    public static String fixNovelPicUrl(String suffix,boolean isVip){
        String prefix = "http://static2."+NetHostManager.getInstance().getDomainShort();
        if (isVip){
            prefix = "http://staticvip2."+NetHostManager.getInstance().getDomainShort();
        }

        if (suffix.startsWith("/")){
            return prefix+suffix;
        }
        return prefix+"/"+suffix;
    }

    /**
     * 单词的图片拼接
     * //http://static2.iyuba.cn/images/words/287/25/8.jpg
     * 接口数据为：289/25/1.jpg
     * 适用的类型：所有的类型
     * 使用的模块：单词模块
     */
    public static String fixWordPicUrl(String suffix){
        String preFix = "http://static2."+NetHostManager.getInstance().getDomainShort()+"/images/words";
        if (!suffix.startsWith("/")){
            suffix = "/"+suffix;
        }
        return preFix+suffix;
    }

    /*************音频拼接*************/
    /****小说****/
    //音频文件的拼接
    //会员：http://staticvip2.iyuba.cn/+字段返回的内容（例如：http://static2.iyuba.cn/bookworm/images/1_1.jpg）
    //非会员：http://static2.iyuba.cn/+字段返回的内容（例如：http://staticvip2.iyuba.cn/bookworm/images/1_1.jpg）
    public static String fixNovelAudioUrl(String urlSuffix){
        if (urlSuffix.startsWith("/")){
            return "http://staticvip2."+NetHostManager.getInstance().getDomainShort()+urlSuffix;
        }

        return "http://staticvip2."+NetHostManager.getInstance().getDomainShort()+"/"+urlSuffix;
    }

    /****新概念****/
    //音频播放地址--美音
    //规则:http://static2.iyuba.cn/newconcept/bookid_voaid%1000.mp3 //bookid_voaid对1000取余数.mp3
    public static String fixConceptUSPlayUrl(String bookId,String voaId){
        if (TextUtils.isEmpty(bookId)||TextUtils.isEmpty(voaId)){
            return null;
        }

        int play1 = Integer.parseInt(voaId)%1000;
        String playSuffix = bookId+"_"+play1+".mp3";
        return UrlLibrary.HTTP_STATIC2+ NetHostManager.getInstance().getDomainShort()+"/newconcept/"+playSuffix;
    }

    //音频播放地址--英音
    //规则:http://static2.iyuba.cn/newconcept/british/bookid/bookid_(voaid/10)%1000.mp3//bookid_voaid除以10后对1000取余数.mp3
    public static String fixConceptUKPlayUrl(String bookId,String voaId){
        if (TextUtils.isEmpty(bookId)||TextUtils.isEmpty(voaId)){
            return null;
        }

        int play1 = Integer.parseInt(voaId)/10;
        play1 = play1%1000;
        String playSuffix = bookId+"_"+play1;
        return UrlLibrary.HTTP_STATIC2+NetHostManager.getInstance().getDomainShort()+"/newconcept/british/"+bookId+"/"+playSuffix+".mp3";
    }

    //音频播放地址--青少版
    //规则：
    public static String fixConceptJuniorPlayUrl(String voaId){
        if (TextUtils.isEmpty(voaId)){
            return null;
        }

        return "http://staticvip."+NetHostManager.getInstance().getDomainShort()+"/sounds/voa/sentence/202005/"+voaId+"/"+voaId+ TypeLibrary.FileType.MP3;
    }

    /***中小学****/
    //获取课程播放的音频地址
    //这里需要使用的地址：http://staticvip.iyuba.cn/sounds/voa/202002/313002.mp3
    //需要处理成的地址：http://staticvip.iyuba.cn/sounds/voa/sentence/202002/313003/313003.mp3
    public static String fixJuniorAudioUrl(String voaId,String suffix){
        if (TextUtils.isEmpty(suffix)){
            return null;
        }

        //将完整的音频url中截取相应的数据
        String patternText = "/sounds/voa/";
        int index = suffix.indexOf(patternText);

        //取出后面需要使用的数据
        int fileIndex = suffix.lastIndexOf(".");

        //第一段数据
        //这里的数据：http://staticvip.iyuba.cn/sounds/voa/
        String prefixStr = suffix.substring(0,index+patternText.length());

        //第二段数据
        //这里的数据：/202002/313001
        String suffixStr = suffix.substring(index+patternText.length()-1,fileIndex);

        //合并数据
        return prefixStr+"sentence"+suffixStr+"/"+voaId+ TypeLibrary.FileType.MP3;
    }

    /****评测****/
    /**
     * 评测的音频播放地址--中小学类型
     * http://userspeech.iyuba.cn/voa/wav8/202303/concept/20230302/16777456102709654.mp3
     * 接口数据为：wav8/202303/concept/20230302/16777456102709654.mp3
     * 适用于类型：中小学类型
     * 适用于模块：句子评测或者单词评测
     */
    public static String fixJuniorEvalAudioUrl(String suffix){
        String playPrefix = UrlLibrary.HTTP_USERSPEECH+NetHostManager.getInstance().getDomainShort()+"/voa";
        if (!suffix.startsWith("/")){
            suffix = "/"+suffix;
        }
        return playPrefix+suffix;
    }

    /**
     * 评测的音频播放地址--小说类型
     * http://iuserspeech.iyuba.cn:9001/voa/wav8/202306/bookworm/20230612/16865748809653924.mp3
     * 接口数据为：wav6/202307/bookworm/20230706/16886219972130768.mp3
     * 适用于类型：小说
     * 适用于模块：句子评测
     */
    public static String fixNovelEvalAudioUrl(String suffix){
        String playPrefix = UrlLibrary.HTTP_IUSERSPEECH+NetHostManager.getInstance().getDomainShort()+UrlLibrary.SUFFIX_9001+"/voa";
        if (!suffix.startsWith("/")){
            suffix = "/"+suffix;
        }
        return playPrefix+suffix;
    }

    /***************视频拼接****************/
    //中小学配音的视频地址
    //http://static0.iyuba.cn/video/voa/313/313002.mp4
    public static String fixJuniorVideoUrl(String suffix,boolean isVip){
        String prefix = "http://static0."+NetHostManager.getInstance().getDomainShort();
        if (isVip){
            prefix = "http://staticvip."+NetHostManager.getInstance().getDomainShort();
        }

        return prefix+suffix;
    }
}
