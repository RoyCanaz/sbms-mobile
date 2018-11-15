package org.totalit.sbms.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import org.totalit.sbms.domain.temp.QuoteTemp;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface QuoteTempDao {
    @Query("SELECT * FROM quotetemp where id=:id")
    QuoteTemp getQuoteTemp(int id);

    @Query("SELECT Max(id) FROM quotetemp")
    int getMaxId();

    @Update
    void updateQuoteTemp(QuoteTemp quoteTemp);

    @Insert(onConflict = REPLACE)
    Long insertQuoteTemp(QuoteTemp quoteTemp);
}
