package com.softdesign.devintensive.pojo;

/**
 * Created by savos on 07.07.2016.
 */

public class VKAuth {

    public VKAuth(){};
    public VKAuth(String vkId, String accessToken){
        this.vkId = vkId;
        this.accessToken = accessToken;
    }

    private String vkId;

    public String getVkId() {
        return vkId;
    }

    public void setVkId(String vkId) {
        this.vkId = vkId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String accessToken;
}
