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

package com.ainirobot.elevatornavigationapk.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ainirobot.coreservice.bean.MultiFloorInfo;
import com.ainirobot.coreservice.client.Definition;
import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.actionbean.Pose;
import com.ainirobot.coreservice.client.listener.CommandListener;
import com.ainirobot.coreservice.client.robotsetting.RobotSettingApi;
import com.ainirobot.elevatornavigationapk.LogTools;
import com.ainirobot.elevatornavigationapk.R;
import com.ainirobot.elevatornavigationapk.bean.TestPlaceBean;
import com.ainirobot.elevatornavigationapk.laddercontrol.RecyclerViewSpacesItemDecoration;
import com.ainirobot.elevatornavigationapk.laddercontrol.SelectDestinationAdapter;
import com.ainirobot.elevatornavigationapk.laddercontrol.SelectFloorAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MultipleMapsFragment extends BaseFragment implements View.OnClickListener,
        SelectDestinationAdapter.ItemClickListener, SelectFloorAdapter.FloorClickListener {

    private static final String TAG = "MultipleMapsFragment";
    private List<MultiFloorInfo> mDatas;
    private List<TestPlaceBean> placeList;
    private SelectFloorAdapter mFloorAdapter;
    private SelectDestinationAdapter mRoomAdapter;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private Pose mCurrentSelectRoom = null;
    private int mCurrentSelectFloor = 0;
    private TextView tvSelected;
    private SelectRoomListener mListener;
    private int MAX_RETRY_COUNT = 100;

    public interface SelectRoomListener {
        void onStart();

        void onStop();

        void onConfirm(List<TestPlaceBean> placeBeans, String loopCount);
    }

    public MultipleMapsFragment(List<MultiFloorInfo> dataMap) {
        super();
        Log.d(TAG, "MultiFloorInfo=======1111" + dataMap);
        this.mDatas = dataMap;
        Log.d(TAG, "MultiFloorInfo=======2222" + this.mDatas);
    }

    @Override
    public View onCreateView(Context context) {
        View view = mInflater.inflate(R.layout.fragment_multiple_maps_layout, null, false);
        initViews(view);
        LogTools.info("Battery level:"+
                RobotSettingApi.getInstance().getRobotString(Definition.ROBOT_SETTINGS_BATTERY_INFO)+"%");
        return view;
    }

    private void initViews(View view) {
        placeList = new ArrayList<>();
        Context context = view.getContext();
        mFloorAdapter = new SelectFloorAdapter(context);
        mFloorAdapter.setClickListener(this);

        mRoomAdapter = new SelectDestinationAdapter(context);
        mRoomAdapter.setClickListener(this);

        tvSelected = view.findViewById(R.id.tv_selected_place);

        RecyclerView roomList = view.findViewById(R.id.delivery_select_dialog_list);
        RecyclerView floorList = view.findViewById(R.id.delivery_select_dialog_layer);

        LinearLayout.LayoutParams floorListLp = (LinearLayout.LayoutParams) floorList.getLayoutParams();
//        floorListLp.width = (int) (DimenUtils.getScreenWidthPixels() * 0.16);
        floorList.setLayoutParams(floorListLp);

        LinearLayout.LayoutParams roomListLp = (LinearLayout.LayoutParams) roomList.getLayoutParams();
//        roomListLp.width = (int) (DimenUtils.getScreenWidthPixels() * 0.72);
        roomList.setLayoutParams(roomListLp);

        roomList.setLayoutManager(new GridLayoutManager(context, 3));
        roomList.setAdapter(mRoomAdapter);
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION,14);//下间距
        roomList.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));

        floorList.setLayoutManager(new GridLayoutManager(context, 4));
        floorList.setAdapter(mFloorAdapter);

        view.findViewById(R.id.delivery_select_dialog_ok).setOnClickListener(this);

        Collections.sort(this.mDatas, (s1, s2) -> Integer.compare(s1.getFloorIndex(), s2.getFloorIndex()));

        mMainHandler.post(() -> {
            if (mFloorAdapter != null) {
                mFloorAdapter.setData(this.mDatas);
                mFloorAdapter.notifyDataSetChanged();
            }
            MultiFloorInfo multiFloorInfo = this.mDatas.get(mCurrentSelectFloor);
            if (multiFloorInfo != null && mRoomAdapter != null) {
                mRoomAdapter.setData(multiFloorInfo.getPoseList());
                mRoomAdapter.notifyDataSetChanged();
            }
        });
        this.setSelectListener(new MultipleMapsFragment.SelectRoomListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {
                initAllMapInfo();
            }

            @Override
            public void onConfirm(List<TestPlaceBean> placeBeans, String loopCount) {
                Log.d(TAG, "lead places=" + placeBeans + "  loopCount: " + loopCount);
                placeList = placeBeans;
                //mSelectRoomDialog.dismiss();
                try {
                    int count = Integer.getInteger(loopCount);
                    if (count >= 1) {
                        MAX_RETRY_COUNT = count;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (placeBeans != null && placeBeans.size() > 0) {
                    showConfirmDest(placeBeans);
                }
            }
        });
    }

    private void showConfirmDest(List<TestPlaceBean> placeBeans) {
        Log.d(TAG, "lead places=" + placeBeans + "  loopCount: " + 11111);
        switchFragment(NavigationDetailsFragment.newInstance(placeBeans));
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

    public void setSelectListener(SelectRoomListener listener) {
        mListener = listener;
    }

    @Override
    public void onItemClick(Pose pose) {
        mCurrentSelectRoom = pose;
    }

    @Override
    public void onFloorClick(int position) {
        if (position < 0 || position >= mDatas.size()) {
            return;
        }
        mCurrentSelectFloor = position;
        List<Pose> currentRooms = mDatas.get(mCurrentSelectFloor).getPoseList();
        if (currentRooms == null) {
            return;
        }
        if (mRoomAdapter != null) {
            mRoomAdapter.setData(currentRooms);
            int roomPosition = currentRooms.indexOf(mCurrentSelectRoom);
            if (roomPosition >= 0) {
                mRoomAdapter.setSelectPosition(roomPosition);
            } else {
                mCurrentSelectRoom = null;
                mRoomAdapter.setSelectPosition(-1);
            }
            mRoomAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick方法");
        switch (v.getId()) {
            case R.id.delivery_select_dialog_ok:
                Log.d(TAG, "onClick方法1");
                if (null == placeList || placeList.size() <= 0) {
                    Log.d(TAG, "onClick方法2");
                    if (mCurrentSelectRoom == null) {
                        Log.d(TAG, "onClick方法3");
                        Toast.makeText(v.getContext(), "请选择房间号", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Log.d(TAG, "onClick方法4");
                        addSelectList(mCurrentSelectRoom.getName(), mDatas.get(mCurrentSelectFloor).getFloorIndex());
                    }
                }
                if (mListener != null) {
                    Log.d(TAG, "onClick方法5");
                    mListener.onConfirm(placeList, "1");
                }
                break;
        }
    }

    private void addSelectList(String dest, int floorIndex) {
        if (placeList == null) {
            placeList = new ArrayList<>();
        }
        placeList.add(new TestPlaceBean(dest, floorIndex));

        updateSelected();
    }

    private void updateSelected() {
        if (tvSelected != null) {
            StringBuilder title = new StringBuilder("已选择：");
            for (TestPlaceBean placeBean : placeList) {
                title.append(placeBean.getPlaceName()).append(" - ");
            }
            tvSelected.setText(title);
        }
    }

    public static Fragment newInstance(List<MultiFloorInfo> dataMap) {
        Log.d(TAG, "MultiFloorInfo=======" + dataMap);
        return new MultipleMapsFragment(dataMap);
    }
}
