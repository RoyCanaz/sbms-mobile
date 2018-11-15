package org.totalit.sbms.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.totalit.sbms.domain.Quote;


import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface QuoteDao {
    @Query("SELECT * FROM quote where id=:id")
    Quote getQuote(Long id);

    @Query("SELECT * FROM quote ORDER BY dateCreated DESC")
    List<Quote> getQuotes();

    @Query("SELECT Max(id) FROM quote")
    int getMaxId();

    @Update
    void updateQuote(Quote quote);

    @Insert(onConflict = REPLACE)
    Long insertQuote(Quote quote);
}
