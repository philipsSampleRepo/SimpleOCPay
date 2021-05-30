package com.ocbc.assignment.ui;


import com.ocbc.assignment.utils.AppExecutors;
import com.ocbc.assignment.viewmodels.UserInfoViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class BaseActivity extends AppCompatActivity {
    UserInfoViewModel userInfoViewModel;
    AppExecutors appExecutors;

    void initViewModel() {
        userInfoViewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);
    }

    void initUI() {
    }

    void initThreads() {
        appExecutors = new AppExecutors();
    }
}
