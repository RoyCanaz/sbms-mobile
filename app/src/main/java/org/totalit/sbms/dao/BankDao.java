package org.totalit.sbms.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.totalit.sbms.domain.Bank;
import org.totalit.sbms.domain.Rate;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface BankDao {
    @Query("SELECT * FROM bank")
    List<Bank> getBanks();

    @Query("Select id FROM bank where name=:name")
    int getId(String name);

    @Query("Select * FROM bank where active=:active")
    Bank getActiveBank(Boolean active);

    @Update
    void updateBank(Bank bank);

    @Insert(onConflict = REPLACE)
    void insertBank(Bank bank);
}
