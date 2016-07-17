package com.softdesign.devintensive.data.managers;


import com.softdesign.devintensive.data.storage.models.User;

import java.util.ArrayList;
import java.util.List;

public class StorageManager {

    private static StorageManager instance;

    private StorageManager() {
    }

    public static synchronized StorageManager getInstance(){
        if (instance == null)
            instance = new StorageManager();
        return instance;
    }

    //region ================DataBase===============
    public List<User> getUserListFromDb(){
        return new ArrayList<>();
    }
    //endregion
}
