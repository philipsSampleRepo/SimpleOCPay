package com.ocbc.assignment.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ocbc.assignment.R;
import com.ocbc.assignment.databinding.ActivityRegisterBinding;
import com.ocbc.assignment.model.User;
import com.ocbc.assignment.utils.CommonUtils;
import com.ocbc.assignment.utils.CustomDialogInterface;
import com.ocbc.assignment.utils.UiUtils;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private ActivityRegisterBinding registerBinding;
    private String erorrMessage;
    private String user;
    private String pwd1;
    private String pwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initViewModel();
        registerUserAction();
    }

    @Override
    void initViewModel() {
        super.initViewModel();
    }

    @Override
    void initUI() {
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = registerBinding.getRoot();
        setContentView(view);
        getSupportActionBar().hide();
        initViewModel();
        initThreads();
    }

    private void registerUser(String user, String pwd) {
        User userInfo = new User();
        userInfo.setAmount(0.0);
        userInfo.setUserName(user);
        userInfo.setPassword(pwd);
        userInfo.setDebpt(0.0);
        userInfo.setOwner(true);

        Log.d(TAG, "onClick: register user");
        appExecutors.diskIO().execute(() -> {
            try {
                long out = userInfoViewModel.insertUser(userInfo);
                Log.d(TAG, "run: " + out);
                if (out != -1) {
                    Log.d(TAG, "onClick: user registration successful...");
                    showRegisterSuccessfulMessage(getString(R.string.user_inserted_successful)
                            , getString(R.string.user_inserted_successful));
                }
            } catch (Exception e) {
                Log.e(TAG, "user exists: " + e.getMessage());
                showRegisterMessage(getString(R.string.user_exists)
                        , getString(R.string.user_exist));
            }
        });
    }

    private void registerUserAction() {
        registerBinding.btnRegister.setOnClickListener(v -> {
            user = registerBinding.regUsername.getText().toString();
            pwd1 = registerBinding.regPassword.getText().toString();
            pwd2 = registerBinding.inputConformPassword.getText().toString();
            erorrMessage = CommonUtils.getInstance()
                    .validateRegistrationForm(RegisterActivity.this,
                            user, pwd1, pwd2);
            if (erorrMessage.length() != 0) {
                showRegisterMessage(getString(R.string.error), erorrMessage);
            } else {
                registerUser(user, pwd1);
            }
        });
    }

    private void showRegisterMessage(String title, String message) {
        appExecutors.mainThread().execute(() -> UiUtils.getInstance()
                .showMaterialDialog(RegisterActivity.this,
                        new CustomDialogInterface() {
                            @Override
                            public void ok(DialogInterface dialogInterface) {
                                Log.d(TAG, "ok: pressed...");
                                dialogInterface.dismiss();
                            }

                            @Override
                            public void cancel(DialogInterface dialogInterface) {

                            }
                        }, title, message, false));
    }

    private void showRegisterSuccessfulMessage(String title, String message) {
        appExecutors.mainThread().execute(() -> {
            try {
                UiUtils.getInstance()
                        .showMaterialDialog(RegisterActivity.this,
                                new CustomDialogInterface() {
                                    @Override
                                    public void ok(DialogInterface dialogInterface) {
                                        Log.d(TAG, "ok: pressed...");
                                        dialogInterface.dismiss();
                                        finish();
                                    }

                                    @Override
                                    public void cancel(DialogInterface dialogInterface) {

                                    }
                                }, title, message, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}