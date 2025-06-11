package com.iyuba.iyubamovies.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by iyuba on 2017/11/30.
 */

public class ImoviesGetDeviceInfo {

        //	private String DeviceType;
//	private String DeviceIP;
//	private String DeviceMAC;
        private Context mContext;

        public ImoviesGetDeviceInfo(Context context) {
            this.mContext = context;
        }

        /**
         * <uses-permission android:name="android.permission.INTERNET"></uses-permission>
         * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
         *
         * @return
         */
        public String getLocalIPAddress() {
            try {
                for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface.getNetworkInterfaces(); mEnumeration.hasMoreElements(); ) {
                    NetworkInterface intf = mEnumeration.nextElement();
                    for (Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIPAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            } catch (SocketException ex) {
                Log.e("Error", ex.toString());
            }
            return null;
        }

        public String getLocalMACAddress() {
            WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

            WifiInfo info = wifi.getConnectionInfo();

            return info.getMacAddress();
        }

        public String getLocalDeviceType() {

            return android.os.Build.MODEL;
        }

        public String getCurrentTime() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String strCurrTime = formatter.format(curDate);
            return strCurrTime;

    }
}
