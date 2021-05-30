package com.ocbc.assignment.utils;

import android.text.TextUtils;

import com.ocbc.assignment.R;
import com.ocbc.assignment.application.OCBCPaymentApplication;
import com.ocbc.assignment.model.User;
import com.ocbc.assignment.ui.BaseActivity;

import org.apache.commons.math3.util.Precision;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public final class CommonUtils {

    private static CommonUtils commonUtils = null;

    public static CommonUtils getInstance() {
        if (commonUtils == null) {
            synchronized (CommonUtils.class) {
                if (commonUtils == null) {
                    commonUtils = new CommonUtils();
                }
            }
        }
        return commonUtils;
    }

    public OCBCPaymentApplication getOCBCApplication(AppCompatActivity context) {
        OCBCPaymentApplication ocbcPaymentApplication = (OCBCPaymentApplication)
                context.getApplication();
        return ocbcPaymentApplication;
    }

    public boolean validateFields(String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        return true;
    }

    public boolean isPasswordIdentical(String pwd1, String pwd2) {
        if (pwd1.equals(pwd2)) {
            return true;
        }
        return false;
    }

    public String validateFields(BaseActivity context, String user,
                                 String pwd) {
        if (!CommonUtils.getInstance().validateFields(user)) {
            return context.getString(R.string.user_name_error);

        }
        if (!CommonUtils.getInstance().validateFields(pwd)) {
            return context.getString(R.string.pwd_error);
        }
        return "";
    }

    public String validateRegistrationForm(BaseActivity context,
                                           String user, String pwd1,
                                           String pwd2) {
        if (!validateFields(user)) {
            return context.getString(R.string.user_name_error);
        }
        if (!validateFields(pwd1)) {
            return context.getString(R.string.pwd_error);
        }
        if (!validateFields(pwd2)) {
            return context.getString(R.string.pwd_error);
        }
        if (!isPasswordIdentical(pwd1, pwd2)) {
            return context.getString(R.string.pwd_mismatch);
        }
        return "";
    }

    public boolean validatePassword(String pwd1, String pwd2) {
        if (pwd1.equals(pwd2)) {
            return true;
        }
        return false;
    }

    public void setGlobalInformation(BaseActivity context, User userInfo) {
        CommonUtils.getInstance().getOCBCApplication(context)
                .setName(userInfo.getUserName());
        CommonUtils.getInstance().getOCBCApplication(context)
                .setCurrentAmount(userInfo.getAmount());
        CommonUtils.getInstance().getOCBCApplication(context)
                .setDebt(userInfo.getDebpt());
    }

    public String setWelcomeMessage(BaseActivity context, String userName) {
        if (TextUtils.isEmpty(userName)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(context.getString(R.string.welcome_message));
        builder.append(" ");
        builder.append(userName);
        builder.append("!");
        return builder.toString();
    }

    public double roundAmount(double amount) {
        return Precision.round(amount, 2);
    }

    public String[] getStringArray(List<User> userList, String userName) {
        if (userList != null) {
            String temp = "";
            ArrayList<String> userArray = new ArrayList<>();
            for (int i = 0; i < userList.size(); i++) {
                temp = userList.get(i).getUserName();
                if (!temp.equals(userName)) {
                    if (temp != null) {
                        userArray.add(temp);
                    }
                }
            }
            return userArray.toArray(new String[userArray.size()]);
        }
        return null;
    }


}
