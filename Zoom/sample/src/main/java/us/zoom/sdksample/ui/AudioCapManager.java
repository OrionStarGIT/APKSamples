package us.zoom.sdksample.ui;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;

public class AudioCapManager {
    private static final String TAG = AudioCapManager.class.getSimpleName();
    //音频相关成员
    private AudioRecord mAudioRecorder = null;
    private final ReentrantLock mRecLocker = new ReentrantLock();
    private AcousticEchoCanceler mAecer = null;
    private final boolean mIsUseAec = false;
    private boolean mThreadRun = false;
    private Thread mProducerThread;
    private int mBufferSize;
    private int mOneSecondBytes;//1秒钟的数据量
    private boolean mIsDoubleChannel = true;

    private AudioRecordCallback mCallback;

    public interface AudioRecordCallback {
        public void onFrameDataIn(byte[] data, boolean isMute);
    }

    public int startRecord(int sampleRate, AudioRecordCallback callback) {
        return startRecord(sampleRate, true, callback);
    }

    private int startRecord(int sampleRate, boolean needStartThread, AudioRecordCallback callback) {
        Log.i(TAG, "Init Recording");

        TEST_FILE_NAME = "/mnt/sdcard/AAA_" + System.currentTimeMillis() + ".pcm";

        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        if (mIsDoubleChannel) {
            //channelConfig = AudioFormat.CHANNEL_IN_FRONT | AudioFormat.CHANNEL_IN_BACK;
            channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        }
        mCallback = callback;
        mOneSecondBytes = sampleRate * 16 * 1 / 8;
        int audioSource = MediaRecorder.AudioSource.MIC;
        //audioSource = MediaRecorder.AudioSource.REMOTE_SUBMIX;
        // get the minimum buffer size that can be used
        int minRecBufSize = AudioRecord.getMinBufferSize(
                sampleRate, channelConfig,
                AudioFormat.ENCODING_PCM_16BIT);

        // double size to be more safe
        int recBufSize = minRecBufSize * 3;
        mBufferSize = minRecBufSize;
        Log.i(TAG, "min buffer size:" + minRecBufSize);

        try {
            if (mAecer != null) {
                mAecer.release();
                mAecer = null;
            }
        } catch (Exception ex) {
            Log.e("AudioCapManager", "audio aecer excep:" + ex.getMessage());
        }

        // release the object
        if (mAudioRecorder != null) {
            mAudioRecorder.release();
            mAudioRecorder = null;
        }

        try {
            audioSource = MediaRecorder.AudioSource.MIC;
            //audioSource = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
            mAudioRecorder = new AudioRecord(
                    audioSource,
                    sampleRate,
                    channelConfig,
                    AudioFormat.ENCODING_PCM_16BIT,
                    recBufSize);
        } catch (Exception ex) {
            Log.e("AudioCapManager", "open audio recore failed:" + ex.getMessage());
            return -1;
        }

        try {
            mAudioRecorder.startRecording();
        } catch (Exception ex) {
            Log.e("AudioCapManager", "start recore failed:" + ex.getMessage());
            return -1;
        }

        Log.i(TAG, "start recore status:" + mAudioRecorder.getState() + "," + mAudioRecorder.getRecordingState());
        if (mAudioRecorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            Log.e("AudioCapManager", "start recore status err");
            return -1;
        }

        if (needStartThread) {
            mThreadRun = true;
            mProducerThread = new Thread(mRunnableRecorder, "AudioProducerThread");
            mProducerThread.start();
        }

        return 0;
    }

    public int stopRecord() {
        Log.i(TAG, "Stop Record");
        mThreadRun = false;
        mRecLocker.lock();
        try {
            if (mAudioRecorder != null && mAudioRecorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                mAudioRecorder.stop();
            }

            if (mAudioRecorder != null) {
                mAudioRecorder.release();
                mAudioRecorder = null;
            }

            if (mProducerThread != null) {
                mProducerThread.join(50);
            }
            mProducerThread = null;

            if (mAecer != null) {
                mAecer.release();
                mAecer = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            mRecLocker.unlock();
        }

        Log.i(TAG, "Stop Record end");
        return 0;
    }

    public String TEST_FILE_NAME = "/mnt/sdcard/AAA_audio.pcm";

    private void saveData(byte[] bytes, int len) {
        try {
            OutputStream os = new FileOutputStream(TEST_FILE_NAME, true);
            os.write(bytes, 0, len);
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Runnable mRunnableRecorder = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "===== Audio Recorder Start ===== ");

            byte[] tempBufRec = new byte[mBufferSize];
            byte[] muteBufRec = new byte[mBufferSize];
            byte[] bfOutLeft = null;
            byte[] bfOutRight = null;
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

            boolean bNeedEmptyPkg = true;
            long tmLastRealSend = System.currentTimeMillis();
            long tmLastMuteSend = System.currentTimeMillis();
            long peroid = (long) (mOneSecondBytes / mBufferSize);
            while (mThreadRun && mAudioRecorder != null) {
                try {
                    Thread.sleep(1);

                    int readBytes = mAudioRecorder.read(tempBufRec, 0, mBufferSize);
                    if (readBytes == mBufferSize) {
                        saveData(tempBufRec, mBufferSize);
                        if (mCallback != null) {
                            tmLastRealSend = System.currentTimeMillis();
                            bNeedEmptyPkg = false;
                            //mCallback.onFrameDataIn(tempBufRec, false);
                            if (bfOutLeft == null) {
                                bfOutLeft = new byte[mIsDoubleChannel ? mBufferSize / 2 : mBufferSize];
                                bfOutRight = new byte[mIsDoubleChannel ? mBufferSize / 2 : mBufferSize];
                            }
                            if (mIsDoubleChannel) {
                                deinterleaveData(tempBufRec, bfOutLeft, bfOutRight, mBufferSize);
                            } else {
                                System.arraycopy(tempBufRec, 0, bfOutLeft, 0, mBufferSize);
                            }
                            mCallback.onFrameDataIn(tempBufRec, false);
                        }
                    } else {
                        long currTime = System.currentTimeMillis();
                        if (bNeedEmptyPkg && currTime - tmLastRealSend > 1 * 1000) //超过1秒都没有采集到数据
                        {
                            if ((currTime - tmLastMuteSend) > peroid) {
                                tmLastMuteSend = currTime;
                                if (mCallback != null) {
                                    //MLog.i("will send empty data");
                                    mCallback.onFrameDataIn(muteBufRec, true);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Record Audio try failed: " + e.getMessage());
                }
            }

            Log.i(TAG, "===== Audio Recorder Stop ===== ");
        }
    };

    private void deinterleaveData(byte[] src, byte[] leftdest, byte[] rightdest, int len) {
        int rIndex = 0;
        int lIndex = 0;
        for (int i = 0; i < len; ) {
            leftdest[rIndex] = src[i];
            leftdest[rIndex + 1] = src[i + 1];
            rIndex = rIndex + 2;

            rightdest[lIndex] = src[i + 2];
            rightdest[lIndex + 1] = src[i + 3];
            lIndex = lIndex + 2;

            i = i + 4;
        }
    }
}