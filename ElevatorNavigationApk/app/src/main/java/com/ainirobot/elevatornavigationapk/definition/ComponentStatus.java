package com.ainirobot.elevatornavigationapk.definition;/*
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

public class ComponentStatus {

    private static final int BASE_COMMON_STATUS = 32710000;
    public static final int STATUS_PRE_WAKEUP = BASE_COMMON_STATUS + 1;
    public static final int STATUS_STANDBY_SUCCESS = BASE_COMMON_STATUS + 2;
    public static final int STATUS_CHARGE_START_IN_PLACE = BASE_COMMON_STATUS + 3;
    public static final int STATUS_VISION_CHARGE_START_GOTO_CHARGING_PILE = BASE_COMMON_STATUS + 4;
    public static final int STATUS_NAVIGATION_MULTI_ROBOT_STATUS_UPDATE = BASE_COMMON_STATUS + 5;
    public static final int STATUS_START_GO_CHARGE = BASE_COMMON_STATUS + 8;

    private static final int BASE_HEAD_STATUS = 32720000;
    public static final int STATUS_TRACK_SUCCESS = BASE_HEAD_STATUS + 1;
    public static final int STATUS_TRACK_FAILED = BASE_HEAD_STATUS + 2;
    public static final int STATUS_TRACK_END = BASE_HEAD_STATUS + 3;
    public static final int STATUS_GUEST_FARAWAY = BASE_HEAD_STATUS + 4;
    public static final int STATUS_GUEST_FARAWAY_END = BASE_HEAD_STATUS + 5;
    public static final int STATUS_PICTURE_PATH = BASE_HEAD_STATUS + 6;
    public static final int STATUS_LEAD_TRACK_FIND_PERSON = BASE_HEAD_STATUS + 7;
    public static final int STATUS_LEAD_GUEST_LOST = BASE_HEAD_STATUS + 8;
    public static final int STATUS_LEAD_GUEST_FARAWAY_TIMEOUT = BASE_HEAD_STATUS + 9;
    public static final int STATUS_LEAD_GUEST_APPEAR = BASE_HEAD_STATUS + 10;
    public static final int STATUS_CAMERA_SWITCH_FAILED = BASE_HEAD_STATUS + 11;
    public static final int STATUS_HEAD_TURN_START = BASE_HEAD_STATUS + 12;
    public static final int STATUS_HEAD_TURN_END = BASE_HEAD_STATUS + 13;
    public static final int STATUS_PERSON_LOST_TIMEOUT = BASE_HEAD_STATUS + 14;
    public static final int STATUS_GUEST_NEAR = BASE_HEAD_STATUS + 15;
    public static final int STATUS_OBSTACLE_DISAPPEAR = BASE_HEAD_STATUS + 16;
    public static final int STATUS_TURN_HEAD_MAX_UP_ANGLE = BASE_HEAD_STATUS + 22;
    public static final int STATUS_TURN_HEAD_MAX_DOWN_ANGLE = BASE_HEAD_STATUS + 23;

    private static final int BASE_NAVIGATION_STATUS = 32730000;
    public static final int STATUS_START_NAVIGATION = BASE_NAVIGATION_STATUS + 1;
    public static final int STATUS_NAVIGATION_AVOID = BASE_NAVIGATION_STATUS + 2;
    public static final int STATUS_NAVIGATION_AVOID_END = BASE_NAVIGATION_STATUS + 3;
    public static final int STATUS_OBSTACLES_AVOID = BASE_NAVIGATION_STATUS + 4;
    public static final int STATUS_DISTANCE_WITH_DESTINATION = BASE_NAVIGATION_STATUS + 5;
    public static final int STATUS_RESET_ESTIMATE_START = BASE_NAVIGATION_STATUS + 6;
    public static final int STATUS_AUTO_RESET_ESTIMATE_SUCCESS = BASE_NAVIGATION_STATUS + 7;
    public static final int STATUS_AUTO_RESET_ESTIMATE_FAIL = BASE_NAVIGATION_STATUS + 8;
    public static final int STATUS_ESTIMATE_LOST = BASE_NAVIGATION_STATUS + 9;
    public static final int STATUS_NAVIGATION_NEAR_DESTINATION = BASE_NAVIGATION_STATUS + 10;
    public static final int STATUS_NAVIGATION_AVOID_START = BASE_NAVIGATION_STATUS + 11;
    public static final int STATUS_CRUISE_AVOID_GO_NEXT_POINT = BASE_NAVIGATION_STATUS + 12;
    public static final int STATUS_START_CRUISE = BASE_NAVIGATION_STATUS + 13;
    public static final int STATUS_CRUISE_REACH_POINT = BASE_NAVIGATION_STATUS + 14;
    public static final int STATUS_CRUISE_AVOID_POINT = BASE_NAVIGATION_STATUS + 15;
    public static final int STATUS_NAVIGATION_RESET_ESTIMATE_SUCCESS = BASE_NAVIGATION_STATUS + 16;
    public static final int STATUS_ALREADY_AT_START_CHARGE_POINT = BASE_NAVIGATION_STATUS + 17;
    public static final int STATUS_ADVERT_START = BASE_NAVIGATION_STATUS + 18;
    public static final int STATUS_ADVERT_STOP = BASE_NAVIGATION_STATUS + 19;
    public static final int STATUS_NAVI_MULTI_ROBOT_WAITING = BASE_NAVIGATION_STATUS + 20;
    public static final int STATUS_NAVI_MULTI_ROBOT_WAITING_END = BASE_NAVIGATION_STATUS + 21;
    public static final int STATUS_NAVIGATION_AVOID_IMMEDIATELY = BASE_NAVIGATION_STATUS + 22;
    public static final int STATUS_AUTO_RESET_ESTIMATE_START = BASE_NAVIGATION_STATUS + 23;
    public static final int STATUS_RESET_ESTIMATE_SINGLE_FAILED = BASE_NAVIGATION_STATUS + 24;
    public static final int STATUS_NAVIGATION_GO_STRAIGHT = BASE_NAVIGATION_STATUS + 25;
    public static final int STATUS_NAVIGATION_TURN_LEFT = BASE_NAVIGATION_STATUS + 26;
    public static final int STATUS_NAVIGATION_TURN_RIGHT = BASE_NAVIGATION_STATUS + 27;
    public static final int STATUS_NAVIGATION_SET_PRIORITY_FAILED = BASE_NAVIGATION_STATUS + 28;

    private static final int BASE_NAVIGATION_TRANSFER_STATUS = 32740000;
    public static final int STATUS_NAVIGATION_DISABLE_MULTIPLE_MODE = BASE_NAVIGATION_TRANSFER_STATUS + 1;
    public static final int STATUS_NAVIGATION_TRANSFER_START_NAVIGATION = BASE_NAVIGATION_TRANSFER_STATUS + 2;
    public static final int STATUS_NAVIGATION_TRANSFER_REPLACE_DESTINATION = BASE_NAVIGATION_TRANSFER_STATUS + 3;

    //梯控
    private static final int BASE_ELEVATOR_STATUS = 32750000;
    public static final int STATUS_ARRIVED_ELEVATOR_CENTER_CORRECT_ORIENTATION = BASE_ELEVATOR_STATUS + 23;
    public static final int STATUS_PREPARE_TAKE_ELEVATOR = BASE_ELEVATOR_STATUS + 22;
    public static final int STATUS_BACK_TO_ELEVATOR_CENTER = BASE_ELEVATOR_STATUS + 21;
    public static final int STATUS_UPDATE_SWITCH_MAP_SUCCESS = BASE_ELEVATOR_STATUS + 20;
    public static final int STATUS_UPDATE_SWITCH_MAP_START = BASE_ELEVATOR_STATUS + 19;
    public static final int STATUS_UPDATE_ELEVATOR_DOOR_STATE = BASE_ELEVATOR_STATUS + 18;
    public static final int STATUS_ELEVATOR_FLOOR_NOT_FOUND_IN_MAP_MAPPING = BASE_ELEVATOR_STATUS + 17;
    public static final int STATUS_RETRY_EXIT_ELEVATOR = BASE_ELEVATOR_STATUS + 16;
    public static final int STATUS_START_GO_DESTINATION = BASE_ELEVATOR_STATUS + 15;
    public static final int STATUS_ROBOT_CURRENT_MAP_FLOOR_INFO = BASE_ELEVATOR_STATUS + 14;
    public static final int STATUS_ELEVATOR_ROBOT_POSITION_INFO = BASE_ELEVATOR_STATUS + 13;
    public static final int STATUS_ELEVATOR_FLOOR_INFO = BASE_ELEVATOR_STATUS + 12;
    public static final int STATUS_START_BACK_TO_ELEVATOR_GATE = BASE_ELEVATOR_STATUS + 11;
    public static final int STATUS_GO_CENTER_FAILED_AND_TRY_BACK = BASE_ELEVATOR_STATUS + 10;
    public static final int STATUS_ROBOT_OUT_OF_ELEVATOR = BASE_ELEVATOR_STATUS + 9;
    public static final int STATUS_TAKE_ELEVATOR_TO_TARGET_FLOOR_SUCCESS = BASE_ELEVATOR_STATUS + 8;
    public static final int STATUS_ELEVATOR_ARRIVED_TARGET_FLOOR = BASE_ELEVATOR_STATUS + 7;
    public static final int STATUS_ELEVATOR_ARRIVED_CURRENT_FLOOR = BASE_ELEVATOR_STATUS + 6;
    public static final int STATUS_ARRIVED_ELEVATOR_CENTER = BASE_ELEVATOR_STATUS + 5;
    public static final int STATUS_ARRIVED_ELEVATOR_GATE = BASE_ELEVATOR_STATUS + 4;
    public static final int STATUS_START_GO_ELEVATOR_CENTER = BASE_ELEVATOR_STATUS + 3;
    public static final int STATUS_START_GO_ELEVATOR_GATE = BASE_ELEVATOR_STATUS + 2;
    public static final int STATUS_CALL_ELEVATOR_AND_WAIT = BASE_ELEVATOR_STATUS + 1;
}
