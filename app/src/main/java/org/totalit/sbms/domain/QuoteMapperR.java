package org.totalit.sbms.domain;

import com.google.gson.annotations.Expose;

import java.util.List;

public class QuoteMapperR {
    @Expose
    Quote quote;
    @Expose
    List<QuoteItem> quoteItem;

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public List<QuoteItem> getQuoteItem() {
        return quoteItem;
    }

    public void setQuoteItem(List<QuoteItem> quoteItem) {
        this.quoteItem = quoteItem;
    }
}
