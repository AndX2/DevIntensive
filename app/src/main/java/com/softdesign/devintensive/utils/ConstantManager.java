package com.softdesign.devintensive.utils;

/**
 * Created by savos on 22.06.2016.
 */

public interface ConstantManager {

    //PreferenceManager
    String USER_PHONE_KEY = "User_key_1";
    String USER_MAIL_KEY = "User_key_2";
    String USER_VK_KEY = "User_key_3";
    String USER_GIT_KEY = "User_key_4";
    String USER_MYSELF_KEY = "User_key_5";
    String VK_ID = "User_key_6";
    String VK_TOKEN = "User_key_7";
    String GIT_AUTH_TOKEN = "User_key_8";
    String USER_PROFILE_JSON = "User_key_9";
    String PASS_FINGERPRINT = "pass fingerPrint";
    String USER_PROFILE = "User_key_10";
    String FIRST_LAUNCHE_FLAG = "first launch flag";

    //MainActivity
    String TAG_PREFIX = "Main activity";
    String COLOR_MODE_KEY = "Color mode key";
    String EDIT_MODE_KEY = "Edit text key";
    String USER_PHOTO_KEY = "User photo key";

    long ACTIVITY_MAIN_TIME_SHOW_BOTTOM_SHEET_PICK_PHOTO = 3000;
    int REQUEST_CODE_CAMERA_PICTURE = 500;
    int REQUEST_CODE_GALLERY_PICTURE = 501;
    int PERMISSION_REQUEST_SETTING_CODE = 502;
    int CAMERA_PERMISSION_REQUEST_CODE = 503;

    //VKAuthActivity
    String VK_APP_ID = "5535159";
    String GIT_APP_ID = "47ea27470ddeb8dd366f";
    String API_LOGIN_URL = "http://devintensive.softdesign-apps.ru/api/login";


    //AuthActivity
    long AUTH_ERROR_FLASH_DELAY = 500;


    String PARCEL_USER_KEY = "Parcel userDTO key";
    String TAG_USER_LIST_ADAPTER = "LogTagUserListAdapter";
    String TAG_PROFILE_USER = "Profile user activity";
    String TAG_LIST_USER = "TagUserListActivity";
    long SEARCH_LATENCY = 5000;
    long SPLASH_DELAY = 3000;
}
