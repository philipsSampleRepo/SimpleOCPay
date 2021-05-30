package com.ocbc.assignment.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_info", indices = @Index(value = {"userName"}, unique = true))
public class User {

    @ColumnInfo(name = "password")
    private String password;

    @PrimaryKey
    @NonNull
    private String userName;

    @ColumnInfo(name = "amount")
    private double amount;

    @ColumnInfo(name = "debpt")
    private double debpt;

    @ColumnInfo(name = "received")
    private double received;

    @ColumnInfo(name = "isOwner")
    private boolean isOwner;


    public double getReceived() {
        return received;
    }

    public void setReceived(double received) {
        this.received = received;
    }

    public double getDebpt() {
        return debpt;
    }

    public void setDebpt(double debpt) {
        this.debpt = debpt;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
