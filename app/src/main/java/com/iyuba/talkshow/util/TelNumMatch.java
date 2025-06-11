package com.iyuba.talkshow.util;

/**
 * 用于判断一串数字是否是手机号
 * 
 * @author Administrator
 * 
 */
public class TelNumMatch {
	// 中国移动
	private final static String YD = "^[1]{1}(([3]{1}[4-9]{1})|([4]{1}[7]{1})|([5]{1}[012789]{1})|([7]{1}[8]{1})|([8]{1}[23478]{1}))[0-9]{8}$";
	private final static String YD_NAME = "China Mobile";
	// 中国联通
	private final static String LT = "^[1]{1}(([3]{1}[0-2]{1})|([4]{1}[5]{1})|([5]{1}[56]{1})|([7]{1}[6]{1})|([8]{1}[56]{1}))[0-9]{8}$";
	private final static String LT_NAME = "China Unicom";
	// 中国电信
	private final static String DX = "^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([8]{1}[019]{1})|([7]{1}[7]{1}))[0-9]{8}$";
	private final static String DX_NAME = "China Telecom";

	public static boolean isPhonenumberLegal(String phone) {
		if (phone.length() != 11) {
			return false;
		} else {
			return true;
//			return (phone.matches(YD) || phone.matches(LT) || phone.matches(DX));
		}
	}

	public static String getOperatorName(String phone) {
		String result;
		if (phone.matches(YD)) {
			result = YD_NAME;
		} else if (phone.matches(LT)) {
			result = LT_NAME;
		} else if (phone.matches(DX)) {
			result = DX_NAME;
		} else {
			result = "Unknown";
		}
		return result;
	}
}