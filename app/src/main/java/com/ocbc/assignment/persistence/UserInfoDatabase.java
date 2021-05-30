package com.ocbc.assignment.persistence;

import android.content.Context;

import com.ocbc.assignment.model.User;

import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {User.class}, version = 1, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class UserInfoDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "user_info_db";

    private static UserInfoDatabase instance;

    public static synchronized UserInfoDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    UserInfoDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract UserInfoDAO getCamLocationDAO();
}
