package org.totalit.sbms.domain;

import com.google.gson.annotations.Expose;

import org.totalit.sbms.domain.temp.QuoteItemTemp;
import org.totalit.sbms.domain.temp.QuoteTemp;

import java.util.List;

public class QuoteMapper {
    @Expose
    QuoteTemp quote;
    @Expose
    List<QuoteItemTemp> quoteItem;

    public QuoteTemp getQuote() {
        return quote;
    }

    public void setQuote(QuoteTemp quote) {
        this.quote = quote;
    }

    public List<QuoteItemTemp> getQuoteItem() {
        return quoteItem;
    }

    public void setQuoteItem(List<QuoteItemTemp> quoteItem) {
        this.quoteItem = quoteItem;
    }
}
