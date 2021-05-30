package com.ocbc.assignment.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ocbc.assignment.model.User;

import java.lang.reflect.Type;

import androidx.room.TypeConverter;

public class DataConverter {

    @TypeConverter
    public String fromLocation(User user) {
        if (user == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<User>() {
        }.getType();
        String json = gson.toJson(user, type);
        return json;
    }

    @TypeConverter
    public User toLocation(String user) {
        if (user == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<User>() {
        }.getType();
        User location = gson.fromJson(user, type);
        return location;
    }
}