package com.task.utils;

import com.google.gson.Gson;

public class ResponseArrayUtils {
    public static <T> T[] getResponseAsArray(String response, Class<T[]> responseType) {
        Gson gson = new Gson();
        return gson.fromJson(response, responseType);
    }
}