package com.softdesign.devintensive.data.chronos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.pojo.UserProfile;

import java.util.List;

/**
 * Created by savos on 20.07.2016.
 */

public class ChronosLoadUsersFromDbByName extends ChronosOperation<List<User>> {

    String query;

    public ChronosLoadUsersFromDbByName(String query){
        this.query = query;
    }
    @Nullable
    @Override
    public List<User> run() {
        Log.d("Chronos", "Load user list by name in thread: " + Thread.currentThread().getName());
        final List<User> tmp = DataManager.getInstance().getStorageManager().getUserListByName(query);
        return tmp;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<List<User>>> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<List<User>> {

    }
}
