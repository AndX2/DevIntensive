package com.softdesign.devintensive.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by savos on 10.07.2016.
 */

public class GsonHelper {

    public static String getJsonFromObject(Object object, Class<?> typeClass){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(object, typeClass);
    }

    public static Object getObjectFromJson(String json, Class<?> typeClass){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(json, typeClass);
    }
}
