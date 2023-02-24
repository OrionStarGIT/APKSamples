package com.ainirobot.zoom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingError;
import us.zoom.sdk.MeetingParameter;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.MeetingViewsOptions;
import us.zoom.sdk.StartMeetingOptions;
import us.zoom.sdk.StartMeetingParamsWithoutLogin;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;
import us.zoom.sdk.ZoomSDKRawDataMemoryMode;

public class MainActivity_backup extends Activity implements Constants, MeetingServiceListener, ZoomSDKInitializeListener {

	private final static String TAG = "Zoom SDK Example";
	
	public final static String ACTION_RETURN_FROM_MEETING = "com.ainirobot.zoom.action.ReturnFromMeeting";
	public final static String EXTRA_TAB_ID = "tabId";

	public final static int TAB_WELCOME = 1;
	public final static int TAB_MEETING = 2;
	public final static int TAB_PAGE_2  = 3;

	public final static String APP_KEY = "kI2PfJFUlPwoiEx40oqw1sOSoABVY7nX6qzw";
	public final static String APP_SECRET = "Vlx3IlD5CXbL9WpB5FylMd6zajUrV1Y7Kij3";
	public final static String MEETING_NO  = "85224729671";
	public final static String PASSWORD  = "052340";
	
	private final static int STYPE = MeetingService.USER_TYPE_API_USER;
	private final static String DISPLAY_NAME = "ZoomUS SDK";

//	private View viewTabWelcome;
//	private View viewTabMeeting;
//	private View viewTabPage2;
//	private Button btnTabWelcome;
//	private Button btnTabMeeting;
//	private Button btnTabPage2;
	private ZoomSDK zoomSDK;
	private JoinMeetingOptions opts;
	private static JoinMeetingOptions meetingOptions = new JoinMeetingOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);

		setupTabs();

		zoomSDK = ZoomSDK.getInstance();
		
//		if(savedInstanceState == null) {
//			ZoomSDKInitParams initParams = new ZoomSDKInitParams();
//			initParams.jwtToken = SDK_JWTTOKEN;
//			initParams.domain = WEB_DOMAIN;
//			zoomSDK.initialize(this, this, initParams);
			ZoomSDKInitParams initParams = new ZoomSDKInitParams();
//			initParams.jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOm51bGwsImlzcyI6IlpTVFliR0RKU2xTdWJoUm43SU5JR1EiLCJleHAiOjE2NzIyMTQ1NDIsImlhdCI6MTY3MTYwOTc0Mn0.zUWRQvpOx93dOOU6-TXKsZW_h10hl84RzUlcnoVgH5c";
			initParams.appKey=APP_KEY;
			initParams.appSecret=APP_SECRET;
			initParams.enableLog = true;
			initParams.enableGenerateDump =true;
			initParams.logSize = 5;
			initParams.domain=Constants.WEB_DOMAIN;
			initParams.videoRawDataMemoryMode = ZoomSDKRawDataMemoryMode.ZoomSDKRawDataMemoryModeStack;
			zoomSDK.initialize(this, this, initParams);
