package com.example.projectcalculator.data;

import android.content.Context;

import org.json.JSONObject;

import java.io.InputStream;

public class ConfigReader {

    private static JSONObject config;

    public static void loadConfig(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("config.data.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            config = new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getApiKey() {
        return config.optString("apiKey", "");
    }

    public static String getBaseUrl() {
        return config.optString("baseUrl", "");
    }

    public static String getQuery() {
        return config.optString("query", "");
    }
}
