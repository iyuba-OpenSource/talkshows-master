package com.iyuba.lib_common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @desction:
 * @date: 2023/3/2 15:48
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 */
public class LibBigDecimalUtil {

    public static double trans2Double(String doubleStr){
        BigDecimal bigDecimal = new BigDecimal(doubleStr);
        bigDecimal = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static double trans2Double(double doubleStr){
        BigDecimal bigDecimal = new BigDecimal(doubleStr);
        bigDecimal = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static double trans2Double(int scale,double doubleStr){
        BigDecimal bigDecimal = new BigDecimal(doubleStr);
        bigDecimal = bigDecimal.setScale(scale,BigDecimal.ROUND_HALF_DOWN);
        return bigDecimal.doubleValue();
    }
}