//			}

		if(zoomSDK.isInitialized()) {
			registerMeetingServiceListener();
		}
	}

	private void setupTabs() {
//		viewTabWelcome = findViewById(R.id.viewTabWelcome);
//		viewTabMeeting = findViewById(R.id.viewTabMeeting);
//		viewTabPage2 = findViewById(R.id.viewTabPage2);
//		btnTabWelcome = (Button)findViewById(R.id.btnTabWelcome);
//		btnTabMeeting = (Button)findViewById(R.id.btnTabMeeting);
//		btnTabPage2 = (Button)findViewById(R.id.btnTabPage2);
		
		selectTab(TAB_WELCOME);
		
//		btnTabMeeting.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				selectTab(TAB_MEETING);
//			}
//		});
		
//		btnTabWelcome.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				selectTab(TAB_WELCOME);
//			}
//		});
		
//		btnTabPage2.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				selectTab(TAB_PAGE_2);
//			}
//		});
	}
	
	private void selectTab(int tabId) {
		if(tabId == TAB_WELCOME) {
//			viewTabWelcome.setVisibility(View.VISIBLE);
//			viewTabMeeting.setVisibility(View.GONE);
//			viewTabPage2.setVisibility(View.GONE);
//			btnTabWelcome.setSelected(true);
//			btnTabMeeting.setSelected(false);
//			btnTabPage2.setSelected(false);
		} else if(tabId == TAB_PAGE_2) {
//			viewTabWelcome.setVisibility(View.GONE);
//			viewTabMeeting.setVisibility(View.GONE);
//			viewTabPage2.setVisibility(View.VISIBLE);
//			btnTabWelcome.setSelected(false);
//			btnTabMeeting.setSelected(false);
//			btnTabPage2.setSelected(true);
		} else if(tabId == TAB_MEETING) {
			ZoomSDK zoomSDK = ZoomSDK.getInstance();
			if(!zoomSDK.isInitialized()) {
				Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
				return;
			}
			
			MeetingService meetingService = zoomSDK.getMeetingService();
			if(meetingService == null)
				return;
			
			if(meetingService.getMeetingStatus() == MeetingStatus.MEETING_STATUS_IDLE){
				
//				viewTabWelcome.setVisibility(View.GONE);
//				viewTabPage2.setVisibility(View.GONE);
//				viewTabMeeting.setVisibility(View.VISIBLE);
//				btnTabWelcome.setSelected(false);
//				btnTabPage2.setSelected(false);
//				btnTabMeeting.setSelected(true);
				joinMeeting();
//				startMeeting();
			} else {
				meetingService.returnToMeeting(this);
			}
			
			overridePendingTransition(0, 0);
		}
	}

	private void joinMeeting() {
		int ret = -1;
		MeetingService meetingService = zoomSDK.getMeetingService();
		if(meetingService == null) {
			Toast.makeText(this, "ZoomSDK 初始化错误", Toast.LENGTH_LONG).show();
		}
		opts = new JoinMeetingOptions();
		fillMeetingOption(opts);
		opts.no_audio = meetingOptions.no_audio;

		// some available options
		opts.no_driving_mode = true;
		opts.no_invite = true;
		opts.no_meeting_end_message = true;
		opts.no_titlebar = true;
		opts.no_bottom_toolbar = true;
		opts.no_dial_in_via_phone = true;
		opts.no_dial_out_to_phone = true;
		opts.no_disconnect_audio = true;
		opts.no_share = true;
		//opts.invite_options = InviteOptions.INVITE_VIA_EMAIL + InviteOptions.INVITE_VIA_SMS;
		opts.no_audio = true;
		opts.no_video = false;
		opts.meeting_views_options = MeetingViewsOptions.NO_BUTTON_SHARE;
		opts.no_meeting_error_message = true;
		//opts.participant_id = "participant id";
		JoinMeetingParams params = new JoinMeetingParams();

		params.displayName = DISPLAY_NAME;
		params.meetingNo = MEETING_NO;
		params.password = PASSWORD;
		int response = meetingService.joinMeetingWithParams(this, params, opts);
	}

	private void fillMeetingOption(JoinMeetingOptions opts) {
		opts.no_driving_mode = meetingOptions.no_driving_mode;
		opts.no_invite = meetingOptions.no_invite;
		opts.no_meeting_end_message = meetingOptions.no_meeting_end_message;
		opts.no_meeting_error_message = meetingOptions.no_meeting_error_message;
		opts.no_titlebar = meetingOptions.no_titlebar;
		opts.no_bottom_toolbar = meetingOptions.no_bottom_toolbar;
		opts.no_dial_in_via_phone = meetingOptions.no_dial_in_via_phone;
		opts.no_dial_out_to_phone = meetingOptions.no_dial_out_to_phone;
		opts.no_disconnect_audio = meetingOptions.no_disconnect_audio;
		opts.no_record = meetingOptions.no_record;
		opts.no_share = meetingOptions.no_share;
		//opts.no_video = meetingOptions.no_video;
		opts.meeting_views_options = meetingOptions.meeting_views_options;
		opts.invite_options = meetingOptions.invite_options;
		opts.customer_key = meetingOptions.customer_key;
		opts.custom_meeting_id = meetingOptions.custom_meeting_id;
		opts.no_unmute_confirm_dialog=meetingOptions.no_unmute_confirm_dialog;
		opts.no_webinar_register_dialog=meetingOptions.no_webinar_register_dialog;
		opts.no_chat_msg_toast = meetingOptions.no_chat_msg_toast;
//		return opts;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		// disable animation
		overridePendingTransition(0,0);
		
		String action = intent.getAction();
		
		if(ACTION_RETURN_FROM_MEETING.equals(action)) {
			int tabId = intent.getIntExtra(EXTRA_TAB_ID, TAB_WELCOME);
			selectTab(tabId);
		}
	}

	@Override
	public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
		Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);
		
		if(errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
			Toast.makeText(this, "Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG);
		} else {
			Toast.makeText(this, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG).show();
			
			registerMeetingServiceListener();
		}
	}

	private void registerMeetingServiceListener() {
		ZoomSDK zoomSDK = ZoomSDK.getInstance();
		MeetingService meetingService = zoomSDK.getMeetingService();
		if(meetingService != null) {
			meetingService.addListener(this);
		}
	}
	
	@Override
	protected void onDestroy() {
		ZoomSDK zoomSDK = ZoomSDK.getInstance();
		
		if(zoomSDK.isInitialized()) {
			MeetingService meetingService = zoomSDK.getMeetingService();
			meetingService.removeListener(this);
		}
		
		super.onDestroy();
	}

	public void startMeeting() {
		
		ZoomSDK zoomSDK = ZoomSDK.getInstance();

		if(!zoomSDK.isInitialized()) {
			Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
			return;
		}

		if(MEETING_ID == null) {
			Toast.makeText(this, "MEETING_ID in Constants can not be NULL", Toast.LENGTH_LONG).show();
			return;
		}
		
		MeetingService meetingService = zoomSDK.getMeetingService();

		StartMeetingOptions opts = new StartMeetingOptions();
		opts.no_driving_mode = true;
//		opts.no_meeting_end_message = true;
		opts.no_titlebar = true;
		opts.no_bottom_toolbar = true;
		opts.no_invite = true;

		StartMeetingParamsWithoutLogin params = new StartMeetingParamsWithoutLogin();
		params.userId = USER_ID;
		params.zoomAccessToken = ZOOM_ACCESS_TOKEN;
		params.meetingNo = MEETING_ID;
		params.displayName = DISPLAY_NAME;

		int ret = meetingService.startMeetingWithParams(this, params, opts);
		
		Log.i(TAG, "onClickBtnStartMeeting, ret=" + ret);
	}

	@Override
	public void onMeetingStatusChanged(MeetingStatus meetingStatus, int errorCode,
			int internalErrorCode) {
		
		if(meetingStatus == meetingStatus.MEETING_STATUS_FAILED && errorCode == MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE) {
			Toast.makeText(this, "Version of ZoomSDK is too low!", Toast.LENGTH_LONG).show();
		}
		
		if(meetingStatus == MeetingStatus.MEETING_STATUS_IDLE || meetingStatus == MeetingStatus.MEETING_STATUS_FAILED) {
			selectTab(TAB_WELCOME);
		}
	}

	@Override
	public void onZoomAuthIdentityExpired() {

	}

	@Override
	public void onMeetingParameterNotification(MeetingParameter meetingParameter) {

	}
}
