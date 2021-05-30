package com.ocbc.assignment.viewmodels;

import android.app.Application;

import com.ocbc.assignment.model.User;
import com.ocbc.assignment.repository.Repository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UserInfoViewModel extends AndroidViewModel {

    private Repository repository;

    public UserInfoViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
    }

    public long insertUser(User user) {
        return repository.insertUser(user);
    }

    public User getUser(String id) {
        return repository.getUser(id);
    }

    public LiveData<List<User>> getRecepients() {
        return repository.getRecepients();
    }

    public void updateUser(double amount, double debt, double received, String userID) {
        repository.updateUser(amount, debt, received, userID);
    }
}
