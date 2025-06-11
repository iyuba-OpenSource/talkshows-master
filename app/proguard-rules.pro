# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-ignorewarnings
-libraryjars libs/youdaosdk_4.2.10.aar
-keep class com.youdao.sdk { *;}
-libraryjars libs/msa_mdid_1.0.13.aar
-keep class com.bun.miitmdid.core.** { *;}
-keep class cn.sharesdk.**{*;}
-keep class com.mob.**{*;}
-dontwarn cn.sharesdk.**
-dontwarn com.mob.**

-libraryjars libs/alipaysdk-15.8.00.201112210139.aar

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# banner 的混淆代码
-keep class com.youth.banner.Banner {
    *;
 }

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.iyuba.talkshow.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}