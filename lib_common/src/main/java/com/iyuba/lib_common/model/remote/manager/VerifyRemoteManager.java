package com.iyuba.lib_common.model.remote.manager;

import com.iyuba.lib_common.model.remote.RemoteHelper;
import com.iyuba.lib_common.model.remote.bean.App_check;
import com.iyuba.lib_common.model.remote.service.VerifyService;
import com.iyuba.lib_common.util.LibHelpUtil;
import com.iyuba.lib_common.util.LibResUtil;

import io.reactivex.Observable;

/**
 * @title:
 * @date: 2024/1/5 16:58
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class VerifyRemoteManager {

    //接口-获取审核信息
    public static Observable<App_check> getVerifyData(int verifyId){
        String version = LibHelpUtil.getAppVersion(LibResUtil.getInstance().getContext(), LibResUtil.getInstance().getContext().getPackageName());
        if (LibHelpUtil.isBelongToOppoPhone()){
            version = "oppo_"+version;
        }

        VerifyService verifyService = RemoteHelper.getInstance().createJson(VerifyService.class);
        return verifyService.verify(verifyId,version);
    }
}
