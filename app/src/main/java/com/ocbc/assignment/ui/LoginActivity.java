package com.ocbc.assignment.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ocbc.assignment.R;
import com.ocbc.assignment.databinding.ActivityMainBinding;
import com.ocbc.assignment.model.User;
import com.ocbc.assignment.utils.AppExecutors;
import com.ocbc.assignment.utils.CommonUtils;
import com.ocbc.assignment.utils.CustomDialogInterface;
import com.ocbc.assignment.utils.Router;
import com.ocbc.assignment.utils.UiUtils;
import com.ocbc.assignment.utils.UpdateListener;
import com.ocbc.assignment.viewmodels.UserInfoViewModel;

public class LoginActivity extends BaseActivity {
    private final String TAG = LoginActivity.class.getSimpleName();
    private ActivityMainBinding activityMainBinding;
    private String user;
    private String pwd;
    private String errorMessage = "";
    private User userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        initUI();
    }

    @Override
    void initViewModel() {
        super.initViewModel();
    }

    @Override
    void initUI() {
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();
        initThreads();
        launchSignup();
        initViewModel();
        setCred();
        login();
        createRecipient();
    }

    //TODO delete this
    private void setCred() {
        activityMainBinding.username.setText("Bob");
        activityMainBinding.password.setText("1234");
    }

    private void launchSignup() {
        activityMainBinding.textViewSignUp.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: sign up pressed...");
            Router.getInstance().startRegisterActivity(this);
        });
    }

    private void login() {
        activityMainBinding.btnlogin.setOnClickListener(v -> {
            loginAction();
        });
    }

    private void showErrorMessage(String title, String message) {
        appExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                UiUtils.getInstance()
                        .showMaterialDialog(LoginActivity.this, new CustomDialogInterface() {
                            @Override
                            public void ok(DialogInterface dialogInterface) {
                                Log.d(TAG, "ok: pressed...");
                                dialogInterface.dismiss();
                            }

                            @Override
                            public void cancel(DialogInterface dialogInterface) {

                            }
                        }, title, message, false);
            }
        });
    }

    private void showErrorMessage(String title, String message, boolean isMultiChoice) {
        appExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                UiUtils.getInstance()
                        .showMaterialDialog(LoginActivity.this, new CustomDialogInterface() {
                            @Override
                            public void ok(DialogInterface dialogInterface) {
                                Log.d(TAG, "ok: pressed...");
                                dialogInterface.dismiss();
                                Router.getInstance()
                                        .startRegisterActivity(LoginActivity.this);
                            }

                            @Override
                            public void cancel(DialogInterface dialogInterface) {
                                dialogInterface.dismiss();
                            }
                        }, title, message, isMultiChoice);
            }
        });
    }

    private void getUser(UpdateListener listener) {
        appExecutors.diskIO().execute(() ->
                listener.getUser(userInfoViewModel.getUser(user)));
    }

    private void loginAction() {
        user = activityMainBinding.username.getText().toString();
        pwd = activityMainBinding.password.getText().toString();
        errorMessage = CommonUtils.getInstance().validateFields(LoginActivity.this,
                user, pwd);

        if (errorMessage.length() != 0) {
            showErrorMessage(getString(R.string.error), errorMessage);
        } else {
            getUser(user -> {
                userInfo = user;

                if (userInfo == null) {
                    Log.d(TAG, "loginAction: user does not exist...");
                    showErrorMessage(getString(R.string.error),
                            getString(R.string.user_does_not_exist), true);
                } else {
                    if (CommonUtils.getInstance().validatePassword(pwd,
                            userInfo.getPassword())) {
                        Log.d(TAG, "login: login success...");
                        CommonUtils.getInstance().setGlobalInformation(
                                LoginActivity.this, userInfo);
                        Router.getInstance().startTopupActivity(LoginActivity.this);
                    } else {
                        Log.d(TAG, "login: login failure...");
                        showErrorMessage(getString(R.string.login_error),
                                getString(R.string.login_fail_msg));
                    }
                }

            });
        }
    }

    private void createRecipient() {
        User user = new User();
        user.setUserName("Alice");
        user.setOwner(false);
        user.setAmount(100);
        user.setDebpt(0);
        user.setPassword("1234");
        appExecutors.diskIO().execute(() -> {
            try {
                long out = userInfoViewModel.insertUser(user);
                Log.d(TAG, "recipient: " + out);
                if (out != -1) {
                    Log.d(TAG, "onClick: user registration successful...");
                }
            } catch (Exception e) {
                Log.e(TAG, "recipient exists: " + e.getMessage());
            }
        });
    }
}