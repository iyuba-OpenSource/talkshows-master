package com.iyuba.lib_common.util;

import io.reactivex.disposables.Disposable;

/**
 * @title:
 * @date: 2023/4/27 10:47
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class LibRxUtil {

    //解除绑定
    public static void unDisposable(Disposable dis){
        if (dis!=null&&!dis.isDisposed()){
            dis.dispose();
        }
    }

}
