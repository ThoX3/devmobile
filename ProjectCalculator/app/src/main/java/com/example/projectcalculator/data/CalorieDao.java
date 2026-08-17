package com.example.projectcalculator.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CalorieDao {
    @Insert
    void insert(CalorieData calorieData);

    @Query("SELECT * FROM calories_table WHERE name = :itemName LIMIT 1")
    CalorieData getCaloriesForItem(String itemName);
}

