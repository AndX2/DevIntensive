package com.softdesign.devintensive.data.managers;


import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;

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
        List<User> users = DataManager.getInstance().getDaoSession().queryBuilder(User.class)
                .where(UserDao.Properties.CodeLines.gt(0))
                .orderDesc(UserDao.Properties.CodeLines)
                .build()
                .list();
        return users;
    }

    public List<User> getUserListByName(String query){
        List<User> userList = new ArrayList<>();
        try{

            userList = DataManager.getInstance().getDaoSession().queryBuilder(User.class)
                    .where(UserDao.Properties.Raiting.gt(0), UserDao.Properties.SearchName.like("%" + query.toUpperCase() + "%"))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();
        }catch (Exception e){

        }
        return userList;
    }


    //endregion
}
