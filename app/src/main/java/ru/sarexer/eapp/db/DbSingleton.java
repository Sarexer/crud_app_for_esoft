package ru.sarexer.eapp.db;

import android.content.Context;

import androidx.room.Room;

public class DbSingleton {
    private static DbSingleton instance;
    public AppDatabase database;

    private DbSingleton(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "database").build();
    }

    public static DbSingleton getInstance(Context context) {
        if(instance == null){
            instance = new DbSingleton(context);
        }

        return instance;
    }
}
