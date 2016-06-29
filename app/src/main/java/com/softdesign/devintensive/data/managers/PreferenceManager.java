package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;

import com.softdesign.devintensive.ui.utils.ConstantManager;
import com.softdesign.devintensive.ui.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savos on 29.06.2016.
 */

public class PreferenceManager {

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
}
