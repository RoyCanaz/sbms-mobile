package org.totalit.sbms.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.totalit.sbms.domain.temp.QuoteItemTemp;
import org.totalit.sbms.domain.temp.QuoteTemp;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface QuoteItemTempDao {
    @Query("SELECT * FROM quoteitemtemp where id=:id")
    QuoteItemTemp getQuoteItemTemp(int id);

    @Query("SELECT * FROM quoteitemtemp where quoteId=:quoteId")
    List<QuoteItemTemp> getQuoteItemByQuote(Long quoteId);

    @Query("SELECT * FROM quoteitemtemp where quoteId=:quoteId and productId=:productId")
    QuoteItemTemp getQuoteItemByQuoteAndProduct(Long quoteId, Long productId);

    @Update
    int updateQuoteItemTemp(QuoteItemTemp quoteItemTemp);

    @Insert(onConflict = REPLACE)
    Long insertQuoteItemTemp(QuoteItemTemp  quoteItemTemp);

    @Query("DELETE FROM quoteitemtemp where quoteId=:quoteId and productId=:productId")
    public void deleteQuoteItem(Long quoteId, Long productId);
}
