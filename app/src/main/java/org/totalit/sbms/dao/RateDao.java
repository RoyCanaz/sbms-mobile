package org.totalit.sbms.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.totalit.sbms.domain.Bank;
import org.totalit.sbms.domain.Category;
import org.totalit.sbms.domain.Rate;

import java.util.List;
import java.util.Set;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface RateDao {
    @Query("SELECT * FROM rate")
    List<Rate> getRates();

    @Query("Select id FROM rate where name=:name")
    int getId(String name);

    @Update
    void updateRate(Rate rate);

    @Query("Select * FROM rate where active=:active")
    Rate getActiveRate(Boolean active);

    @Insert(onConflict = REPLACE)
    void insertRate(Rate rate);
}
