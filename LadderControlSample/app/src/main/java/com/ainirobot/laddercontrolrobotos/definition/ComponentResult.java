/*
 *
 * Copyright (C) 2017 OrionStar Technology Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.ainirobot.laddercontrolrobotos.definition;

public class ComponentResult {

    private static final int BASE_COMMON_RESULT = 32610000;
    public static final int RESULT_SUCCESS = BASE_COMMON_RESULT + 1;
    public static final int RESULT_INTERRUPT = BASE_COMMON_RESULT + 2;
    public static final int RESULT_TIMEOUT = BASE_COMMON_RESULT + 3;
    public static final int RESULT_GUEST_FAR_AWAY = BASE_COMMON_RESULT + 4;
    public static final int RESULT_GUEST_LOST_TIMEOUT = BASE_COMMON_RESULT + 5;
    public static final int RESULT_RESET_ESTIMATE_FAIL = BASE_COMMON_RESULT + 6;
    public static final int RESULT_NAVIGATION_ARRIVED = BASE_COMMON_RESULT + 7;
    public static final int RESULT_NAVIGATION_STOP_SUCCESS = BASE_COMMON_RESULT + 8;
    public static final int RESULT_NAVIGATION_FAILURE = BASE_COMMON_RESULT + 9;
    public static final int RESULT_REGISTER_SUCCESS = BASE_COMMON_RESULT + 10;
    public static final int RESULT_MODIFY_NAME_SUCCESS = BASE_COMMON_RESULT + 11;
    public static final int RESULT_LEAD_COMPLETE = BASE_COMMON_RESULT + 12;
    public static final int RESULT_LEAD_STOP_BY_CLICK_UI = BASE_COMMON_RESULT + 13;
    public static final int RESULT_LEAD_STOP_BY_SPEECH = BASE_COMMON_RESULT + 14;
    public static final int RESULT_HEAD_TURN_SUCCESS = BASE_COMMON_RESULT + 15;
    public static final int RESULT_HEAD_TURN_GROUP_SUCCESS = BASE_COMMON_RESULT + 16;
    public static final int RESULT_TRICK_GROUP_SUCCESS = BASE_COMMON_RESULT + 17;
    public static final int RESULT_STANDBY_END_SUCCESS = BASE_COMMON_RESULT + 18;
    public static final int RESULT_MOTION_STOP_SUCCESS = BASE_COMMON_RESULT + 19;
    public static final int RESULT_OK_NO_ESTIMATE_CHARGING = BASE_COMMON_RESULT + 20;
    public static final int RESULT_OK_ESTIMATE_WITHIN_RANGE = BASE_COMMON_RESULT + 21;
    public static final int RESULT_NO_ESTIMATE_NO_CHARGING = BASE_COMMON_RESULT + 22;
    public static final int RESULT_ESTIMATE_WITHOUT_RANGE = BASE_COMMON_RESULT + 23;
    public static final int RESULT_LIGHT_GROUP_SUCCESS = BASE_COMMON_RESULT + 24;
    public static final int RESULT_NAVIGATION_TRANSFER_ARRIVED = BASE_COMMON_RESULT + 25;
    public static final int RESULT_NAVIGATION_DESTINATION_AVAILABLE = BASE_COMMON_RESULT + 26;
    public static final int RESULT_NAVIGATION_IN_DESTINATION_RANGE = BASE_COMMON_RESULT + 27;
    public static final int RESULT_OK_NO_ESTIMATE_CHARGING_WIRE = BASE_COMMON_RESULT + 28;
    public static final int RESULT_OK_ESTIMATE_WITHIN_RANGE_WIRE = BASE_COMMON_RESULT + 29;
    public static final int RESULT_BACK_TO_ELEVATOR_GATE_SUCCESS = BASE_COMMON_RESULT + 30;
    public static final int RESULT_ELEVATOR_CONFIG_INVALID = BASE_COMMON_RESULT + 31;
}
