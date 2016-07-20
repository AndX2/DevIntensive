package com.softdesign.devintensive.data.eventbus;

import com.softdesign.devintensive.data.storage.models.User;

import java.util.List;

/**
 * Created by savos on 20.07.2016.
 */

public class UserListLoadedEvent {

    public final List<User> userList;

    public UserListLoadedEvent(List<User> userList) {
        this.userList = userList;
    }
}
