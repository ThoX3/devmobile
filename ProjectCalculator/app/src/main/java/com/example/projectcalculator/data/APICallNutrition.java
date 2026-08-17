package com.example.projectcalculator.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APICallNutrition {

    public void fetchAndSaveCalories(Context context) {
        ConfigReader.loadConfig(context);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String apiKey = ConfigReader.getApiKey();
            String baseUrl = ConfigReader.getBaseUrl();
            String query = ConfigReader.getQuery();
            String url = baseUrl + query;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("X-Api-Key", apiKey)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    parseAndSaveCalories(responseBody, context);
                } else {
                    System.out.println("Request failed with code: " + response.code());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void parseAndSaveCalories(String response, Context context) {
        try {
            CalorieDatabase db = CalorieDatabase.getDatabase(context);
            JSONArray items = new JSONObject(response).getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String name = item.getString("name");
                double calories = item.getDouble("calories");

                CalorieData data = new CalorieData();
                data.setName(name);
                data.setCalories(calories);

                db.caloriesDao().insert(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
