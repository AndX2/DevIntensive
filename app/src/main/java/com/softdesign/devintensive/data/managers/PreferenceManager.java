package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.softdesign.devintensive.pojo.UserProfile;
import com.softdesign.devintensive.pojo.VKAuth;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.DevIntensiveApplication;
import com.softdesign.devintensive.utils.GsonHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savos on 29.06.2016.
 */

public class PreferenceManager {

    public static final String TAG = "PreferenceManagerTag";

    private SharedPreferences mSharedPreferences;
    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_MAIL_KEY, ConstantManager.USER_VK_KEY, ConstantManager.USER_GIT_KEY,
            ConstantManager.USER_MYSELF_KEY};

    public PreferenceManager() {

        this.mSharedPreferences = DevIntensiveApplication.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userFields) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();

    }

    public List<String> loadUserProfileData() {
        List<String> userFields = new ArrayList<>();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            userFields.add(i, mSharedPreferences.getString(USER_FIELDS[i], null));
        }
        return userFields;
    }

    public void saveUserPhoto(Uri savedPhotoUri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, savedPhotoUri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY,
                "android.resource://com.softdesign.devintensive/drawable/user_avatar"));
    }

    public void saveVKAuth(VKAuth vkAuth) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.VK_ID, vkAuth.getVkId());
        editor.putString(ConstantManager.VK_TOKEN, vkAuth.getAccessToken());
        editor.apply();
    }

    public VKAuth loadVKAuth() {
        if (mSharedPreferences.getString(ConstantManager.VK_TOKEN, null) != null)
            return new VKAuth(mSharedPreferences.getString(ConstantManager.VK_ID, null),
                    mSharedPreferences.getString(ConstantManager.VK_TOKEN, null));
        return null;
    }

    public void saveGitAuth(String gitToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.GIT_AUTH_TOKEN, gitToken);
        editor.apply();
    }

    public String loadGitAuth() {
        return mSharedPreferences.getString(ConstantManager.GIT_AUTH_TOKEN, null);
    }

    public void saveUserProfileJson(String userProfileJson){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PROFILE_JSON, userProfileJson);
        editor.apply();
    }

    public String loadUserInfo(){
        return mSharedPreferences.getString(ConstantManager.USER_PROFILE_JSON, null);

    }

    public void savePassFingerPrint (String passFingerPrint){
        Log.d(TAG, "passFingerPrint= " + passFingerPrint);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.PASS_FINGERPRINT, passFingerPrint);
        editor.apply();
    }

    public boolean checkPassFingerPrint (String passFingerPrint) {
        Log.d(TAG, passFingerPrint + " <> " +mSharedPreferences.getString(ConstantManager.PASS_FINGERPRINT, null) +
                " = " + passFingerPrint.equals(mSharedPreferences.getString(ConstantManager.PASS_FINGERPRINT, null)));
        return passFingerPrint.equals(mSharedPreferences.getString(ConstantManager.PASS_FINGERPRINT, null));

    }

    public void saveUserProfile(UserProfile userProfile){
        String json = GsonHelper.getJsonFromObject(userProfile, UserProfile.class);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PROFILE, json);
        editor.apply();
    }

    public UserProfile loadUserProfile(){
        String json = mSharedPreferences.getString(ConstantManager.USER_PROFILE, null);
        if (json == null) return null;
        return (UserProfile) GsonHelper.getObjectFromJson(json, UserProfile.class);

    }

}
