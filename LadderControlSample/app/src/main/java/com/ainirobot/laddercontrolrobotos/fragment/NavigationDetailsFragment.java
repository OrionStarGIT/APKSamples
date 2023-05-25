/*
 *  Copyright (C) 2017 OrionStar Technology Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.ainirobot.laddercontrolrobotos.fragment;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.ainirobot.coreservice.bean.MultiFloorInfo;
import com.ainirobot.coreservice.client.Definition;
import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.listener.ActionListener;
import com.ainirobot.coreservice.client.listener.CommandListener;
import com.ainirobot.coreservice.client.robotsetting.RobotSettingApi;
import com.ainirobot.coreservice.utils.DelayTask;
import com.ainirobot.laddercontrolrobotos.LogTools;
import com.ainirobot.laddercontrolrobotos.R;
import com.ainirobot.laddercontrolrobotos.bean.TestPlaceBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NavigationDetailsFragment extends BaseFragment {
    private static final String TAG = "NavigationDetailsFrag";
    private List<TestPlaceBean> mPlaceBeans;
    private AppCompatTextView tvMsg;
    private TextView leadConfirmBtn, leadCancelBtn;
    private TextView tvTitle;
    //导航类型 0：高级导航  1：原NavigationComponent
    private int naviType = 0;
    private int naviCount = 0;
    private long naviStartTime;
    private List<MultiFloorInfo> mDatas;

    public NavigationDetailsFragment(List<TestPlaceBean> placeBeans) {
        super();
        this.mPlaceBeans = placeBeans;
    }

    @Override
    public View onCreateView(Context context) {
        View root = mInflater.inflate(R.layout.fragment_navigation_details, null, false);
        initViews(root);
        LogTools.info("Battery level:"+
                RobotSettingApi.getInstance().getRobotString(Definition.ROBOT_SETTINGS_BATTERY_INFO)+"%");
        return root;
    }

    private void initViews(View rootView) {
        Log.d(TAG, "showConfirmDest=======" + this.mPlaceBeans);
        tvTitle = rootView.findViewById(R.id.tvDestination);
        View tvMsg = rootView.findViewById(R.id.tvDestinationDesc);
        updateTitle(this.mPlaceBeans);

        String dest = this.mPlaceBeans.get(0).getPlaceName();
        int floor = this.mPlaceBeans.get(0).getFloorIndex();
        leadCancelBtn = rootView.findViewById(R.id.lead_cancel);
        leadCancelBtn.setOnClickListener(view -> {
            leadConfirmBtn.setEnabled(true);
            leadConfirmBtn.setVisibility(View.VISIBLE);
            // 重设状态
            stopNavi();
            Log.d(TAG, "停止导航=======" + this.mPlaceBeans);
//            showLeadEntry();
            Log.d(TAG, "展示点位列表页面=======" + this.mPlaceBeans);
        });
        leadConfirmBtn = rootView.findViewById(R.id.lead_confirm);
        leadConfirmBtn.setOnClickListener(view -> {
            naviType = 0;   //高级导航
            startNavigation(dest, floor);
            leadConfirmBtn.setEnabled(false);
            leadConfirmBtn.setVisibility(View.GONE);
        });
    }

    private void startNavigation(String dest, int floor) {
        //梯控导航去点位：
        RobotApi.getInstance().startElevatorNavigation(Definition.DEBUG_REQ_ID,
                dest,
                floor,
                new ActionListener() {
                    @Override
                    public void onResult(int status, String message, String extraData) throws RemoteException {
                        Log.e("mName", "onResult: " + status + " responseString：" + message + " extraData：" + extraData);
                        handlerResult(status, message, extraData);
                    }

                    @Override
                    public void onError(int errorCode, String errorString, String extraData) throws RemoteException {
                        Log.e("mName", "onError: " + errorCode + " errorString：" + errorString + " extraData：" + extraData);
                        handlerError(errorCode, errorString, extraData);
                    }

                    @Override
                    public void onStatusUpdate(int status, String data, String extraData) throws RemoteException {
                        Log.e("mName", "onStatusUpdate: " + status + " data：" + data + " extraData：" + extraData);
                        handlerStatusUpdate(status, data, extraData);
                    }
                });
    }

    private void handlerResult(int status, String message, String extraData) {
        switch (status) {
            case Definition.RESULT_OK:
                //stop(ComponentResult.RESULT_NAVIGATION_ARRIVED, message, extraData);
                break;
            case Definition.ACTION_RESPONSE_STOP_SUCCESS:
                //stop(status, message + " 停止成功", extraData);
                break;
            case Definition.RESULT_FAILURE:
                //stop(status, message + " 导航失败", extraData);
                break;
            case Definition.STATUS_NAVI_OUT_MAP:
                //stop(ComponentError.ERROR_NAVIGATION_OUT_MAP, message, extraData);
                break;
            case Definition.STATUS_NAVI_GLOBAL_PATH_FAILED:
                //stop(ComponentError.ERROR_NAVIGATION_GLOBAL_PATH_FAILED, message, extraData);
                break;
            case Definition.STATUS_NAVI_MULTI_MAP_NOT_MATCH:
            case Definition.STATUS_NAVI_MULTI_LORA_DISCONNECT:
            case Definition.STATUS_NAVI_MULTI_LORA_CONFIG_FAIL:
            case Definition.STATUS_NAVI_MULTI_VERSION_NOT_MATCH:
                //stop(ComponentError.ERROR_MULTIPLE_MODE_ERROR, message, extraData);
                break;
            case Definition.STATUS_NAVI_WHEEL_SLIP:
                //stop(ComponentError.ERROR_NAVI_WHEEL_SLIP, message, extraData);
                break;
            default:
                //stop(status, message, extraData);
                break;
        }
    }

    private void handlerError(int errorCode, String message, String extraData) {
        switch (errorCode) {
            case Definition.ERROR_NOT_ESTIMATE:
//                stop(ComponentError.ERROR_NOT_ESTIMATE, "navigation not estimate", extraData);
                break;
            case Definition.ERROR_IN_DESTINATION:
//                stop(ComponentError.ERROR_NAVIGATION_ALREADY_IN_DESTINATION, "already in destination", extraData);
                break;
            case Definition.ERROR_DESTINATION_NOT_EXIST:
//                stop(ComponentError.ERROR_DESTINATION_NOT_EXIST, "destination not exist", extraData);
                break;
            case Definition.ERROR_DESTINATION_CAN_NOT_ARRAIVE:
//                stop(ComponentError.ERROR_DESTINATION_CAN_NOT_ARRIVE, "navigation moving time out", extraData);
                break;
            case Definition.ERROR_MULTI_ROBOT_WAITING_TIMEOUT:
//                stop(ComponentError.ERROR_MULTI_ROBOT_WAITING_TIMEOUT);
                break;
            case Definition.ERROR_NAVIGATION_AVOID_TIMEOUT:
//                stop(ComponentError.ERROR_NAVIGATION_AVOID_TIMEOUT, "navigation avoid time out", "");
            case Definition.ACTION_RESPONSE_ALREADY_RUN:
//                stop(ComponentError.ERROR_REQUEST_RES_BUSY);
                break;
            case Definition.ACTION_RESPONSE_REQUEST_RES_ERROR:
//                stop(ComponentError.ERROR_REQUEST_RES_FAILED);
                break;
            case Definition.ERROR_WHEEL_OVER_CURRENT_RUN_OUT:
//                stop(ComponentError.ERROR_WHEEL_OVER_CURRENT_RUN_OUT, "wheel over current retry count run out", extraData);
                break;
            case Definition.ACTION_RESPONSE_RES_UNAVAILBALE:
//                stop(ComponentError.ERROR_OPEN_RADAR_FAILED, "res unavailable: " + message, extraData);
                break;
            default:
//                stop(errorCode, message, extraData);
                break;
        }
    }

    private void handlerStatusUpdate(int status, String data, String extraData) {
        switch (status) {
            case Definition.STATUS_GOAL_OCCLUDED:
            case Definition.STATUS_NAVI_AVOID:
//                updateStatus(ComponentStatus.STATUS_NAVIGATION_AVOID_START,"navigation avoid start", extraData);
                break;
            case Definition.STATUS_GOAL_OCCLUDED_END:
            case Definition.STATUS_NAVI_AVOID_END:
//                updateStatus(ComponentStatus.STATUS_NAVIGATION_AVOID_END,"navigation avoid end", extraData);
                break;
            case Definition.STATUS_NAVI_OBSTACLES_AVOID:
//                updateStatus(ComponentStatus.STATUS_OBSTACLES_AVOID,"pause to obstacles avoid", extraData);
                break;
            case Definition.STATUS_START_NAVIGATION:
//                updateStatus(ComponentStatus.STATUS_START_NAVIGATION,"start navigation");
                break;
            case Definition.STATUS_NAVI_MULTI_ROBOT_WAITING:
//                updateStatus(ComponentStatus.STATUS_NAVI_MULTI_ROBOT_WAITING,"navigation multi robot waiting");
                break;
            case Definition.STATUS_NAVI_MULTI_ROBOT_WAITING_END:
//                updateStatus(ComponentStatus.STATUS_NAVI_MULTI_ROBOT_WAITING_END,"navigation multi robot waiting end");
                break;
            case Definition.STATUS_NAVI_GO_STRAIGHT:
//                updateStatus(ComponentStatus.STATUS_NAVIGATION_GO_STRAIGHT, data,extraData);
                break;
            case Definition.STATUS_NAVI_TURN_LEFT:
//                updateStatus(ComponentStatus.STATUS_NAVIGATION_TURN_LEFT, data, extraData);
                break;
            case Definition.STATUS_NAVI_TURN_RIGHT:
//                updateStatus(ComponentStatus.STATUS_NAVIGATION_TURN_RIGHT, data, extraData);
                break;
            case Definition.STATUS_NAVI_SET_PRIORITY_FAILED:
//                updateStatus(ComponentStatus.STATUS_NAVIGATION_SET_PRIORITY_FAILED, data, extraData);
                break;
            default:
//                updateStatus(status, data, extraData);
                break;
        }
    }

    private void showLeadEntry() {
        initAllMapInfo();
        switchFragment(MultipleMapsFragment.newInstance(mDatas));
    }

    private void initAllMapInfo() {
        //获取多地图点位列表：
        RobotApi.getInstance().getMultiFloorConfigAndPose(Definition.DEBUG_REQ_ID, new CommandListener() {
            @Override
            public void onResult(int result, String message, String extraData) {
                Type type = new TypeToken<ArrayList<MultiFloorInfo>>() {
                }.getType();
                try {
                    Log.d(TAG, "initAllMapInfo: " + result + " msg:" + message);
                    mDatas = new Gson().fromJson(message, type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void stopNavi() {
        naviType = 0;
        naviCount = 0;
        DelayTask.cancel(TAG);
        this.mPlaceBeans = new ArrayList<>();
        RobotApi.getInstance().stopAdvanceNavigation(0);
    }

    private void updateTitle(List<TestPlaceBean> placeBeans) {
        if (placeBeans.size() == 1) {
            tvTitle.setText(placeBeans.get(0).getPlaceName());
        } else {
            tvTitle.setText("循环执行");
            StringBuilder places = new StringBuilder();
            for (int i = 0; i < placeBeans.size(); i++) {
                places.append(placeBeans.get(i).getPlaceName());
                if (i != placeBeans.size() - 1) {
                    places.append(" - ");
                }
            }
            tvMsg.setText(places);
            naviStartTime = System.currentTimeMillis();
        }
    }

    public static Fragment newInstance(List<TestPlaceBean> placeBeans) {
        return new NavigationDetailsFragment(placeBeans);
    }
}
