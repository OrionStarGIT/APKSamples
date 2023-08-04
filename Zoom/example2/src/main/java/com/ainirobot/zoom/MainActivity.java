package com.ainirobot.zoom;

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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements Constants, MeetingServiceListener, ZoomSDKInitializeListener {

	private final static String TAG = "Zoom SDK Example";
	
	public final static String ACTION_RETURN_FROM_MEETING = "com.ainirobot.zoom.action.ReturnFromMeeting";
	public final static String EXTRA_TAB_ID = "tabId";
	public boolean audioStatus = true;

	public final static int TAB_WELCOME = 1;
	public final static int TAB_MEETING = 2;
	public final static int TAB_PAGE_2  = 3;
	public static int i = 0;

	/**
	 * TODO zoom秘钥 后续改为打包时填入
	 */
	public static String APP_KEY = "";
	public static String APP_SECRET = "";
	public final static String MEETING_NO  = "85224729671";
	public final static String PASSWORD  = "052340";
	
	private final static int STYPE = MeetingService.USER_TYPE_API_USER;
	private final static String DISPLAY_NAME = "ZoomUS SDK";

//	private View viewTabWelcome;
//	private View viewTabMeeting;
//	private View viewTabPage2;
//	private Button btnTabWelcome;
	private ImageView imageViewExit;
	private TextView textViewExit;
	private ImageView imageViewLogo;
	private Button btnTabMeeting;
	private SelectorImageView imageviewMicrophone;
//	private Button btnTabPage2;
	private ZoomSDK zoomSDK;
	private JoinMeetingOptions opts;
	private static JoinMeetingOptions meetingOptions = new JoinMeetingOptions();

	private CustomDialog.Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		setupTabs();
		builder = new CustomDialog.Builder(this);
		checkAppConfig();
	}

	private void checkAppConfig() {
		SharedPreferences proper = AppConfig.getProperties(MainActivity.this);
		String appKey = proper.getString("appKey", "");
		String appSecret = proper.getString("appSecret", "");
		if (appKey == "" || appSecret == "") {
			showDialog();
		} else {
			zoomInit(appKey, appSecret);
		}
	}

	private void zoomInit(String appKey, String appSecret) {
		zoomSDK = ZoomSDK.getInstance();
//		if(savedInstanceState == null) {
//			ZoomSDKInitParams initParams = new ZoomSDKInitParams();
//			initParams.jwtToken = SDK_JWTTOKEN;
//			initParams.domain = WEB_DOMAIN;
//			zoomSDK.initialize(this, this, initParams);
		ZoomSDKInitParams initParams = new ZoomSDKInitParams();
//			initParams.jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOm51bGwsImlzcyI6IlpTVFliR0RKU2xTdWJoUm43SU5JR1EiLCJleHAiOjE2NzIyMTQ1NDIsImlhdCI6MTY3MTYwOTc0Mn0.zUWRQvpOx93dOOU6-TXKsZW_h10hl84RzUlcnoVgH5c";
		initParams.appKey=appKey;
		initParams.appSecret=appSecret;
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
		imageViewExit = (ImageView)findViewById(R.id.imageViewExit);
		textViewExit = (TextView)findViewById(R.id.textViewExit);
		imageViewLogo = (ImageView)findViewById(R.id.imageViewLogo);
		btnTabMeeting = (Button)findViewById(R.id.btnTabMeeting);
		imageviewMicrophone = (SelectorImageView)findViewById(R.id.imageviewMicrophone);
//		btnTabPage2 = (Button)findViewById(R.id.btnTabPage2);
		
		selectTab(TAB_WELCOME);

		imageViewExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.finish();
			}
		});

		textViewExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.finish();
			}
		});

		imageViewLogo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				++i;
				if (i % 5 == 0) {
					showDialog();
				}
			}
		});

		btnTabMeeting.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				selectTab(TAB_MEETING);
			}
		});

		imageviewMicrophone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageviewMicrophone.toggle(!imageviewMicrophone.isChecked());
			}
		});
		
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

		EditText meeting_code = (EditText) findViewById(R.id.meetingCode);
		String meetingCode = meeting_code.getText().toString();

		EditText display_name = (EditText) findViewById(R.id.displayName);
		String displayName = display_name.getText().toString();

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

		params.displayName = displayName;
		params.meetingNo = meetingCode;
		params.password = "";
		audioStatus = SelectorImageView.isChecked;
		Log.v(TAG, "checkbox的值：" + audioStatus);
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
			btnTabMeeting.setEnabled(true);
			btnTabMeeting.setText("JOIN NOW");
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

	private void showDialog() {
		OnClickListener onCancelClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "App key or app secret invalid ！", Toast.LENGTH_LONG).show();
			}
		};
		OnClickListener onConfimClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				String app_key = builder.getAppKey();
				String app_secret = builder.getappSecret();
				String resultAppKey = AppConfig.setProperties(MainActivity.this, "appKey", app_key);
				String resultAppSecret = AppConfig.setProperties(MainActivity.this, "appSecret", app_secret);
				Log.v(TAG, "您输入的内容是：ip：" + app_key + "端口：" + app_secret + "  设置结果resultAppKey：" + resultAppKey + "  设置结果resultAppSecret：" + resultAppSecret);
				zoomInit(app_key, app_secret);
			}
		};
		showInfoDialog("App Secret is requested, please set it up", "setup the SDK Key and SDK Secret", "CANCEL", onCancelClickListener, "OK", onConfimClickListener);
	}
	protected void showInfoDialog(String waring, String info, String cancelText, OnClickListener cancelOnClick, String confirmText,
								  OnClickListener confirmOnClick) {
		builder.setTitle("提示");
		builder.setWarning(waring);
		builder.setInfo(info);
		builder.setButtonCancel(cancelText, cancelOnClick);
		builder.setButtonConfirm(confirmText, confirmOnClick);
		CustomDialog customDialog = builder.create();
		customDialog.show();
	}
}
