package com.ainirobot.laddercontrolrobotos.definition;/*
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

public class ComponentError {

    private static final int BASE_COMMON_ERROR = -32600000;
    public static final int ERROR_REQUEST_RES_BUSY = BASE_COMMON_ERROR - 1;
    public static final int ERROR_REQUEST_RES_FAILED = BASE_COMMON_ERROR - 2;
    public static final int ERROR_RECOGNIZE_TIMEOUT = BASE_COMMON_ERROR -3;
    public static final int ERROR_OPEN_PERSON_DETECT_FAILED = BASE_COMMON_ERROR - 4;
    public static final int ERROR_RECEPTION_REGISTER_TIMEOUT = BASE_COMMON_ERROR - 5;
    public static final int ERROR_SOUND_LOCALIZATION_FAILED = BASE_COMMON_ERROR - 6;
    public static final int ERROR_CHARGE_START_NAVI_TIMEOUT = BASE_COMMON_ERROR - 7;
    public static final int ERROR_CHARGE_START_PARSE_IN_LOCATION = BASE_COMMON_ERROR - 8;
    public static final int ERROR_CHARGE_START_NOT_MOVE = BASE_COMMON_ERROR - 9;
    public static final int ERROR_CHARGE_START_PSB_FAIL = BASE_COMMON_ERROR - 10;
    public static final int ERROR_CHARGE_START_PBS_NO_SIGNAL = BASE_COMMON_ERROR - 11;
    public static final int ERROR_VISION_CHARGE_START_FAIL = BASE_COMMON_ERROR - 12;
    public static final int ERROR_VISION_CHARGE_STOP_FAIL = BASE_COMMON_ERROR - 13;
    public static final int ERROR_VISION_CHARGE_TIMEOUT = BASE_COMMON_ERROR - 14;
    public static final int ERROR_LEAVE_PILE_OPEN_RADAR_FAIL = BASE_COMMON_ERROR - 15;
    public static final int ERROR_LEAVE_PILE_MOTION_AVOID_FAIL = BASE_COMMON_ERROR - 16;
    public static final int ERROR_LEAVE_PILE_TIMEOUT_FAIL = BASE_COMMON_ERROR - 17;
    public static final int ERROR_QUERY_POSE_LIST_FAILED = BASE_COMMON_ERROR - 18;
    public static final int ERROR_LEAVE_PILE_FAIL_WIRE_CHARGING = BASE_COMMON_ERROR - 19;
    public static final int ERROR_SAIPH_PRO_ZCB_LED_FAIL = BASE_COMMON_ERROR - 20;

    private static final int BASE_PARAMS_ERROR = -32610000;
    public static final int ERROR_PARAMS_PHOTO_PATH_INVALID = BASE_PARAMS_ERROR - 1;
    public static final int ERROR_PARAMS_GUEST_NAME_INVALID = BASE_PARAMS_ERROR - 2;
    public static final int ERROR_PARAMS_TASK_ID_INVALID = BASE_PARAMS_ERROR - 3;
    public static final int ERROR_PARAMS_PLACE_NAME_INVALID = BASE_PARAMS_ERROR - 4;
    public static final int ERROR_PARAMS_PERSON_COULD_NOT_BE_TRACKED = BASE_PARAMS_ERROR - 5;
    public static final int ERROR_PARAMS_DISTANCE_INVALID = BASE_PARAMS_ERROR - 6;
    public static final int ERROR_PARAMS_PERSON_ID_INVALID = BASE_PARAMS_ERROR - 7;
    public static final int ERROR_PARAMS_SOUND_LOCALIZATION_ANGLE_INVALID = BASE_PARAMS_ERROR - 8;
    public static final int ERROR_PARAMS_RESERVATION_TYPE_INVALID = BASE_PARAMS_ERROR - 9;
    public static final int ERROR_PARAMS_RESERVATION_CODE_INVALID = BASE_PARAMS_ERROR - 10;
    public static final int ERROR_PARAMS_JSON_PARSER_ERROR = BASE_PARAMS_ERROR - 11;
    public static final int ERROR_PARAMS_REGISTER_ID_INVALID = BASE_PARAMS_ERROR - 12;
    public static final int ERROR_PARAMS_HEAD_TURN_BEAN_INVALID = BASE_PARAMS_ERROR - 13;
    public static final int ERROR_PARAMS_HEAD_TURN_BEAN_GROUP_INVALID = BASE_PARAMS_ERROR - 14;
    public static final int ERROR_PARAMS_TRICK_BEAN_GROUP_INVALID = BASE_PARAMS_ERROR - 15;
    public static final int ERROR_PARAMS_CRUISE_ROUTE_INVALID = BASE_PARAMS_ERROR - 16;
    public static final int ERROR_PARAMS_CRUISE_START_INDEX_INVALID = BASE_PARAMS_ERROR - 17;
    public static final int ERROR_PARAMS_BASIC_MOTION_BEAN_INVALID = BASE_PARAMS_ERROR - 18;
    public static final int ERROR_PARAMS_LIGHT_BEAN_GROUP_INVALID = BASE_PARAMS_ERROR - 52;
    public static final int ERROR_PARAMS_MAX_RANGE_INVALID = BASE_PARAMS_ERROR - 53;

    private static final int BASE_NAVI_ERROR = -32620000;
    public static final int ERROR_NOT_ESTIMATE = BASE_NAVI_ERROR - 1;
    public static final int ERROR_NAVIGATION_OUT_MAP = BASE_NAVI_ERROR - 2;
    public static final int ERROR_PLACE_NOT_EXIST = BASE_NAVI_ERROR - 3;
    public static final int ERROR_GET_CURRENT_POSE_FAILED = BASE_NAVI_ERROR - 4;
    public static final int ERROR_GET_PLACE_LIST_FAILED = BASE_NAVI_ERROR - 5;
    public static final int ERROR_NAVIGATION_AVOID_TIMEOUT = BASE_NAVI_ERROR - 6;
    public static final int ERROR_DESTINATION_NOT_EXIST = BASE_NAVI_ERROR - 7;
    public static final int ERROR_DESTINATION_CAN_NOT_ARRIVE = BASE_NAVI_ERROR - 8;
    public static final int ERROR_NAVIGATION_GLOBAL_PATH_FAILED = BASE_NAVI_ERROR - 9;
    public static final int ERROR_TOO_FAR_WITH_PLACE = BASE_NAVI_ERROR - 10;
    public static final int ERROR_GET_PLACE_LIST_EMPTY = BASE_NAVI_ERROR - 13;
    public static final int ERROR_NAVIGATION_RESET_ESTIMATE_FAIL = BASE_NAVI_ERROR - 14;
    public static final int ERROR_NAVIGATION_ALREADY_IN_DESTINATION = BASE_NAVI_ERROR - 15;
    public static final int ERROR_FORWARD_FAILED = BASE_NAVI_ERROR - 16;
    public static final int ERROR_NAVIGATION_NO_MAP = BASE_NAVI_ERROR - 17;
    public static final int ERROR_NO_CRUISE_ROUTE = BASE_NAVI_ERROR - 18;
    public static final int ERROR_CRUISE_POINT_NOT_EXIT = BASE_NAVI_ERROR - 19;
    public static final int ERROR_SET_CRUISE_ROUTE_FAILED = BASE_PARAMS_ERROR - 20;
    public static final int ERROR_CRUISE_GO_POSITION_FAILED = BASE_PARAMS_ERROR - 21;
    public static final int ERROR_GET_CHARGE_POSE_FAILED = BASE_NAVI_ERROR -22;
    public static final int ERROR_CHARGE_POINT_CAN_NOT_ARRIVE = BASE_NAVI_ERROR - 23;
    public static final int ERROR_RECOVER_POSITIVE_FAILED = BASE_NAVI_ERROR - 24;
    public static final int ERROR_FORWARD_TIMEOUT = BASE_NAVI_ERROR - 25;
    public static final int ERROR_NAVIGATION_BACK_FAILED = BASE_NAVI_ERROR - 26;
    public static final int ERROR_TURN_BACK_FAILED = BASE_NAVI_ERROR -27;
    public static final int ERROR_CRUISE_PERSISTENT_IMMOBILITY = BASE_NAVI_ERROR - 28;
    public static final int ERROR_MULTI_ROBOT_WAITING_TIMEOUT = BASE_NAVI_ERROR - 29;
    public static final int ERROR_MOTION_AVOID_STOP = BASE_NAVI_ERROR - 29;
    public static final int ERROR_WHEEL_OVER_CURRENT_RUN_OUT = BASE_NAVI_ERROR - 30;
    public static final int ERROR_OPEN_RADAR_FAILED = BASE_NAVI_ERROR - 31;
    public static final int ERROR_NAVI_WHEEL_SLIP = BASE_NAVI_ERROR - 32;
    public static final int ERROR_GET_ROVER_CONFIG_FAILED = BASE_NAVI_ERROR - 33;
    public static final int ERROR_SET_ROVER_CONFIG_FAILED = BASE_NAVI_ERROR - 34;
    public static final int ERROR_GET_ROVER_CONFIG_TIMEOUT = BASE_NAVI_ERROR - 35;


    private static final int BASE_HEAD_ERROR = -32630000;
    public static final int ERROR_TRACK_TARGET_NOT_FOUND = BASE_HEAD_ERROR - 1;
    public static final int ERROR_SWITCH_CAMERA_FAILED = BASE_HEAD_ERROR - 2;
    public static final int ERROR_GET_PICTURE_FAILED = BASE_HEAD_ERROR - 3;
    public static final int ERROR_FIND_PERSON_TIMEOUT = BASE_HEAD_ERROR - 4;
    public static final int ERROR_HEAD_TURN_TIMEOUT = BASE_HEAD_ERROR - 5;
    public static final int ERROR_HEAD_TURN_FAILED = BASE_HEAD_ERROR - 6;
    public static final int ERROR_HEAD_TURN_INTERRUPT = BASE_HEAD_ERROR - 7;
    public static final int ERROR_HEAD_TRACK_FAILED = BASE_HEAD_ERROR - 8;
    public static final int ERROR_HEAD_GUEST_LOST = BASE_HEAD_ERROR - 9;
    public static final int ERROR_HEAD_TURN_NOT_SUPPORT = BASE_HEAD_ERROR - 10;

    private static final int BASE_REMOTE_ERROR = -32640000;
    public static final int ERROR_REMOTE_RECEPTION_REGISTER_FAILED = BASE_REMOTE_ERROR - 1;
    public static final int ERROR_REMOTE_DETECT_FAILED = BASE_REMOTE_ERROR -2;
    public static final int ERROR_REMOTE_RESERVATION_CODE_NOT_EXIST = BASE_REMOTE_ERROR -3;
    public static final int ERROR_REMOTE_RESERVATION_CODE_USED = BASE_REMOTE_ERROR -4;
    public static final int ERROR_REMOTE_RESERVATION_CODE_LIMITED = BASE_REMOTE_ERROR -5;
    public static final int ERROR_REMOTE_RESERVATION_CODE_INVALID = BASE_REMOTE_ERROR -6;
    public static final int ERROR_REMOTE_RESERVATION_CODE_EXPIRED = BASE_REMOTE_ERROR -7;
    public static final int ERROR_REMOTE_RESERVATION_NAME_REPEATED = BASE_REMOTE_ERROR -8;
    public static final int ERROR_REMOTE_RESERVATION_SERVER_NO_DATA = BASE_REMOTE_ERROR -9;
    public static final int ERROR_REGISTER_FAILED = BASE_REMOTE_ERROR - 10;
    public static final int ERROR_MODIFY_NAME_FAILED = BASE_REMOTE_ERROR - 11;
    public static final int ERROR_STAFF_MODIFY_NAME_FORBID = BASE_REMOTE_ERROR - 12;
    public static final int ERROR_REGISTER_PICTURE_INVALID = BASE_REMOTE_ERROR - 13;

    private static final int BASE_NAVIGATION_TRANSFER_ERROR = -32650000;
    public static final int ERROR_DESTINATION_LIST_NOT_EXIST = BASE_NAVIGATION_TRANSFER_ERROR - 1;
    public static final int ERROR_LOAD_MULTIPLE_ROBOT_CONFIG_ERROR = BASE_NAVIGATION_TRANSFER_ERROR -2;
    public static final int ERROR_NO_AVAILABLE_DESTINATION = BASE_NAVIGATION_TRANSFER_ERROR -3;
    public static final int ERROR_NAVIGATION_DISABLE_MULTIPLE_MODE = BASE_NAVIGATION_TRANSFER_ERROR -4;
    public static final int ERROR_NAVIGATION_CHECK_TIMEOUT = BASE_NAVIGATION_TRANSFER_ERROR -5;
    public static final int ERROR_MULTIPLE_MODE_ERROR = BASE_NAVIGATION_TRANSFER_ERROR -6;

    private static final int BASE_UVC_CAMERA_ERROR = -32660000;
    public static final int ERROR_CAMERA_DATA_ERROR = BASE_UVC_CAMERA_ERROR - 1;
    public static final int ERROR_API_REQUEST_ERROR = BASE_UVC_CAMERA_ERROR - 2;

}
