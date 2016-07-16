package com.softdesign.devintensive.net.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.softdesign.devintensive.pojo.UserProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savos on 16.07.2016.
 */

public class UserListRes {


    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("second_name")
    @Expose
    private String secondName;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("repositories")
    @Expose
    private UserProfile.Repositories repositories;
    @SerializedName("profileValues")
    @Expose
    private UserProfile.ProfileValues profileValues;
    @SerializedName("publicInfo")
    @Expose
    private UserProfile.PublicInfo publicInfo;
    @SerializedName("specialization")
    @Expose
    public String specialization;
    @SerializedName("updated")
    @Expose
    private String updated;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public UserProfile.Repositories getRepositories() {
        return repositories;
    }

    public UserProfile.ProfileValues getProfileValues() {
        return profileValues;
    }

    public UserProfile.PublicInfo getPublicInfo() {
        return publicInfo;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getUpdated() {
        return updated;
    }

    public Integer getV() {
        return v;
    }

    public String getFullName(){
        return firstName + " " + secondName;
    }
}
