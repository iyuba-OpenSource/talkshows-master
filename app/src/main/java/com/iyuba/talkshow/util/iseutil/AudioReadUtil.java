package com.iyuba.talkshow.util.iseutil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by iyuba on 2018/1/9.
 */

public class AudioReadUtil {
    public static byte[] readAudioFile( String filename) {
        try {
            InputStream ins = new FileInputStream(filename);
            byte[] data = new byte[ins.available()];

            ins.read(data);
            ins.close();

            return data;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
