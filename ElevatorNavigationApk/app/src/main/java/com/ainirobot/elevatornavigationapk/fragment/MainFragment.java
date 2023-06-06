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
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.ainirobot.coreservice.bean.MultiFloorInfo;
import com.ainirobot.coreservice.client.Definition;
import com.ainirobot.coreservice.client.RobotApi;
import com.ainirobot.coreservice.client.listener.CommandListener;
import com.ainirobot.elevatornavigationapk.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends BaseFragment {
    private static final String TAG = "ElevatorTestFragment";

    private Button mSelect_point_fragment;
    private Button mExit;
    private List<MultiFloorInfo> mDatas;
    private View mContentView;

    @Override
    public View onCreateView(Context context) {
        mContentView = mInflater.inflate(R.layout.fragment_main_layout,null,false);
        initData();
        bindViews(mContentView);
        hideBackView();
        hideResultView();
        return mContentView;
    }

    private void initData() {
        Log.d(TAG, "initData");
        //SpeechApi.getInstance().playText(getString(R.string.lead_tts_input_room_manual), null);
        initAllMapInfo();
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

    private void bindViews(View root) {
        mSelect_point_fragment = (Button) root.findViewById(R.id.select_point_fragment);
        mExit = (Button) root.findViewById(R.id.exit);
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        mSelect_point_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(MultipleMapsFragment.newInstance(mDatas));
            }
        });
    }

    public static Fragment newInstance() {
        return new MainFragment();
    }
}
