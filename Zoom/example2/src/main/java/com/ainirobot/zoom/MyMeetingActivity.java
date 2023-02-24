package com.ainirobot.zoom;

import us.zoom.sdk.ChatMessageDeleteType;
import us.zoom.sdk.FreeMeetingNeedUpgradeType;
import us.zoom.sdk.IZoomSDKAudioRawDataDelegate;
import us.zoom.sdk.IZoomSDKAudioRawDataHelper;
import us.zoom.sdk.IZoomSDKAudioRawDataSender;
import us.zoom.sdk.IZoomSDKVirtualAudioMicEvent;
import us.zoom.sdk.InMeetingAudioController;
import us.zoom.sdk.InMeetingChatController;
import us.zoom.sdk.InMeetingChatMessage;
import us.zoom.sdk.InMeetingEventHandler;
import us.zoom.sdk.InMeetingService;
import us.zoom.sdk.InMeetingServiceListener;
import us.zoom.sdk.MeetingActivity;
import us.zoom.sdk.MeetingParameter;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.VideoQuality;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKAudioRawData;
import us.zoom.sdk.ZoomSDKAudioRawDataHelper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioFormat;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import java.nio.ByteBuffer;
import java.util.List;

public class MyMeetingActivity extends MeetingActivity {

	private ImageView imageLeaveZoomMeeting;
//	private Button btnSwitchToNextCamera;
	private ImageView imageVideo;
	private ImageView imageAudio;
//	private Button btnParticipants;
//	private Button btnShare;
//	private Button btnStopShare;
//	private Button btnMoreOptions;

	private View viewTabMeeting;
//	private Button btnTabWelcome;
//	private Button btnTabMeeting;
//	private Button btnTabPage2;
	private InMeetingService inMeetingService;
	private InMeetingAudioController inMeetingAudioController;
	private IZoomSDKAudioRawDataHelper audioRawDataHelper;
	private AudioManager mAudioRecorder;


	public final static int REQUEST_CAMERA_CODE = 1010;
	public final static int REQUEST_AUDIO_CODE = 1011;

	private boolean OPEN = false;

	@Override
	protected int getLayout() {
		return R.layout.my_meeting_layout;
	}

	@Override
	protected boolean isAlwaysFullScreen() {
		return false;
	}

	@Override
	protected boolean isSensorOrientationEnabled() {
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!ZoomSDK.getInstance().isInitialized()) {
			finish();
			return;
		}

		requestPermission();

		inMeetingService = ZoomSDK.getInstance().getInMeetingService();
		inMeetingAudioController= inMeetingService.getInMeetingAudioController();
		audioRawDataHelper = new ZoomSDKAudioRawDataHelper();
		initListeners();
		disableFullScreenMode();

//		setupTabs();

		imageLeaveZoomMeeting = (ImageView) findViewById(R.id.imageLeaveZoomMeeting);
//		btnSwitchToNextCamera = (Button)findViewById(R.id.btnSwitchToNextCamera);
		imageVideo = (ImageView)findViewById(R.id.imageVideo);
		imageAudio = (ImageView)findViewById(R.id.imageAudio);
//		btnParticipants = (Button)findViewById(R.id.btnParticipants);
//		btnShare = (Button)findViewById(R.id.btnShare);
//		btnStopShare = (Button)findViewById(R.id.btnStopShare);
//		btnMoreOptions = (Button)findViewById(R.id.btnMoreOptions);

		imageLeaveZoomMeeting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AudioManager.getInstance().stopRecord();
				inMeetingService.leaveCurrentMeeting(true);
			}
		});

//		btnSwitchToNextCamera.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				inMeetingService.getInMeetingVideoController().switchToNextCamera();
//			}
//		});

		imageVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (inMeetingService.getInMeetingVideoController().isMyVideoMuted()) {
					inMeetingService.getInMeetingVideoController().muteMyVideo(false);
					imageVideo.setImageResource(R.drawable.mini_zoom_btn_camera_on);
				} else {
					inMeetingService.getInMeetingVideoController().muteMyVideo(true);
					imageVideo.setImageResource(R.drawable.mini_zoom_btn_camera_off);
				}
			}
		});

		imageAudio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchAudio();
			}
		});

//		btnParticipants.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				inMeetingService.showZoomParticipantsUI(MyMeetingActivity.this, 1100);
//			}
//		});

//		btnShare.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//			}
//		});

//		btnStopShare.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				stopShare();
//			}
//		});

