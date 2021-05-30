package com.ocbc.assignment.persistence;

import com.ocbc.assignment.model.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.ABORT;

@Dao
public interface UserInfoDAO {
    @Insert(onConflict = ABORT)
    long insertUser(User user);

    @Query("SELECT * FROM user_info WHERE userName =:userID")
    User getUser(String userID);

    @Query("SELECT * FROM user_info")
    LiveData<List<User>> getRecipients();

    @Query("UPDATE user_info SET amount=:amount, debpt=:debt,received=:received WHERE userName = :userID")
    void update(double amount, double debt, double received, String userID);
}
