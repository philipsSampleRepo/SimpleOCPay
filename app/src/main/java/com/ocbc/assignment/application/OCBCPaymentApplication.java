package com.ocbc.assignment.application;

import android.app.Application;


public class OCBCPaymentApplication extends Application {

    private String name;
    private double currentAmount = 0.0;
    private double debt = 0.0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }
}