//		btnMoreOptions.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//			}
//		});
	}

	private void updateAudioUi() {
		switchAudio();
		if (SelectorImageView.isChecked) {
			imageAudio.setImageResource(R.drawable.mini_zoom_btn_microphone_on);
			Log.d("updateAudioUi", "------AudioUi111"+ "开启");
		} else {
			Log.d("updateAudioUi", "------AudioUi222"+ "没开启");
			imageAudio.setImageResource(R.drawable.mini_zoom_btn_microphone_off);
		}
	}

	private void switchAudio() {
		Log.d("获取用户id", "---"+inMeetingService.getMyUserID());
		if (inMeetingAudioController.isAudioConnected()) {
			inMeetingAudioController.muteMyAudio(!inMeetingAudioController.isMyAudioMuted());
			if (inMeetingAudioController.isMyAudioMuted()) {
				imageAudio.setImageResource(R.drawable.mini_zoom_btn_microphone_off);
			} else {
				imageAudio.setImageResource(R.drawable.mini_zoom_btn_microphone_on);
			}
			Log.d("updateAudioUi", "------AudioUi333"+ inMeetingAudioController.isMyAudioMuted());
		} else {
			inMeetingAudioController.muteMyAudio(inMeetingAudioController.isMyAudioMuted());
//					inMeetingAudioController.connectAudioWithVoIP();
			connectThirdPartyAudio();
			imageAudio.setImageResource(R.drawable.mini_zoom_btn_microphone_on);
			Log.d("updateAudioUi", "------AudioUi444"+ !inMeetingAudioController.isAudioConnected());
		}
	}

	private void connectThirdPartyAudio() {
		audioRawDataHelper.setExternalAudioSource(new IZoomSDKVirtualAudioMicEvent() {
			@Override
			public void onMicInitialize(IZoomSDKAudioRawDataSender iZoomSDKAudioRawDataSender) {
//					iZoomSDKAudioRawDataSender.send()
				Log.d("开始录音0", "On virtual mic initialize");
				startRecording(iZoomSDKAudioRawDataSender);
			}

			@Override
			public void onMicStartSend() {
				Log.d("开始录音1", "On virtual mic start send");
			}

			@Override
			public void onMicStopSend() {
				Log.d("开始录音2", "On virtual mic stop send");
			}

			@Override
			public void onMicUninitialized() {
				Log.d("开始录音3", "On virtual mic uninitialized");
				if (mAudioRecorder != null) {
					mAudioRecorder.stopRecord();
				}
			}
		});
	}

	private void requestPermission() {
		if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(MyMeetingActivity.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
		}

		if(Build.VERSION.SDK_INT>=23 && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO_CODE);
		}
	}

	private void startRecording(IZoomSDKAudioRawDataSender sender) {
		int bufferSize = 48000;
		AudioManager.getInstance().startRecord(AudioFormat.CHANNEL_IN_MONO, bufferSize,
				new AudioManager.AudioRecordCallback() {
					@Override
					public void onFrameDataIn(byte[] data) {
						//data就是录制的识别数据
						Log.d("开始录音ing", "data.size:" + data.length);
						sender.send(ByteBuffer.wrap(data), data.length, 48000);
					}
				});
	}

	private void initListeners() {

		ZoomSDK.getInstance().getMeetingService().addListener(new MeetingServiceListener() {
			@Override
			public void onMeetingStatusChanged(MeetingStatus meetingStatus, int i, int i1) {
				if (meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING) {
					updateButtonsStatus();
					updateAudioUi();
				}
			}

			@Override
			public void onMeetingParameterNotification(MeetingParameter meetingParameter) {

			}
		});
		inMeetingService.addListener(new InMeetingServiceListener() {
			@Override
			public void onShareMeetingChatStatusChanged(boolean start) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息1", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息2", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onMeetingNeedPasswordOrDisplayName(boolean b, boolean b1, InMeetingEventHandler inMeetingEventHandler) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息3", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息4", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onWebinarNeedRegister(String s) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息5", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息6", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onJoinWebinarNeedUserNameAndEmail(InMeetingEventHandler inMeetingEventHandler) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息7", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息8", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onMeetingNeedCloseOtherMeeting(InMeetingEventHandler handler) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息9", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息10", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onMeetingFail(int i, int i1) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息11", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息12", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onMeetingLeaveComplete(long l) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息13", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息14", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onMeetingUserJoin(List<Long> list) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息15", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息16", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onMeetingUserLeave(List<Long> list) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息17", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息18", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onMeetingUserUpdated(long l) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息19", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息20", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onMeetingHostChanged(long l) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息21", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息22", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onMeetingCoHostChanged(long l) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息23", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息24", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onMeetingCoHostChange(long userId, boolean isCoHost) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息25", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息26", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onActiveVideoUserChanged(long l) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息27", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息28", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onActiveSpeakerVideoUserChanged(long l) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息29", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息30", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onSpotlightVideoChanged(boolean b) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息31", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息32", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onSpotlightVideoChanged(List<Long> userList) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息33", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息34", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onUserVideoStatusChanged(long l, VideoStatus videoStatus) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息35", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息36", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onUserNetworkQualityChanged(long l) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息37", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息38", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onSinkMeetingVideoQualityChanged(VideoQuality videoQuality, long userId) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息39", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息40", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onMicrophoneStatusError(InMeetingAudioController.MobileRTCMicrophoneError mobileRTCMicrophoneError) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息41", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息42", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onUserAudioStatusChanged(long l, AudioStatus audioStatus) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息43", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息44", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onHostAskUnMute(long l) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息45", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息46", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onHostAskStartVideo(long l) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息47", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息48", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onUserAudioTypeChanged(long l) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息49", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息50", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onMyAudioSourceTypeChanged(int i) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息51", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息52", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onLowOrRaiseHandStatusChanged(long l, boolean b) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息53", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息54", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onChatMessageReceived(InMeetingChatMessage inMeetingChatMessage) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息55", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息56", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onChatMsgDeleteNotification(String msgID, ChatMessageDeleteType deleteBy) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息57", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息58", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onSilentModeChanged(boolean b) {
				updateButtonsStatus();
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息59", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息60", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onFreeMeetingReminder(boolean b, boolean b1, boolean b2) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息61", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息62", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onMeetingActiveVideo(long l) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息63", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息64", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onSinkAttendeeChatPriviledgeChanged(int i) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息65", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息66", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onSinkAllowAttendeeChatNotification(int i) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息67", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息68", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onSinkPanelistChatPrivilegeChanged(InMeetingChatController.MobileRTCWebinarPanelistChatPrivilege privilege) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息69", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息70", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onUserNameChanged(long l, String s) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息71", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息72", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onUserNamesChanged(List<Long> userList) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息73", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息74", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onPermissionRequested(String[] permissions) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息75", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息76", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onFreeMeetingNeedToUpgrade(FreeMeetingNeedUpgradeType freeMeetingNeedUpgradeType, String s) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息77", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息78", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onFreeMeetingUpgradeToGiftFreeTrialStart() {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息79", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息80", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onFreeMeetingUpgradeToGiftFreeTrialStop() {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息81", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息82", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onFreeMeetingUpgradeToProMeeting() {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息83", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息84", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onClosedCaptionReceived(String message, long senderId) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息85", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息86", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onRecordingStatus(RecordingStatus recordingStatus) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息87", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息88", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onInvalidReclaimHostkey() {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息89", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息90", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onHostVideoOrderUpdated(List<Long> orderList) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息91", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息92", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onFollowHostVideoOrderChanged(boolean bFollow) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息93", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息94", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onAllHandsLowered() {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息95", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息96", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onLocalRecordingStatus(long userId, RecordingStatus status) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息97", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息98", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}

			@Override
			public void onLocalVideoOrderUpdated(List<Long> localOrderList) {
				audioRawDataHelper.subscribe(new IZoomSDKAudioRawDataDelegate() {
					@Override
					public void onMixedAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData) {
						Log.d("监听电脑段消息99", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}

					@Override
					public void onOneWayAudioRawDataReceived(ZoomSDKAudioRawData zoomSDKAudioRawData, int i) {
						Log.d("监听电脑段消息100", String.valueOf(zoomSDKAudioRawData.getChannelNum()));
					}
				});
			}
		});
	}

	@Override
	public void onConfigurationChanged (Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		disableFullScreenMode();
	}

	private void disableFullScreenMode() {
		getWindow().setFlags(~WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);
	}

	private void setupTabs() {
		viewTabMeeting = findViewById(R.id.viewTabMeeting);
//		btnTabWelcome = (Button)findViewById(R.id.btnTabWelcome);
//		btnTabMeeting = (Button)findViewById(R.id.btnTabMeeting);
//		btnTabPage2 = (Button)findViewById(R.id.btnTabPage2);

		selectTab(MainActivity.TAB_MEETING);

//		btnTabMeeting.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				selectTab(MainActivity.TAB_MEETING);
//			}
//		});

//		btnTabWelcome.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				selectTab(MainActivity.TAB_WELCOME);
//			}
//		});

//		btnTabPage2.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				selectTab(MainActivity.TAB_PAGE_2);
//			}
//		});
	}

	private void selectTab(int tabId) {
		if(tabId == MainActivity.TAB_MEETING) {
//			btnTabWelcome.setSelected(false);
//			btnTabPage2.setSelected(false);
//			btnTabMeeting.setSelected(true);
		} else {
			switchToMainActivity(tabId);
		}
	}

	private void switchToMainActivity(int tab) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setAction(MainActivity.ACTION_RETURN_FROM_MEETING);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.putExtra(MainActivity.EXTRA_TAB_ID, tab);

		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateButtonsStatus();

		// disable animation
		overridePendingTransition(0, 0);
	}

	@Override
	protected void onStartShare() {
//		btnShare.setVisibility(View.GONE);
//		btnStopShare.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onStopShare() {
//		btnShare.setVisibility(View.VISIBLE);
//		btnStopShare.setVisibility(View.GONE);
	}

	private void updateButtonsStatus() {

		boolean enabled = (inMeetingService.isMeetingConnected() && !inMeetingService.isInWaitingRoom());

//		btnSwitchToNextCamera.setEnabled(enabled);
//		btnAudio.setEnabled(enabled);
//		btnParticipants.setEnabled(enabled);
//		btnShare.setEnabled(enabled);
//		btnMoreOptions.setEnabled(enabled);
		if(inMeetingService.getInMeetingShareController().isSharingOut()) {
//			btnShare.setVisibility(View.GONE);
//			btnStopShare.setVisibility(View.VISIBLE);
		} else {
//			btnShare.setVisibility(View.VISIBLE);
//			btnStopShare.setVisibility(View.GONE);
		}
	}
}
