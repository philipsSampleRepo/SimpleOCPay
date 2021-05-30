package com.ocbc.assignment.ui;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ocbc.assignment.R;
import com.ocbc.assignment.application.OCBCPaymentApplication;
import com.ocbc.assignment.databinding.ActivityTopupBinding;
import com.ocbc.assignment.model.User;
import com.ocbc.assignment.utils.CommonUtils;
import com.ocbc.assignment.utils.Constants;
import com.ocbc.assignment.utils.CustomDialogInterface;
import com.ocbc.assignment.utils.UiUtils;
import com.ocbc.assignment.utils.UpdateListener;

import java.util.List;

public class TopupActivity extends BaseActivity {

    private static final String TAG = TopupActivity.class.getSimpleName();
    private ActivityTopupBinding activityTopupBinding = null;
    private String userName;
    private double currentAmount = 0.0;
    private double debtAmount = 0.0;
    private String[] userArray;
    private String selectedRecipientID;
    private User recipient;
    private double transferAmount;
    private double debt;
    private double currentValue;
    private double payeeDept;
    private double payeeCurrentAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        initUI();
    }

    @Override
    void initViewModel() {
        super.initViewModel();
    }

    @Override
    void initUI() {
        activityTopupBinding = ActivityTopupBinding.inflate(getLayoutInflater());
        View view = activityTopupBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setActionbar();
        setTitle();
        initViewModel();
        initThreads();
        setUserName();
        topupAction();
        setSpinner();
        performTransfer();
        // initFab();
    }

    private void setActionbar() {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeButtonEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setTitle() {
        setTitle(getString(R.string.topup_title));
    }

    private void setUserName() {
        OCBCPaymentApplication application = CommonUtils.getInstance()
                .getOCBCApplication(TopupActivity.this);
        userName = application.getName();
        currentAmount = application.getCurrentAmount();
        debtAmount = application.getDebt();
        String welcomeMsg = CommonUtils.getInstance()
                .setWelcomeMessage(TopupActivity.this, userName);
        activityTopupBinding.includedLayout.userNameTxt.setText(welcomeMsg);
        activityTopupBinding.includedLayout.currentbalanceLbl.setText(getString(R.string.currentblc_lbl));
        activityTopupBinding.includedLayout
                .currentblanceTxt.setText(String.valueOf(currentAmount));
    }

    private void setSpinner() {
        LiveData<List<User>> recipientList = userInfoViewModel.getRecepients();
        recipientList.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    userArray = CommonUtils.getInstance().getStringArray(users, userName);
                    Log.d(TAG, "recipientList: " + userArray.length);
                    if (userArray != null && userArray.length != 0) {
                        ArrayAdapter adapter = new ArrayAdapter(TopupActivity.this,
                                android.R.layout.simple_spinner_item, userArray);
                        adapter.setDropDownViewResource(
                                android.R.layout.simple_spinner_dropdown_item);
                        activityTopupBinding.includedLayout.spinner.setAdapter(adapter);
                    } else {
                        showAmountValidationMessage(getString(R.string.recipient_error),
                                getString(R.string.recipient_error_msg));
                    }
                }
            }
        });
        activityTopupBinding.includedLayout.spinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        selectedRecipientID = userArray[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedRecipientID = userArray[0];
                    }
                });
    }

    private void performTransfer() {

        activityTopupBinding.includedLayout.transferBtn
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: Transfer...");
                        String amount = activityTopupBinding.includedLayout
                                .transferAmount.getText().toString();
                        if (TextUtils.isEmpty(amount)) {
                            showAmountValidationMessage(getString(R.string.invalid_amount),
                                    getString(R.string.invalid_amount_msg));
                        } else {
                            transferAmount = Double.parseDouble(amount);
                            Log.d(TAG, "Transfer Amount " + amount);
                            if (transferAmount == 0) {
                                showAmountValidationMessage(getString(R.string.invalid_amount),
                                        getString(R.string.invalid_amount_msg));
                            } else {
                                String currentAmount = activityTopupBinding.includedLayout
                                        .currentblanceTxt.getText().toString();
                                currentValue = Double.parseDouble(currentAmount);

                                getUser(selectedRecipientID, new UpdateListener() {
                                    @Override
                                    public void getUser(User user) {
                                        payeeDept = user.getDebpt();
                                        payeeCurrentAmount = user.getAmount();

                                        if (payeeDept > 0) {
                                            if (transferAmount > payeeDept) {
                                                updateRecord(transferAmount - payeeDept,
                                                        0.0, 0.0, selectedRecipientID);
                                            } else {
                                                updateRecord(0.0,
                                                        payeeDept - transferAmount,
                                                        0.0, selectedRecipientID);
                                            }
                                        } else {
                                            if (transferAmount < currentValue) {
                                                currentValue = currentValue - transferAmount;
                                                updateRecord(currentValue, 0,
                                                        0, userName);
                                                currentValue = currentValue;
                                                updateRecipient(transferAmount);
                                                //TODO delete below
//                                    selectRecipient(selectedRecipientID, user -> {
//                                        recipient = user;
//                                        double recipientCurrentAmount = recipient.getAmount();
//                                        updateRecord(recipientCurrentAmount + transferAmount,
//                                                recipient.getUserName());
//                                    });
                                            } else {
                                                //updateRecipient(0.0);
                                                debt = transferAmount - currentValue;
                                                updateRecord(0.0, debt, 0, userName);
                                                updateRecipient(currentValue);
                                                currentValue = 0.0;
                                                //TODO delete below
//                                    selectRecipient(selectedRecipientID, new UpdateListener() {
//                                        @Override
//                                        public void getUser(User user) {
//                                            recipient = user;
//                                            double recipientCurrentAmount = recipient.getAmount();
//                                            updateRecord(recipientCurrentAmount + currentValue,
//                                                    recipient.getUserName());
//                                        }
//                                    });
                                            }
                                        }
                                        appExecutors.mainThread().execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                activityTopupBinding.includedLayout
                                                        .currentblanceTxt
                                                        .setText(String.valueOf(currentValue));
                                                showToast(getString(R.string.successful_transfer));
                                                activityTopupBinding.includedLayout.transferAmount.setText("");
                                            }
                                        });
                                    }
                                });

                                //TODO put inside mainthread...

