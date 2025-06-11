//package com.iyuba.talkshow.util.ise;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log;
//
//import com.iflytek.cloud.EvaluatorListener;
//import com.iflytek.cloud.EvaluatorResult;
//import com.iflytek.cloud.SpeechConstant;
//import com.iflytek.cloud.SpeechError;
//import com.iflytek.cloud.SpeechEvaluator;
//import com.iflytek.ise.result.Result;
//import com.iflytek.ise.result.xml.XmlResultParser;
//import com.iyuba.voaseries.CrashApplication;
//
//
//import com.naman14.androidlame.AndroidLame;
//import com.naman14.androidlame.LameBuilder;
//import com.naman14.androidlame.WaveReader;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//class IseManager {
//    private static final String TAG = IseManager.class.getSimpleName();
//
//    private static IseManager instance = new IseManager();
//
//    private static final String LANGUAGE = "en_us";
//    private static final String ISE_CATEGORY = "read_sentence";
//    private static final String RESULT_LEVEL = "complete";
//    private static final String VAD_BOS = "5000";
//    private static final String VAD_EOS = "1800";
//    private static final String KEY_SPEECH_TIMEOUT = "-1";
//
//    private static final int SAMPLE_RATE = 16000;
//
//    private SpeechEvaluator mSpeechEvaluator;
//    private OnEvaluateListener mEvaluateListener;
//    private EvaluatorListenerImpl defEvaluatorListener;
//
//    private ExecutorService mExecutor;
//    private Handler mDelivery;
//
//    private IseManager() {
//        defEvaluatorListener = new EvaluatorListenerImpl();
//        mSpeechEvaluator = SpeechEvaluator.createEvaluator(CrashApplication.getInstance(), null);
//        mSpeechEvaluator.setParameter(SpeechConstant.LANGUAGE, LANGUAGE);
//        mSpeechEvaluator.setParameter(SpeechConstant.ISE_CATEGORY, ISE_CATEGORY);
//        mSpeechEvaluator.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
//        mSpeechEvaluator.setParameter(SpeechConstant.SAMPLE_RATE, String.valueOf(SAMPLE_RATE));
//        mSpeechEvaluator.setParameter(SpeechConstant.VAD_BOS, VAD_BOS);
//        mSpeechEvaluator.setParameter(SpeechConstant.VAD_EOS, VAD_EOS);
//        mSpeechEvaluator.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, KEY_SPEECH_TIMEOUT);
//        mSpeechEvaluator.setParameter(SpeechConstant.RESULT_LEVEL, RESULT_LEVEL);
//
//        mExecutor = Executors.newFixedThreadPool(1);
//        mDelivery = new Handler(Looper.getMainLooper());
//    }
//
//    public static IseManager getInstance() {
//        return instance;
//    }
//
//    public void setEvaluateListener(OnEvaluateListener l) {
//        mEvaluateListener = l;
//    }
//
//    public void startEvaluate(ReadEvaluateItem item) {
//        item.clearOldFiles();
////        mSpeechEvaluator.setParameter(SpeechConstant.ISE_AUDIO_PATH, item.getRecordPCMPath());
//        defEvaluatorListener.setItem(item);
//        try {
//            mSpeechEvaluator.startEvaluating(item.sentence, null, defEvaluatorListener);
//        } catch (Exception e) {
//            // TODO this must be useless, fuck iflytek
//            SpeechError error = new SpeechError(233, e.getMessage());
//            if (mEvaluateListener != null) mEvaluateListener.onError(error);
//        }
//    }
//
//    public boolean isEvaluating() {
//        return mSpeechEvaluator.isEvaluating();
//    }
//
//    public void stopEvaluating() {
//        mSpeechEvaluator.stopEvaluating();
//    }
//
//    public void cancelEvaluate() {
//        mSpeechEvaluator.cancel();
//    }
//
//    /**
//     * @param item
//     */
//    private void transformPcmToWav(ReadEvaluateItem item) {
//        File wavFile = new File(item.getRecordWavPath());
//        File pcmFile = new File(item.getRecordPCMPath());
//        FileCopyUtil.fileChannelCopy(pcmFile, wavFile);
//        WavWriter myWavWriter;
//        try {
//            myWavWriter = new WavWriter(wavFile, SAMPLE_RATE);
//            myWavWriter.writeHeader();
//            myWavWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void onErrorHappen(final String message) {
//        if (mEvaluateListener != null) {
//            mDelivery.post(new Runnable() {
//                @Override
//                public void run() {
//                    mEvaluateListener.onError(new SpeechError(233, message));
//                }
//            });
//        }
//    }
//
//    private class EvaluatorListenerImpl implements EvaluatorListener {
//
//        ReadEvaluateItem item;
//
//        private XmlResultParser xmlResultParser = new XmlResultParser();
//
//        @Override
//        public void onResult(EvaluatorResult evaluatorResult, boolean isLast) {
//            if (isLast) {
//                final Result result = xmlResultParser.parse(evaluatorResult.getResultString());
//                if (result.is_rejected) {
//                    new File(item.getRecordPCMPath()).delete();
//                    if (mEvaluateListener != null) mEvaluateListener.onResultRejected();
//                } else {
//                    transformPcmToWav(item);
//                    new File(item.getRecordPCMPath()).delete();
//                    mExecutor.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            File wavFile = new File(item.getRecordWavPath());
//                            File mp3File = new File(item.getRecordMp3Path());
//                            BufferedOutputStream bos;
//                            final int CHUNK_SIZE = 8192;
//                            final int OUTPUT_STREAM_BUFFER = 8192;
//                            WaveReader waveReader = new WaveReader(wavFile);
//                            try {
//                                waveReader.openWave();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                                onErrorHappen(e.getMessage());
//                                return;
//                            }
//                            AndroidLame androidLame = new LameBuilder()
//                                    .setInSampleRate(waveReader.getSampleRate())
//                                    .setOutChannels(waveReader.getChannels())
//                                    .setVbrMode(LameBuilder.VbrMode.VBR_DEFAUT)
//                                    .setOutSampleRate(waveReader.getSampleRate())
//                                    .setQuality(5)
//                                    .build();
//                            try {
//                                bos = new BufferedOutputStream(new FileOutputStream(mp3File),
//                                        OUTPUT_STREAM_BUFFER);
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                                onErrorHappen(e.getMessage());
//                                return;
//                            }
//
//                            short[] buffer_l = new short[CHUNK_SIZE];
//                            short[] buffer_r = new short[CHUNK_SIZE];
//                            byte[] mp3Buf = new byte[(int) (7200 + buffer_l.length * 2 * 1.25)];
//                            int bytesRead = 0;
//                            int channels = waveReader.getChannels();
//                            while (true) {
//                                try {
//                                    if (channels == 2) {
//                                        bytesRead = waveReader.read(buffer_l, buffer_r, CHUNK_SIZE);
//                                        if (bytesRead > 0) {
//                                            int bytesEncoded = 0;
//                                            bytesEncoded = androidLame.encode(buffer_l, buffer_r, bytesRead, mp3Buf);
//                                            if (bytesEncoded > 0) {
//                                                try {
//                                                    bos.write(mp3Buf, 0, bytesEncoded);
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        } else break;
//                                    } else {
//                                        bytesRead = waveReader.read(buffer_l, CHUNK_SIZE);
//                                        if (bytesRead > 0) {
//                                            int bytesEncoded = 0;
//                                            bytesEncoded = androidLame.encode(buffer_l, buffer_l, bytesRead, mp3Buf);
//                                            if (bytesEncoded > 0) {
//                                                try {
//                                                    bos.write(mp3Buf, 0, bytesEncoded);
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//
//                                        } else break;
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            int outputMp3buf = androidLame.flush(mp3Buf);
//
//                            if (outputMp3buf > 0) {
//                                try {
//                                    bos.write(mp3Buf, 0, outputMp3buf);
//                                    bos.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            new File(item.getRecordWavPath()).delete();
//                            item.setSendable(true);
//                            mDelivery.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (mEvaluateListener != null) mEvaluateListener.onResultAccepted(result);
//                                }
//                            });
//                        }
//                    });
//
//                }
//            }
//        }
//
//        @Override
//        public void onError(SpeechError error) {
//            if (mEvaluateListener != null) mEvaluateListener.onError(error);
//        }
//
//        @Override
//        public void onBeginOfSpeech() {
//            if (mEvaluateListener != null) mEvaluateListener.onBeginOfSpeech();
//        }
//
//        @Override
//        public void onEndOfSpeech() {
//            if (mEvaluateListener != null) mEvaluateListener.onEndOfSpeech();
//        }
//
//        @Override
//        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//            Log.i(TAG, "eventType : " + eventType + " arg1 : " + arg1 + " arg2 : " + arg2);
//        }
//
//        @Override
//        public void onVolumeChanged(int volume, byte[] arg1) {
//            if (mEvaluateListener != null) mEvaluateListener.onVolumeChanged(volume);
//        }
//
//        public void setItem(ReadEvaluateItem item) {
//            this.item = item;
//        }
//
//    }
//
//    public interface OnEvaluateListener {
//        void onBeginOfSpeech();
//
//        void onEndOfSpeech();
//
//        void onVolumeChanged(int volume);
//
//        void onError(SpeechError error);
//
//        void onResultRejected();
//
//        void onResultAccepted(Result result);
//    }
//}
