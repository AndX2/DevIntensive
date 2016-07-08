package com.softdesign.devintensive.data.managers;

import android.util.Log;

import com.softdesign.devintensive.pojo.Repo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savos on 08.07.2016.
 */

public class UserInfoManager {

    public static final String TAG = "LogTagUserInfoManager";

    private static UserInfoManager instance;
    private UserInfoManager(){
        jsonUserInfo = DataManager.getInstance().getPreferenceManager().loadUserInfo();
        if (jsonUserInfo != null){
            parseSetUserInfo(jsonUserInfo);
            Log.d(TAG, "constructor");
        }
    }

    synchronized public static UserInfoManager getInstance(){
        if (instance == null){
            instance = new UserInfoManager();
        }
        return instance;
    }

    private String firstName;
    private String secondName;
    private List<Repo> repo;
    private String eMail;
    private String vk;
    private String phone;
    private int homeTask;
    private int projects;
    private int linesCode;
    private int rait;
    private String bio;
    private String avatar;
    private String photo;

    private String jsonUserInfo;

    private boolean isEmpty = true;

    public boolean setJsonUserInfo(String json){
        boolean isValid = parseSetUserInfo(json);
        if (isValid) {
            DataManager.getInstance().getPreferenceManager().saveUserProfileJson(json);
        }
        return isValid;
    }

    private boolean parseSetUserInfo(String json){
        try {
            JSONObject userInfo = new JSONObject(json);
            JSONObject data = userInfo.getJSONObject("data");
            JSONObject user = data.getJSONObject("user");
            String firstName = user.getString("first_name");
            String secondName = user.getString("second_name");
            JSONObject repositories = user.getJSONObject("repositories");
            JSONArray repoJsonArray = repositories.getJSONArray("repo");
            List<Repo> repo = new ArrayList<>();
            for (int i = 0; i < repoJsonArray.length(); i++){
                JSONObject tmp = repoJsonArray.getJSONObject(i);
                String _id = tmp.getString("_id");
                String git = tmp.getString("git");
                String title = tmp.getString("title");
                repo.add(new Repo(_id, git, title));
            }
            JSONObject contacts = user.getJSONObject("contacts");
            String eMail = contacts.getString("email");
            String vk = contacts.getString("vk");
            String phone = contacts.getString("phone");
            JSONObject profileValues = user.getJSONObject("profileValues");
            int homeTask = profileValues.getInt("homeTask");
            int projects = profileValues.getInt("projects");
            int linesCode = profileValues.getInt("linesCode");
            int rait = profileValues.getInt("rait");
            JSONObject publicInfo = user.getJSONObject("publicInfo");
            String bio = publicInfo.getString("bio");
            String avatar = publicInfo.getString("avatar");
            String photo = publicInfo.getString("photo");


            isEmpty = false;
            this.firstName = firstName;
            this.secondName = secondName;
            this.repo = repo;
            this.eMail = eMail;
            this.vk = vk;
            this.phone = phone;
            this.homeTask = homeTask;
            this.projects = projects;
            this.linesCode = linesCode;
            this.rait = rait;
            this.bio = bio;
            this.photo = photo;
            this.avatar = avatar;
            Log.d(TAG, "parsing DONE!");

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        Log.d(TAG, "parsing ERROR!");
        return true;

    }

    //==============Getters===============================

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public List<Repo> getRepo() {
        return repo;
    }

    public String geteMail() {
        return eMail;
    }

    public String getVk() {
        return vk;
    }

    public String getPhone() {
        return phone;
    }

    public int getHomeTask() {
        return homeTask;
    }

    public int getProjects() {
        return projects;
    }

    public int getLinesCode() {
        return linesCode;
    }

    public int getRait() {
        return rait;
    }

    public String getBio() {
        return bio;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPhoto() {
        return photo;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
