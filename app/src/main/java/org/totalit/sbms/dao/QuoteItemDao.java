package org.totalit.sbms.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.totalit.sbms.domain.QuoteItem;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface QuoteItemDao {

    @Query("SELECT * FROM quoteitem where id=:id")
    QuoteItem getQuoteItem(int id);

    @Query("SELECT * FROM QuoteItem where quoteId=:quoteId")
    List<QuoteItem> getQuoteItemByQuote(Long quoteId);

    @Query("SELECT * FROM QuoteItem where quoteId=:quoteId and productId=:productId")
    QuoteItem getQuoteItemByQuoteAndProduct(Long quoteId, Long productId);

    @Update
    int updateQuoteItem(QuoteItem quoteItem);

    @Insert(onConflict = REPLACE)
    Long insertQuoteItem(QuoteItem  quoteItem);



    @Query("DELETE FROM QuoteItem where quoteId=:quoteId and productId=:productId")
    public void deleteQuoteItem(Long quoteId, Long productId);
}
