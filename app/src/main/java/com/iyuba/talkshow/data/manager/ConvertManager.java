//package com.iyuba.talkshow.data.manager;
//
//import android.content.Context;
//
//import com.iyuba.talkshow.data.model.Convert;
//import com.iyuba.talkshow.util.BaseStorageUtil;
//import com.iyuba.talkshow.util.ConvertAudio;
//import com.iyuba.talkshow.util.StorageUtil;
//
//import java.io.File;
//import java.util.ArrayDeque;
//import java.util.Deque;
//import java.util.Iterator;
//
//import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
//import timber.log.Timber;
//
//public class ConvertManager {
//    private Context mContext;
//    private Deque<Convert> mDeque;
//    private int mStatus = Status.FREE;
//    private int mConvertNum;
//
//    private static class ConvertManagerHolder {
//        static ConvertManager INSTANCE = new ConvertManager();
//    }
//
//    public static ConvertManager getInstance(Context context) {
//        ConvertManager manager = ConvertManagerHolder.INSTANCE;
//        manager.mContext = context;
//        return manager;
//    }
//
//    private ConvertManager() {
//        this.mDeque = new ArrayDeque<>();
//    }
//
//    public void init() {
//        clear();
//        mConvertNum = 0;
//    }
//
//    public void addConvert(Convert convert) {
//        Timber.e("********" + convert.paraId());
//        deleteConvert(convert.paraId());
//        mDeque.add(convert);
//        execute();
//    }
//
//    private void deleteConvert(int paraId) {
//        Iterator<Convert> it = mDeque.iterator();
//        while (it.hasNext()) {
//            Convert tmp = it.next();
//            if(tmp.paraId() == paraId) {
//                it.remove();
//            }
//        }
//    }
//
//    public void clear() {
//        mDeque.clear();
//    }
//
//    public int getConvertNum() {
//        return mConvertNum;
//    }
//
//    private void execute() {
//        if (mStatus == Status.FREE && mDeque.size() != 0) {
//            mStatus = Status.EXECUTE;
//            final Convert convert = mDeque.pop();
//            Timber.e("*******execute" + convert.paraId());
//            ConvertAudio.wavToMp3(mContext,
//                    getParaRecordFile(convert).getAbsolutePath(),
//                    new IConvertCallback() {
//                        @Override
//                        public void onSuccess(File file) {
//                            renameFile(file, convert);
//                            convert.callback().onSuccess(file);
//                            mConvertNum++;
//                            mStatus = Status.FREE;
//                            execute();
//                        }
//
//                        @Override
//                        public void onFailure(Exception e) {
//                            convert.callback().onFailure(e);
//                            mConvertNum++;
//                            mStatus = Status.FREE;
//                            execute();
//                        }
//                    });
//        }
//    }
//
//    private void renameFile(File file, Convert convert) {
//        File destFile = StorageUtil.getParaRecordAacFile(mContext,
//                convert.voaId(), convert.paraId(), convert.timestamp());
//        file.renameTo(destFile);
//    }
//
//    private File getParaRecordFile(Convert convert) {
//        String dir = convert.voaId() + File.separator + convert.timestamp();
//        return BaseStorageUtil.getFile(mContext, dir, convert.filename());
//    }
//
//    interface Status {
//        int FREE = 0;
//        int EXECUTE = 1;
//    }
//}
