package com.example.projectcalculator.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CalorieData.class}, version = 1, exportSchema = false)
public abstract class CalorieDatabase extends RoomDatabase {
    public abstract CalorieDao caloriesDao();

    private static volatile CalorieDatabase INSTANCE;

    public static CalorieDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CalorieDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CalorieDatabase.class, "calorie_database.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}