//                                String currentAmount = activityTopupBinding.includedLayout
//                                        .currentblanceTxt.getText().toString();
//                                double currentValue = Double.parseDouble(currentAmount);
//                                double newVal = CommonUtils.getInstance()
//                                        .roundAmount(currentValue + topup);
//                                if (newVal > Constants.MAX_VAL) {
//                                    showAmountValidationMessage(getString(R.string.topup_max_error)
//                                            , getString(R.string.topup_max_error_msg));
//                                } else {
//                                    activityTopupBinding.includedLayout
//                                            .currentblanceTxt.setText(String.valueOf(newVal));
//                                    activityTopupBinding.includedLayout.topupAmount.setText("");
//                                    updateRecord(newVal);
                            }
                        }
                    }

                });
    }

    private void topupAction() {
        activityTopupBinding.includedLayout.topupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Topup...");
                getUser(userName, new UpdateListener() {
                    @Override
                    public void getUser(User user) {
                        debtAmount = user.getDebpt();
                    }
                });

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        String amount = activityTopupBinding.includedLayout.topupAmount.getText().toString();
                        if (TextUtils.isEmpty(amount)) {
                            showAmountValidationMessage(getString(R.string.invalid_amount),
                                    getString(R.string.invalid_amount_msg));
                        } else {
                            double topup = Double.parseDouble(amount);
                            Log.d(TAG, "Topup Amount " + amount);
                            if (topup == 0) {
                                showAmountValidationMessage(getString(R.string.invalid_amount),
                                        getString(R.string.invalid_amount_msg));
                            } else {
                                String currentAmount = activityTopupBinding.includedLayout
                                        .currentblanceTxt.getText().toString();
                                double currentValue = Double.parseDouble(currentAmount);

                                if (debtAmount > 0) {
                                    if (topup < debtAmount) {
//                                        getUser(userName, new UpdateListener() {
//                                            @Override
//                                            public void getUser(User user) {
//                                                debtAmount = user.getDebpt();
                                        updateRecord(0.0, debtAmount - topup,
                                                0.0,
                                                userName);

//                                            }
//                                        });
                                        activityTopupBinding.includedLayout
                                                .currentblanceTxt.setText(String.valueOf(0.0));
                                        activityTopupBinding.includedLayout.topupAmount.setText("");
                                        showToast(getString(R.string.pay_debt));
                                        getUser(selectedRecipientID, new UpdateListener() {
                                            @Override
                                            public void getUser(User user) {
                                                double currentAmount = user.getAmount();
                                                updateRecord(currentAmount + topup,
                                                        selectedRecipientID);
                                            }
                                        });
                                    } else {
//                                        getUser(userName, new UpdateListener() {
//                                            @Override
//                                            public void getUser(User user) {
//                                                debtAmount = user.getDebpt();
                                        updateRecord(topup - debtAmount, 0.0,
                                                0.0,
                                                userName);

//                                            }
//                                        });
                                        activityTopupBinding.includedLayout
                                                .currentblanceTxt.setText(String.valueOf(topup - debtAmount));
                                        activityTopupBinding.includedLayout.topupAmount.setText("");
                                        showToast(getString(R.string.pay_debt));
                                        getUser(selectedRecipientID, new UpdateListener() {
                                            @Override
                                            public void getUser(User user) {
                                                double currentAmount = user.getAmount();
                                                updateRecord(currentAmount + debtAmount,
                                                        selectedRecipientID);
                                            }
                                        });
                                    }
                                } else {
                                    double newVal = CommonUtils.getInstance().roundAmount(currentValue + topup);
                                    activityTopupBinding.includedLayout.topupAmount.setText("");
                                    updateRecord(newVal, userName);
                                    activityTopupBinding.includedLayout
                                            .currentblanceTxt.setText(String.valueOf(newVal));
                                }
                            }
                        }
                    }
                });

            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(TopupActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void updateRecipient(double amount) {
        getUser(selectedRecipientID, user -> {
            recipient = user;
            double recipientCurrentAmount = recipient.getAmount();
            updateRecord(recipientCurrentAmount + amount,
                    recipient.getUserName());
        });
    }

    private void updateRecord(double newVal, String userName) {
        appExecutors.diskIO().execute(() -> {
            userInfoViewModel.updateUser(newVal, 0, 0, userName);
            Log.d(TAG, "update record output");
        });
    }

    private void updateRecord(double newVal, double debt, double received, String userID) {
        appExecutors.diskIO().execute(() -> {
            userInfoViewModel.updateUser(newVal, debt, received, userID);
            Log.d(TAG, "update record output");
        });
    }

    private void getUser(String userID, UpdateListener listener) {
        appExecutors.diskIO().execute(() -> {
            User userLiveData = userInfoViewModel.getUser(userID);
            listener.getUser(userLiveData);
        });
    }

    private void showAmountValidationMessage(String title, String message) {
        appExecutors.mainThread().execute(() -> UiUtils.getInstance()
                .showMaterialDialog(TopupActivity.this,
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void initFab() {
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//    }
}