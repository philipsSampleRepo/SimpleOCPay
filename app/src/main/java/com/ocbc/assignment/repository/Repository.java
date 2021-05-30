package com.ocbc.assignment.repository;

import android.content.Context;

import com.ocbc.assignment.model.User;
import com.ocbc.assignment.persistence.UserInfoDAO;
import com.ocbc.assignment.persistence.UserInfoDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class Repository {

    private static final String TAG = "LocationRepository";

    private static Repository instance;
    private UserInfoDAO userInfoDAO;

    public static Repository getInstance(Context context) {
        if (instance == null) {
            synchronized (Repository.class) {
                if (instance == null) {
                    instance = new Repository(context);
                }
            }
        }
        return instance;
    }

    private Repository(Context context) {
        userInfoDAO = UserInfoDatabase.getInstance(context).getCamLocationDAO();
    }

    public long insertUser(User user) {
        return userInfoDAO.insertUser(user);
    }

    public User getUser(String id) {
        return userInfoDAO.getUser(id);
    }

    public LiveData<List<User>> getRecepients() {
        return userInfoDAO.getRecipients();
    }

    public void updateUser(double amount, double debt, double received, String userID) {
        userInfoDAO.update(amount, debt, received, userID);
    }
}
