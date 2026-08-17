package com.example.projectcalculator.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "calories_table")
public class CalorieData {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private double calories;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }
}
