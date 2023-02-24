package us.zoom.sdksample.inmeetingfunction.customizedmeetingui.rawdata;


import android.content.Context;
import android.media.AudioFormat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import us.zoom.sdk.IZoomSDKAudioRawDataDelegate;
import us.zoom.sdk.IZoomSDKAudioRawDataHelper;
import us.zoom.sdk.IZoomSDKAudioRawDataSender;
import us.zoom.sdk.IZoomSDKVirtualAudioMicEvent;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKAudioRawData;
import us.zoom.sdk.ZoomSDKAudioRawDataHelper;
import us.zoom.sdksample.ui.AudioCapManager;
import us.zoom.sdksample.ui.AudioManager;

public class AudioRawDataUtil {

    static final String TAG = "AudioRawDataUtil";

    private Map<Integer, FileChannel> map = new HashMap<>();

    private Context mContext;

    IZoomSDKAudioRawDataHelper audioRawDataHelper;

    private AudioManager mAudioRecorder;

//    private AudioCapManager mAudioRecorder;

    public AudioRawDataUtil(Context context) {
        mContext = context.getApplicationContext();
        audioRawDataHelper = new ZoomSDKAudioRawDataHelper();
        audioRawDataHelper.setExternalAudioSource(new IZoomSDKVirtualAudioMicEvent() {
            @Override
            public void onMicInitialize(IZoomSDKAudioRawDataSender iZoomSDKAudioRawDataSender) {
//                iZoomSDKAudioRawDataSender.send()
                Log.d(TAG, "On virtual mic initialize");
                startRecording(iZoomSDKAudioRawDataSender);
            }

            @Override
            public void onMicStartSend() {
                Log.d(TAG, "On virtual mic start send");
            }

            @Override
            public void onMicStopSend() {
                Log.d(TAG, "On virtual mic stop send");
            }

            @Override
            public void onMicUninitialized() {
                Log.d(TAG, "On virtual mic uninitialized");
                if (mAudioRecorder != null) {
                    mAudioRecorder.stopRecord();
                }
            }
        });
    }

    private void startRecording(IZoomSDKAudioRawDataSender sender) {
//        if (mAudioRecorder == null) {
//            mAudioRecorder = new AudioCapManager();
//        }
        //AudioFormat.CHANNEL_IN_STEREO双声道
        //AudioFormat.CHANNEL_IN_MONO单声道
        int bufferSize = 48000;
        AudioManager.getInstance().startRecord(AudioFormat.CHANNEL_IN_MONO, bufferSize,
                new AudioManager.AudioRecordCallback() {
                    @Override
                    public void onFrameDataIn(byte[] data) {
                        //data就是录制的识别数据
                        Log.d(TAG, "data.size:" + data.length);
                        sender.send(ByteBuffer.wrap(data), data.length, 48000);
                    }
                });
//        int sampleRate = 48000;
//        int iRet = mAudioRecorder.startRecord(sampleRate, (buffer, isMute) -> {
//                    Log.i(TAG, "get pcm date from auidorecord, len:" + buffer.length + "  isMute: " + isMute);
//                    sender.send(ByteBuffer.wrap(buffer), buffer.length, 48000);
//                }
//        );
//        Log.i(TAG, "start record rlt is:" + iRet);
    }

    private FileChannel createFileChannel(int userId) {
        String fileName = "/sdcard/Android/data/" + mContext.getPackageName() + "/files/" + userId + ".pcm";
        File file = new File(fileName);
        try {
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            FileChannel fileChannel = fileOutputStream.getChannel();
            return fileChannel;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private IZoomSDKAudioRawDataDelegate dataDelegate = new IZoomSDKAudioRawDataDelegate() {
        @Override
        public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData rawData) {
            Log.d(TAG, "onMixedAudioRawDataReceived:" + rawData.getBufferLen());
            saveAudioRawData(rawData, 0);

        }

        public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData rawData, int userId) {
            Log.d(TAG, "onOneWayAudioRawDataReceived:" + rawData.getBufferLen() + " userId=" + userId);
            saveAudioRawData(rawData, userId);
        }
    };

    private void saveAudioRawData(ZoomSDKAudioRawData rawData, int userId) {
        try {
            Log.d(TAG, "onMixedAudioRawDataReceived:" + rawData.getBufferLen());
            FileChannel fileChannel = map.get(userId);
            if (null == fileChannel) {
                fileChannel = createFileChannel(userId);
                map.put(userId, fileChannel);
            }
            if (null != fileChannel) {
                fileChannel.write(rawData.getBuffer(), rawData.getBufferLen());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void subscribeAudio() {
        audioRawDataHelper.subscribe(dataDelegate);
    }

    public void unSubscribe() {

        for (FileChannel fileChannel : map.values()) {
            if (null != fileChannel) {
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        audioRawDataHelper.unSubscribe();
    }
}
