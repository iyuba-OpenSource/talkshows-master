package com.iyuba.talkshow.event;

import com.mob.tools.proguard.PublicMemberKeeper;

public class LoginResult {

    private String phone;
    private String nickName;
    private String openId;
    private String userIconUrl;
    private String userIconUr2;
    private String userIconUr3;
    private String email;
    private String operator;
    private LoginResult.LoginType loginType;

    public LoginResult() {
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String var1) {
        this.phone = var1;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String var1) {
        this.nickName = var1;
    }

    public String getOpenId() {
        return this.openId;
    }

    public void setOpenId(String var1) {
        this.openId = var1;
    }

    public String getUserIconUrl() {
        return this.userIconUrl;
    }

    public void setUserIconUrl(String var1) {
        this.userIconUrl = var1;
    }

    public String getUserIconUr2() {
        return this.userIconUr2;
    }

    public void setUserIconUr2(String var1) {
        this.userIconUr2 = var1;
    }

    public String getUserIconUr3() {
        return this.userIconUr3;
    }

    public void setUserIconUr3(String var1) {
        this.userIconUr3 = var1;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String var1) {
        this.email = var1;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String var1) {
        this.operator = var1;
    }

    public LoginResult.LoginType getLoginType() {
        return this.loginType;
    }

    public void setLoginType(LoginResult.LoginType var1) {
        this.loginType = var1;
    }

    public static enum LoginType implements PublicMemberKeeper {
        ONE_KEY,
        SMS;

        private LoginType() {
        }
    }
}
